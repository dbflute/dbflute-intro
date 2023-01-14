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
 * The bean class as return for remote API of GET /document/decomment/{clientName}/pickup.
 * @author FreeGen
 */
type DocumentDecommentPickupResult = {
  /** list of table part
enable empty first (NotNull) */
  tables: Array<DocumentDecommentPickupResult_TablePart>
}

/**
 * The part class of TablePart.
 * @author FreeGen
 */
type DocumentDecommentPickupResult_TablePart = {
  /** table name
e.g. "MEMBER" (Required) */
  tableName: string

  /** list of conflicted mapping
 this list could be empty when conflict mapping was not exists (NotNull) */
  mappings: Array<DocumentDecommentPickupResult_MappingPart>

  /** list of decomment properties associated table (NotNull) */
  properties: Array<DocumentDecommentPickupResult_PropertyPart>

  /** list of column part, contains saved comments (NullAllowed) */
  columns?: Array<DocumentDecommentPickupResult_ColumnPart>
}

/**
 * The part class of MappingPart.
 * @author FreeGen
 */
type DocumentDecommentPickupResult_MappingPart = {
  /** new table name after mapped
e.g. "NEW_TABLE_NAME" (Required) */
  newTableName: string

  /** new column name after mapped
this could be null if target type was table (NullAllowed) */
  newColumnName?: string

  /** the list of ancestor authors
e.g. ["cabos", "hakiba", "deco"] (NotNull) */
  authors: Array<string>

  /** mapping code generated when decomment mapped
e.g. "EF89371" (Required) */
  mappingCode: string

  /** author of this decomment mapping
e.g. "deco" (Required) */
  mappingOwner: string

  /** time of decomment mapped
e.g. "2018-04-22T17:35:22 (Required) */
  mappingDatetime: string

  /** list of merged mapping code
e.g. ["HF7ELSE"] (NotNull) */
  previousMappings: Array<string>
}

/**
 * The part class of PropertyPart.
 * @author FreeGen
 */
type DocumentDecommentPickupResult_PropertyPart = {
  /** decomment saved as decomment piece map
e.g. "decomment means 'deco' + 'database comment'" (Required) */
  decomment: string

  /** table or column comment on table definition
The comments on database may be blank so null allowed (NullAllowed) */
  databaseComment?: string

  /** table or column comment version
The comment version will update when the decomment (Required) */
  commentVersion: number

  /** the list of ancestor authors
e.g. ["cabos", "hakiba", "deco"] (NotNull) */
  authors: Array<string>

  /** piece code generated when decomment edited
e.g. "EF89371" (Required) */
  pieceCode: string

  /** time of edit decomment
e.g. "2017-11-11T18:32:22 (Required) */
  pieceDatetime: string

  /** author of this decomment piece
e.g. "deco" (Required) */
  pieceOwner: string

  /** Branch name of this decomment piece
e.g. "develop" (NullAllowed) */
  pieceGitBranch?: string

  /** list of merged piece code
e.g. ["HF7ELSE"] (NotNull) */
  previousPieces: Array<string>
}

/**
 * The part class of ColumnPart.
 * @author FreeGen
 */
type DocumentDecommentPickupResult_ColumnPart = {
  /** column name
e.g. "MEMBER_NAME" (Required) */
  columnName: string

  /** list of decomment properties associated column (NotNull) */
  mappings: Array<DocumentDecommentPickupResult_MappingPart>

  /** list of decomment properties associated column
enable empty if alter doc task branch and topic branch existed piece was not mapped were merged (NotNull) */
  properties: Array<DocumentDecommentPickupResult_PropertyPart>
}
