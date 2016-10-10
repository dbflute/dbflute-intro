/*
 * Copyright 2014-2016 the original author or authors.
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
 * @author p1us2er0
 * @author jflute
 */
public class IntroPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String MYDBFLUTE_PATH = "mydbflute";
    private static final String ENGINE_TEMPLATE_PATH = MYDBFLUTE_PATH + "/dbflute-%1$s";

    /**
     * <pre>
     * e.g. "."
     *  dbflute-intro
     *   |-dbflute_maihamadb // client
     *   |-mydbflute         // engine
     *   |-dbflute-intro.jar
     * </pre>
     */
    public static final String BASE_DIR_PATH = ".";

    // ===================================================================================
    //                                                                              Client
    //                                                                              ======
    /**
     * <pre>
     * e.g.
     *  buildIntroPath(): .
     * </pre>
     * @return The path to the DBFlute intro. (NotNull)
     */
    public String buildIntroPath() {
        return BASE_DIR_PATH;
    }

    public File findIntroDir() {
        return new File(BASE_DIR_PATH);
    }

    /**
     * <pre>
     * e.g.
     *  buildClientPath("maihamadb"): ./dbflute_maihamadb
     *  buildClientPath("maihamadb", "dfprop"): ./dbflute_maihamadb/dfprop
     * </pre>
     * @param clientProject The project name of DBFlute client. (NotNull)
     * @param resources The varying array of resources in DBFlute client. (NotNull, EmptyAllowed)
     * @return The path to the DBFlute client. (NotNull)
     */
    public String buildClientPath(String clientProject, String... resources) {
        final String suffix = resources.length > 0 ? Srl.connectByDelimiter(Arrays.asList(resources), "/") : null;
        return buildBasicPath("dbflute_" + clientProject) + (suffix != null ? "/" + suffix : "");
    }

    public File findClientDir(String clientProject) {
        return new File(buildClientPath(clientProject));
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
    //                                                                        Assist Logic
    //                                                                        ============
    private String buildBasicPath(String path) {
        return BASE_DIR_PATH + "/" + path;
    }
}
