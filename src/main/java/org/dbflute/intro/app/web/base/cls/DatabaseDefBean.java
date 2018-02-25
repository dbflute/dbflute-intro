/*
 * Copyright 2014-2018 the original author or authors.
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
public class DatabaseDefBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public final String databaseName;
    public final String driverName;
    public final String urlTemplate;
    public final String defaultSchema;
    public final boolean schemaRequired;
    public final boolean schemaUpperCase;
    public final boolean userInputAssist;
    public final boolean embeddedJar;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseDefBean(ClsTargetDatabase databaseInfoDef) {
        this.databaseName = databaseInfoDef.getDatabaseName();
        this.driverName = databaseInfoDef.getJdbcDriverFqcn();
        this.urlTemplate = databaseInfoDef.getUrlTemplate();
        this.defaultSchema = databaseInfoDef.getDefaultSchema();
        this.schemaRequired = databaseInfoDef.isSchemaRequiredFlgTrue();
        this.schemaUpperCase = databaseInfoDef.isSchemaUpperCaseFlgTrue();
        this.userInputAssist = databaseInfoDef.isUserInputAssistFlgTrue();
        this.embeddedJar = databaseInfoDef.isEmbeddedJarFlgTrue();
    }
}
