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
package org.dbflute.intro.app.web.intro;

import java.util.Map;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute at garden place plaza
 */
public class IntroActionTest extends UnitIntroTestCase {

    public void test_manifest() {
        // ## Arrange ##
        IntroAction action = new IntroAction();
        inject(action);

        // ## Act ##
        JsonResponse<Map<String, Object>> response = action.manifest(); // cannot get at local development

        // ## Assert ##
        showJson(response);
        validateJsonData(response);
    }

    public void test_configuration() {
        // ## Arrange ##
        IntroAction action = new IntroAction();
        inject(action);

        // ## Act ##
        JsonResponse<Map<String, Object>> response = action.configuration();

        // ## Assert ##
        showJson(response);
        Map<String, Object> resultMap = validateJsonData(response).getJsonResult();
        assertTrue(resultMap.containsKey("serverUrl"));
        assertTrue(resultMap.containsKey("apiServerUrl"));
    }
}
