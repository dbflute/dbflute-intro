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
