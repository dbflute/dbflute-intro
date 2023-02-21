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
package org.dbflute.intro.app.web.dfprop.document;

import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;

// TODO you @Requiredを付けたい (Booleanは基本的にRequiredでOK) by jflute (2023/01/12)
/**
 * @author deco
 * @author subaru
 */
public class DfpropDocumentResult {

    /** テーブル名やカラム名が大文字ベースかどうか？ (大文字に変換するかどうか？) */
    public final Boolean upperCaseBasic;

    /** DBコメントの中の別名を区切るデリミタ (DBコメントに別名を入れてない場合は必ずnull) */
    public final String aliasDelimiterInDbComment;

    /** DBコメントが別名ベースかどうか？ (デリミタがないとき別名かどうか？) */
    public final Boolean dbCommentOnAliasBasis;

    /** 差分チェックでカラムの定義順を含めるかどうか？ */
    public final Boolean checkColumnDefOrderDiff;

    /** 差分チェックでDBコメントを含めるかどうか？ */
    public final Boolean checkDbCommentDiff;

    /** 差分チェックでストアドプロシージャを含めるかどうか？ */
    public final Boolean checkProcedureDiff;

    public DfpropDocumentResult(LittleAdjustmentMap tableNameUpperInfo, DocumentMap documentMap) {
        // TODO you LittleAdjustmentMap自身に判定メソッドを置きたい by jflute (2023/01/12)
        upperCaseBasic = tableNameUpperInfo.isTableDispNameUpperCase && tableNameUpperInfo.isTableSqlNameUpperCase
                && tableNameUpperInfo.isColumnSqlNameUpperCase;
        aliasDelimiterInDbComment = documentMap.getAliasDelimiterInDbComment().orElse(null);
        dbCommentOnAliasBasis = documentMap.isDbCommentOnAliasBasis();
        checkColumnDefOrderDiff = documentMap.isCheckColumnDefOrderDiff();
        checkDbCommentDiff = documentMap.isCheckDbCommentDiff();
        checkProcedureDiff = documentMap.isCheckProcedureDiff();
    }
}
