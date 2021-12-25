/*
 * Copyright 2014-2021 the original author or authors.
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
import org.dbflute.util.Srl;
import org.lastaflute.core.exception.LaSystemException;

/**
 * DIコンテナ利用のUnitTestクラスは、このクラスを継承する。<br>
 * 自動的にテスト用DBFluteクライアントが作成される。(不要な場合はsuppress可能) <br>
 * @author t-awane
 * @author deco
 * @author jflute
 * @author hakiba
 * @author cabos
 */
public abstract class UnitIntroTestCase extends WebContainerTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // -----------------------------------------------------
    //                                        DBFlute CLient
    //                                        --------------
    // Intro自体で使っているDBFluteクライアントがテスト用クライアントの元になる
    protected static final String SRC_CLIENT_PATH = "dbflute_introdb";

    // テスト用の設定ファイルなどを置く場所、dbflute_introdbそのままだとテストしづらいため
    protected static final String TEST_RESOURCE_BASE = "/src/test/resources/default";

    // dbflute_introdbがこの名前でコピーされて、テストで自由に使えるようになる
    protected static final String TEST_CLIENT_PATH = "dbflute_testdb";
    protected static final String TEST_CLIENT_PROJECT = "testdb";

    // -----------------------------------------------------
    //                                             Decomment
    //                                             ---------
    private static final String TEST_RESOURCE_PICKUP_FILE_PATH = TEST_RESOURCE_BASE + "/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String TEST_RESOURCE_PICKUP_PATH = TEST_RESOURCE_BASE + "/schema/decomment/piece";
    private static final String PICKUP_FILE_PATH = "/schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String PIECE_DIR_PATH = "/schema/decomment/piece";

    // -----------------------------------------------------
    //                                             Hacomment
    //                                             ---------
    private static final String TEST_RESOURCE_HACOMMENT_PICKUP_FILE_PATH =
            TEST_RESOURCE_BASE + "/schema/hacomment/pickup/hacomment-pickup.dfmap";
    private static final String TEST_RESOURCE_HACOMMENT_PICKUP_PATH = TEST_RESOURCE_BASE + "/schema/hacomment/piece";
    private static final String HACOMMENT_PICKUP_FILE_PATH = "/schema/hacomment/pickup/hacomment-pickup.dfmap";
    private static final String HACOMMENT_PIECE_DIR_PATH = "/schema/hacomment/piece";

    // -----------------------------------------------------
    //                                               Playsql
    //                                               -------
    protected static final String TEST_RESOURCE_PLAYSQL_DIR_PATH = TEST_RESOURCE_BASE + "/playsql";
    protected static final String PLAYSQL_DIR_PATH = "/playsql";

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
    protected void createTestClient() { // setUp()から呼ばれる想定
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
    protected void deleteTestClient() { // tearDown()から呼ばれる想定
        File clientDir = new File(getProjectDir(), TEST_CLIENT_PATH);
        try {
            FileUtils.deleteDirectory(clientDir);
        } catch (IOException e) {
            throw new LaSystemException("Cannot delete dir:" + clientDir, e);
        }
    }

    // -----------------------------------------------------
    //                                          Prepare File
    //                                          ------------
    // #needs_fix jflute more use prepareFileForTestClient() (2021/12/25)
    /**
     * テスト用のDBFluteクライアント内のファイルを探す。<br>
     * @param filePath DBFluteクライアントからの相対パス e.g. dfprop/schemaPolicyMap.dfprop (NotNull)
     * @return 指定されたパスを示すファイルオブジェクト、存在チェックはされない (NotNull)
     */
    protected File findTestClientFile(String filePath) { // UnitTestの中で好きなときに呼んでね
        // 元々のprepare...というメソッド名だったんだけど、prepareって補完ノイズが多いので変更した by jflute (2021/12/25)
        // (他のprepareと意味も違うし。例えば prepareTestDecommentFiles() と比べると)
        // 引数のパスは、一応JavaDoc的には先頭スラッシュなし想定だが、テストのメソッドだし吸収しておく by jflute (2021/12/25)
        return new File(getProjectDir(), TEST_CLIENT_PATH + "/" + Srl.ltrim(filePath, "/"));
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
        FileUtils.copyDirectory(srcPieceDir, destPieceDir, file -> file.isDirectory() || file.getName().endsWith(".dfmap"));
    }

    protected File getTestDecommentPickupFile() {
        return findTestClientFile(PICKUP_FILE_PATH);
    }

    protected File getTestDecommentPieceDir() {
        return findTestClientFile(PIECE_DIR_PATH);
    }

    // ===================================================================================
    //                                                                      Test Hacomment
    //                                                                      ==============
    protected void prepareTestHacommentFiles() throws IOException {
        File srcPickupFile = new File(getProjectDir(), TEST_RESOURCE_HACOMMENT_PICKUP_FILE_PATH);
        File srcPieceDir = new File(getProjectDir(), TEST_RESOURCE_HACOMMENT_PICKUP_PATH);
        File destPickupFile = getTestHacommentPickupFile();
        File destPieceDir = getTestHacommentPieceDir();
        FileUtils.copyFile(srcPickupFile, destPickupFile);
        FileUtils.copyDirectory(srcPieceDir, destPieceDir, file -> file.isDirectory() || file.getName().endsWith(".dfmap"));
    }

    protected File getTestHacommentPickupFile() {
        return findTestClientFile(HACOMMENT_PICKUP_FILE_PATH);
    }

    protected File getTestHacommentPieceDir() {
        return findTestClientFile(HACOMMENT_PIECE_DIR_PATH);
    }

    // ===================================================================================
    //                                                                        Test PlaySQL
    //                                                                        ============
    protected void preparePlaysqlFiles() throws IOException {
        File srcPlaysqlDir = new File(getProjectDir(), TEST_RESOURCE_PLAYSQL_DIR_PATH);
        File destPlaysqlDir = getTestPlaysqlDir();
        FileUtils.copyDirectory(srcPlaysqlDir, destPlaysqlDir);
    }

    protected File getTestPlaysqlDir() {
        return findTestClientFile(PLAYSQL_DIR_PATH);
    }
}
