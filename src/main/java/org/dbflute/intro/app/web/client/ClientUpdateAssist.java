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
package org.dbflute.intro.app.web.client;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.logic.client.ClientReadLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.dfprop.database.DatabaseInfoLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientPart;
import org.dbflute.intro.dbflute.allcommon.CDef.TargetDatabase;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientUpdateAssist {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientReadLogic clientReadLogic;
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private DatabaseInfoLogic databaseInfoLogic;

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    public void moreValidateCreate(IntroMessages messages, ClientCreateBody clientCreateBody) {
        String clientName = clientCreateBody.client.projectName;
        ClientPart client = clientCreateBody.client;
        if (clientReadLogic.getProjectNameList().contains(clientName)) {
            messages.addErrorsWelcomeClientAlreadyExists("projectName", clientName);
        }
        TargetDatabase databaseCd = client.databaseCode;
        if (databaseCd != null && !databaseInfoLogic.isEmbeddedJar(databaseCd) && Objects.isNull(client.jdbcDriver)) {
            messages.addErrorsDatabaseNeedsJar("database", databaseCd.alias());
        }
        Optional.ofNullable(client.jdbcDriver)
                .map(driverPart -> driverPart.fileName)
                .filter(s -> StringUtils.isNotEmpty(s) && !s.endsWith(".jar"))
                .ifPresent(fileName -> messages.addErrorsDatabaseNeedsJar("jdbcDriver", fileName));
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    public ClientModel mappingToClientModel(ClientPart clientBody) {
        return newClientModel(clientBody);
    }

    private ClientModel newClientModel(ClientPart clientBody) {
        ProjectInfra projectInfra = prepareProjectInfra(clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectInfra prepareProjectInfra(ClientPart clientBody) {
        String projectName = clientBody.projectName;
        if (Objects.isNull(clientBody.jdbcDriver)) {
            return new ProjectInfra(projectName, clientBody.dbfluteVersion);
        }
        ExtlibFile extlibFile =
                clientPhysicalLogic.createExtlibFile(projectName, clientBody.jdbcDriver.fileName, clientBody.jdbcDriver.data);
        return new ProjectInfra(projectName, clientBody.dbfluteVersion, extlibFile);
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

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    public void testConnectionIfPossible(ClientModel clientModel) {
        String dbfluteVersion = clientModel.getProjectInfra().getDbfluteVersion();
        OptionalThing<String> jdbcDriverJarPath = clientModel.getProjectInfra().getJdbcDriverExtlibFile().map(ExtlibFile::getCanonicalPath);
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        testConnectionLogic.testConnection(dbfluteVersion, jdbcDriverJarPath, databaseInfoMap);
    }
}
