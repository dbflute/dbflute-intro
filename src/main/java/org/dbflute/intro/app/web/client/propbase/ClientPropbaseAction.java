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

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.logic.engine.EngineInfoLogic;
import org.dbflute.intro.app.logic.log.LogPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientPropbaseAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;
    @Resource
    private LogPhysicalLogic logPhysicalLogic;
    @Resource
    private EngineInfoLogic engineInfoLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<ClientPropbaseResult> index(String clientName) {
        ClientModel clientModel = clientInfoLogic.findClient(clientName).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientName, clientName);
        });
        ClientPropbaseResult detailBean = mappingToOperationResult(clientModel);
        return asJson(detailBean);
    }

    private ClientPropbaseResult mappingToOperationResult(ClientModel clientModel) {
        ClientPropbaseResult operation = new ClientPropbaseResult();
        prepareBasic(operation, clientModel);
        String clientName = clientModel.getProjectInfra().getProjectName();
        operation.hasSchemaHtml = documentLogic.existsSchemaHtml(clientName);
        operation.hasHistoryHtml = documentLogic.existsHistoryHtml(clientName);
        operation.hasSyncCheckResultHtml = documentLogic.existsSyncCheckResultHtml(clientName);
        operation.hasAlterCheckResultHtml = documentLogic.existsAlterCheckResultHtml(clientName);
        boolean isDebugEngineVersion = engineInfoLogic.getExistingVersionList().contains("1.x"); // 1.x is version for debug
        if (engineInfoLogic.existsNewerVersionThan("1.2.0") || isDebugEngineVersion) {
            operation.violatesSchemaPolicy = logPhysicalLogic.existsViolationSchemaPolicyCheck(clientName);
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
