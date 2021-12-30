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
package org.dbflute.intro.app.web.client;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;

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
    //                                                                              Delete
    //                                                                              ======
    public void test_delete_basic() throws Exception {
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
