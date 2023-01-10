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
package org.dbflute.intro.app.web.welcome;

import javax.validation.Valid;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 */
public class WelcomeCreateBody {

    /** 一つのDBFluteクライアントを新規作成するための入力情報 */
    @Required
    @Valid
    public WelcomeCreateBody.ClientPart client;

    public static class ClientPart {

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

        /** 自動生成コードの基底パッケージ (.dbflute.はここでは含まない) e.g. org.docksidestage.showbase */
        @Required
        public String packageBase;

        /** JDBCドライバーのクラス名 e.g. com.mysql.jdbc.Driver */
        @Required
        public String jdbcDriverFqcn;

        /** DBのメインスキーマの接続情報、databaseInfoMap.dfpropに反映される */
        @Required
        @Valid
        public WelcomeCreateBody.ClientPart.DatabaseSettingsPart mainSchemaSettings;

        public static class DatabaseSettingsPart {

            /** JDBCの接続URL e.g. jdbc:mysql://localhost:3306/maihamadb */
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

        // TODO you いま思ったんだけど、Welcomeでは最新バージョン固定なので、Javaの中で最新を取れば良いのではないだろうか？ by jflute (2023/01/10)
        // (続き) というか、DBFluteエンジンのダウンロードはActionの中で最新バージョンを取得していて...
        // DBFluteクライアントの参照先(_project.sh)として利用されるのがリクエストされたものになっている。
        // フロントからはもらわずに、Actionで取得した最新バージョンをそのままクライアントでも使えば良いかなと。
        /** DBFluteクライアント作成時のDBFluteエンジンのバージョン、基本的に最新 e.g. maihamapass */
        @Required
        public String dbfluteVersion;

        // done hakiba why no validation? comment it by jflute (2017/04/06)
        // you don't need jar file, when target database is embedded jar. so, no validation.
        /** JDBCドライバーのライブラリ情報、組み込まれてないDBMSのときだけ必要 */
        @Valid
        public JdbcDriverPart jdbcDriver;

        public static class JdbcDriverPart {

            // done hakiba add required with ClientError by jflute (2017/04/06)
            // if fileName or data is null, it's client problem.
            /** JDBCドライバーのjarファイルのファイル名 (パスなし、extlib配下で使われる) e.g. mysql-connector-java-5.1.46-bin.jar */
            @Required(groups = ClientError.class)
            public String fileName;

            /** JDBCドライバーのjarファイルの中身のデータ(Base64形式)、豪快だね e.g. QWERTYUIOP */
            @Required(groups = ClientError.class)
            public String data;
        }
    }

    // ===================================================================================
    //                                                                       Create Option
    //                                                                       =============
    // TODO you testsConnectionとかusesSystemProxiesにしたいかも？ by jflute (2023/01/10)
    /** DBMSへの接続テストを行うかどうか？ e.g. false */
    @Required
    public Boolean testConnection;

    // TODO you デフォルト値がfalseなら、Requiredで良いのではないだろうか？ by jflute (2023/01/10)
    // TODO you これ使ってないような？ (welcome.tagもwelcome.tsも) by jflute (2023/01/10)
    /** プロキシサーバーを使うかどうか？ (Introではネットワークを使うので、プロキシ環境なら必要) e.g. false */
    public Boolean useSystemProxies = false; // only use if user selected
}
