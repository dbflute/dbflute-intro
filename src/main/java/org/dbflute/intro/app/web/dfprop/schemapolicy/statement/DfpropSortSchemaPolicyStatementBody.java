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
package org.dbflute.intro.app.web.dfprop.schemapolicy.statement;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author hakiba
 */
public class DfpropSortSchemaPolicyStatementBody {
    public DfpropSortSchemaPolicyStatementBody(String mapType, Integer fromIndex, Integer toIndex) {
        this.mapType = mapType;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    @NotNull
    public String mapType;
    @Min(value = 0)
    @NotNull
    public Integer fromIndex;
    @Min(value = 0)
    @NotNull
    public Integer toIndex;
}
