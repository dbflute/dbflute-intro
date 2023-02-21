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
 * The bean class as param for remote API of POST /document/decomment/{clientName}/mapping.
 * @author FreeGen
 */
type DocumentDecommentMappingBody = {
  /** mapping list (NotNull) */
  mappings: Array<DocumentDecommentMappingBody_MappingPart>
}

/**
 * The part class of MappingPart.
 * @author FreeGen
 */
type DocumentDecommentMappingBody_MappingPart = {
  /** old table name
e.g. OLD_TABLE_NAME (Required) */
  oldTableName: string

  /** old column name
this field can be null if mapping is for table
e.g. OLD_COLUMN_NAME (NullAllowed) */
  oldColumnName?: string

  /** new table name
e.g. NEW_TABLE_NAME (Required) */
  newTableName: string

  /** new column name
this field can be null if mapping is for table
e.g. NEW_COLUMN_NAME (NullAllowed) */
  newColumnName?: string

  /** mapping target type
e.g. COLUMN: * `COLUMN` - Column. * `TABLE` - Table. :: fromCls(DfDecoMapPieceTargetType) (enumValue=[COLUMN, TABLE]) (Required) */
  targetType: string

  /** the current author
Only used when run as decomment server {@link IntroSystemLogic#isDecommentServer()}
e.g. "cabos" (NullAllowed) */
  author?: string

  /** the list of ancestor authors
Current mapping author is derived by server at first decomment so empty allowed (NotNull) */
  authors: Array<string>

  /** the list of previous piece code
Current mapping code is derived by server at first decomment so empty allowed (NotNull) */
  previousMappings: Array<string>
}
