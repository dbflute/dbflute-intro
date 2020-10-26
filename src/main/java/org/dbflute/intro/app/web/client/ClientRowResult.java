/*
 * Copyright 2014-2020 the original author or authors.
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
import org.lastaflute.web.validation.Required;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientRowResult {

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
    public String packageBase;
    @Required
    public String jdbcDriverFqcn;

    @Required
    @Valid
    public DatabaseSettingsPart mainSchemaSettings;

    @Valid
    public DatabaseSettingsPart systemUserSettings;

    // TODO hakiba implement another DatabaseSettingsPart with the main user and system user, but we do not seriously consider implementing AdditionalUserMap yet.
    public static class DatabaseSettingsPart {
        // url property is mandatory for main user, but not for system user
        public String url;
        public String schema;
        @Required
        public String user;
        public String password;
    }

    @Required
    public String dbfluteVersion;

    public String jdbcDriverJarPath;

    // #hope documentOption, outsideSqlOption by jflute
    @Valid
    public OptionPart optionBean;

    public static class OptionPart {

        // documentMap.dfprop is not required so may be null
        // #thinking jflute however... can boolean property be false as default? (2019/10/24)
        public Boolean dbCommentOnAliasBasis;
        public String aliasDelimiterInDbComment;
        public Boolean checkColumnDefOrderDiff;
        public Boolean checkDbCommentDiff;
        public Boolean checkProcedureDiff;
        public Boolean generateProcedureParameterBean; // If outsideSqlMap.dfprop does not exist, this property is null.
        public String procedureSynonymHandlingType;
    }

    @NotNull
    public Map<String, DatabaseSettingsPart> schemaSyncCheckMap;

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    @Required
    public Boolean hasSchemaHtml;
    @Required
    public Boolean hasHistoryHtml;
    @Required
    public Boolean hasReplaceSchema;
}
