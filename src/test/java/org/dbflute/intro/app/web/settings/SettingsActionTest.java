/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.app.web.settings;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author hakiba at garden place plaza
 */
public class SettingsActionTest extends UnitIntroTestCase {

    public void test_index() throws Exception {
        // ## Arrange ##
        SettingsAction action = new SettingsAction();
        inject(action);

        // ## Act ##
        JsonResponse<SettingsResult> response = action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        assertNotNull(response);
    }
}
