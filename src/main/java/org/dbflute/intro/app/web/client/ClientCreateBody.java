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
package org.dbflute.intro.app.web.client;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

// TODO you WelcomeCreateBodyのtodoと通じる話がある by jflute (2023/01/11)
/**
 * @author p1us2er0
 * @author jflute
 * @author hakiba
 * @author subaru
 */
public class ClientCreateBody {

    /** 一つのDBFluteクライアントを新規作成するための入力情報 */
    @Required
    @Valid
    public ClientPart client;

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

        /** 自動生成コードの基底パッケージ (dbfluteはここでは含まない) e.g. "org.docksidestage.showbase" */
        @Required
        public String packageBase;

        /** JDBCドライバーのクラス名 e.g. "com.mysql.jdbc.Driver" */
        @Required
        public String jdbcDriverFqcn;

        /** DBのメインスキーマの接続情報、databaseInfoMap.dfpropに反映される */
        @Required
        @Valid
        public DatabaseSettingsPart mainSchemaSettings;

        /** DBのシステムユーザー接続情報、ReplaceSchemaのadditionalUserで使うもの */
        // TODO you ReplaceSchema画面で後付けで設定できるような感じで良いのでは？ by jflute (2023/01/11)
        //@Valid
        public DatabaseSettingsPart systemUserSettings;

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

        /** DBFluteクライアント作成時のDBFluteエンジンのバージョン、基本的に最新 e.g. "1.2.6" */
        @Required
        public String dbfluteVersion;

        // you don't need jar file, when target database is embedded jar. so, no validation.
        /** JDBCドライバーのライブラリ情報、組み込まれてないDBMSのときだけ必要 */
        @Valid
        public JdbcDriverPart jdbcDriver;

        public static class JdbcDriverPart {

            // if fileName or data is null, it's client problem.
            /** JDBCドライバーのjarファイルのファイル名 (パスなし、extlib配下で使われる) e.g. "mysql-connector-java-5.1.46-bin.jar" */
            @Required(groups = ClientError.class)
            public String fileName;

            /** JDBCドライバーのjarファイルの中身のデータ(Base64形式)、豪快だね e.g. QWERTYUIOP */
            @Required(groups = ClientError.class)
            public String data;
        }

        /** その他オプションの情報、主にIntroでよく使う系のdfpropのプロパティ */
        // #pending option body validation after client implementation (2016/08/13)
        //@Required
        //@Valid
        public OptionBody optionBody;

        public static class OptionBody {

            //@Required
            public Boolean dbCommentOnAliasBasis;
            public String aliasDelimiterInDbComment;
            //@Required
            public Boolean checkColumnDefOrderDiff;
            //@Required
            public Boolean checkDbCommentDiff;
            //@Required
            public Boolean checkProcedureDiff;
            //@Required
            public Boolean generateProcedureParameterBean;
            public String procedureSynonymHandlingType;
        }
    }

    // ===================================================================================
    //                                                                       Create Option
    //                                                                       =============
    /** DBMSへの接続テストを行うかどうか？ e.g. false */
    @Required
    public Boolean testConnection;
}
