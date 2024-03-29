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
package org.dbflute.intro.app.logic.intro;

import java.io.File;
import java.util.Arrays;

import org.dbflute.util.Srl;

/**
 * The logic for DBFlute Intro physical operation. <br>
 * Introが起動しているディレクトリの情報を扱う。DBFluteクライアント配下などは、それぞれ個別のLogicクラスにて。
 * @author p1us2er0
 * @author jflute
 */
public class IntroPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /**
     * DBFlute Introの実行ディレクトリのベースパス。(基本的に相対パス) <br>
     * <pre>
     * e.g. "." だったら dbflute-intro.jar と同じレベルのディレクトリ
     *  dbflute-intro
     *   |-dbflute_maihamadb // client
     *   |-mydbflute         // engine
     *   |-dbflute-intro.jar
     * </pre>
     */
    private static final String BASE_DIR_PATH = ".";

    private static final String MYDBFLUTE_PATH = "mydbflute";
    private static final String ENGINE_TEMPLATE_PATH = MYDBFLUTE_PATH + "/dbflute-%1$s";

    // ===================================================================================
    //                                                                               Intro
    //                                                                               =====
    /**
     * DBFlute Introの実行ディレクトリのベースパスを構築する。(基本的に相対パスで固定的)
     * <pre>
     * e.g.
     *  buildIntroPath(): .
     * </pre>
     * @return The path to the DBFlute intro, 後ろにスラッシュは付かない (NotNull)
     */
    public String buildIntroPath() {
        return BASE_DIR_PATH;
    }

    /**
     * DBFlute Introの実行ディレクトリのベースパスを表現するFileオブジェクトを探す。
     * @return The file object to the DBFlute intro directory (NotNull)
     */
    public File findIntroDir() {
        return new File(BASE_DIR_PATH);
    }

    // ===================================================================================
    //                                                                              Client
    //                                                                              ======
    /**
     * <pre>
     * e.g.
     *  buildClientPath("maihamadb"): ./dbflute_maihamadb
     *  buildClientPath("maihamadb", "dfprop"): ./dbflute_maihamadb/dfprop
     * </pre>
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param resources The varying array of resources in DBFlute client. (NotNull, EmptyAllowed)
     * @return The path to the DBFlute client. (NotNull)
     */
    public String buildClientPath(String projectName, String... resources) {
        final String suffix = resources.length > 0 ? Srl.connectByDelimiter(Arrays.asList(resources), "/") : null;
        return buildBasicPath("dbflute_" + projectName) + (suffix != null ? "/" + suffix : "");
    }

    public File findClientDir(String projectName) {
        return new File(buildClientPath(projectName));
    }

    // ===================================================================================
    //                                                                              Engine
    //                                                                              ======
    public String buildEnginePath(String dbfluteVersion, String... resources) {
        final String suffix = resources.length > 0 ? Srl.connectByDelimiter(Arrays.asList(resources), "/") : null;
        return buildBasicPath(String.format(ENGINE_TEMPLATE_PATH, dbfluteVersion)) + (suffix != null ? "/" + suffix : "");
    }

    public File findEngineDir(String dbfluteVersion) {
        return new File(buildEnginePath(dbfluteVersion));
    }

    public String buildMydbflutePath() {
        return buildBasicPath(MYDBFLUTE_PATH);
    }

    public File findMydbfluteDir() {
        return new File(buildMydbflutePath());
    }

    // ===================================================================================
    //                                                                              System
    //                                                                              ======
    // done anyone move to physical logic? or to caller class by jflute (2021/05/01)
    /**
     * Javaシステムにおいて、プロキシサーバーを利用したネットワークアクセスを許すかどうか？を設定する。
     * @param useSystemProxies the determination for system property "java.net.useSystemProxies".
     */
    public void setupSystemProxyUse(boolean useSystemProxies) {
        // https://docs.oracle.com/javase/jp/8/docs/api/java/net/doc-files/net-properties.html
        System.setProperty("java.net.useSystemProxies", String.valueOf(useSystemProxies));
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private String buildBasicPath(String path) {
        return BASE_DIR_PATH + "/" + path;
    }
}
