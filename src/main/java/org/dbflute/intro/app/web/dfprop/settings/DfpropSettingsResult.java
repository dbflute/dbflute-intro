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
package org.dbflute.intro.app.web.dfprop.settings;

import javax.validation.Valid;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 */
public class DfpropSettingsResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
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

    /** 自動生成コードの基底パッケージ (dbfluteはここでは含まない) e.g. "org.docksidestage.showbase" */
    @Required
    public String packageBase;

    /** JDBCドライバーのクラス名 e.g. "com.mysql.jdbc.Driver" */
    @Required
    public String jdbcDriverFqcn;

    /** DBのメインスキーマの接続情報、databaseInfoMap.dfpropに記載されているもの */
    @Required
    @Valid
    public DatabaseSettingsPart mainSchemaSettings;

    public static class DatabaseSettingsPart {

        /** JDBCの接続URL e.g. "jdbc:mysql://localhost:3306/maihamadb" */
        @Required
        public String url;

        /** JDBCの接続スキーマ、DBMSによっては指定なし e.g. maihamadb */
        public String schema;

        /** JDBCの接続ユーザー e.g. maihamauser */
        @Required
        public String user;

        /** JDBCの接続パスワード、パスワードなしなら空っぽ e.g. maihamapass */
        public String password;
    }

    /** DBFluteクライアントが利用するDBFluteのバージョン e.g. "1.2.6" */
    @Required
    public String dbfluteVersion;

    // TODO you 画面で使ってるところが見つからない... by jflute (2023/01/13)
    /** extlib配下のJDBCドライバーのjarファイルのcanonical path e.g. "/Users/intro/xxx.../extlib/yyy.jar" (NullAllowed) */
    public String jdbcDriverJarPath;

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
