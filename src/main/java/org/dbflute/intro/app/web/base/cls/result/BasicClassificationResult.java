/*
 * Copyright 2014-2025 the original author or authors.
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

import java.util.List;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * 基本的な区分値定義のレスポンスオブジェクト。<br>
 * DBMSの種別など、横断的に使う区分値たち。
 * @author jflute (2025/10/28 Tuesday at ichihara)
 */
public class BasicClassificationResult {

    // TODO jflute JSON上の項目名、複数形？List？ (2025/10/28)
    @Required
    @Valid
    public List<DatabaseDefPart> targetDatabaseList;

    @Required
    @Valid
    public List<LanguageDefPart> targetLanguageList;

    @Required
    @Valid
    public List<ContainerDefPart> targetContainerList;
}
