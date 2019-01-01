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
package org.dbflute.intro.app.web.playsql;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author jflute
 */
public class PlaysqlActionTest extends UnitIntroTestCase {

    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    @Test
    public void test_list_success() throws Exception {
        // ## Arrange ##
        PlaysqlAction action = new PlaysqlAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<PlaysqlBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<PlaysqlBean>> jsonData = validateJsonData(response);
        List<PlaysqlBean> beans = jsonData.getJsonResult();
        assertHasAnyElement(beans);
        beans.forEach(bean -> {
            log(bean.fileName);
            assertTrue(bean.fileName.endsWith(".sql"));
        });
    }

    @Test
    public void test_update_success() throws Exception {
        // ## Arrange ##
        PlaysqlAction action = new PlaysqlAction();
        inject(action);

        PlaysqlUpdateBody body = new PlaysqlUpdateBody();
        File playsqlDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/");
        File playsqlBefore = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"))[0];
        String fileName = playsqlBefore.getName();
        body.content = "content";

        // ## Act ##
        action.update(TEST_CLIENT_PROJECT, fileName, body);

        // ## Assert ##
        File playsqlAfter = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/" + fileName);
        String content = flutyFileLogic.readFile(playsqlAfter);
        log(fileName, content);
        assertEquals(body.content, content);
    }
}
