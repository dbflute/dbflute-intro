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

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.database.DatabaseInfoLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientPart;
import org.dbflute.intro.app.web.client.ClientRowResult.OptionPart;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.intro.dbflute.allcommon.CDef.TargetDatabase;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 * @author hakiba
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
    private DatabaseInfoLogic databaseInfoLogic;
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 List
    //                                                ------
    @Execute
    public JsonResponse<List<ClientRowResult>> list() {
        List<String> projectList = clientInfoLogic.getProjectList();
        List<ClientRowResult> beans = projectList.stream().map(project -> {
            return mappingToRowBean(clientInfoLogic.findClient(project).get());
        }).collect(Collectors.toList());
        return asJson(beans);
    }

    private ClientRowResult mappingToRowBean(ClientModel clientModel) {
        ClientRowResult rowBean = new ClientRowResult();
        prepareBasic(rowBean, clientModel);
        prepareDatabase(rowBean, clientModel);
        rowBean.systemUserSettings = (ClientRowResult.DatabaseSettingsPart) clientModel.getReplaceSchemaMap().flatMap(replaceSchemaMap -> {
            return replaceSchemaMap.getAdditionalUserMap().flatMap(additionalUserMap -> {
                return additionalUserMap.getSystemUserMap().map(systemUserMap -> {
                    ClientRowResult.DatabaseSettingsPart databaseBean = new ClientRowResult.DatabaseSettingsPart();
                    databaseBean.url = systemUserMap.getDbConnectionBox().getUrl();
                    databaseBean.schema = systemUserMap.getDbConnectionBox().getSchema();
                    databaseBean.user = systemUserMap.getDbConnectionBox().getUser();
                    databaseBean.password = systemUserMap.getDbConnectionBox().getPassword();
                    return databaseBean;
                });
            });
        }).orElse(null);
        rowBean.optionBean = prepareOption(clientModel);
        rowBean.schemaSyncCheckMap = new LinkedHashMap<>();
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

        String clientProject = clientModel.getProjectInfra().getClientProject();
        rowBean.hasSchemaHtml = documentLogic.existsSchemaHtml(clientProject);
        rowBean.hasHistoryHtml = documentLogic.existsHistoryHtml(clientProject);
        rowBean.hasReplaceSchema = clientInfoLogic.existsReplaceSchema(clientProject);
        return rowBean;
    }

    private void prepareBasic(ClientRowResult client, ClientModel clientModel) {
        ProjectInfra projectInfra = clientModel.getProjectInfra();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        client.projectName = projectInfra.getClientProject();
        client.databaseCode = basicInfoMap.getDatabase();
        client.languageCode = basicInfoMap.getTargetLanguage();
        client.containerCode = basicInfoMap.getTargetContainer();
        client.packageBase = basicInfoMap.getPackageBase();
        client.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        client.dbfluteVersion = projectInfra.getDbfluteVersion();
        client.jdbcDriverJarPath = projectInfra.getJdbcDriverExtlibFile().map(ExtlibFile::getCanonicalPath).orElse(null);
    }

    private void prepareDatabase(ClientRowResult client, ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        ClientRowResult.DatabaseSettingsPart databaseBean = new ClientRowResult.DatabaseSettingsPart();
        databaseBean.url = databaseInfoMap.getDbConnectionBox().getUrl();
        databaseBean.schema = databaseInfoMap.getDbConnectionBox().getSchema();
        databaseBean.user = databaseInfoMap.getDbConnectionBox().getUser();
        databaseBean.password = databaseInfoMap.getDbConnectionBox().getPassword();
        client.mainSchemaSettings = databaseBean;
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
    //                                             Operation
    //                                             ---------
    @Execute
    public JsonResponse<ClientOperationResult> operation(String clientProject) {
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientOperationResult detailBean = mappingToOperationResult(clientModel);
        return asJson(detailBean);
    }

    private ClientOperationResult mappingToOperationResult(ClientModel clientModel) {
        ClientOperationResult operation = new ClientOperationResult();
        prepareBasic(operation, clientModel);
        String clientProject = clientModel.getProjectInfra().getClientProject();
        operation.hasSchemaHtml = documentLogic.existsSchemaHtml(clientProject);
        operation.hasHistoryHtml = documentLogic.existsHistoryHtml(clientProject);
        operation.hasSyncCheckResultHtml = documentLogic.existsSyncCheckResultHtml(clientProject);
        return operation;
    }

    private void prepareBasic(ClientOperationResult client, ClientModel clientModel) {
        ProjectInfra projectInfra = clientModel.getProjectInfra();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        client.projectName = projectInfra.getClientProject();
        client.databaseCode = basicInfoMap.getDatabase();
        client.languageCode = basicInfoMap.getTargetLanguage();
        client.containerCode = basicInfoMap.getTargetContainer();
    }

    // -----------------------------------------------------
    //                                                Update
    //                                                ------
    @Execute
    public JsonResponse<Void> create(String projectName, ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {
            ClientPart client = clientCreateBody.client;
            if (clientInfoLogic.getProjectList().contains(projectName)) {
                messages.addErrorsWelcomeClientAlreadyExists("projectName", projectName); // TODO: hakiba refactor type-safe (2016/10/10)
            }
            TargetDatabase databaseCd = client.databaseCode;
            if (!databaseInfoLogic.isEmbeddedJar(databaseCd) && Objects.isNull(client.jdbcDriver)) {
                messages.addErrorsDatabaseNeedsJar("database", databaseCd.alias());
            }
            Optional.ofNullable(client.jdbcDriver)
                .map(driverPart -> driverPart.fileName)
                .filter(s -> StringUtils.isNotEmpty(s) && !s.endsWith(".jar"))
                .ifPresent(fileName -> messages.addErrorsDatabaseNeedsJar("jdbcDriver", fileName));
        });
        ClientModel clientModel = mappingToClientModel(projectName, clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> edit(String projectName, ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(projectName, clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.updateClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    private ClientModel mappingToClientModel(String projectName, ClientPart clientBody) {
        return newClientModel(projectName, clientBody);
    }

    private ClientModel newClientModel(String projectName, ClientPart clientBody) {
        ProjectInfra projectInfra = prepareProjectInfra(projectName, clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectInfra prepareProjectInfra(String projectName, ClientPart clientBody) {
        if (Objects.isNull(clientBody.jdbcDriver)) {
            return new ProjectInfra(projectName, clientBody.dbfluteVersion);
        }
        return new ProjectInfra(projectName, clientBody.dbfluteVersion, clientBody.jdbcDriver.fileName, clientBody.jdbcDriver.data);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientPart clientBody) {
        return OptionalThing.ofNullable(clientBody.mainSchemaSettings, () -> {}).map(databaseBody -> {
            DbConnectionBox connectionBox =
                    new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            AdditionalSchemaMap additionalSchemaMap = new AdditionalSchemaMap(new LinkedHashMap<>()); // #pending see the class code
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, connectionBox, additionalSchemaMap);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }

    private void testConnectionIfPossible(ClientModel clientModel) {
        String dbfluteVersion = clientModel.getProjectInfra().getDbfluteVersion();
        OptionalThing<String> jdbcDriverJarPath = clientModel.getProjectInfra().getJdbcDriverExtlibFile().map(ExtlibFile::getCanonicalPath);
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        testConnectionLogic.testConnection(dbfluteVersion, jdbcDriverJarPath, databaseInfoMap);
    }

    @Execute
    public JsonResponse<Void> delete(String clientProject) {
        clientUpdateLogic.deleteClient(clientProject);
        return JsonResponse.asEmptyBody();
    }
}
