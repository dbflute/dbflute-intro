package org.dbflute.intro.app.web.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.intro.unit.DocumentDecommentUnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 * @author hakiba
 * @author deco
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
        body.tableName = "TABLE_NAME";
        body.columnName = "COLUMN_NAME";
        body.targetType = AppCDef.PieceTargetType.Column;
        body.decomment = "orange";
        body.databaseComment = "rime";
        body.commentVersion = 1L;
        body.authors = Arrays.asList("cabos", "sudachi");
        body.previousPieces = Collections.singletonList("FE893L1");
        return body;
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_read_PickupResult() throws Exception {
        // ## Arrange ##
        prepareTestFiles();
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
