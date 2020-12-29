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
package org.dbflute.intro.app.web.dfprop;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author hakiba
 */
public class DfpropEditSchemaPolicyBody {

    public static class WholeMap {
        @Valid
        public List<Theme> themeList;
    }

    public static class TableMap {
        @Valid
        public List<Theme> themeList;
    }

    public static class ColumnMap {
        @Valid
        public List<Theme> themeList;
    }

    public static class Theme {
        @NotNull
        public String typeCode;
        @NotNull
        public Boolean isActive;
    }

    @Valid
    public WholeMap wholeMap;
    @Valid
    public TableMap tableMap;
    @Valid
    public ColumnMap columnMap;
}
