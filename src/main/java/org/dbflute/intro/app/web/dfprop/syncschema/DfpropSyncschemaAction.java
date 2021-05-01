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
package org.dbflute.intro.app.web.dfprop.syncschema;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author prprmurakami
 */
public class DfpropSyncschemaAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;

    // -----------------------------------------------------
    //                                        EditSyncSchema
    //                                        --------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String clientName, DfpropEditSyncSchemaBody body) {
        validate(body, messages -> {});
        final DbConnectionBox dbConnectionBox = new DbConnectionBox(body.url, body.schema, body.user, body.password);
        final SchemaSyncCheckMap schemaSyncCheckMap = new SchemaSyncCheckMap(dbConnectionBox, body.isSuppressCraftDiff);
        dfpropUpdateLogic.replaceSchemaSyncCheckMap(clientName, schemaSyncCheckMap);
        return JsonResponse.asEmptyBody();
    }
}
