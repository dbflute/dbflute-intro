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
 * The bean class as return for remote API of GET /dfprop/schemapolicy/{clientName}.
 * @author FreeGen
 */
type DfpropSchemapolicyResult = {
  /** (Required) */
  wholeMap: DfpropSchemapolicyResult_WholeMapPart

  /** (Required) */
  tableMap: DfpropSchemapolicyResult_TableMapPart

  /** (Required) */
  columnMap: DfpropSchemapolicyResult_ColumnMapPart
}

/**
 * The part class of WholeMapPart.
 * @author FreeGen
 */
type DfpropSchemapolicyResult_WholeMapPart = {
  /** (NotNull) */
  themeList: Array<DfpropSchemapolicyResult_ThemePart>
}

/**
 * The part class of ThemePart.
 * @author FreeGen
 */
type DfpropSchemapolicyResult_ThemePart = {
  /** (Required) */
  name: string

  /** (Required) */
  description: string

  /** (Required) */
  typeCode: string

  /** (Required) */
  isActive: boolean
}

/**
 * The part class of TableMapPart.
 * @author FreeGen
 */
type DfpropSchemapolicyResult_TableMapPart = {
  /** (NotNull) */
  themeList: Array<DfpropSchemapolicyResult_ThemePart>

  /** (NotNull) */
  statementList: Array<string>
}

/**
 * The part class of ColumnMapPart.
 * @author FreeGen
 */
type DfpropSchemapolicyResult_ColumnMapPart = {
  /** (NotNull) */
  themeList: Array<DfpropSchemapolicyResult_ThemePart>

  /** (NotNull) */
  statementList: Array<string>
}
