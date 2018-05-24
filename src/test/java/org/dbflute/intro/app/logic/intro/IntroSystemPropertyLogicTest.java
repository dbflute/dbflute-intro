package org.dbflute.intro.app.logic.intro;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos
 */
public class IntroSystemPropertyLogicTest extends UnitIntroTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearCache();
    }

    private void clearCache() {
        IntroSystemPropertyLogic.SYSTEM_PROPERTY_MAP.remove(IntroSystemPropertyLogic.DECOMMENT_SERVER_KEY);
    }

    public void test_isDecommentServer() throws Exception {
        // ### Arrange ###
        IntroSystemPropertyLogic logic = new IntroSystemPropertyLogic();
        inject(logic);
        System.clearProperty("intro.decomment.server");
        System.setProperty("intro.decomment.server", "true");

        // ### Act ###
        // ### Assert ###
        assertTrue(logic.isDecommentServerKey());
    }

    public void test_isNotDecommentServer() throws Exception {
        // ### Arrange ###
        IntroSystemPropertyLogic logic = new IntroSystemPropertyLogic();
        inject(logic);
        System.clearProperty("intro.decomment.server");
        System.setProperty("intro.decomment.server", "false");

        // ### Act ###
        // ### Assert ###
        assertFalse(logic.isDecommentServerKey());
    }
}
