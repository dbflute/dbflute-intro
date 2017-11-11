/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.unit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dbflute.utflute.lastaflute.WebContainerTestCase;
import org.lastaflute.core.exception.LaSystemException;

/**
 * @author t-awane
 * @author deco
 * @author jflute
 */
public abstract class UnitIntroTestCase extends WebContainerTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // -----------------------------------------------------
    //                                        DBFlute CLient
    //                                        --------------
    private static final String SRC_CLIENT_PATH = "dbflute_introdb";

    protected static final String TEST_CLIENT_PATH = "dbflute_testdb";
    protected static final String TEST_CLIENT_PROJECT = "testdb";

    // -----------------------------------------------------
    //                                             Decomment
    //                                             ---------
    private static final String TEST_RESOURCE_PICKUP_FILE_PATH = "/src/test/resources/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String TEST_RESOURCE_PICKUP_PATH = "/src/test/resources/schema/decomment/piece";
    private static final String PICKUP_FILE_PATH = "/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String PIECE_DIR_PATH = "/schema/decomment/piece";

    // ===================================================================================
    //                                                                            Settings
    //                                                                            ========
    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (!isSuppressTestClient()) {
            createTestClient();
        }
    }

    @Override
    public void tearDown() throws Exception {
        if (!isSuppressTestClient()) {
            deleteTestClient();
        }
        super.tearDown();
    }

    protected boolean isSuppressTestClient() { // you can override
        return false;
    }

    // ===================================================================================
    //                                                                         Test Client
    //                                                                         ===========
    /**
     * Create dbflute client. <br>
     * Use in setUp of test class. <br>
     * Don't forget to delete files in tearDown!!
     */
    protected void createTestClient() {
        File srcDir = new File(getProjectDir(), SRC_CLIENT_PATH);
        File destDir = new File(getProjectDir(), TEST_CLIENT_PATH);
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            throw new LaSystemException("Cannot copy dir:" + srcDir + "to dir:" + destDir, e);
        }
    }

    /**
     * Delete dbflute client. <br>
     * Use in tearDown of test class. <br>
     * Don't forget to create files in setUp!!
     */
    protected void deleteTestClient() {
        File clientDir = new File(getProjectDir(), TEST_CLIENT_PATH);
        try {
            FileUtils.deleteDirectory(clientDir);
        } catch (IOException e) {
            throw new LaSystemException("Cannot delete dir:" + clientDir, e);
        }
    }

    // ===================================================================================
    //                                                                      Test Decomment
    //                                                                      ==============
    protected void prepareTestDecommentFiles() throws IOException {
        // done hakiba change to call way to test plain state by jflute (2017/09/28)
        File srcPickupFile = new File(getProjectDir(), TEST_RESOURCE_PICKUP_FILE_PATH);
        File srcPieceDir = new File(getProjectDir(), TEST_RESOURCE_PICKUP_PATH);
        File destPickupFile = getTestDecommentPickupFile();
        File destPieceDir = getTestDecommentPieceDir();
        FileUtils.copyFile(srcPickupFile, destPickupFile);
        FileUtils.copyDirectory(srcPieceDir, destPieceDir);
        File[] unneededFiles = destPieceDir.listFiles(file -> file.isFile() && !file.getName().endsWith(".dfmap"));
        if (unneededFiles != null) {
            for (File unneededFile : unneededFiles) {
                unneededFile.delete(); // e.g. readme.txt
            }
        }
    }

    protected File getTestDecommentPickupFile() {
        return new File(getProjectDir(), TEST_CLIENT_PATH + PICKUP_FILE_PATH);
    }

    protected File getTestDecommentPieceDir() {
        return new File(getProjectDir(), TEST_CLIENT_PATH + PIECE_DIR_PATH);
    }
}
