/*
 * Copyright 2014-2019 the original author or authors.
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
package org.dbflute.intro.app.web.dfprop;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author deco
 * @author hakiba
 */
public class DfpropActionTest extends UnitIntroTestCase {

    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_index_success() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<DfpropBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<DfpropBean>> jsonData = validateJsonData(response);
        List<DfpropBean> dfpropList = jsonData.getJsonResult();
        assertHasAnyElement(dfpropList);
        dfpropList.forEach(dfpropBean -> {
            log(dfpropBean.fileName);
            assertTrue(dfpropBean.fileName.endsWith(".dfprop"));
        });
    }

    public void test_update_success() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);

        DfpropUpdateBody body = new DfpropUpdateBody();
        File dfpropDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/");
        File dfpropBefore = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"))[0];
        String fileName = dfpropBefore.getName();
        body.content = "content";

        // ## Act ##
        action.update(TEST_CLIENT_PROJECT, fileName, body);

        // ## Assert ##
        File dfpropAfter = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/" + fileName);
        String content = flutyFileLogic.readFile(dfpropAfter);
        log(fileName, content);
        assertEquals(body.content, content);
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

    public void test_schemapolicy_statement_register() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);
        prepareEmptySchemaPolicyMap();
        registerStatementTestCases.forEach(testCase -> {
            if (testCase.isValid) {
                // ## Act ##
                JsonResponse<String> response = action.schemapolicyStatementRegister(TEST_CLIENT_PROJECT, prepareBody(testCase));
                // ## Assert ##
                assertEquals(testCase.name, testCase.expected, response.getJsonResult());
                assertTrue(testCase.name, findStatementsOf(testCase.type).contains(testCase.expected));
            } else {
                // ## Act & Assert ##
                assertException(ValidationErrorException.class, () -> {
                    action.schemapolicyStatementRegister(TEST_CLIENT_PROJECT, prepareBody(testCase));
                });
            }
        });
    }

    private List<String> findStatementsOf(String mapType) {
        List<String> statementList = Collections.emptyList();
        SchemaPolicyMap policyMap = dfpropInfoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT);
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
}
