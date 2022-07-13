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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.dbflute.utflute.lastaflute.WebContainerTestCase;
import org.dbflute.util.Srl;
import org.lastaflute.core.exception.LaSystemException;

/**
 * DIコンテナ利用のUnitTestクラスは、このクラスを継承する。<br>
 * 自動的にテスト用DBFluteクライアントが作成される。(不要な場合はsuppress可能) <br>
 *
 * <p>テスト用クライアントのディレクトリ構造などの定義やFileオブジェクト取得などを定義する。
 * ディレクトリ構造などは色々なクラスで利用する可能性があるので、できるだけ共有する。
 * (クラスが大きくなってきたら、構造を定義するだけのクラスに切り出して利用してもいいかも) </p>
 *
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
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // パス定義の命名ポリシー:
    // o プロジェクトルートからの相対パスであれば: 定数名に何も付けず最後は単なる _PATH
    // o 他の場所、例えばDBFluteクライアントからの相対パスであれば: 定数名は CLIENT_XXX_PATH
    // o 相対パスには先頭にスラッシュを付けない
    //
    // ※このへんだんだん合わなくなってきたら変えてOK (ひとまず整理しただけなので)
    // _/_/_/_/_/_/_/_/_/_/
    // -----------------------------------------------------
    //                                        DBFlute Client
    //                                        --------------
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // プロジェクトルートからの想定パス
    // _/_/_/_/_/_/_/_/_/_/
    // Intro自体で使っているDBFluteクライアントがテスト用クライアントの元になる
    private static final String SRC_CLIENT_PATH = "dbflute_introdb"; // 単なるディレクトリ名に見えるけど相対パス扱い

    private static final String SRC_CLIENT_PROJECT = "introdb"; // これは名前

    // それ↑がこの名前でコピーされて、テストで自由に使えるようになる
    private static final String TEST_CLIENT_PATH = "dbflute_testdb"; // こっちも

    // テスト用の設定ファイルなどを置く場所、dbflute_introdbそのままだとテストしづらいため
    private static final String TEST_RESOURCE_PATH = "src/test/resources/default"; // #thinking jflute what "default" means? (2021/12/26)

    // 大抵のロジックにて、これを引数に特定クライアントの処理ができるのでよく使う
    // (テストコードの中ではこちらをよく使うのでprotected: メソッドで取得する方が融通利くのでちょと変えたいかも!?)
    protected static final String TEST_CLIENT_PROJECT = "testdb"; // これは名前

    // -----------------------------------------------------
    //                                             Decomment
    //                                             ---------
    // DBFluteクライアントからの相対パス
    private static final String CLIENT_DECOMMENT_PICKUP_FILE_PATH = "schema/decomment/pickup/decomment-pickup.dfmap";
    private static final String CLIENT_DECOMMENT_PIECE_DIR_PATH = "schema/decomment/piece";

    // -----------------------------------------------------
    //                                             Hacomment
    //                                             ---------
    // DBFluteクライアントからの相対パス
    private static final String CLIENT_HACOMMENT_PICKUP_FILE_PATH = "schema/hacomment/pickup/hacomment-pickup.dfmap";
    private static final String CLIENT_HACOMMENT_PIECE_DIR_PATH = "schema/hacomment/piece";

    // -----------------------------------------------------
    //                                               Playsql
    //                                               -------
    // DBFluteクライアントからの相対パス
    private static final String CLIENT_PLAYSQL_PATH = "playsql";
    private static final String CLIENT_PLAYSQL_MIGRATION_PATH = CLIENT_PLAYSQL_PATH + "/migration";
    private static final String CLIENT_PLAYSQL_MIGRATION_ALTER_PATH = CLIENT_PLAYSQL_MIGRATION_PATH + "/alter";
    private static final String CLIENT_PLAYSQL_MIGRATION_HISTORY_PATH = CLIENT_PLAYSQL_MIGRATION_PATH + "/history";
    private static final String CLIENT_PLAYSQL_MIGRATION_UNRELEASED_PATH =
            CLIENT_PLAYSQL_MIGRATION_HISTORY_PATH + "/unreleased-checked-alter";

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
        return false; // もしテスト用クライアントを使いたくない場合は、オーバーライドしてtrueを戻せばOK
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
            // 各自動生成docのファイル名もテスト用プロジェクトに合わせてリネームしておく
            Stream<Path> docs = Files.walk(Paths.get(destDir.getPath(), "output", "doc"));
            docs.forEach(path -> {
                File original = path.toFile();
                File renamed = new File(path.toFile().getAbsolutePath().replace(SRC_CLIENT_PROJECT, TEST_CLIENT_PROJECT));
                original.renameTo(renamed);
            });
            docs.close();
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
    //                                             Find File
    //                                             ---------
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

    /**
     * テスト用に用意してあるリソースファイルを探す。<br>
     * @param filePath DBFluteクライアントからの相対パス e.g. dfprop/schemaPolicyMap.dfprop (NotNull)
     * @return 指定されたパスを示すファイルオブジェクト、存在チェックはされない (NotNull)
     */
    protected File findTestResourceFile(String filePath) { // UnitTestの中で好きなときに呼んでね
        return new File(getProjectDir(), TEST_RESOURCE_PATH + "/" + Srl.ltrim(filePath, "/"));
    }

    // ===================================================================================
    //                                                                      Test Decomment
    //                                                                      ==============
    protected void prepareTestDecommentFiles() throws IOException {
        // done hakiba change to call way to test plain state by jflute (2017/09/28)
        // テスト用リソースのパスもDBFluteクライアントでの構造と同じようにする
        File srcPickupFile = findTestResourceFile(CLIENT_DECOMMENT_PICKUP_FILE_PATH);
        File srcPieceDir = findTestResourceFile(CLIENT_DECOMMENT_PIECE_DIR_PATH);
        File destPickupFile = getTestDecommentPickupFile();
        File destPieceDir = getTestDecommentPieceDir();
        FileUtils.copyFile(srcPickupFile, destPickupFile);
        FileUtils.copyDirectory(srcPieceDir, destPieceDir, file -> file.isDirectory() || file.getName().endsWith(".dfmap"));
    }

    protected File getTestDecommentPickupFile() {
        return findTestClientFile(CLIENT_DECOMMENT_PICKUP_FILE_PATH);
    }

    protected File getTestDecommentPieceDir() {
        return findTestClientFile(CLIENT_DECOMMENT_PIECE_DIR_PATH);
    }

    // ===================================================================================
    //                                                                      Test Hacomment
    //                                                                      ==============
    protected void prepareTestHacommentFiles() throws IOException {
        // テスト用リソースのパスもDBFluteクライアントでの構造と同じようにする
        File srcPickupFile = findTestResourceFile(CLIENT_HACOMMENT_PICKUP_FILE_PATH);
        File srcPieceDir = findTestResourceFile(CLIENT_HACOMMENT_PIECE_DIR_PATH);
        File destPickupFile = getTestHacommentPickupFile();
        File destPieceDir = getTestHacommentPieceDir();
        FileUtils.copyFile(srcPickupFile, destPickupFile);
        FileUtils.copyDirectory(srcPieceDir, destPieceDir, file -> file.isDirectory() || file.getName().endsWith(".dfmap"));
    }

    protected File getTestHacommentPickupFile() {
        return findTestClientFile(CLIENT_HACOMMENT_PICKUP_FILE_PATH);
    }

    protected File getTestHacommentPieceDir() {
        return findTestClientFile(CLIENT_HACOMMENT_PIECE_DIR_PATH);
    }

    // ===================================================================================
    //                                                                        Test PlaySQL
    //                                                                        ============
    protected void preparePlaysqlFiles() throws IOException {
        File testResourcePlaysqlDir = findTestResourceFile(CLIENT_PLAYSQL_PATH); // テスト用リソースでも同じ構造で定義
        FileUtils.copyDirectory(testResourcePlaysqlDir, getTestPlaysqlDir());
    }

    protected File getTestPlaysqlDir() {
        return findTestClientFile(CLIENT_PLAYSQL_PATH);
    }

    protected File getTestPlaysqlMigrationDir() {
        return findTestClientFile(CLIENT_PLAYSQL_MIGRATION_PATH);
    }

    protected File getTestPlaysqlMigrationAlterDir() {
        return findTestClientFile(CLIENT_PLAYSQL_MIGRATION_ALTER_PATH);
    }

    protected File getTestPlaysqlMigrationHistoryDir() {
        return findTestClientFile(CLIENT_PLAYSQL_MIGRATION_HISTORY_PATH);
    }

    protected File getTestPlaysqlMigrationUnreleasedDir() {
        return findTestClientFile(CLIENT_PLAYSQL_MIGRATION_UNRELEASED_PATH);
    }
}
