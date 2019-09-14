/*
 * Copyright 2014-2019 the original author or authors.
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

import java.util.List;

import javax.validation.Valid;

import org.dbflute.intro.app.logic.document.AlterSqlBean;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author deco at rinshi-no-mori
 * @author subaru
 * @author cabos
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
    @Required
    public String dbfluteVersion;

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    @Required
    public Boolean hasSchemaHtml;
    @Required
    public Boolean hasHistoryHtml;
    @Required
    public Boolean hasSyncCheckResultHtml;
    @Required
    public Boolean hasAlterCheckResultHtml;

    // ===================================================================================
    //                                                                         Alter Check
    //                                                                         ===========
    // TODO cabos Delete It. (Refactoring) (2019-09-14)
    public CDef.NgMark ngMark;
    @Valid
    public List<AlterSqlBean> editingAlterSqls;
    @Valid
    public List<AlterSqlBean> stackedAlterSqls;

    // ===================================================================================
    //                                                                 Schema Policy Check
    //                                                                 ===================
    public Boolean violatesSchemaPolicy;
}
