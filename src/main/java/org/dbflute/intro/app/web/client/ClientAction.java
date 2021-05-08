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

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.dfprop.database.DatabaseInfoLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientPart;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 * @author hakiba
 * @author cabos
 * @author subaru
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
    private ClientPhysicalLogic clientPhysicalLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private DatabaseInfoLogic databaseInfoLogic;
    @Resource
    private ClientUpdateAssist clientAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {
            moreValidateCreate(messages, clientCreateBody);
        });
        ClientModel clientModel = mappingToClientModel(clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    // #needs_fix anyone ClientCreateBody in edit()? should be ClientUpdateBody? by jflute (2021/05/08)
    // #needs_fix anyone path parameter "projectName" is unused...needed? or should validate anything? by jflute (2021/05/08)
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String projectName, ClientCreateBody clientCreateBody) {
        // #needs_fix anyone should more-validate? (like create()) by jflute (2021/05/08)
        validate(clientCreateBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.updateClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> delete(String projectName) {
        clientUpdateLogic.deleteClient(projectName);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    private void moreValidateCreate(IntroMessages messages, ClientCreateBody clientCreateBody) {
        clientAssist.moreValidateCreate(messages, clientCreateBody);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private ClientModel mappingToClientModel(ClientPart clientBody) {
        return clientAssist.mappingToClientModel(clientBody);
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private void testConnectionIfPossible(ClientModel clientModel) {
        clientAssist.testConnectionIfPossible(clientModel);
    }
}
