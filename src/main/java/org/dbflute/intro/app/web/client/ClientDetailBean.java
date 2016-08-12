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

import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.Required;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientDetailBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public ClientBean clientBean;
    @Required
    public Boolean schemahtml;
    @Required
    public Boolean historyhtml;
    @Required
    public Boolean replaceSchema;

    public static class ClientBean {

        @Required
        public String projectName;
        @Required
        public String databaseType;
        @Required
        public String languageType;
        @Required
        public String containerType;
        @Required
        public String packageBase;
        @Required
        public String jdbcDriver;

        public DatabaseBean databaseBean;
        public DatabaseBean systemUserDatabaseBean;

        public static class DatabaseBean {

            @Required
            public String url;
            public String schema;
            @Required
            public String user;
            public String password;
        }

        public String jdbcDriverJarPath;
        @Required
        public String dbfluteVersion;
        public OptionBean optionBean;

        public static class OptionBean {

            public boolean dbCommentOnAliasBasis = true;
            public String aliasDelimiterInDbComment = ":";
            public boolean checkColumnDefOrderDiff = true;
            public boolean checkDbCommentDiff = true;
            public boolean checkProcedureDiff = true;
            public boolean generateProcedureParameterBean = true;
            public String procedureSynonymHandlingType = "INCLUDE";
        }

        @NotNull
        public Map<String, DatabaseBean> schemaSyncCheckMap;
    }
}
