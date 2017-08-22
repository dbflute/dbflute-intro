package org.dbflute.intro.app.web.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart;
import org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart.DecommentColumnPart;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 */
public class DocumentDecommentActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    @SuppressWarnings("FieldCanBeLocal")
    private static String TEST_RESOURCE_PICKUP_FILE_PATH = "/src/test/resources/schema/decomment/pickup/decomment-pickup.dfmap";
    @SuppressWarnings("FieldCanBeLocal")
    private static String TEST_RESOURCE_PICKUP_PATH = "/src/test/resources/schema/decomment/piece";
    @SuppressWarnings("FieldCanBeLocal")
    private static String PICKUP_FILE_PATH = "/schema/decomment/pickup/decomment-pickup.dfmap";
    @SuppressWarnings("FieldCanBeLocal")
    private static String PIECE_DIR_PATH = "/schema/decomment/piece";

    // ===================================================================================
    //                                                                            Override
    //                                                                            ========
    @Override
    public void setUp() throws Exception {
        super.setUp();
        File srcPickupFile = new File(getProjectDir(), TEST_RESOURCE_PICKUP_FILE_PATH);
        File srcPieceDir = new File(getProjectDir(), TEST_RESOURCE_PICKUP_PATH);
        File destPickupFile = new File(getProjectDir(), TEST_CLIENT_PATH + PICKUP_FILE_PATH);
        File destPieceDir = new File(getProjectDir(), TEST_CLIENT_PATH + PIECE_DIR_PATH);
        FileUtils.copyFile(srcPickupFile, destPickupFile);
        FileUtils.copyDirectory(srcPieceDir, destPieceDir);
    }

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    @Test
    public void test_save_createHakiMap() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);
        DecommentPostBody body = createSampleBody();

        // ## Act ##
        action.save("introdb", body);

        // ## Assert ##
        // Assert by visual confirmation
    }

    private DecommentPostBody createSampleBody() {
        DecommentPostBody body = new DecommentPostBody();
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

    @Test
    public void test_read_PickupResult() throws Exception {
        // ## Arrange ##
        // TODO done hakiba put test data in test/resources by hakiba (2017/08/18)
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);

        // ## Act ##
        JsonResponse<DecommentPickupResult> response = action.pickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // TODO done hakiba like this by jflute (2017/08/17)
        //showJson(response);
        //TestingJsonData<DecommentPickupResult> jsonData = validateJsonData(response);
        //DecommentPickupResult result = jsonData.getJsonResult();

        showJson(response);
        validateJsonData(response);
    }
}
