/*
 * Copyright 2014-2016 the original author or authors.
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
 */
public class ClientOperationResult {

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

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    // TODO deco pri.C convert to camel by jflute (2017/02/23)
    @Required
    public Boolean hasSchemahtml;
    @Required
    public Boolean hasHistoryhtml;
    @Required
    public Boolean hasSynccheckresulthtml;
}
