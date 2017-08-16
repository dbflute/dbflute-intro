package org.dbflute.intro.app.logic.document.decomment;

import java.util.List;

import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DocumentDecommentPhysicalLogicTest extends UnitIntroTestCase {

    @Test
    public void test_readAllPiece() {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String clientProject = "introdb";
        List<DfDecoMapPiece> pieces = logic.readAllDecommentPieceMap(clientProject);

        // ## Assert ##
        // Assert by visual confirmation
        pieces.forEach(piece -> log(piece));
    }

    @Test
    public void test_readMergedDecommentPickupMap() {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String clientProject = "introdb";
        DfDecoMapPickup pickup = logic.readMergedDecommentPickupMap(clientProject);

        // ## Assert ##
        // Assert by visual confirmation
        log(pickup);
    }
}
