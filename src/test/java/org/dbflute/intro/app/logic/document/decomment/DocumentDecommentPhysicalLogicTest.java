package org.dbflute.intro.app.logic.document.decomment;

import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DocumentDecommentPhysicalLogicTest extends DocumentDecommentUnitIntroTestCase {

    @Test
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
