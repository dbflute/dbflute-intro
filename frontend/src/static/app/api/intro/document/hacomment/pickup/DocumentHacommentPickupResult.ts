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
 * The bean class as return for remote API of GET /document/hacomment/{clientName}/pickup.
 * @author FreeGen
 */
type DocumentHacommentPickupResult = {
  /** (NotNull) */
  diffList: Array<DocumentHacommentPickupResult_DiffPart>
}

/**
 * The part class of DiffPart.
 * @author FreeGen
 */
type DocumentHacommentPickupResult_DiffPart = {
  /** (Required) */
  diffCode: string

  /** (Required) */
  diffDate: string

  /** (NotNull) */
  properties: Array<DocumentHacommentPickupResult_PropertyPart>
}

/**
 * The part class of PropertyPart.
 * @author FreeGen
 */
type DocumentHacommentPickupResult_PropertyPart = {
  /** (Required) */
  hacomment: string

  /** (NullAllowed) */
  diffComment?: string

  /** (NotNull) */
  authorList: Array<string>

  /** (Required) */
  pieceCode: string

  /** (Required) */
  pieceOwner: string

  /** (Required) */
  pieceDatetime: string

  /** (NotNull) */
  previousPieceList: Array<string>
}
