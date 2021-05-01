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

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author deco at rinshi-no-mori
 * @author subaru
 * @author cabos
 */
public class ClientBasicResult {

    // ===================================================================================
    //                                                                         Client Info
    //                                                                         ===========
    @Required
    public String projectName;
    @Required
    public CDef.TargetDatabase databaseCode;
    @Required
    public CDef.TargetLanguage languageCode;
    @Required
    public CDef.TargetContainer containerCode;
    @Required
    public String dbfluteVersion;

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    // done cabos move own logic place (2019-10-19)
    // fix this issue https://github.com/dbflute/dbflute-intro/issues/260
    @Required
    public Boolean hasSchemaHtml;
    @Required
    public Boolean hasHistoryHtml;
    @Required
    public Boolean hasSyncCheckResultHtml;
    @Required
    public Boolean hasAlterCheckResultHtml;

    // ===================================================================================
    //                                                                 Schema Policy Check
    //                                                                 ===================
    public Boolean violatesSchemaPolicy;
}
