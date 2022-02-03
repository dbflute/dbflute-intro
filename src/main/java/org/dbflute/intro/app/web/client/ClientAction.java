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

import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * DBFluteクライアントをまるごと制御するAction。(作成、削除など) <br>
 * クライアント内の個々の機能の制御は個々のActionにて。ここではまるごと制御のみ。
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
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private ClientUpdateAssist clientAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        // 個々の処理、それぞれまあまあデカいのでAssistに切り出している
        validate(clientCreateBody, messages -> {
            clientAssist.moreValidateCreate(messages, clientCreateBody);
        });
        ClientModel clientModel = clientAssist.mappingToClientModel(clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            clientAssist.testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    // the function of wholly updating client does not exist in Intro by jflute (2022/01/15)
    //@NotAvailableDecommentServer
    //@Execute
    //public JsonResponse<Void> edit(String projectName, ClientCreateBody clientCreateBody) {

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> delete(String projectName) {
        clientUpdateLogic.deleteClient(projectName);
        return JsonResponse.asEmptyBody();
    }
}
