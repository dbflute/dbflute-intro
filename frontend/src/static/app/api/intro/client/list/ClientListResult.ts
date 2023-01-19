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
 * The bean class as return for remote API of GET /client/list/.
 * @author FreeGen
 */
type ClientListResult = {
  /** DBFluteクライアントのプロジェクト名 e.g. maihamadb (Required) */
  projectName: string

  /** DBMSを識別するコード e.g. mysql: * `mysql` - MySQL. * `postgresql` - PostgreSQL. * `oracle` - Oracle. * `db2` - Db2, DB2. * `sqlserver` - SQLServer. * `h2` - H2Database, H2 Database. * `derby` - ApacheDerby, Apache Derby. :: fromCls(CDef$TargetDatabase) (enumValue=[mysql, postgresql, oracle, db2, sqlserver, h2, derby]) (Required) */
  databaseCode: string

  /** 自動生成コードの言語を識別するコード e.g. java: * `java` - Java. * `csharp` - C, C#. * `scala` - Scala. :: fromCls(CDef$TargetLanguage) (enumValue=[java, csharp, scala]) (Required) */
  languageCode: string

  /** 自動生成コードが利用するDIコンテナを識別するコード e.g. lasta_di: * `lasta_di` - LastaDi, Lasta Di. * `spring` - SpringFramework, Spring Framework. * `guice` - GoogleGuice, Google Guice. * `seasar` - SeasarS2Container, Seasar (S2Container). * `cdi` - Cdi, CDI. :: fromCls(CDef$TargetContainer) (enumValue=[lasta_di, spring, guice, seasar, cdi]) (Required) */
  containerCode: string
}
