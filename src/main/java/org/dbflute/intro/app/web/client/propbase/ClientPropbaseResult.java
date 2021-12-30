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
    // #needs_fix anyone move own logic place by jflute (2021/05/08) :: first (2019-10-19)
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
