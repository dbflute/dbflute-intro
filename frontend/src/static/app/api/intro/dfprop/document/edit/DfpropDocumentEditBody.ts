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

/**
 * The bean class as param for remote API of POST /dfprop/document/edit/{projectName}.
 * @author FreeGen
 */
type DfpropDocumentEditBody = {
  /** テーブル名やカラム名が大文字ベースかどうか？ (大文字に変換するかどうか？) (Required) */
  upperCaseBasic: boolean

  /** DBコメントの中の別名を区切るデリミタ (DBコメントに別名を入れてない場合は必ずnull) (NullAllowed) */
  aliasDelimiterInDbComment?: string

  /** DBコメントが別名ベースかどうか？ (デリミタがないとき別名かどうか？) (Required) */
  dbCommentOnAliasBasis: boolean

  /** 差分チェックでカラムの定義順を含めるかどうか？ (Required) */
  checkColumnDefOrderDiff: boolean

  /** 差分チェックでDBコメントを含めるかどうか？ (Required) */
  checkDbCommentDiff: boolean

  /** 差分チェックでストアドプロシージャを含めるかどうか？ (Required) */
  checkProcedureDiff: boolean
}
