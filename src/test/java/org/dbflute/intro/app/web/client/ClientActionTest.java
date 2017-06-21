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
package org.dbflute.intro.app.web.client;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;

import java.io.File;
import java.util.List;

/**
 * @author jflute
 */
public class ClientActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

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
        List<ClientRowResult> detailBeanList = jsonData.getJsonResult();
        assertHasAnyElement(detailBeanList);
    }

    public void test_operation_success() throws Exception {
        // ## Arrange ##
        ClientAction action = new ClientAction();
        inject(action);

        // ## Act ##
        JsonResponse<ClientOperationResult> response = action.operation(UnitIntroTestCase.TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        TestingJsonData<ClientOperationResult> jsonData = validateJsonData(response);
        ClientOperationResult operation = jsonData.getJsonResult();
        assertEquals(UnitIntroTestCase.TEST_CLIENT_PROJECT, operation.projectName);
    }

    public void test_operation_fail() throws Exception {
        // ## Arrange ##
        ClientAction action = new ClientAction();
        inject(action);

        // ## Act ##
        try {
            action.operation("akirakaniokasii_project_name");
        } catch (ClientNotFoundException ignore) {
            markHere("client_not_found");
        }
        // ## Assert ##
        assertMarked("client_not_found");
    }

    public void test_delete_success() throws Exception {
        // ## Arrange ##
        ClientAction action = new ClientAction();
        inject(action);
        File client = introPhysicalLogic.findClientDir(UnitIntroTestCase.TEST_CLIENT_PROJECT);
        assertTrue(client.exists());

        // ## Act ##
        action.delete(UnitIntroTestCase.TEST_CLIENT_PROJECT);

        // ## Assert ##
        assertFalse(client.exists());
    }
}
