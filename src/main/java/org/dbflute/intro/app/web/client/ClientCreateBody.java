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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.Required;

/**
 * @author p1us2er0
 */
public class ClientCreateBody {

    @Required
    @Valid
    public ClientBody clientBody;

    /**
     * @author p1us2er0
     */
    public static class ClientBody {

        // ===================================================================================
        //                                                                           Attribute
        //                                                                           =========
        @Required
        public String project;

        @Required
        public String database;

        @Required
        public String targetLanguage;

        @Required
        public String targetContainer;

        @Required
        public String packageBase;

        @Required
        public String jdbcDriver;

        @Required
        @Valid
        public DatabaseBody databaseBody;

        @Required
        @Valid
        public DatabaseBody systemUserDatabaseBody;

        /**
         * @author p1us2er0
         */
        public static class DatabaseBody {

            // ===================================================================================
            //                                                                           Attribute
            //                                                                           =========
            public String url;
            public String schema;
            public String user;
            public String password;
        }

        public String jdbcDriverJarPath;

        @Required
        public String dbfluteVersion;

        @Required
        @Valid
        public OptionBody optionBody;

        /**
         * @author p1us2er0
         */
        public static class OptionBody {

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

        @NotNull
        public Map<String, DatabaseBody> schemaSyncCheckMap;
    }

    @Required
    public Boolean testConnection = true;
}
