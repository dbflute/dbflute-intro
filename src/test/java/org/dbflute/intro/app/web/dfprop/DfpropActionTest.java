/*
 * Copyright 2014-2017 the original author or authors.
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
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 */
public class DfpropActionTest extends UnitIntroTestCase {

    @Resource
    private FlutyFileLogic flutyFileLogic;

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
}
