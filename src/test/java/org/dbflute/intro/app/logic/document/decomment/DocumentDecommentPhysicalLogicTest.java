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
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapPropertyPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.lastaflute.core.time.SimpleTimeManager;

/**
 * @author hakiba
 * @author jflute
 * @author cabos at garden place plaza
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
        final String sampleTableName = "EBISU_GARDEN_PLACE_PLAZA";
        final String sampleAuthor = "cabos";
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
        final String expFileName = "decomment-piece-" + sampleTableName + "-" + logic.getCurrentDateStr() + "-" + sampleAuthor + ".dfmap";

        // ## Act ##
        final String fileName = logic.buildPieceFileName(sampleTableName, sampleAuthor);

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
        Arrays.asList(pieceMaps).forEach(fileName -> {
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
        decoMapPiece.setAuthor("cabos");
        decoMapPiece.setMerged(false);
        decoMapPiece.setTableList(Collections.singletonList(createDfDecoMapTablePart()));
        return decoMapPiece;
    }

    private DfDecoMapTablePart createDfDecoMapTablePart() {
        DfDecoMapTablePart decoMapTablePart = new DfDecoMapTablePart();
        decoMapTablePart.setTableName("MEMBER");
        decoMapTablePart.setPropertyList(Collections.emptyList());
        decoMapTablePart.setColumnList(Collections.singletonList(createDfDecoMapColumnPart()));
        return decoMapTablePart;
    }

    private DfDecoMapColumnPart createDfDecoMapColumnPart() {
        DfDecoMapColumnPart decoMapColumnPart = new DfDecoMapColumnPart();
        decoMapColumnPart.setColumnName("MEMBER_NAME");
        decoMapColumnPart.setPropertyList(Collections.singletonList(createColumnPropertyPart()));
        return decoMapColumnPart;
    }

    private DfDecoMapPropertyPart createColumnPropertyPart() {
        DfDecoMapPropertyPart columnPropertyPart = new DfDecoMapPropertyPart();
        columnPropertyPart.setDecomment("piari");
        columnPropertyPart.setDatabaseComment("sea");
        columnPropertyPart.setPieceDatetime(currentLocalDateTime());
        columnPropertyPart.setCommentVersion(1);
        columnPropertyPart.setAuthorList(Collections.singletonList("cabos"));
        return columnPropertyPart;
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
