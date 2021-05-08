/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.app.web.client.list;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.client.list.ClientRowResult.OptionPart;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientListAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<ClientRowResult>> index() {
        List<String> projectList = clientInfoLogic.getProjectList();
        List<ClientRowResult> beans = projectList.stream().map(project -> {
            ClientModel clientModel = clientInfoLogic.findClient(project).get();
            return mappingToRowBean(clientModel);
        }).collect(Collectors.toList());
        return asJson(beans);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
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

        String clientName = clientModel.getProjectInfra().getClientProject();
        rowBean.hasSchemaHtml = documentLogic.existsSchemaHtml(clientName);
        rowBean.hasHistoryHtml = documentLogic.existsHistoryHtml(clientName);
        rowBean.hasReplaceSchema = clientInfoLogic.existsReplaceSchema(clientName);
        return rowBean;
    }

    // -----------------------------------------------------
    //                                                 Basic
    //                                                 -----
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

    // -----------------------------------------------------
    //                                              Database
    //                                              --------
    private void prepareDatabase(ClientRowResult client, ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        ClientRowResult.DatabaseSettingsPart databaseBean = new ClientRowResult.DatabaseSettingsPart();
        databaseBean.url = databaseInfoMap.getDbConnectionBox().getUrl();
        databaseBean.schema = databaseInfoMap.getDbConnectionBox().getSchema();
        databaseBean.user = databaseInfoMap.getDbConnectionBox().getUser();
        databaseBean.password = databaseInfoMap.getDbConnectionBox().getPassword();
        client.mainSchemaSettings = databaseBean;
    }

    // -----------------------------------------------------
    //                                                Option
    //                                                ------
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
}
