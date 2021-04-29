package org.dbflute.intro.app.web.dfprop.syncschema;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

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
