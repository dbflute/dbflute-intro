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

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dfprop.DfpropReadLogic;
import org.dbflute.intro.app.logic.dfprop.schemapolicy.DfpropSchemaPolicyReadLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author deco
 * @author hakiba
 * @author prprmurakami
 */
public class DfpropSyncschemaActionTest extends UnitIntroTestCase {

    @Resource
    private DfpropReadLogic dfpropInfoLogic;
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_schemapolicy_statement_register() throws Exception {
        // ## Arrange ##
        DfpropSchemapolicyStatementAction action = new DfpropSchemapolicyStatementAction();
        inject(action);
        prepareEmptySchemaPolicyMap();
        registerStatementTestCases.forEach(testCase -> {
            if (testCase.isValid) {
                // ## Act ##
                JsonResponse<String> response = action.register(TEST_CLIENT_PROJECT, prepareBody(testCase));
                // ## Assert ##
                assertEquals(testCase.name, testCase.expected, response.getJsonResult());
                assertTrue(testCase.name, findStatementsOf(testCase.type).contains(testCase.expected));
            } else {
                // ## Act & Assert ##
                assertException(ValidationErrorException.class, () -> {
                    action.register(TEST_CLIENT_PROJECT, prepareBody(testCase));
                });
            }
        });
    }

    public void test_schemapolicy_statement_delete_tableMap() throws Exception {
        // ## Arrange ##
        List<String> before = findStatementsOf("tableMap");
        String deleteStatement = before.get(0);
        DfpropSchemapolicyStatementAction action = new DfpropSchemapolicyStatementAction();
        inject(action);

        // ## Act ##
        DfpropDeleteSchemaPolicyStatementBody body = new DfpropDeleteSchemaPolicyStatementBody();
        body.mapType = "tableMap";
        body.statement = deleteStatement;
        action.delete(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        List<String> after = findStatementsOf("tableMap");
        assertFalse(after.contains(deleteStatement));
    }

    public void test_schemapolicy_statement_delete_columnMap() throws Exception {
        // ## Arrange ##
        List<String> before = findStatementsOf("columnMap");
        String deleteStatement = before.get(0);
        DfpropSchemapolicyStatementAction action = new DfpropSchemapolicyStatementAction();
        inject(action);

        // ## Act ##
        DfpropDeleteSchemaPolicyStatementBody body = new DfpropDeleteSchemaPolicyStatementBody();
        body.mapType = "columnMap";
        body.statement = deleteStatement;
        action.delete(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        List<String> after = findStatementsOf("columnMap");
        assertFalse(after.contains(deleteStatement));
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private List<String> findStatementsOf(String mapType) {
        List<String> statementList = Collections.emptyList();
        SchemaPolicyMap policyMap = dfpropSchemaPolicyReadLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT);
        if (mapType.equals("tableMap")) {
            statementList = policyMap.tableMap.statementList;
        } else if (mapType.equals("columnMap")) {
            statementList = policyMap.columnMap.statementList;
        }
        return statementList;
    }

    private DfpropRegisterSchemaPolicyStatementBody prepareBody(RegisterStatementTestCase testCase) {
        DfpropRegisterSchemaPolicyStatementBody.ConditionPart conditionPart = new DfpropRegisterSchemaPolicyStatementBody.ConditionPart();
        conditionPart.operator = testCase.conditionOperator;
        conditionPart.conditions = testCase.conditionValues;
        DfpropRegisterSchemaPolicyStatementBody.ExpectedPart expectedPart = new DfpropRegisterSchemaPolicyStatementBody.ExpectedPart();
        expectedPart.operator = testCase.expectedOperator;
        expectedPart.expected = testCase.expectedValues;
        DfpropRegisterSchemaPolicyStatementBody body = new DfpropRegisterSchemaPolicyStatementBody();
        body.type = testCase.type;
        body.subject = testCase.subject;
        body.condition = conditionPart;
        body.expected = expectedPart;
        body.comment = testCase.comment;
        return body;
    }

    private void prepareEmptySchemaPolicyMap() {
        File srcFile = new File(getProjectDir(), TEST_RESOURCE_BASE + "/dfprop/" + "noSetting_schemaPolicyMap.dfprop");
        File destFile = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/schemaPolicyMap.dfprop");
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class RegisterStatementTestCase {
        String name;
        String type;
        String subject;
        String conditionOperator;
        List<String> conditionValues;
        String expectedOperator;
        List<String> expectedValues;
        String comment;
        String expected;
        boolean isValid = true;

        public static RegisterStatementTestCase nameOf(String name) {
            RegisterStatementTestCase testCase = new RegisterStatementTestCase();
            testCase.name = name;
            return testCase;
        }

        public RegisterStatementTestCase by(String type, String subject, String conditionOperator, List<String> conditionValues,
                String expectedOperator, List<String> expectedValues, String comment) {
            this.type = type;
            this.subject = subject;
            this.conditionOperator = conditionOperator;
            this.conditionValues = conditionValues;
            this.expectedOperator = expectedOperator;
            this.expectedValues = expectedValues;
            this.comment = comment;
            return this;
        }

        public RegisterStatementTestCase hasExpected(String expected) {
            this.expected = expected;
            return this;
        }

        public RegisterStatementTestCase isInvalid() {
            this.isValid = false;
            return this;
        }
    }

  //@formatter:off
    List<RegisterStatementTestCase> registerStatementTestCases = Arrays.asList(
        RegisterStatementTestCase
            .nameOf("add TableMap statement")
            .by(
                "tableMap",
                "tableName",
                "and",
                Collections.singletonList("$$ALL$$"),
                "and",
                Collections.singletonList("fkName is prefix:FK_$$table$$"),
                "add FK_ to the table name prefix")
            .hasExpected("if tableName is $$ALL$$ then fkName is prefix:FK_$$table$$ => add FK_ to the table name prefix"),
        RegisterStatementTestCase
            .nameOf("add ColumnMap statement")
            .by(
                "columnMap",
                "columnName",
                "and",
                Collections.singletonList("suffix:_ID"),
                "and",
                Collections.singletonList("alias is pattern:.+ID(\\(.+\\))?$"),
                "IDカラムなら論理名は \"なんとかID\" にしよう")
            .hasExpected("if columnName is suffix:_ID then alias is pattern:.+ID(\\(.+\\))?$ => IDカラムなら論理名は \"なんとかID\" にしよう"),
        RegisterStatementTestCase
            .nameOf("add multiple statement")
            .by(
                "columnMap",
                "tableName",
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                "and",
                Collections.singletonList("classification"),
                "区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)")
            .hasExpected("if tableName is prefix:CLS_ and column is pk then classification => 区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)"),
        RegisterStatementTestCase
            .nameOf("type is null")
            .by(
                null,
                "tableName",
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                "and",
                Collections.singletonList("classification"),
                "区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)")
            .isInvalid(),
        RegisterStatementTestCase
            .nameOf("subject is null")
            .by(
                "columnMap",
                null,
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                "and",
                Collections.singletonList("classification"),
                "区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)")
            .isInvalid(),
        RegisterStatementTestCase
            .nameOf("condition is null")
            .by(
                "columnMap",
                "tableName",
                null,
                null,
                "and",
                Collections.singletonList("classification"),
                "区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)")
            .isInvalid(),
        RegisterStatementTestCase
            .nameOf("expected is null")
            .by(
                "columnMap",
                "tableName",
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                null,
                null,
                "区分値テーブルだったら、PKは区分値カラムのはず (dfpropを確認しよう)")
            .isInvalid(),
        RegisterStatementTestCase
            .nameOf("comment is null")
            .by(
                "columnMap",
                "tableName",
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                "and",
                Collections.singletonList("classification"),
                null)
            .isInvalid(),
        RegisterStatementTestCase
            .nameOf("comment is empty")
            .by(
                "columnMap",
                "tableName",
                "and",
                Arrays.asList("prefix:CLS_", "column is pk"),
                "and",
                Collections.singletonList("classification"),
                "")
            .isInvalid()
    );
    //@formatter:on
}
