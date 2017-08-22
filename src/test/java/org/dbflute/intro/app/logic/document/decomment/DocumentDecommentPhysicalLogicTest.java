package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DocumentDecommentPhysicalLogicTest extends UnitIntroTestCase {

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
    // TODO hakiba fix duplicate by hakiba (2017/08/22)
    @SuppressWarnings("Duplicates")
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

    @Test
    public void test_readMergedDecommentPickupMap() {
        // TODO done hakiba put test data in test/resources by hakiba (2017/08/18)
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // TODO done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedDecommentPickupMap(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(pickup);
    }
}
