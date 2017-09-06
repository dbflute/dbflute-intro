package org.dbflute.intro.app.logic.document.decomment;

import java.util.Arrays;
import java.util.List;

import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 * @author cabos
 */
public class DocumentDecommentPhysicalLogicTest extends DocumentDecommentUnitIntroTestCase {

    @Test
    public void test_getAuthor_checkAvailableUser() {
        // ## Arrange ##
        List<String> notAvailableCharList = Arrays.asList("/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0");
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String author = logic.getAuthor();

        // ## Assert ##
        assertNotNull(author);
        notAvailableCharList.forEach(ch -> assertFalse(author.contains(ch)));
    }

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
