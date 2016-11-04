/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.web.client;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

import java.util.List;

/**
 * @author jflute
 */
public class ClientActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                               Test
    //                                                                              ======
    public void test_list_success() throws Exception {
        // ## Arrange ##
        ClientAction action = new ClientAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<ClientRowResult>> response = action.list();

        // ## Assert ##
        showJson(response);
        TestingJsonData<List<ClientRowResult>> jsonData = validateJsonData(response);
        List<ClientRowResult> detailBeanList = jsonData.getJsonBean();
        assertHasAnyElement(detailBeanList);
    }
}
