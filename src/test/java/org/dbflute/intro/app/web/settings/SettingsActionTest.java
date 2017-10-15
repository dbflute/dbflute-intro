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
