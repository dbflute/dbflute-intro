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
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 */
public class DfpropSettingsUpdateBody {

    // TODO you 実際の画面で更新できる項目よりも多くのものが定義されているような by jflute (2023/01/13)
    // いったんは画面で使ってるものだけに絞ったほうが良いかな...
    // って思ったんだけど、dfpropへの更新の仕方が複数のdfpropまるごとが前提なので、それで列挙されてるのかも？
    // まあそれでも、更新に必要な固定項目はサーバー側で補完すれば良いので、レイヤ間のデータやり取りは最低限にしたいところ。
    @Required
    @Valid
    public ClientPart client;

    public static class ClientPart {

        /** DBMSを識別するコード e.g. mysql */
        @Required
        public CDef.TargetDatabase databaseCode;

        /** 自動生成コードの言語を識別するコード e.g. java */
        @Required
        public CDef.TargetLanguage languageCode;

        /** 自動生成コードが利用するDIコンテナを識別するコード e.g. lasta_di */
        @Required
        public CDef.TargetContainer containerCode;

        /** 自動生成コードの基底パッケージ (dbfluteパッケージは含まない) e.g. "org.docksidestage.showbase" */
        @Required
        public String packageBase;

        /** JDBCドライバーのクラス名 e.g. "com.mysql.jdbc.Driver" */
        @Required
        public String jdbcDriverFqcn;

        /** DBのメインスキーマの接続情報、databaseInfoMap.dfpropに反映される */
        @Required
        @Valid
        public ClientPart.DatabaseSettingsPart mainSchemaSettings;

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

        // #for_now hakiba not use these items, may use at future (2017/01/19)
        /** DBFluteクライアントが利用するDBFluteのバージョン e.g. "1.2.6" */
        @Required
        public String dbfluteVersion;
    }
}
