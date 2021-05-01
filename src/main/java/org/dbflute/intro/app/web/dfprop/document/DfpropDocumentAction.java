package org.dbflute.intro.app.web.dfprop.document;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

public class DfpropDocumentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                          EditDocument
    //                                          ------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String clientName, DfpropDocumentEditBody body) {
        validate(body, messages -> {});
        final LittleAdjustmentMap littleAdjustmentMap = LittleAdjustmentMap.createAsTableNameUpperCase(body.upperCaseBasic);
        dfpropUpdateLogic.replaceLittleAdjustmentMap(clientName, littleAdjustmentMap);
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setAliasDelimiterInDbComment(body.aliasDelimiterInDbComment);
        documentMap.setDbCommentOnAliasBasis(body.dbCommentOnAliasBasis);
        documentMap.setCheckColumnDefOrderDiff(body.checkColumnDefOrderDiff);
        documentMap.setCheckDbCommentDiff(body.checkDbCommentDiff);
        documentMap.setCheckProcedureDiff(body.checkProcedureDiff);
        dfpropUpdateLogic.replaceDocumentMap(clientName, documentMap);
        return JsonResponse.asEmptyBody();
    }
}
