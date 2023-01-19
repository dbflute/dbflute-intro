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
 * The bean class as return for remote API of GET /playsql/migration/alter/{projectName}.
 * @author FreeGen
 */
type PlaysqlMigrationAlterResult = {
  /** (NullAllowed) */
  ngMarkFile?: PlaysqlMigrationAlterResult_NgMarkFilePart

  /** list of editing sql files in dbflute_client/playsql/migration/alter directory (NullAllowed) */
  editingFiles?: Array<PlaysqlMigrationAlterResult_SQLFilePart>

  /** (NullAllowed) */
  checkedZip?: PlaysqlMigrationAlterResult_CheckedZipPart

  /** (NullAllowed) */
  unreleasedDir?: PlaysqlMigrationAlterResult_UnreleasedDirPart
}

/**
 * The part class of NgMarkFilePart.
 * @author FreeGen
 */
type PlaysqlMigrationAlterResult_NgMarkFilePart = {
  /**  * `previous-NG` - PreviousNG. * `alter-NG` - AlterNG. * `next-NG` - NextNG. :: fromCls(CDef$NgMark) (enumValue=[previous-NG, alter-NG, next-NG]) (Required) */
  ngMark: string

  /** file content e.g. ALTER TABLE MEMBER ADD MAIHAMA_VISITED VARCHAR(3); (Required) */
  content: string
}

/**
 * The part class of SQLFilePart.
 * @author FreeGen
 */
type PlaysqlMigrationAlterResult_SQLFilePart = {
  /** file name e.g. alter-sql-SAMPLE (Required) */
  fileName: string

  /** file content e.g. ALTER TABLE MEMBER ADD MAIHAMA_VISITED VARCHAR(3); (Required) */
  content: string
}

/**
 * The part class of CheckedZipPart.
 * @author FreeGen
 */
type PlaysqlMigrationAlterResult_CheckedZipPart = {
  /** zip file name e.g. 20190831_2249/checked-alter-to-20190422-2332 (Required) */
  fileName: string

  /** list of checked sql files (NullAllowed) */
  checkedFiles?: Array<PlaysqlMigrationAlterResult_SQLFilePart>
}

/**
 * The part class of UnreleasedDirPart.
 * @author FreeGen
 */
type PlaysqlMigrationAlterResult_UnreleasedDirPart = {
  /** list of checked sql files (NullAllowed) */
  checkedFiles?: Array<PlaysqlMigrationAlterResult_SQLFilePart>
}
