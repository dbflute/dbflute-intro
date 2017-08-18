package org.dbflute.intro.app.logic.document.decomment;

import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DocumentDecommentPhysicalLogicTest extends UnitIntroTestCase {

    @Test
    public void test_readMergedDecommentPickupMap() {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // TODO hakiba use testdb by jflute (2017/08/17)
        String clientProject = "introdb";
        DfDecoMapPickup pickup = logic.readMergedDecommentPickupMap(clientProject);

        // ## Assert ##
        // Assert by visual confirmation
        log(pickup);
    }
}
