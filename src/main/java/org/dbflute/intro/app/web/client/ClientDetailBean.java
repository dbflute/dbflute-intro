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

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientDetailBean {

    @Required
    public ClientBean clientBean;

    public static class ClientBean {

        @Required
        public String clientProject;
        @Required
        public CDef.TargetDatabase targetDatabase;
        @Required
        public CDef.TargetLanguage targetLanguage;
        @Required
        public CDef.TargetContainer targetContainer;
        @Required
        public String packageBase;
        @Required
        public String jdbcDriverFqcn;

        @Required
        @Valid
        public DatabaseBean databaseBean;

        @Valid
        public DatabaseBean systemUserBean;

        public static class DatabaseBean {

            @Required
            public String url;
            public String schema; // contains additional schema by comma
            @Required
            public String user;
            public String password;
        }

        public String jdbcDriverJarPath;

        @Required
        public String dbfluteVersion;

        @Valid
        public OptionBean optionBean;

        public static class OptionBean {

            @Required
            public Boolean dbCommentOnAliasBasis;
            public String aliasDelimiterInDbComment;
            @Required
            public Boolean checkColumnDefOrderDiff;
            @Required
            public Boolean checkDbCommentDiff;
            @Required
            public Boolean checkProcedureDiff;
            @Required
            public Boolean generateProcedureParameterBean;
            public String procedureSynonymHandlingType;
        }

        @NotNull
        public Map<String, DatabaseBean> schemaSyncCheckMap;
    }

    @Required
    public Boolean schemahtml;
    @Required
    public Boolean historyhtml;
    @Required
    public Boolean replaceSchema;
}
