package org.dbflute.intro.app.web.document.decomment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.web.document.decomment.DecommentPickupResult.TablePart;
import org.dbflute.intro.app.web.document.decomment.DecommentPickupResult.TablePart.ColumnPart;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 * @author hakiba
 * @author deco
 * @author jflute
 */
public class DocumentDecommentActionTest extends UnitIntroTestCase {

    @Resource
    private DocumentAuthorLogic documentAuthorLogic;

    // ===================================================================================
    //                                                                               Save
    //                                                                              ======
    // -----------------------------------------------------
    //                                                 Table
    //                                                 -----
    public void test_save_table() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);
        DecommentSaveBody body = createSaveBodyOfTable();
        File pieceDir = getTestDecommentPieceDir();
        assertFalse(pieceDir.exists());
        assertFalse(pieceDir.isDirectory());

        // ## Act ##
        action.save(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // Assert by visual confirmation
        // done cabos file assert by testdb by jflute (2017/09/07)
        File pieceFile = verifyPieceFile(pieceDir);
        Map<String, Object> actualMap = new MapListFile().readMap(new FileInputStream(pieceFile));
        log("[Saved Piece]: {}", pieceFile.getName());
        actualMap.forEach((key, value) -> {
            log("  {} = {}", key, value);
        });
        // TODO cabos fail by formatVersion by jflute (2017/11/11)
        //assertEquals("1.0", actualMap.get("formatVersion"));
        assertEquals(body.tableName, actualMap.get("tableName"));
        assertEquals(body.columnName, actualMap.get("columnName"));
        assertEquals(body.decomment, actualMap.get("decomment"));
        assertEquals(body.databaseComment, actualMap.get("databaseComment"));
        assertEquals(body.commentVersion, Long.valueOf(actualMap.get("commentVersion").toString()));
        List<String> expectedAuthorList = newArrayList(body.authors);
        expectedAuthorList.add(documentAuthorLogic.getAuthor());
        assertEquals(expectedAuthorList, actualMap.get("authorList"));
        assertNotNull(actualMap.get("pieceDatetime"));
        assertEquals(documentAuthorLogic.getAuthor(), actualMap.get("pieceOwner"));
        assertEquals(body.previousPieces, actualMap.get("previousPieceList"));
    }

    // -----------------------------------------------------
    //                                                Column
    //                                                ------
    public void test_save_column() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);
        DecommentSaveBody body = createSaveBodyOfColumn();
        File pieceDir = getTestDecommentPieceDir();
        assertFalse(pieceDir.exists());
        assertFalse(pieceDir.isDirectory());

        // ## Act ##
        action.save(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // Assert by visual confirmation
        // done cabos file assert by testdb by jflute (2017/09/07)
        File pieceFile = verifyPieceFile(pieceDir);
        Map<String, Object> actualMap = new MapListFile().readMap(new FileInputStream(pieceFile));
        log("[Saved Piece]: {}", pieceFile.getName());
        actualMap.forEach((key, value) -> {
            log("  {} = {}", key, value);
        });
        // TODO cabos fail by formatVersion by jflute (2017/11/11)
        //assertEquals("1.0", actualMap.get("formatVersion"));
        assertEquals(body.tableName, actualMap.get("tableName"));
        assertEquals(body.columnName, actualMap.get("columnName"));
        assertEquals(body.decomment, actualMap.get("decomment"));
        assertEquals(body.databaseComment, actualMap.get("databaseComment"));
        assertEquals(body.commentVersion, Long.valueOf(actualMap.get("commentVersion").toString()));
        List<String> expectedAuthorList = newArrayList(body.authors);
        expectedAuthorList.add(documentAuthorLogic.getAuthor());
        assertEquals(expectedAuthorList, actualMap.get("authorList"));
        assertNotNull(actualMap.get("pieceDatetime"));
        assertEquals(documentAuthorLogic.getAuthor(), actualMap.get("pieceOwner"));
        assertEquals(body.previousPieces, actualMap.get("previousPieceList"));
    }

    // -----------------------------------------------------
    //                                          Assist Logic
    //                                          ------------
    private DecommentSaveBody createSaveBodyOfTable() {
        DecommentSaveBody body = new DecommentSaveBody();
        body.merged = false;
        body.tableName = "MEMBER";
        body.columnName = null;
        body.targetType = DfDecoMapPieceTargetType.Table;
        body.decomment = "orange";
        body.databaseComment = "rime";
        body.commentVersion = 0L;
        body.authors = Arrays.asList("cabos", "sudachi");
        body.previousPieces = Collections.singletonList("LSFI83SF");
        return body;
    }

    private DecommentSaveBody createSaveBodyOfColumn() {
        DecommentSaveBody body = new DecommentSaveBody();
        body.merged = false;
        body.tableName = "MEMBER";
        body.columnName = "MEMBER_NAME";
        body.targetType = DfDecoMapPieceTargetType.Column;
        body.decomment = "orange";
        body.databaseComment = "rime";
        body.commentVersion = 0L;
        body.authors = Arrays.asList("cabos", "sudachi");
        body.previousPieces = Collections.singletonList("FE893L1");
        return body;
    }

    private File verifyPieceFile(File pieceDir) {
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        File[] pieceFiles = pieceDir.listFiles();
        assertNotNull(pieceFiles);
        assertEquals(1, pieceFiles.length);
        File pieceFile = pieceFiles[0];
        Pattern pattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        assertTrue(pattern.matcher(pieceFile.getName()).find());
        return pieceFile;
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_pickup_basic() throws Exception {
        // ## Arrange ##
        prepareTestDecommentFiles();
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);

        // ## Act ##
        JsonResponse<DecommentPickupResult> response = action.pickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        TestingJsonData<DecommentPickupResult> jsonData = validateJsonData(response);
        DecommentPickupResult result = jsonData.getJsonResult();
        {
            TablePart member = extractPickupTableAsOne(result, "MEMBER");
            assertEquals(1, member.properties.size()); // from piece
            {
                ColumnPart birthdate = extractPickupColumnAsOne(member, "BIRTHDATE");
                assertHasOnlyOneElement(birthdate.properties);
                assertEquals("hakiba", birthdate.properties.get(0).pieceOwner);
            }
        }
        {
            TablePart login = extractPickupTableAsOne(result, "MEMBER_LOGIN");
            assertEquals(1, login.properties.size()); // from pickup
            {
                ColumnPart loginDatetime = extractPickupColumnAsOne(login, "LOGIN_DATETIME");
                assertHasOnlyOneElement(loginDatetime.properties); // from pickup
                assertEquals("hakiba", loginDatetime.properties.get(0).pieceOwner);
            }
            {
                ColumnPart statusCode = extractPickupColumnAsOne(login, "LOGIN_MEMBER_STATUS_CODE");
                assertEquals(2, statusCode.properties.size()); // conflict from pickup
                assertEquals("deco", statusCode.properties.get(0).pieceOwner);
            }
        }
        {
            TablePart purchase = extractPickupTableAsOne(result, "PURCHASE");
            assertEquals(2, purchase.properties.size()); // conflict
            {
                ColumnPart purchaseDatetime = extractPickupColumnAsOne(purchase, "PURCHASE_DATETIME");
                assertEquals(3, purchaseDatetime.properties.size()); // conflict, should be cabos, hakiba, deco
            }
        }
    }

    public void test_pickup_empty() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);

        // ## Act ##
        JsonResponse<DecommentPickupResult> response = action.pickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        TestingJsonData<DecommentPickupResult> jsonData = validateJsonData(response);
        assertHasZeroElement(jsonData.getJsonResult().tables);
    }

    // -----------------------------------------------------
    //                                          Assist Logic
    //                                          ------------
    private TablePart extractPickupTableAsOne(DecommentPickupResult result, String tableName) {
        List<TablePart> tableList = result.tables.stream().filter(table -> {
            return table.tableName.equals(tableName);
        }).collect(Collectors.toList());
        assertHasOnlyOneElement(tableList);
        return tableList.get(0);
    }

    private ColumnPart extractPickupColumnAsOne(TablePart member, String columnName) {
        List<ColumnPart> columnList =
                member.columns.stream().filter(column -> column.columnName.equals(columnName)).collect(Collectors.toList());
        assertHasOnlyOneElement(columnList);
        return columnList.get(0);
    }
}
