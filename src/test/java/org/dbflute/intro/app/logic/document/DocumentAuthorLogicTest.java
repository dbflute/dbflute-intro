package org.dbflute.intro.app.logic.document;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos (at sumida jazz festival)
 */
public class DocumentAuthorLogicTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                             Pre and Post Processing
    //                                                             =======================
    // -----------------------------------------------------
    //                                             Attribute
    //                                             ---------
    private String userName;

    // -----------------------------------------------------
    //                                            Processing
    //                                            ----------
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.userName = System.getProperty(DocumentAuthorLogic.USER_NAME_KEY);
    }

    @Override
    public void tearDown() throws Exception {
        recoverSystemProperty(DocumentAuthorLogic.USER_NAME_KEY, userName);
        super.tearDown();
    }

    private void recoverSystemProperty(String key, String cachedProperty) {
        if (cachedProperty != null) {
            System.setProperty(key, cachedProperty);
        } else {
            System.clearProperty(key);
        }
    }

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_getAuthor() throws Exception {
        // ## Arrange ##
        final DocumentAuthorLogic logic = new DocumentAuthorLogic();
        inject(logic);
        final String userName = "nanako.kono";
        System.setProperty(DocumentAuthorLogic.USER_NAME_KEY, userName);

        // ## Act ##
        String author = logic.getAuthor();

        // ## Assert ##
        assertEquals(userName, author);
    }
}
