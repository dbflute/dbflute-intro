package org.dbflute.intro.app.logic.document.decomment;

import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;

/**
 * @author hakiba
 * @author cabos
 * @author jflute
 */
public class DocumentDecommentPhysicalLogicTest extends DocumentDecommentUnitIntroTestCase {

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public void test_getAuthor() {
        // ## Arrange ##
        registerMock(new DocumentAuthorLogic() {
            @Override
            public String getAuthor() {
                return "ca/<bo s";
            }
        });
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String author = logic.getAuthor();

        // ## Assert ##
        log("author: {}", author);
        assertNotNull(author);
        assertEquals("ca__bo_s", author);
        DocumentDecommentPhysicalLogic.REPLACE_CHAR_MAP.keySet().forEach(ch -> assertFalse(author.contains(ch)));
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_readMergedDecommentPickupMap() {
        // done hakiba put test data in test/resources by hakiba (2017/08/18)
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedDecommentPickupMap(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
    }
}
