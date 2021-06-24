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
package org.dbflute.intro.app.web.dfprop.schemapolicy.statement;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.schemapolicy.DfpropSchemaPolicyReadLogic;
import org.dbflute.intro.app.logic.dfprop.schemapolicy.DfpropSchemaPolicyUpdateLogic;
import org.dbflute.intro.app.logic.exception.SubjectableMapTypeNotExistException;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.mylasta.appcls.AppCDef.SubjectableMapType;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author prprmurakami
 * @author jflute
 */
public class DfpropSchemapolicyStatementAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;
    @Resource
    private DfpropSchemaPolicyUpdateLogic dfpropSchemaPolicyUpdateLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                         AddSchemaPolicyCheckStatement
    //                         -----------------------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<String> register(String clientName, DfpropRegisterSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        SchemaPolicyStatement statement = mappingToStatement(body);
        String builtStatement = dfpropSchemaPolicyUpdateLogic.registerSchemaPolicyStatement(clientName, statement);
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
    public JsonResponse<List<String>> subject(DfpropSchemapolicyStatementSubjectForm form) {
        validate(form, message -> {});
        if (form.mapType == SubjectableMapType.Table) {
            return asJson(dfpropSchemaPolicyReadLogic.getStatementTableMapSubjectList() // 
                    .stream()
                    .map(ject -> ject.getTitle())
                    .collect(Collectors.toList()));
        } else if (form.mapType == SubjectableMapType.Column) {
            return asJson(dfpropSchemaPolicyReadLogic.getStatementColumnMapSubjectList() //
                    .stream()
                    .map(ject -> ject.getTitle())
                    .collect(Collectors.toList()));
        } else {
            throw new SubjectableMapTypeNotExistException("There is no matching SubjectableMapType. mapType: " + form.mapType);
        }
    }

    // -----------------------------------------------------
    //                      DeleteSchemaPolicyCheckStatement
    //                      --------------------------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> delete(String clientName, DfpropDeleteSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        dfpropSchemaPolicyUpdateLogic.deleteSchemaPolicyStatement(clientName, body.mapType, body.statement);
        return JsonResponse.asEmptyBody();
    }
}
