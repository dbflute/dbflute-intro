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
package org.dbflute.intro.app.web.client.propbase;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * The result of client basic information for shared with functions of one client.
 * @author deco at rinshi-no-mori
 * @author subaru
 * @author cabos
 * @author jflute
 */
public class ClientPropbaseResult { // prop-base means basic properties of client

    // ===================================================================================
    //                                                                         Client Info
    //                                                                         ===========
    // basically for header information
    /** DBFluteクライアントのプロジェクト名 e.g. maihamadb */
    @Required
    public String projectName;

    /** DBMSを識別するコード e.g. mysql */
    @Required
    public CDef.TargetDatabase databaseCode;

    /** 自動生成コードの言語を識別するコード e.g. java */
    @Required
    public CDef.TargetLanguage languageCode;

    /** 自動生成コードが利用するDIコンテナを識別するコード e.g. lasta_di */
    @Required
    public CDef.TargetContainer containerCode;

    /** そのDBFluteクライアントが利用するDBFluteのバージョン e.g. 1.2.6 */
    @Required
    public String dbfluteVersion;

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    // #needs_fix anyone move own logic place by jflute (2021/05/08) :: first (2019-10-19)
    // fix this issue https://github.com/dbflute/dbflute-intro/issues/260
    /** SchemaHTMLが自動生成されているかどうか？ e.g. false */
    @Required
    public Boolean hasSchemaHtml;

    /** HistoryHTMLが自動生成されているかどうか？ e.g. false */
    @Required
    public Boolean hasHistoryHtml;

    /** SchemaSyncCheckの結果HTMLが存在しているかどうか？ e.g. false */
    @Required
    public Boolean hasSyncCheckResultHtml;

    /** AlterCheckの結果HTMLが存在しているかどうか？ e.g. false */
    @Required
    public Boolean hasAlterCheckResultHtml;

    // ===================================================================================
    //                                                                 Schema Policy Check
    //                                                                 ===================
    // TODO you フロントでは if (client.violatesSchemaPolicy) { してるだけなので、Requiredでも良いのでは？ by jflute (2023/01/11)
    /** SchemaPolicyの違反がある状態かどうか？ (古いバージョンの場合はnull) e.g. false */
    public Boolean violatesSchemaPolicy;
}
