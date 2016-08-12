/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.web.client;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.logic.task.TaskExecutionLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectMeta;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientBody;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean.DatabaseBean;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean.OptionBean;
import org.dbflute.intro.dbflute.allcommon.CDef.TaskType;
import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.intro.mylasta.exception.ClientNotFoundException;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 */
public class ClientAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private TaskExecutionLogic taskExecutionLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;
    @Resource
    private IntroClsAssist introClsAssist;
    @Resource
    private ResponseManager responseManager;
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classification() {
        Map<String, Map<?, ?>> classificationMap = introClsAssist.getClassificationMap();
        return asJson(classificationMap);
    }

    @Execute
    public JsonResponse<List<ClientDetailBean>> list() {
        List<String> projectList = clientInfoLogic.getProjectList();
        List<ClientDetailBean> beans = projectList.stream().map(project -> {
            return mappingToDetailBean(clientInfoLogic.findClient(project).get());
        }).collect(Collectors.toList());
        return asJson(beans);
    }

    @Execute
    public JsonResponse<ClientDetailBean> detail(String clientProject) {
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientDetailBean detailBean = mappingToDetailBean(clientModel);
        return asJson(detailBean);
    }

    protected ClientDetailBean mappingToDetailBean(ClientModel clientModel) {
        ClientBean clientBean = new ClientBean();
        prepareBasic(clientBean, clientModel);
        prepareDatabase(clientBean, clientModel);
        clientBean.systemUserBean = clientModel.getReplaceSchemaMap().flatMap(replaceSchemaMap -> {
            return replaceSchemaMap.getAdditionalUserMap().flatMap(additionalUserMap -> {
                return additionalUserMap.getSystemUserMap().map(systemUserMap -> {
                    DatabaseBean databaseBean = new DatabaseBean();
                    databaseBean.url = systemUserMap.getDbConnectionBox().getUrl();
                    databaseBean.schema = systemUserMap.getDbConnectionBox().getSchema();
                    databaseBean.user = systemUserMap.getDbConnectionBox().getUser();
                    databaseBean.password = systemUserMap.getDbConnectionBox().getPassword();
                    return databaseBean;
                });
            });
        }).orElse(null);
        clientBean.optionBean = prepareOption(clientModel);
        // #pending by jflute
        //    Map<String, DatabaseInfoMap> schemaSyncCheckMap = clientModel.getSchemaSyncCheckMap();
        //    if (schemaSyncCheckMap != null) {
        //        clientBean.schemaSyncCheckMap = new LinkedHashMap<>();
        //        schemaSyncCheckMap.entrySet().forEach(schemaSyncCheck -> {
        //            DatabaseInfoMap schemaSyncCheckModel = schemaSyncCheck.getValue();
        //            DatabaseBean schemaSyncCheckBean = new DatabaseBean();
        //            schemaSyncCheckBean.url = schemaSyncCheckModel.getUrl();
        //            schemaSyncCheckBean.schema = schemaSyncCheckModel.getSchema();
        //            schemaSyncCheckBean.user = schemaSyncCheckModel.getUser();
        //            schemaSyncCheckBean.password = schemaSyncCheckModel.getPassword();
        //            clientBean.schemaSyncCheckMap.put(schemaSyncCheck.getKey(), schemaSyncCheckBean);
        //        });
        //    }

        ClientDetailBean detailBean = new ClientDetailBean();
        detailBean.clientBean = clientBean;
        String clientProject = clientModel.getProjectMeta().getClientProject();
        detailBean.schemahtml = documentLogic.existsSchemaHtml(clientProject);
        detailBean.historyhtml = documentLogic.existsHistoryHtml(clientProject);
        detailBean.replaceSchema = clientInfoLogic.existsReplaceSchema(clientProject);
        return detailBean;
    }

    private void prepareBasic(ClientBean clientBean, ClientModel clientModel) {
        ProjectMeta projectMeta = clientModel.getProjectMeta();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        clientBean.clientProject = projectMeta.getClientProject();
        clientBean.targetDatabase = basicInfoMap.getDatabase();
        clientBean.targetLanguage = basicInfoMap.getTargetLanguage();
        clientBean.targetContainer = basicInfoMap.getTargetContainer();
        clientBean.packageBase = basicInfoMap.getPackageBase();
        clientBean.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        clientBean.jdbcDriverJarPath = projectMeta.getJdbcDriverJarPath().orElse(null);
        clientBean.dbfluteVersion = projectMeta.getDbfluteVersion().orElse(null);
    }

    private void prepareDatabase(ClientBean clientBean, ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        DatabaseBean databaseBean = new DatabaseBean();
        databaseBean.url = databaseInfoMap.getDbConnectionBox().getUrl();
        databaseBean.schema = databaseInfoMap.getDbConnectionBox().getSchema();
        databaseBean.user = databaseInfoMap.getDbConnectionBox().getUser();
        databaseBean.password = databaseInfoMap.getDbConnectionBox().getPassword();
        clientBean.databaseBean = databaseBean;
    }

    private OptionBean prepareOption(ClientModel clientModel) {
        OptionBean optionBean = new OptionBean();
        clientModel.getDocumentMap().ifPresent(documentMap -> {
            optionBean.dbCommentOnAliasBasis = documentMap.isDbCommentOnAliasBasis();
            optionBean.aliasDelimiterInDbComment = documentMap.getAliasDelimiterInDbComment().orElse(null);
            optionBean.checkColumnDefOrderDiff = documentMap.isCheckColumnDefOrderDiff();
            optionBean.checkDbCommentDiff = documentMap.isCheckDbCommentDiff();
            optionBean.checkProcedureDiff = documentMap.isCheckProcedureDiff();
        });
        clientModel.getOutsideSqlMap().ifPresent(outsideSqlMap -> {
            optionBean.generateProcedureParameterBean = outsideSqlMap.isGenerateProcedureParameterBean();
            optionBean.procedureSynonymHandlingType = outsideSqlMap.getProcedureSynonymHandlingType();
        });
        return optionBean;
    }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> update(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.updateClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    protected ClientModel mappingToClientModel(ClientBody clientBody) {
        ClientModel clientModel = newClientModel(clientBody);
        // TODO jflute intro: re-making (2016/08/12)
        return clientModel;
    }

    private ClientModel newClientModel(ClientBody clientBody) {
        ProjectMeta projectMeta = prepareProjectMeta(clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectMeta, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectMeta prepareProjectMeta(ClientBody clientBody) {
        return new ProjectMeta(clientBody.clientProject, clientBody.jdbcDriverJarPath, clientBody.dbfluteVersion);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientBody clientBody) {
        return new BasicInfoMap(clientBody.targetDatabase, clientBody.targetLanguage, clientBody.targetContainer, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientBody clientBody) {
        return OptionalThing.ofNullable(clientBody.databaseBody, () -> {}).map(databaseBody -> {
            DbConnectionBox dbConnectionInfo =
                    new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, dbConnectionInfo);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }

    private void testConnectionIfPossible(ClientModel clientModel) {
        clientModel.getProjectMeta().getJdbcDriverJarPath().ifPresent(jarPath -> {
            clientModel.getProjectMeta().getDbfluteVersion().ifPresent(dbfluteVersion -> {
                testConnectionLogic.testConnection(jarPath, dbfluteVersion, clientModel.getDatabaseInfoMap());
            });
        });
    }

    @Execute
    public JsonResponse<Void> delete(String project) {
        clientUpdateLogic.deleteClient(project);
        return JsonResponse.asEmptyBody();
    }

    // TODO jflute intro: independent (2016/07/19)
    @Execute
    public JsonResponse<Void> task(String project, AppCDef.TaskInstruction instruction, OptionalThing<String> env) {
        List<TaskType> taskTypeList = introClsAssist.toTaskTypeList(instruction);
        HttpServletResponse response = prepareTaskResponse();
        taskExecutionLogic.execute(project, taskTypeList, env, () -> response.getOutputStream());
        return JsonResponse.asEmptyBody();
    }

    private HttpServletResponse prepareTaskResponse() {
        HttpServletResponse response = responseManager.getResponse();
        response.setContentType("text/plain; charset=UTF-8");
        return response;
    }
}
