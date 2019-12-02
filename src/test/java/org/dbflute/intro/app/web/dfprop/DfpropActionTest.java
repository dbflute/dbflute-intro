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
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

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

    public void test_schemapolicy_statement_register_tableMap() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);
        prepareEmptySchemaPolicyMap();
        String type = "tableMap";
        String statement = "if tableName is $$ALL$$ then fkName is prefix:FK_$$table$$";

        // ## Act ##
        DfpropRegisterSchemaPolicyStatementBody body = new DfpropRegisterSchemaPolicyStatementBody();
        body.type = type;
        body.statement = statement;
        action.schemapolicyStatementRegister(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        SchemaPolicyTableMap tableMap = dfpropInfoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap;
        assertTrue(tableMap.statementList.contains(statement));
    }

    public void test_schemapolicy_statement_register_columnMap() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);
        prepareEmptySchemaPolicyMap();
        String type = "columnMap";
        String statement = "if columnName is suffix:_ID then alias is pattern:.+ID(\\(.+\\))?$ => IDカラムなら論理名は \"なんとかID\" にしよう";

        // ## Act ##
        DfpropRegisterSchemaPolicyStatementBody body = new DfpropRegisterSchemaPolicyStatementBody();
        body.type = type;
        body.statement = statement;
        action.schemapolicyStatementRegister(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        SchemaPolicyColumnMap columnMap = dfpropInfoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap;
        assertTrue(columnMap.statementList.contains(statement));
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
