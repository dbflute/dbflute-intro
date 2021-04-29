package org.dbflute.intro.app.web.dfprop.schemapolicy.statement;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

public class DfpropSchemapolicyStatementAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;

    // -----------------------------------------------------
    //                         AddSchemaPolicyCheckStatement
    //                         -----------------------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<String> register(String clientName, DfpropRegisterSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        SchemaPolicyStatement statement = mappingToStatement(body);
        String builtStatement = dfpropUpdateLogic.registerSchemaPolicyStatement(clientName, statement);
        return asJson(builtStatement);
    }

    private SchemaPolicyStatement mappingToStatement(DfpropRegisterSchemaPolicyStatementBody body) {
        SchemaPolicyStatement.Condition condition = new SchemaPolicyStatement.Condition(body.condition.operator, body.condition.conditions);
        SchemaPolicyStatement.Expected expected = new SchemaPolicyStatement.Expected(body.expected.operator, body.expected.expected);
        return new SchemaPolicyStatement(body.type, body.subject, condition, expected, body.comment);
    }

    // -----------------------------------------------------
    //                       GetschemapolicyStatementSubject
    //                       -------------------------------
    @Execute
    public JsonResponse<List<String>> subject() {
        return asJson(dfpropInfoLogic.getStatementSubjectList());
    }

    // -----------------------------------------------------
    //                      DeleteSchemaPolicyCheckStatement
    //                      --------------------------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> delete(String clientName, DfpropDeleteSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        dfpropUpdateLogic.deleteSchemaPolicyStatement(clientName, body.mapType, body.statement);
        return JsonResponse.asEmptyBody();
    }

}
