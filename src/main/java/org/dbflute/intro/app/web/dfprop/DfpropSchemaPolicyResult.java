/*
 * Copyright 2014-2019 the original author or authors.
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
package org.dbflute.intro.app.web.dfprop;

import java.util.List;
import java.util.stream.Collectors;

import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;

/**
 * @author hakiba
 */
public class DfpropSchemaPolicyResult {

    public static class WholeMap {
        List<Theme> themeList;

        public WholeMap(SchemaPolicyWholeMap wholeMap) {
            this.themeList = wholeMap.themeList.stream().map(theme -> new Theme(theme)).collect(Collectors.toList());
        }
    }

    public static class TableMap {
        List<Theme> themeList;

        public TableMap(SchemaPolicyTableMap tableMap) {
            this.themeList = tableMap.themeList.stream().map(theme -> new Theme(theme)).collect(Collectors.toList());
        }
    }

    public static class ColumnMap {
        List<Theme> themeList;

        public ColumnMap(SchemaPolicyColumnMap columnMap) {
            this.themeList = columnMap.themeList.stream().map(theme -> new Theme(theme)).collect(Collectors.toList());
        }
    }

    public static class Theme {
        String name;
        String description;
        String typeCode;
        Boolean isActive;

        public Theme(SchemaPolicyWholeMap.Theme theme) {
            this.name = theme.type.name();
            this.description = theme.type.description;
            this.typeCode = theme.type.code;
            this.isActive = theme.isActive;
        }

        public Theme(SchemaPolicyTableMap.Theme theme) {
            this.name = theme.type.name();
            this.description = theme.type.description;
            this.typeCode = theme.type.code;
            this.isActive = theme.isActive;
        }

        public Theme(SchemaPolicyColumnMap.Theme theme) {
            this.name = theme.type.name();
            this.description = theme.type.description;
            this.typeCode = theme.type.code;
            this.isActive = theme.isActive;
        }
    }

    public WholeMap wholeMap;
    public TableMap tableMap;
    public ColumnMap columnMap;

    public DfpropSchemaPolicyResult(SchemaPolicyMap schemaPolicyMap) {
        this.wholeMap = new WholeMap(schemaPolicyMap.wholeMap);
        this.tableMap = new TableMap(schemaPolicyMap.tableMap);
        this.columnMap = new ColumnMap(schemaPolicyMap.columnMap);
    }
}
