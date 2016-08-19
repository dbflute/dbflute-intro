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

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectMeta;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientBody;
import org.dbflute.intro.app.web.client.ClientDetailResult.ClientPart;
import org.dbflute.intro.app.web.client.ClientDetailResult.ClientPart.DatabaseSettingsPart;
import org.dbflute.intro.app.web.client.ClientDetailResult.ClientPart.OptionPart;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 */
public class ClientAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;
    @Resource
    private IntroClsAssist introClsAssist;
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                Select
    //                                                ------
    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classifications() {
        Map<String, Map<?, ?>> classificationMap = introClsAssist.getClassificationMap();
        return asJson(classificationMap);
    }

    @Execute
    public JsonResponse<List<ClientDetailResult>> list() {
        List<String> projectList = clientInfoLogic.getProjectList();
        List<ClientDetailResult> beans = projectList.stream().map(project -> {
            return mappingToDetailBean(clientInfoLogic.findClient(project).get());
        }).collect(Collectors.toList());
        return asJson(beans);
    }

    @Execute
    public JsonResponse<ClientDetailResult> detail(String clientProject) {
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientDetailResult detailBean = mappingToDetailBean(clientModel);
        return asJson(detailBean);
    }

    protected ClientDetailResult mappingToDetailBean(ClientModel clientModel) {
        ClientPart clientBean = new ClientPart();
        prepareBasic(clientBean, clientModel);
        prepareDatabase(clientBean, clientModel);
        clientBean.systemUserSettings = clientModel.getReplaceSchemaMap().flatMap(replaceSchemaMap -> {
            return replaceSchemaMap.getAdditionalUserMap().flatMap(additionalUserMap -> {
                return additionalUserMap.getSystemUserMap().map(systemUserMap -> {
                    DatabaseSettingsPart databaseBean = new DatabaseSettingsPart();
                    databaseBean.url = systemUserMap.getDbConnectionBox().getUrl();
                    databaseBean.schema = systemUserMap.getDbConnectionBox().getSchema();
                    databaseBean.user = systemUserMap.getDbConnectionBox().getUser();
                    databaseBean.password = systemUserMap.getDbConnectionBox().getPassword();
                    return databaseBean;
                });
            });
        }).orElse(null);
        clientBean.optionBean = prepareOption(clientModel);
        clientBean.schemaSyncCheckMap = new LinkedHashMap<>();
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

        ClientDetailResult detailBean = new ClientDetailResult();
        detailBean.clientBean = clientBean;
        String clientProject = clientModel.getProjectMeta().getClientProject();
        detailBean.schemahtml = documentLogic.existsSchemaHtml(clientProject);
        detailBean.historyhtml = documentLogic.existsHistoryHtml(clientProject);
        detailBean.replaceSchema = clientInfoLogic.existsReplaceSchema(clientProject);
        return detailBean;
    }

    private void prepareBasic(ClientPart clientBean, ClientModel clientModel) {
        ProjectMeta projectMeta = clientModel.getProjectMeta();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        clientBean.projectName = projectMeta.getClientProject();
        clientBean.targetDatabase = basicInfoMap.getDatabase();
        clientBean.targetLanguage = basicInfoMap.getTargetLanguage();
        clientBean.targetContainer = basicInfoMap.getTargetContainer();
        clientBean.packageBase = basicInfoMap.getPackageBase();
        clientBean.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        clientBean.dbfluteVersion = projectMeta.getDbfluteVersion();
        clientBean.jdbcDriverJarPath = projectMeta.getJdbcDriverJarPath().orElse(null);
    }

    private void prepareDatabase(ClientPart clientBean, ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        DatabaseSettingsPart databaseBean = new DatabaseSettingsPart();
        databaseBean.url = databaseInfoMap.getDbConnectionBox().getUrl();
        databaseBean.schema = databaseInfoMap.getDbConnectionBox().getSchema();
        databaseBean.user = databaseInfoMap.getDbConnectionBox().getUser();
        databaseBean.password = databaseInfoMap.getDbConnectionBox().getPassword();
        clientBean.mainSchemaSettings = databaseBean;
    }

    private OptionPart prepareOption(ClientModel clientModel) {
        OptionPart optionBean = new OptionPart();
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
        return new ProjectMeta(clientBody.clientProject, clientBody.dbfluteVersion, clientBody.jdbcDriverJarPath);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientBody clientBody) {
        return new BasicInfoMap(clientBody.targetDatabase, clientBody.targetLanguage, clientBody.targetContainer, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientBody clientBody) {
        return OptionalThing.ofNullable(clientBody.databaseBody, () -> {}).map(databaseBody -> {
            DbConnectionBox connectionBox =
                    new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            AdditionalSchemaMap additionalSchemaMap = new AdditionalSchemaMap(new LinkedHashMap<>()); // #pending see the class code
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, connectionBox, additionalSchemaMap);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }

    private void testConnectionIfPossible(ClientModel clientModel) {
        String dbfluteVersion = clientModel.getProjectMeta().getDbfluteVersion();
        OptionalThing<String> jdbcDriverJarPath = clientModel.getProjectMeta().getJdbcDriverJarPath();
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        testConnectionLogic.testConnection(dbfluteVersion, jdbcDriverJarPath, databaseInfoMap);
    }

    @Execute
    public JsonResponse<Void> delete(String clientProject) {
        clientUpdateLogic.deleteClient(clientProject);
        return JsonResponse.asEmptyBody();
    }
}
