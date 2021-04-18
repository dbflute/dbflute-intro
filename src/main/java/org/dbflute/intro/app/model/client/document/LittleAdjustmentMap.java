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

/**
 * @author deco
 */
public class LittleAdjustmentMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public final Boolean isTableDispNameUpperCase;
    public final Boolean isTableSqlNameUpperCase;
    public final Boolean isColumnSqlNameUpperCase;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    // done deco factory method by jflute (2017/03/30)
    public static LittleAdjustmentMap createAsTableNameUpperCase(Boolean isUpperCaseBasic) {
        return new LittleAdjustmentMap(isUpperCaseBasic, isUpperCaseBasic, isUpperCaseBasic);
    }

    public LittleAdjustmentMap(Boolean isTableDispNameUpperCase, Boolean isTableSqlNameUpperCase, Boolean isColumnSqlNameUpperCase) {
        this.isTableDispNameUpperCase = isTableDispNameUpperCase;
        this.isTableSqlNameUpperCase = isTableSqlNameUpperCase;
        this.isColumnSqlNameUpperCase = isColumnSqlNameUpperCase;
    }
}
