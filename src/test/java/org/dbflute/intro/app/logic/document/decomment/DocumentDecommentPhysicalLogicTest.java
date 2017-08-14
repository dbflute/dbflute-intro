package org.dbflute.intro.app.logic.document.decomment;

import java.util.List;

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
        // TODO hakiba fix assert by hakiba (2017/08/14)
        pieces.forEach(dfDecoMapPiece -> log(dfDecoMapPiece.convertMap()));
    }
}
