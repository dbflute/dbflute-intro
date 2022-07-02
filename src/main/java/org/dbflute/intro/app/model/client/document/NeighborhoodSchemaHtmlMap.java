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
package org.dbflute.intro.app.model.client.document;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author hakiba
 */
public class NeighborhoodSchemaHtmlMap {

    public static class NeighborhoodSchemaHtml {
        // ===================================================================================
        //                                                                           Attribute
        //                                                                           =========
        public String path;

        public NeighborhoodSchemaHtml(String path) {
            this.path = path;
        }
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public Map<String, NeighborhoodSchemaHtml> map = new HashMap<>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public NeighborhoodSchemaHtmlMap(Map<String, Map<String, String>> neighborhoodSchemaHtmlMap) {
        neighborhoodSchemaHtmlMap.forEach((key, neighborhood) -> {
            this.map.put(key, new NeighborhoodSchemaHtml(neighborhood.get("path")));
        });
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Stream<Map.Entry<String, NeighborhoodSchemaHtml>> stream() {
        return this.map.entrySet().stream();
    }
}
