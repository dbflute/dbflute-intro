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
 * The bean class as param for remote API of POST /document/decomment/{clientName}/save.
 * @author FreeGen
 */
type DocumentDecommentSaveBody = {
  /** table name
e.g. "MEMBER" (Required) */
  tableName: string

  /** column name
The decomment target may be TABLE so null allowed (NullAllowed) */
  columnName?: string

  /** decomment target type
e.g. COLUMN: * `COLUMN` - Column. * `TABLE` - Table. :: fromCls(DfDecoMapPieceTargetType) (enumValue=[COLUMN, TABLE]) (Required) */
  targetType: string

  /** inputted column comment on the schema (Required) */
  decomment: string

  /** column comment on table definition
The comments on database may be blank so null allowed (NullAllowed) */
  databaseComment?: string

  /** column comment version
The comment version will update when the decomment (Required) */
  commentVersion: number

  /** the input author
Only used when run as decomment server {@link IntroSystemLogic#isDecommentServer()}
e.g. "cabos" (NullAllowed) */
  inputAuthor?: string

  /** the list of ancestor authors
Current piece author is derived by server at first decomment so empty allowed (NotNull) */
  authors: Array<string>

  /** the list of previous piece code
Current piece code is derived by server at first decomment so empty allowed (NotNull) */
  previousPieces: Array<string>
}
