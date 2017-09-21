package org.dbflute.intro.app.web.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.intro.app.web.document.decomment.DecommentSaveBody.DecommentTablePart;
import org.dbflute.intro.app.web.document.decomment.DecommentSaveBody.DecommentTablePart.DecommentColumnPart;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 * @author hakiba
 */
public class DocumentDecommentActionTest extends DocumentDecommentUnitIntroTestCase {

    // ===================================================================================
    //                                                                               Save
    //                                                                              ======
    public void test_save_createHakiMap() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);
        DecommentSaveBody body = createSampleBody();

        // ## Act ##
        action.save(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // Assert by visual confirmation
        // done cabos file assert by testdb by jflute (2017/09/07)
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

    private DecommentSaveBody createSampleBody() {
        DecommentSaveBody body = new DecommentSaveBody();
        body.merged = false;
        body.table = createSampleTablePart();
        return body;
    }

    private DecommentTablePart createSampleTablePart() {
        DecommentTablePart tablePart = new DecommentTablePart();
        tablePart.tableName = "TABLE_NAME";
        tablePart.columns = Collections.singletonList(createSampleColumnPart());
        return tablePart;
    }

    private DecommentColumnPart createSampleColumnPart() {
        DecommentColumnPart columnPart = new DecommentColumnPart();
        columnPart.authorList = Arrays.asList("cabos", "sudachi");
        columnPart.columnName = "COLUMN_NAME";
        columnPart.commentVersion = 1L;
        columnPart.decomment = "orange";
        columnPart.databaseComment = "rime";
        columnPart.previousWholeComment = "lemon";
        return columnPart;
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_read_PickupResult() throws Exception {
        // ## Arrange ##
        // done hakiba put test data in test/resources by hakiba (2017/08/18)
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);

        // ## Act ##
        JsonResponse<DecommentPickupResult> response = action.pickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // done hakiba like this by jflute (2017/08/17)
        //showJson(response);
        //TestingJsonData<DecommentPickupResult> jsonData = validateJsonData(response);
        //DecommentPickupResult result = jsonData.getJsonResult();

        showJson(response);
        validateJsonData(response);
    }
}
