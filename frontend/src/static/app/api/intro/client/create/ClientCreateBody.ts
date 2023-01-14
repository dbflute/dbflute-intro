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
 * The bean class as param for remote API of POST /client/create.
 * @author FreeGen
 */
type ClientCreateBody = {
  /** (Required) */
  client: ClientCreateBody_ClientPart

  /** DBMSへの接続テストを行うかどうか？ e.g. false (Required) */
  testConnection: boolean
}

/**
 * The part class of ClientPart.
 * @author FreeGen
 */
type ClientCreateBody_ClientPart = {
  /** DBFluteクライアントのプロジェクト名 e.g. maihamadb (Required) */
  projectName: string

  /** DBMSを識別するコード e.g. mysql: * `mysql` - MySQL. * `postgresql` - PostgreSQL. * `oracle` - Oracle. * `db2` - Db2, DB2. * `sqlserver` - SQLServer. * `h2` - H2Database, H2 Database. * `derby` - ApacheDerby, Apache Derby. :: fromCls(CDef$TargetDatabase) (enumValue=[mysql, postgresql, oracle, db2, sqlserver, h2, derby]) (Required) */
  databaseCode: string

  /** 自動生成コードの言語を識別するコード e.g. java: * `java` - Java. * `csharp` - C, C#. * `scala` - Scala. :: fromCls(CDef$TargetLanguage) (enumValue=[java, csharp, scala]) (Required) */
  languageCode: string

  /** 自動生成コードが利用するDIコンテナを識別するコード e.g. lasta_di: * `lasta_di` - LastaDi, Lasta Di. * `spring` - SpringFramework, Spring Framework. * `guice` - GoogleGuice, Google Guice. * `seasar` - SeasarS2Container, Seasar (S2Container). * `cdi` - Cdi, CDI. :: fromCls(CDef$TargetContainer) (enumValue=[lasta_di, spring, guice, seasar, cdi]) (Required) */
  containerCode: string

  /** 自動生成コードの基底パッケージ (dbfluteはここでは含まない) e.g. "org (Required) */
  packageBase: string

  /** JDBCドライバーのクラス名 e.g. "com (Required) */
  jdbcDriverFqcn: string

  /** (Required) */
  mainSchemaSettings: ClientCreateBody_DatabaseSettingsPart

  /** (NullAllowed) */
  systemUserSettings?: ClientCreateBody_DatabaseSettingsPart

  /** DBFluteクライアント作成時のDBFluteエンジンのバージョン、基本的に最新 e.g. "1 (Required) */
  dbfluteVersion: string

  /** (NullAllowed) */
  jdbcDriver?: ClientCreateBody_JdbcDriverPart

  /** (NullAllowed) */
  optionBody?: ClientCreateBody_OptionBodyPart

  /** SchemaSyncCheckするためのDB情報 (Required) */
  schemaSyncCheckMap: any
}

/**
 * The part class of DatabaseSettingsPart.
 * @author FreeGen
 */
type ClientCreateBody_DatabaseSettingsPart = {
  /** JDBCの接続URL e.g. "jdbc:mysql://localhost:3306/maihamadb" (Required) */
  url: string

  /** JDBCの接続スキーマ、DBMSによっては指定なし e.g. maihamadb (NullAllowed) */
  schema?: string

  /** JDBCの接続ユーザー e.g. maihamauser (Required) */
  user: string

  /** JDBCの接続パスワード、パスワードなしなら空っぽ e.g. maihamapass (NullAllowed) */
  password?: string
}

/**
 * The part class of JdbcDriverPart.
 * @author FreeGen
 */
type ClientCreateBody_JdbcDriverPart = {
  /** JDBCドライバーのjarファイルのファイル名 (パスなし、extlib配下で使われる) e.g. "mysql-connector-java-5 (Required) */
  fileName: string

  /** JDBCドライバーのjarファイルの中身のデータ(Base64形式)、豪快だね e.g. QWERTYUIOP (Required) */
  data: string
}

/**
 * The part class of OptionBodyPart.
 * @author FreeGen
 */
type ClientCreateBody_OptionBodyPart = {
  /** (NullAllowed) */
  dbCommentOnAliasBasis?: boolean

  /** (NullAllowed) */
  aliasDelimiterInDbComment?: string

  /** (NullAllowed) */
  checkColumnDefOrderDiff?: boolean

  /** (NullAllowed) */
  checkDbCommentDiff?: boolean

  /** (NullAllowed) */
  checkProcedureDiff?: boolean

  /** (NullAllowed) */
  generateProcedureParameterBean?: boolean

  /** (NullAllowed) */
  procedureSynonymHandlingType?: string
}
