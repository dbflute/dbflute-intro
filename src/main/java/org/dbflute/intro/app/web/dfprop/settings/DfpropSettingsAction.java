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
package org.dbflute.intro.app.web.dfprop.settings;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.dfprop.database.DatabaseInfoLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.dfprop.settings.DfpropSettingsUpdateBody.ClientPart;
import org.dbflute.intro.app.web.dfprop.settings.DfpropSettingsUpdateBody.ClientPart.DatabaseSettingsPart;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author hakiba
 * @author jflute
 * @author cabos
 */
public class DfpropSettingsAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DatabaseInfoLogic databaseInfoLogic;
    @Resource
    private ClientInfoLogic clientInfoLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                              Settings
    //                                              --------
    @Execute
    public JsonResponse<DfpropSettingsResult> index(String clientName) {
        ClientModel clientModel = clientInfoLogic.findClient(clientName).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientName, clientName);
        });
        DfpropSettingsResult result = mappingToSettingsResult(clientModel);
        return asJson(result);
    }

    private DfpropSettingsResult mappingToSettingsResult(ClientModel clientModel) {
        DfpropSettingsResult result = new DfpropSettingsResult();
        ProjectInfra projectInfra = clientModel.getProjectInfra();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        result.projectName = projectInfra.getProjectName();
        result.databaseCode = basicInfoMap.getDatabase();
        result.languageCode = basicInfoMap.getTargetLanguage();
        result.containerCode = basicInfoMap.getTargetContainer();
        result.packageBase = basicInfoMap.getPackageBase();
        result.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        result.dbfluteVersion = projectInfra.getDbfluteVersion();
        result.jdbcDriverJarPath = projectInfra.getJdbcDriverExtlibFile().map(ExtlibFile::getCanonicalPath).orElse(null);
        DbConnectionBox dbConnectionBox = clientModel.getDatabaseInfoMap().getDbConnectionBox();
        result.mainSchemaSettings = new DfpropSettingsResult.DatabaseSettingsPart();
        result.mainSchemaSettings.url = dbConnectionBox.getUrl();
        result.mainSchemaSettings.schema = dbConnectionBox.getSchema();
        result.mainSchemaSettings.user = dbConnectionBox.getUser();
        result.mainSchemaSettings.password = dbConnectionBox.getPassword();

        return result;
    }

    // -----------------------------------------------------
    //                                                  Edit
    //                                                  ----
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String clientName, DfpropSettingsUpdateBody settingsBody) {
        validate(settingsBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(clientName, settingsBody.client);
        databaseInfoLogic.updateDatabaseInfoMap(clientModel);
        return JsonResponse.asEmptyBody();
    }

    private ClientModel mappingToClientModel(String clientName, ClientPart clientBody) {
        return newClientModel(clientName, clientBody);
    }

    private ClientModel newClientModel(String projectName, ClientPart clientBody) {
        ProjectInfra projectInfra = prepareProjectInfra(projectName, clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectInfra prepareProjectInfra(String clientName, ClientPart clientBody) {
        return new ProjectInfra(clientName, clientBody.dbfluteVersion, null);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientPart clientBody) {
        DatabaseSettingsPart dbSettings = clientBody.mainSchemaSettings;
        DbConnectionBox connectionBox = new DbConnectionBox(dbSettings.url, dbSettings.schema, dbSettings.user, dbSettings.password);
        return DatabaseInfoMap.createWithoutAdditional(clientBody.jdbcDriverFqcn, connectionBox);
    }
}
