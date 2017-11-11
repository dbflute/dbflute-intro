package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.lastaflute.core.time.SimpleTimeManager;

/**
 * @author hakiba
 * @author jflute
 * @author cabos at garden place plaza
 * @author deco
 */
public class DocumentDecommentPhysicalLogicTest extends DocumentDecommentUnitIntroTestCase {

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    // -----------------------------------------------------
    //                                                author
    //                                                ------
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

    // -----------------------------------------------------
    //                                             file name
    //                                             ---------
    public void test_getCurrentDateStr() throws Exception {
        // ## Arrange ##
        final LocalDateTime current = currentLocalDateTime();
        registerMock(new SimpleTimeManager() {
            @Override
            public LocalDateTime currentDateTime() {
                return current;
            }
        });
        // e.g 20170316-123456-789
        String expDatePattern = "yyyyMMdd-HHmmss-SSS";
        final String expDateStr = DateTimeFormatter.ofPattern(expDatePattern).format(current);

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        final String dateStr = logic.getCurrentDateStr();

        // ## Assert ##
        assertEquals(dateStr, expDateStr);
    }

    public void test_buildPieceFileName() throws Exception {
        // ## Arrange ##
        final String sampleTableName = "EBISU_GARDEN_PLACE";
        final String sampleColumnName = "PLAZA";
        final String sampleAuthor = "cabos";
        final String samplePieceCode = "FE893L1";
        final LocalDateTime current = currentLocalDateTime();
        registerMock(new SimpleTimeManager() {
            @Override
            public LocalDateTime currentDateTime() {
                return current;
            }
        });

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // e.g decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap
        final String expFileName = "decomment-piece-" + sampleTableName + "-" + sampleColumnName + "-" + logic.getCurrentDateStr() + "-"
                + sampleAuthor + "-" + samplePieceCode + ".dfmap";

        // ## Act ##
        final String fileName = logic.buildPieceFileName(sampleTableName, sampleColumnName, sampleAuthor, samplePieceCode);

        // ## Assert ##
        assertEquals(fileName, expFileName);
    }

    // -----------------------------------------------------
    //                                             file path
    //                                             ---------
    public void test_buildDecommentPieceDirPath() throws Exception {
        // ## Arrange ##
        final String expDirPath = "./" + TEST_CLIENT_PATH + "/schema/decomment/piece";
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String pieceDirPath = logic.buildDecommentPieceDirPath(TEST_CLIENT_PROJECT);

        // ## Assert ##
        assertEquals(expDirPath, pieceDirPath);
    }

    public void test_buildDecommentPiecePath() throws Exception {
        // ## Arrange ##
        final String sampleFileName = "sampleFileName.exe";
        final String expFilePath = "./" + TEST_CLIENT_PATH + "/schema/decomment/piece/" + sampleFileName;
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String pieceFilePath = logic.buildDecommentPiecePath(TEST_CLIENT_PROJECT, sampleFileName);

        // ## Assert ##
        assertEquals(expFilePath, pieceFilePath);
    }

    // -----------------------------------------------------
    //                                           create file
    //                                           -----------
    public void test_createPieceMapFile_init() throws Exception {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
        final File sampleFile = new File(logic.buildDecommentPiecePath(TEST_CLIENT_PROJECT, sampleFileName));

        assertFalse(sampleFile.exists()); // before check file not exits

        // ## Act ##
        logic.createPieceMapFile(sampleFile);

        // ## Assert ##
        assertTrue(sampleFile.exists());
    }

    public void test_createPieceMapFile_prepared() throws Exception {
        // ## Arrange ##
        super.prepareTestFiles();

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
        final File sampleFile = new File(logic.buildDecommentPiecePath(TEST_CLIENT_PROJECT, sampleFileName));

        assertFalse(sampleFile.exists()); // before check file not exits

        // ## Act ##
        logic.createPieceMapFile(sampleFile);

        // ## Assert ##
        assertTrue(sampleFile.exists());
    }

    // -----------------------------------------------------
    //                                        save piece map
    //                                        --------------
    public void test_saveDecommentPieceMap_init() throws Exception {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(logic.buildDecommentPieceDirPath(TEST_CLIENT_PROJECT));

        // ## Act ##
        logic.saveDecommentPieceMap(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

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
        super.prepareTestFiles();

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(logic.buildDecommentPieceDirPath(TEST_CLIENT_PROJECT));

        // ## Act ##
        logic.saveDecommentPieceMap(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        assertTrue(pieceMaps.length > 0);
        Arrays.stream(pieceMaps)
                .filter(fileName -> fileName.endsWith(".dfmap")) // exclude un need filed
                .forEach(fileName -> {
                    log(fileName);
                    assertTrue(expFileNamePattern.matcher(fileName).find());
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
        decoMapPiece.setMerged(false);
        decoMapPiece.setTableName("MEMBER");
        decoMapPiece.setColumnName("MEMBER_NAME");
        decoMapPiece.setTargetType(AppCDef.PieceTargetType.Column);
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
