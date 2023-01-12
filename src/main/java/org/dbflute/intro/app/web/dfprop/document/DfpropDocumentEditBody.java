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

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 * @author subaru
 * @author jflute
 */
public class DfpropDocumentEditBody {

    /** テーブル名やカラム名が大文字ベースかどうか？ (大文字に変換するかどうか？) */
    @Required
    public Boolean upperCaseBasic;

    /** DBコメントの中の別名を区切るデリミタ (DBコメントに別名を入れてない場合は必ずnull) */
    public String aliasDelimiterInDbComment;

    /** DBコメントが別名ベースかどうか？ (デリミタがないとき別名かどうか？) */
    @Required
    public Boolean dbCommentOnAliasBasis;

    // TODO you checksにしたいかも？ by jflute (2023/01/12)
    /** 差分チェックでカラムの定義順を含めるかどうか？ */
    @Required
    public Boolean checkColumnDefOrderDiff;

    /** 差分チェックでDBコメントを含めるかどうか？ */
    @Required
    public Boolean checkDbCommentDiff;

    /** 差分チェックでストアドプロシージャを含めるかどうか？ */
    @Required
    public Boolean checkProcedureDiff;
}
