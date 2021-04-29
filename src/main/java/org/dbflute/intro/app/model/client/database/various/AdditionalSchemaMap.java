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
package org.dbflute.intro.app.model.client.database.various;

import java.util.Collections;
import java.util.Map;

/**
 * @author jflute
 */
public class AdditionalSchemaMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final Map<String, AdditionalSchemaBox> schemaBoxMap; // not null, read-only

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AdditionalSchemaMap(Map<String, AdditionalSchemaBox> schemaBoxMap) {
        this.schemaBoxMap = Collections.unmodifiableMap(schemaBoxMap);
    }

    public static AdditionalSchemaMap empty() {
        return new AdditionalSchemaMap(Collections.emptyMap());
    }

    // #for_now waiting for function of additional schema implementation by jflute (2016/08/13)
    public static class AdditionalSchemaBox {

        protected final String schema;

        public AdditionalSchemaBox(String schema) {
            this.schema = schema;
        }

        public String getSchema() {
            return schema;
        }
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return "additional:{" + schemaBoxMap + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Map<String, AdditionalSchemaBox> getSchemaBoxMap() {
        return schemaBoxMap;
    }
}
