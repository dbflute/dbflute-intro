/*
 * Copyright 2014-2015 the original author or authors.
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

/**
 * @author p1us2er0
 */
public class ClientDetailBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public ClientBean clientBean;
    public boolean schemahtml;
    public boolean historyhtml;
    public boolean replaceSchema;

    /**
     * @author p1us2er0
     */
    public static class ClientBean {

        // ===================================================================================
        //                                                                           Attribute
        //                                                                           =========
        public String project;

        public String database;

        public String targetLanguage;

        public String targetContainer;

        public String packageBase;

        public String jdbcDriver;

        public DatabaseBean databaseBean;

        public DatabaseBean systemUserDatabaseBean;

        /**
         * @author p1us2er0
         */
        public static class DatabaseBean {

            // ===================================================================================
            //                                                                           Attribute
            //                                                                           =========
            public String url;
            public String schema;
            public String user;
            public String password;
        }

        public String jdbcDriverJarPath;

        public String dbfluteVersion;

        public OptionBean optionBean;

        /**
         * @author p1us2er0
         */
        public static class OptionBean {

            // ===================================================================================
            //                                                                           Attribute
            //                                                                           =========
            public boolean dbCommentOnAliasBasis = true;
            public String aliasDelimiterInDbComment = ":";
            public boolean checkColumnDefOrderDiff = true;
            public boolean checkDbCommentDiff = true;
            public boolean checkProcedureDiff = true;
            public boolean generateProcedureParameterBean = true;
            public String procedureSynonymHandlingType = "INCLUDE";
        }

        public Map<String, DatabaseBean> schemaSyncCheckMap;
    }
}
