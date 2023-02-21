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
package org.dbflute.intro.app.web.client.list;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * DBFluteクライアント一覧における一行のデータ。
 * @author p1us2er0
 * @author jflute
 */
public class ClientRowResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // DBFluteクライアント一覧で必要な項目だけ定義、必要になった時に追加 by jflute (2022/01/15) 
    // _/_/_/_/_/_/_/_/_/_/
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

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
