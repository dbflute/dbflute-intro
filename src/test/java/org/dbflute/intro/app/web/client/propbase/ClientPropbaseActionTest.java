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
package org.dbflute.intro.app.web.client.propbase;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientPropbaseActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    public void test_propbase_success() throws Exception {
        // ## Arrange ##
        ClientPropbaseAction action = new ClientPropbaseAction();
        inject(action);

        // ## Act ##
        JsonResponse<ClientPropbaseResult> response = action.index(UnitIntroTestCase.TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        TestingJsonData<ClientPropbaseResult> jsonData = validateJsonData(response);
        ClientPropbaseResult operation = jsonData.getJsonResult();
        assertEquals(UnitIntroTestCase.TEST_CLIENT_PROJECT, operation.projectName);
    }

    // ===================================================================================
    //                                                                             Failure
    //                                                                             =======
    public void test_propbase_failure() throws Exception {
        // ## Arrange ##
        ClientPropbaseAction action = new ClientPropbaseAction();
        inject(action);

        // ## Act ##
        try {
            action.index("akirakaniokasii_project_name");
        } catch (ClientNotFoundException ignore) {
            markHere("client_not_found");
        }
        // ## Assert ##
        assertMarked("client_not_found");
    }
}
