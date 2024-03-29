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
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author hakiba
 */
public class SchemaDiagramMap {

    public static class SchemaDiagram {
        public final String path;
        public final String width;
        public final String height;

        public SchemaDiagram(String path, String width, String height) {
            this.path = path;
            this.width = width;
            this.height = height;
        }
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final Map<String, SchemaDiagram> map = new HashMap<>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public SchemaDiagramMap(Map<String, Map<String, String>> schemaDiagramMap) {
        schemaDiagramMap.forEach((key, diagram) -> {
            this.map.put(key, new SchemaDiagram(diagram.get("path"), diagram.get("width"), diagram.get("height")));
        });
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public Optional<SchemaDiagram> get(String diagramName) {
        return Optional.ofNullable(this.map.get(diagramName));
    }

    public Stream<Map.Entry<String, SchemaDiagram>> stream() {
        return this.map.entrySet().stream();
    }
}
