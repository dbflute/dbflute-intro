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
package org.dbflute.intro.app.web.welcome;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.engine.EngineInstallLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author hakiba
 */
public class WelcomeAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private EngineInstallLogic engineInstallLogic;
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> create(WelcomeCreateBody welcomeCreateBody) {
        validate(welcomeCreateBody, messages -> {
            String projectName = welcomeCreateBody.client.projectName;
            if (clientInfoLogic.getProjectList().contains(projectName)) {
                messages.addErrorsWelcomeClientAlreadyExists("projectName", projectName); // TODO: hakiba refactor type-safe (2016/10/10)
            }
        });
        String latestVersion = publicPropertiesLogic.findProperties().getDBFluteLatestReleaseVersion();
//        engineInstallLogic.downloadUnzipping(latestVersion);
        ClientModel clientModel = mappingToClientModel(welcomeCreateBody.client);
        if (welcomeCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private ClientModel mappingToClientModel(WelcomeCreateBody.ClientPart clientBody) {
        return newClientModel(clientBody);
    }

    private ClientModel newClientModel(WelcomeCreateBody.ClientPart clientBody) {
        ProjectInfra projectInfra = prepareProjectMeta(clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        return new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
    }

    private ProjectInfra prepareProjectMeta(WelcomeCreateBody.ClientPart clientBody) {
        return new ProjectInfra(clientBody.projectName, clientBody.dbfluteVersion, clientBody.jdbcDriver.fileName, clientBody.jdbcDriver.data);
    }

    private BasicInfoMap prepareBasicInfoMap(WelcomeCreateBody.ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(WelcomeCreateBody.ClientPart clientBody) {
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

}
