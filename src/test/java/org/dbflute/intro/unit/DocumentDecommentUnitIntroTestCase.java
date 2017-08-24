package org.dbflute.intro.unit;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * @author hakiba
 */
public abstract class DocumentDecommentUnitIntroTestCase extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String TEST_RESOURCE_PICKUP_FILE_PATH = "/src/test/resources/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String TEST_RESOURCE_PICKUP_PATH = "/src/test/resources/schema/decomment/piece";
    private static final String PICKUP_FILE_PATH = "/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String PIECE_DIR_PATH = "/schema/decomment/piece";

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
}
