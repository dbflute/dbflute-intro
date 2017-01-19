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
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// TODO hakiba make settings package by jflute (2017/01/19)
/**
 * @author hakiba
 * @author jflute
 */
public class ClientSettingsAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                              Settings
    //                                              --------
    @Execute
    public JsonResponse<ClientSettingsResult> index(String clientProject) {
        // TODO hakiba recyle orElseThrow() by jflute (2017/01/12)
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientSettingsResult result = mappingToSettingsResult(clientModel);
        return asJson(result);
    }

    private ClientSettingsResult mappingToSettingsResult(ClientModel clientModel) {
        ClientSettingsResult result = new ClientSettingsResult();
        ProjectMeta projectMeta = clientModel.getProjectMeta();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        result.projectName = projectMeta.getClientProject();
        result.databaseCode = basicInfoMap.getDatabase();
        result.languageCode = basicInfoMap.getTargetLanguage();
        result.containerCode = basicInfoMap.getTargetContainer();
        result.packageBase = basicInfoMap.getPackageBase();
        result.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        result.dbfluteVersion = projectMeta.getDbfluteVersion();
        result.jdbcDriverJarPath = projectMeta.getJdbcDriverJarPath().orElse(null);
        DbConnectionBox dbConnectionBox = clientModel.getDatabaseInfoMap().getDbConnectionBox();
        result.mainSchemaSettings = new ClientSettingsResult.DatabaseSettingsPart();
        result.mainSchemaSettings.url = dbConnectionBox.getUrl();
        result.mainSchemaSettings.schema = dbConnectionBox.getSchema();
        result.mainSchemaSettings.user = dbConnectionBox.getUser();
        result.mainSchemaSettings.password = dbConnectionBox.getPassword();

        return result;
    }

    // -----------------------------------------------------
    //                                                  Edit
    //                                                  ----
    @Execute
    public JsonResponse<Void> edit(String projectName, ClientUpdateBody clientBody) {
        validate(clientBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(projectName, clientBody.client);
        clientUpdateLogic.updateClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    private ClientModel mappingToClientModel(String projectName, ClientUpdateBody.ClientPart clientBody) {
        return newClientModel(projectName, clientBody);
    }

    private ClientModel newClientModel(String projectName, ClientUpdateBody.ClientPart clientBody) {
        ProjectMeta projectMeta = prepareProjectMeta(projectName, clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectMeta, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectMeta prepareProjectMeta(String projectName, ClientUpdateBody.ClientPart clientBody) {
        return new ProjectMeta(projectName, clientBody.dbfluteVersion, clientBody.jdbcDriverJarPath);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientUpdateBody.ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientUpdateBody.ClientPart clientBody) {
        // TODO jflute next review (2017/01/12)
        // TODO jflute mainSchemaSettings cannot be null (2017/01/12)
        // e.g.
        //DatabaseSettingsPart dbSettings = clientBody.mainSchemaSettings;
        //DbConnectionBox connectionBox = new DbConnectionBox(dbSettings.url, dbSettings.schema, dbSettings.user, dbSettings.password);
        //return DatabaseInfoMap.createWithoutAdditinal(clientBody.jdbcDriverFqcn, connectionBox);

        return OptionalThing.ofNullable(clientBody.mainSchemaSettings, () -> {}).map(databaseBody -> {
            DbConnectionBox connectionBox =
                    new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            AdditionalSchemaMap additionalSchemaMap = new AdditionalSchemaMap(new LinkedHashMap<>()); // #pending see the class code
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, connectionBox, additionalSchemaMap);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }
}
