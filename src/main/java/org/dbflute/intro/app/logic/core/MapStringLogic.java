/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.app.logic.core;

import org.dbflute.helper.dfmap.DfMapStyle;

/**
 * The logic for map string, which is DBFlute map expression
 * @author jflute
 */
public class MapStringLogic {

    // javadoc copied from DfMapStyle (has small modification)
    /**
     * Escape control marks as plain string in the map key and value. <br>
     * (Facade method for DBFlute class)
     * @param value The value, might contain control marks. (NullAllowed: if null, return null)
     * @return The escaped string of the value. (NullAllowed: when the value is null)
     */
    public String escapeControlMark(Object value) {
        // switch to new class DfMapStyle, old method is same as AsMap
        //return new MapListString().escapeControlMark(value);
        return new DfMapStyle().escapeControlMarkAsMap(value);
    }
}
