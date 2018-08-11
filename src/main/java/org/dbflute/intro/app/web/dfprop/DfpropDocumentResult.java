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
package org.dbflute.intro.app.web.dfprop;

import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;

/**
 * @author deco
 */
public class DfpropDocumentResult {

    public final Boolean upperCaseBasic;
    public final String aliasDelimiterInDbComment;
    public final Boolean dbCommentOnAliasBasis;
    public final Boolean checkColumnDefOrderDiff;
    public final Boolean checkDbCommentDiff;
    public final Boolean checkProcedureDiff;

    public DfpropDocumentResult(LittleAdjustmentMap tableNameUpperInfo, DocumentMap documentMap) {
        upperCaseBasic =
            tableNameUpperInfo.isTableDispNameUpperCase &&
            tableNameUpperInfo.isTableSqlNameUpperCase &&
            tableNameUpperInfo.isColumnSqlNameUpperCase;
        aliasDelimiterInDbComment = documentMap.getAliasDelimiterInDbComment().orElse(null);
        dbCommentOnAliasBasis = documentMap.isDbCommentOnAliasBasis();
        checkColumnDefOrderDiff = documentMap.isCheckColumnDefOrderDiff();
        checkDbCommentDiff = documentMap.isCheckDbCommentDiff();
        checkProcedureDiff = documentMap.isCheckProcedureDiff();
    }
}
