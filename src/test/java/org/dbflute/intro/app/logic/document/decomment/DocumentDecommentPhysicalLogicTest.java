package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;

/**
 * @author hakiba
 * @author cabos
 * @author jflute
 */
public class DocumentDecommentPhysicalLogicTest extends DocumentDecommentUnitIntroTestCase {

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void test_saveDecommentPieceMap() {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        logic.saveDecommentPieceMap(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        File pieceDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/schema/decomment/piece");
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        String regex = "^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$";
        Pattern pattern = Pattern.compile(regex);
        Arrays.asList(pieceMaps).forEach(fileName -> {
            log(fileName);
            assertTrue(pattern.matcher(fileName).find());
        });
    }

    // map:{
    //    ; formatVersion = 1.0
    //    ; author = cabos
    //    ; decommentDatetime = 2017/12:31 12:34:56
    //    ; merged = false
    //    ; decoMap = map:{
    //        ; MEMBER = map:{
    //            ; MEMBER_NAME = map:{
    //                ; decomment = piari
    //                ; databaseComment = sea
    //                ; previousWholeComment = seasea
    //                ; commentVersion = 1
    //                ; authorList = list: { cabos }
    //            }
    //        }
    //    }
    // }
    private DfDecoMapPiece createDfDecoMapPiece() {
        DfDecoMapPiece decoMapPiece = new DfDecoMapPiece();
        decoMapPiece.setFormatVersion("1.0");
        decoMapPiece.setAuthor("cabos");
        decoMapPiece.setDecommentDatetime(currentLocalDateTime());
        decoMapPiece.setMerged(false);
        decoMapPiece.setDecoMap(createDfDecoMapTablePart());
        return decoMapPiece;
    }

    private DfDecoMapTablePart createDfDecoMapTablePart() {
        DfDecoMapTablePart decoMapTablePart = new DfDecoMapTablePart();
        decoMapTablePart.setTableName("MEMBER");
        decoMapTablePart.setColumns(Collections.singletonList(createDfDecoMapColumnPart()));
        return decoMapTablePart;
    }

    private DfDecoMapColumnPart createDfDecoMapColumnPart() {
        DfDecoMapColumnPart decoMapColumnPart = new DfDecoMapColumnPart();
        decoMapColumnPart.setColumnName("MEMBER_NAME");
        decoMapColumnPart.setProperties(Collections.singletonList(createColumnPropertyPart()));
        return decoMapColumnPart;
    }

    private DfDecoMapColumnPart.ColumnPropertyPart createColumnPropertyPart() {
        DfDecoMapColumnPart.ColumnPropertyPart columnPropertyPart = new DfDecoMapColumnPart.ColumnPropertyPart();
        columnPropertyPart.setDecomment("piari");
        columnPropertyPart.setDatabaseComment("sea");
        columnPropertyPart.setPreviousWholeComment("seasea");
        columnPropertyPart.setCommentVersion(1);
        columnPropertyPart.setAuthorList(Collections.singletonList("cabos"));
        return columnPropertyPart;
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public void test_getAuthor() throws Exception {
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
    public void test_readMergedDecommentPickupMap_init() throws Exception {
        // ## Arrange ##
        // TODO hakiba null pointer at init state by jflute (2017/10/05)
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedDecommentPickupMap(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
    }

    public void test_readMergedDecommentPickupMap_prepared() throws Exception {
        // done hakiba put test data in test/resources by hakiba (2017/08/18)
        // ## Arrange ##
        prepareTestFiles();
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
