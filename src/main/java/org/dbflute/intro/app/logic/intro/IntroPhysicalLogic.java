/*uili
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

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroPhysicalLogic {

    /**
     * <pre>
     * e.g. "."
     *  dbflute-intro
     *   |-dbflute_exampledb // DBFlute client
     *   |-mydbflute         // DBFlute module
     *   |-dbflute-intro.jar
     * </pre>
     */
    public static final String BASE_DIR_PATH = ".";

    public String buildResourcePath(String path) {
        return BASE_DIR_PATH + "/" + path;
    }

    /**
     * <pre>
     * e.g.
     *  toDBFluteClientPath("maihamadb"): ./dbflute_maihamadb
     * </pre>
     * @param project The project name of DBFlute client. (NotNull)
     * @return The path to the DBFlute client. (NotNull)
     */
    public String toDBFluteClientPath(String project) {
        return buildResourcePath("dbflute_" + project);
    }

    public String toDBFluteClientResourcePath(String project, String resource) {
        return toDBFluteClientPath(project) + "/" + resource;
    }

    public String toDfpropDirPath(String project) {
        return toDBFluteClientResourcePath(project, "dfprop");
    }

    public String toDocumentOutputDirPath(String project) {
        return toDBFluteClientResourcePath(project, "output/doc");
    }
}
