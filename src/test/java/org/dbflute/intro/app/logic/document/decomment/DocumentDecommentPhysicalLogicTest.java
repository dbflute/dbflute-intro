package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author hakiba
 * @author jflute
 * @author cabos at garden place plaza
 * @author deco
 */
public class DocumentDecommentPhysicalLogicTest extends UnitIntroTestCase {

    // TODO cabos move test (2017/11/18)

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    // -----------------------------------------------------
    //                                        save piece map
    //                                        --------------
    public void test_saveDecommentPieceMap_init() throws Exception {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece"));

        // ## Act ##
        logic.saveDecommentPiece(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        assertTrue(pieceMaps.length > 0);
        Arrays.asList(pieceMaps).forEach(fileName -> {
            log(fileName);
            assertTrue(expFileNamePattern.matcher(fileName).find());
        });
    }

    public void test_saveDecommentPieceMap_prepared() throws Exception {
        // ## Arrange ##
        prepareTestDecommentFiles();

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
        inject(physicalLogic);

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece"));

        // ## Act ##
        logic.saveDecommentPiece(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        assertTrue(pieceMaps.length > 0);
        Arrays.stream(pieceMaps).forEach(fileName -> {
            log(fileName);
            assertTrue(expFileNamePattern.matcher(fileName).find());
        });
    }

    private DfDecoMapPiece createDfDecoMapPiece() {
        DfDecoMapPiece decoMapPiece = new DfDecoMapPiece();
        decoMapPiece.setFormatVersion("1.0");
        decoMapPiece.setTableName("MEMBER");
        decoMapPiece.setColumnName("MEMBER_NAME");
        decoMapPiece.setTargetType(DfDecoMapPieceTargetType.Column);
        decoMapPiece.setDecomment("piari");
        decoMapPiece.setDatabaseComment("sea");
        decoMapPiece.setCommentVersion(1L);
        decoMapPiece.setAuthorList(Collections.singletonList("cabos"));
        decoMapPiece.setPieceCode("FE893L1");
        decoMapPiece.setPieceDatetime(currentLocalDateTime());
        decoMapPiece.setPieceOwner("cabos");
        decoMapPiece.setPreviousPieceList(Collections.singletonList("FE893L1"));
        return decoMapPiece;
    }

    // -----------------------------------------------------
    //                                             file name
    //                                             ---------
    //    public void test_buildPieceFileName() throws Exception {
    //        // ## Arrange ##
    //        final String sampleTableName = "EBISU_GARDEN_PLACE";
    //        final String sampleColumnName = "PLAZA";
    //        final String sampleAuthor = "cabos";
    //        final String samplePieceCode = "FE893L1";
    //        final LocalDateTime current = currentLocalDateTime();
    //        registerMock(new SimpleTimeManager() {
    //            @Override
    //            public LocalDateTime currentDateTime() {
    //                return current;
    //            }
    //        });
    //
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //
    //        // e.g decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap
    //        final String expFileName =
    //            "decomment-piece-" + sampleTableName + "-" + sampleColumnName + "-" + logic.getCurrentDateStr() + "-" + sampleAuthor + "-"
    //                + samplePieceCode + ".dfmap";
    //
    //        // ## Act ##
    //        final String fileName = logic.buildPieceFileName(sampleTableName, sampleColumnName, sampleAuthor, samplePieceCode);
    //
    //        // ## Assert ##
    //        assertEquals(fileName, expFileName);
    //    }
    //
    //    public void test_getCurrentDateStr() throws Exception {
    //        // ## Arrange ##
    //        final LocalDateTime current = currentLocalDateTime();
    //        registerMock(new SimpleTimeManager() {
    //            @Override
    //            public LocalDateTime currentDateTime() {
    //                return current;
    //            }
    //        });
    //        // e.g 20170316-123456-789
    //        String expDatePattern = "yyyyMMdd-HHmmss-SSS";
    //        final String expDateStr = DateTimeFormatter.ofPattern(expDatePattern).format(current);
    //
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //
    //        // ## Act ##
    //        final String dateStr = logic.getCurrentDateStr();
    //
    //        // ## Assert ##
    //        assertEquals(dateStr, expDateStr);
    //    }

    // -----------------------------------------------------
    //                                           create file
    //                                           -----------
    //    public void test_createPieceMapFile_init() throws Exception {
    //        // ## Arrange ##
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
    //        inject(physicalLogic);
    //
    //        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
    //        final File sampleFile =
    //            new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece", sampleFileName));
    //
    //        assertFalse(sampleFile.exists()); // before check file not exits
    //
    //        // ## Act ##
    //        logic.createPieceMapFile(sampleFile);
    //
    //        // ## Assert ##
    //        assertTrue(sampleFile.exists());
    //    }
    //
    //    public void test_createPieceMapFile_prepared() throws Exception {
    //        // ## Arrange ##
    //        super.prepareTestDecommentFiles();
    //
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
    //        inject(physicalLogic);
    //
    //        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
    //        final File sampleFile =
    //            new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece", sampleFileName));
    //
    //        assertFalse(sampleFile.exists()); // before check file not exits
    //
    //        // ## Act ##
    //        logic.createPieceMapFile(sampleFile);
    //
    //        // ## Assert ##
    //        assertTrue(sampleFile.exists());
    //    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_readMergedDecommentPickupMap_init() throws Exception {
        // ## Arrange ##
        // done (by cabos) hakiba null pointer at init state by jflute (2017/10/05)
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedPickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
    }

    public void test_readMergedDecommentPickupMap_prepared() throws Exception {
        // done hakiba put test data in test/resources by hakiba (2017/08/18)
        // ## Arrange ##
        prepareTestDecommentFiles();
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedPickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
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
        assertEquals("ca/<bo s", author);
    }
}
