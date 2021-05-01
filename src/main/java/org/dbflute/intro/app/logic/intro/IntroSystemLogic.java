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

/**
 * The logic for DBFlute Intro system operation. (e.g. decomment server)
 * @author cabos
 * @author jflute
 */
public class IntroSystemLogic {

    protected static final String DECOMMENT_SERVER_KEY = "intro.decomment.server";

    /**
     * Whether dbflute-intro is booted as decomment server<br>
     * e.g. java -jar -Dintro.decomment.server=true dbflute-intro.jar
     * @return true if server was booted as decomment server.
     */
    public boolean isDecommentServer() {
        String property = System.getProperty(DECOMMENT_SERVER_KEY, String.valueOf(Boolean.FALSE));
        return Boolean.valueOf(property);
    }
}
