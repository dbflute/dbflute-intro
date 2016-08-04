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
package org.dbflute.intro.app.web.base.cls;

import org.dbflute.intro.dbflute.exentity.ClsTargetDatabase;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DatabaseTypeBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final String databaseName;
    private final String driverName;
    private final String urlTemplate;
    private final String defaultSchema;
    private final boolean schemaRequired;
    private final boolean schemaUpperCase;
    private final boolean userInputAssist;
    private final boolean embeddedJar;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseTypeBean(ClsTargetDatabase databaseInfoDef) {
        this.databaseName = databaseInfoDef.getDatabaseName();
        this.driverName = databaseInfoDef.getJdbcDriverFqcn();
        this.urlTemplate = databaseInfoDef.getUrlTemplate();
        this.defaultSchema = databaseInfoDef.getDefaultSchema();
        this.schemaRequired = databaseInfoDef.isSchemaRequiredFlgTrue();
        this.schemaUpperCase = databaseInfoDef.isSchemaUpperCaseFlgTrue();
        this.userInputAssist = databaseInfoDef.isUserInputAssistFlgTrue();
        this.embeddedJar = databaseInfoDef.isEmbeddedJarFlgTrue();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDatabaseName() {
        return databaseName;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public String getDefultSchema() {
        return defaultSchema;
    }

    public boolean isSchemaRequired() {
        return schemaRequired;
    }

    public boolean isSchemaUpperCase() {
        return schemaUpperCase;
    }

    public boolean isUserInputAssist() {
        return userInputAssist;
    }

    public boolean isEmbeddedJar() {
        return embeddedJar;
    }
}
