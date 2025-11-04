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
package org.dbflute.intro.app.web.base.cls.result;

import org.dbflute.intro.dbflute.exentity.ClsTargetLanguage;
import org.lastaflute.web.validation.Required;

/**
 * 自動生成されるクラスのプログラミング言語に関する定義のレスポンスオブジェクト。
 * @author jflute (2025/10/28 Tuesday at ichihara)
 */
public class LanguageDefPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String languageCode; // PK, not null
    @Required
    public final String languageName; // not null

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public LanguageDefPart(ClsTargetLanguage targetLanguage) {
        this.languageCode = targetLanguage.getLanguageCode();
        this.languageName = targetLanguage.getLanguageName();
    }
}
