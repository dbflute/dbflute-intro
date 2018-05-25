/*
 * Copyright 2014-2018 the original author or authors.
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author cabos
 */
public class IntroSystemPropertyLogic {

    protected static final String DECOMMENT_SERVER_KEY = "intro.decomment.server";
    protected static final Map<String, String> SYSTEM_PROPERTY_MAP = new HashMap<>();

    public boolean isDecommentServer() {
        if (SYSTEM_PROPERTY_MAP.containsKey(DECOMMENT_SERVER_KEY)) {
            return Boolean.valueOf(SYSTEM_PROPERTY_MAP.getOrDefault(DECOMMENT_SERVER_KEY, String.valueOf(Boolean.FALSE)));
        }
        String property = System.getProperty(DECOMMENT_SERVER_KEY, String.valueOf(Boolean.FALSE));
        SYSTEM_PROPERTY_MAP.put(DECOMMENT_SERVER_KEY, property);
        return Boolean.valueOf(property);
    }
}
