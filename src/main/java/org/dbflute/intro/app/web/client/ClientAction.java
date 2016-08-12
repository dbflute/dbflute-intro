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

import java.util.LinkedHashMap;
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
import org.dbflute.intro.app.model.ClientModel;
import org.dbflute.intro.app.model.DatabaseModel;
import org.dbflute.intro.app.model.OptionModel;
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
        List<ClientDetailBean> beanList = projectList.stream().map(project -> {
            return mappingToDetailBean(clientInfoLogic.findClient(project).get());
        }).collect(Collectors.toList());
        return asJson(beanList);
    }

    @Execute
    public JsonResponse<ClientDetailBean> detail(String clientProject) {
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            throw new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientDetailBean detailBean = mappingToDetailBean(clientModel);
        return asJson(detailBean);
    }

    protected ClientDetailBean mappingToDetailBean(ClientModel clientModel) {
        ClientBean clientBean = new ClientBean();
        clientBean.projectName = clientModel.getClientProject();
        clientBean.databaseType = clientModel.getDatabase();
        clientBean.languageType = clientModel.getTargetLanguage();
        clientBean.containerType = clientModel.getTargetContainer();
        clientBean.packageBase = clientModel.getPackageBase();
        clientBean.jdbcDriverFqcn = clientModel.getJdbcDriverFqcn();
        clientBean.jdbcDriverJarPath = clientModel.getJdbcDriverJarPath();
        clientBean.dbfluteVersion = clientModel.getDbfluteVersion();
        DatabaseModel databaseModel = clientModel.getDatabaseModel();
        if (databaseModel != null) {
            DatabaseBean databaseBean = new DatabaseBean();
            databaseBean.url = databaseModel.getUrl();
            databaseBean.schema = databaseModel.getSchema();
            databaseBean.user = databaseModel.getUser();
            databaseBean.password = databaseModel.getPassword();
            clientBean.databaseBean = databaseBean;
        }
        DatabaseModel systemUserDatabaseModel = clientModel.getSystemUserDatabaseModel();
        if (systemUserDatabaseModel != null) {
            DatabaseBean databaseBean = new DatabaseBean();
            databaseBean.url = systemUserDatabaseModel.getUrl();
            databaseBean.schema = systemUserDatabaseModel.getSchema();
            databaseBean.user = systemUserDatabaseModel.getUser();
            databaseBean.password = systemUserDatabaseModel.getPassword();
            clientBean.systemUserDatabaseBean = databaseBean;
        }
        OptionModel optionModel = clientModel.getOptionModel();
        if (optionModel != null) {
            OptionBean optionBean = new OptionBean();
            optionBean.dbCommentOnAliasBasis = optionModel.isDbCommentOnAliasBasis();
            optionBean.aliasDelimiterInDbComment = optionModel.getAliasDelimiterInDbComment();
            optionBean.checkColumnDefOrderDiff = optionModel.isCheckColumnDefOrderDiff();
            optionBean.checkDbCommentDiff = optionModel.isCheckDbCommentDiff();
            optionBean.checkProcedureDiff = optionModel.isCheckProcedureDiff();
            optionBean.generateProcedureParameterBean = optionModel.isGenerateProcedureParameterBean();
            optionBean.procedureSynonymHandlingType = optionModel.getProcedureSynonymHandlingType();
            clientBean.optionBean = optionBean;
        }
        Map<String, DatabaseModel> schemaSyncCheckMap = clientModel.getSchemaSyncCheckMap();
        if (schemaSyncCheckMap != null) {
            clientBean.schemaSyncCheckMap = new LinkedHashMap<>();
            schemaSyncCheckMap.entrySet().forEach(schemaSyncCheck -> {
                DatabaseModel schemaSyncCheckModel = schemaSyncCheck.getValue();
                DatabaseBean schemaSyncCheckBean = new DatabaseBean();
                schemaSyncCheckBean.url = schemaSyncCheckModel.getUrl();
                schemaSyncCheckBean.schema = schemaSyncCheckModel.getSchema();
                schemaSyncCheckBean.user = schemaSyncCheckModel.getUser();
                schemaSyncCheckBean.password = schemaSyncCheckModel.getPassword();
                clientBean.schemaSyncCheckMap.put(schemaSyncCheck.getKey(), schemaSyncCheckBean);
            });
        }

        ClientDetailBean detailBean = new ClientDetailBean();
        detailBean.clientBean = clientBean;
        String clientProject = clientModel.getClientProject();
        detailBean.schemahtml = documentLogic.findDocumentFile(clientProject, "schema").exists();
        detailBean.historyhtml = documentLogic.findDocumentFile(clientProject, "history").exists();
        detailBean.replaceSchema = clientInfoLogic.existsReplaceSchema(clientProject);
        return detailBean;
    }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientModel clientParam = convertToParam(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            testConnection(clientParam);
        }
        clientUpdateLogic.createClient(clientParam);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> update(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientModel clientParam = convertToParam(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            testConnection(clientParam);
        }
        clientUpdateLogic.updateClient(clientParam);
        return JsonResponse.asEmptyBody();
    }

    protected ClientModel convertToParam(ClientBody clientBody) {
        ClientModel clientParam = new ClientModel();
        clientParam.setClientProject(clientBody.project);
        clientParam.setDatabase(clientBody.database);
        clientParam.setTargetLanguage(clientBody.targetLanguage);
        clientParam.setTargetContainer(clientBody.targetContainer);
        clientParam.setPackageBase(clientBody.packageBase);

        OptionalThing.ofNullable(clientBody.databaseBody, () -> {}).ifPresent(databaseBody -> {
            DatabaseModel databaseParam = new DatabaseModel();
            databaseParam.setUrl(databaseBody.jdbcDriver);
            databaseParam.setUrl(databaseBody.url);
            databaseParam.setSchema(databaseBody.schema);
            databaseParam.setUser(databaseBody.user);
            databaseParam.setPassword(databaseBody.password);
            clientParam.setDatabaseModel(databaseParam);
        });

        OptionalThing.ofNullable(clientBody.systemUserDatabaseBody, () -> {}).ifPresent(databaseBody -> {
            DatabaseModel databaseParam = new DatabaseModel();
            databaseParam.setUrl(databaseBody.url);
            databaseParam.setSchema(databaseBody.schema);
            databaseParam.setUser(databaseBody.user);
            databaseParam.setPassword(databaseBody.password);
            clientParam.setSystemUserDatabaseModel(databaseParam);
        });

        clientParam.setJdbcDriverJarPath(clientBody.jdbcDriverJarPath);
        clientParam.setDbfluteVersion(clientBody.dbfluteVersion);

        OptionalThing.ofNullable(clientBody.optionBody, () -> {}).ifPresent(optionBody -> {
            OptionModel optionParam = new OptionModel();
            optionParam.setDbCommentOnAliasBasis(optionBody.dbCommentOnAliasBasis);
            optionParam.setAliasDelimiterInDbComment(optionBody.aliasDelimiterInDbComment);
            optionParam.setCheckColumnDefOrderDiff(optionBody.checkColumnDefOrderDiff);
            optionParam.setCheckDbCommentDiff(optionBody.checkDbCommentDiff);
            optionParam.setCheckProcedureDiff(optionBody.checkProcedureDiff);
            optionParam.setGenerateProcedureParameterBean(optionBody.generateProcedureParameterBean);
            optionParam.setProcedureSynonymHandlingType(optionBody.procedureSynonymHandlingType);
            clientParam.setOptionModel(optionParam);
        });

        clientParam.setSchemaSyncCheckMap(new LinkedHashMap<>());
        clientBody.schemaSyncCheckMap.entrySet().forEach(schemaSyncCheck -> {
            OptionalThing.ofNullable(schemaSyncCheck.getValue(), () -> {}).ifPresent(databaseBody -> {
                DatabaseModel databaseParam = new DatabaseModel();
                databaseParam.setUrl(databaseBody.url);
                databaseParam.setSchema(databaseBody.schema);
                databaseParam.setUser(databaseBody.user);
                databaseParam.setPassword(databaseBody.password);
                clientParam.getSchemaSyncCheckMap().put(schemaSyncCheck.getKey(), databaseParam);
            });
        });

        return clientParam;
    }

    private void testConnection(ClientModel clientModel) {
        String jdbcDriverJarPath = clientModel.getJdbcDriverJarPath();
        String dbfluteVersion = clientModel.getDbfluteVersion();
        String jdbcDriver = clientModel.getJdbcDriverFqcn();
        DatabaseModel databaseParam = clientModel.getDatabaseModel();
        testConnectionLogic.testConnection(jdbcDriverJarPath, dbfluteVersion, jdbcDriver, databaseParam);
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
