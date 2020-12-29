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
package org.dbflute.intro.app.model.client.document;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hakiba
 */
public class SchemaPolicyWholeMap {

    public enum ThemeType {
        //@formatter:off
        UniqueTableAlias("uniqueTableAlias", "The alias of the table is unique."),
        SameColumnAliasIfSameColumnName("sameColumnAliasIfSameColumnName", "If the column names are the same, the column aliases are the same."),
        SameColumnDbTypeIfSameColumnName("sameColumnDbTypeIfSameColumnName", "If the column names are the same, the data type of the column is the same."),
        SameColumnSizeIfSameColumnName("sameColumnSizeIfSameColumnName", "If the column names are the same, the size of the column is the same."),
        SameColumnNameIfSameColumnAlias("sameColumnNameIfSameColumnAlias", "If column aliases are the same, column names are the same.");
        //@formatter:on

        ThemeType(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String code;
        public String description;

        public static ThemeType valueByCode(String code) {
            return Arrays.stream(ThemeType.values())
                    .filter(themeType -> themeType.code.equals(code))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("Illegal code : " + code));
        }
    }

    public static class Theme {
        public ThemeType type;
        public boolean isActive;

        public Theme(ThemeType type, boolean isActive) {
            this.type = type;
            this.isActive = isActive;
        }
    }

    public List<Theme> themeList;

    public static SchemaPolicyWholeMap noSettingInstance() {
        final List<Theme> themeList = Arrays.stream(ThemeType.values()).map(type -> new Theme(type, false)).collect(Collectors.toList());
        return new SchemaPolicyWholeMap(themeList);
    }

    public SchemaPolicyWholeMap(List<Theme> themeList) {
        this.themeList = themeList;
    }
}
