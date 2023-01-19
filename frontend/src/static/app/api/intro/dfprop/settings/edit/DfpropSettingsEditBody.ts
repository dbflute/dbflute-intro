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
 * The bean class as param for remote API of POST /dfprop/settings/edit/{clientName}.
 * @author FreeGen
 */
type DfpropSettingsEditBody = {
  /** (Required) */
  client: DfpropSettingsEditBody_ClientPart
}

/**
 * The part class of ClientPart.
 * @author FreeGen
 */
type DfpropSettingsEditBody_ClientPart = {
  /** DBMSを識別するコード e.g. mysql: * `mysql` - MySQL. * `postgresql` - PostgreSQL. * `oracle` - Oracle. * `db2` - Db2, DB2. * `sqlserver` - SQLServer. * `h2` - H2Database, H2 Database. * `derby` - ApacheDerby, Apache Derby. :: fromCls(CDef$TargetDatabase) (enumValue=[mysql, postgresql, oracle, db2, sqlserver, h2, derby]) (Required) */
  databaseCode: string

  /** 自動生成コードの言語を識別するコード e.g. java: * `java` - Java. * `csharp` - C, C#. * `scala` - Scala. :: fromCls(CDef$TargetLanguage) (enumValue=[java, csharp, scala]) (Required) */
  languageCode: string

  /** 自動生成コードが利用するDIコンテナを識別するコード e.g. lasta_di: * `lasta_di` - LastaDi, Lasta Di. * `spring` - SpringFramework, Spring Framework. * `guice` - GoogleGuice, Google Guice. * `seasar` - SeasarS2Container, Seasar (S2Container). * `cdi` - Cdi, CDI. :: fromCls(CDef$TargetContainer) (enumValue=[lasta_di, spring, guice, seasar, cdi]) (Required) */
  containerCode: string

  /** 自動生成コードの基底パッケージ (dbfluteパッケージは含まない) e.g. "org (Required) */
  packageBase: string

  /** JDBCドライバーのクラス名 e.g. "com (Required) */
  jdbcDriverFqcn: string

  /** (Required) */
  mainSchemaSettings: DfpropSettingsEditBody_DatabaseSettingsPart

  /** DBFluteクライアントが利用するDBFluteのバージョン e.g. "1 (Required) */
  dbfluteVersion: string
}

/**
 * The part class of DatabaseSettingsPart.
 * @author FreeGen
 */
type DfpropSettingsEditBody_DatabaseSettingsPart = {
  /** JDBCの接続URL e.g. "jdbc:mysql://localhost:3306/maihamadb" (Required) */
  url: string

  /** JDBCの接続スキーマ、DBMSによっては指定なし e.g. maihamadb (NullAllowed) */
  schema?: string

  /** JDBCの接続ユーザー e.g. maihamauser (Required) */
  user: string

  /** JDBCの接続パスワード、パスワードなしなら空っぽ e.g. maihamapass (NullAllowed) */
  password?: string
}
