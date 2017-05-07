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
package org.dbflute.intro.app.logic.intro;

import org.dbflute.intro.IntroBoot;

import java.util.Map;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroInfoLogic {

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

    public Map<String, Object> getManifestMap() {
        return IntroBoot.getManifestMap();
    }

    public void setProxy(boolean useSystemProxies) {
        System.setProperty("java.net.useSystemProxies", String.valueOf(useSystemProxies));
    }

    // TODO jflute intro: unused? (2016/07/05)
    //public String getVersion() {
    //    return String.valueOf(getManifestMap().get("Implementation-Version"));
    //}
}
