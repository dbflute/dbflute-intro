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
package org.dbflute.intro.app.web.client.propbase;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientReadLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.logic.engine.EngineReadLogic;
import org.dbflute.intro.app.logic.log.LogPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * DBFluteクライアントの基本プロパティを扱うAction。<br>
 * (プロジェクト名やDBや言語のコードなど)
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientPropbaseAction extends IntroBaseAction { // prop-base means basic properties of client

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientReadLogic clientReadLogic;
    @Resource
    private DocumentPhysicalLogic documentPhysicalLogic;
    @Resource
    private LogPhysicalLogic logPhysicalLogic;
    @Resource
    private EngineReadLogic engineReadLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    /**
     * @param projectName DBFluteクライアントのプロジェクト名 e.g. maihamadb (NotNull)
     * @return DBFluteクライアントの基本プロパティの情報 (NotNull)
     */
    @Execute
    public JsonResponse<ClientPropbaseResult> index(String projectName) {
        ClientModel clientModel = clientReadLogic.findClient(projectName).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + projectName, projectName);
        });
        ClientPropbaseResult detailBean = mappingToOperationResult(clientModel);
        return asJson(detailBean);
    }

    private ClientPropbaseResult mappingToOperationResult(ClientModel clientModel) {
        ClientPropbaseResult operation = new ClientPropbaseResult();
        prepareBasic(operation, clientModel);
        String projectName = clientModel.getProjectInfra().getProjectName();
        operation.hasSchemaHtml = documentPhysicalLogic.existsSchemaHtml(projectName);
        operation.hasHistoryHtml = documentPhysicalLogic.existsHistoryHtml(projectName);
        operation.hasSyncCheckResultHtml = documentPhysicalLogic.existsSyncCheckResultHtml(projectName);
        operation.hasAlterCheckResultHtml = documentPhysicalLogic.existsAlterCheckResultHtml(projectName);
        boolean isDebugEngineVersion = engineReadLogic.getExistingVersionList().contains("1.x"); // 1.x is version for debug
        if (engineReadLogic.existsNewerVersionThan("1.2.0") || isDebugEngineVersion) {
            operation.violatesSchemaPolicy = logPhysicalLogic.existsViolationSchemaPolicyCheck(projectName);
        }
        return operation;
    }

    private void prepareBasic(ClientPropbaseResult client, ClientModel clientModel) {
        ProjectInfra projectInfra = clientModel.getProjectInfra();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        client.projectName = projectInfra.getProjectName();
        client.databaseCode = basicInfoMap.getDatabase();
        client.languageCode = basicInfoMap.getTargetLanguage();
        client.containerCode = basicInfoMap.getTargetContainer();
        client.dbfluteVersion = projectInfra.getDbfluteVersion();
    }
}
