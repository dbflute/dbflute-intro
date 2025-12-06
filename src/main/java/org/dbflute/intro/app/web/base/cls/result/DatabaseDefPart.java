/*
 * Copyright 2014-2025 the original author or authors.
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
package org.dbflute.intro.app.web.base.cls.result;

import org.dbflute.intro.dbflute.exentity.ClsTargetDatabase;
import org.lastaflute.web.validation.Required;

/**
 * DBMS定義のレスポンスオブジェクト。
 * @author p1us2er0
 * @author jflute
 */
public class DatabaseDefPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String databaseCode; // PK, not null
    @Required
    public final String databaseName; // not null
    @Required
    public final String driverName; // not null
    @Required
    public final String urlTemplate; // not null

    public final String defaultSchema; // null allowed

    @Required
    public final Boolean schemaRequired;
    @Required
    public final Boolean schemaUpperCase;
    @Required
    public final Boolean userInputAssist;
    @Required
    public final Boolean embeddedJar;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseDefPart(ClsTargetDatabase targetDatabase) {
        this.databaseCode = targetDatabase.getDatabaseCode();
        this.databaseName = targetDatabase.getDatabaseName();
        this.driverName = targetDatabase.getJdbcDriverFqcn();
        this.urlTemplate = targetDatabase.getUrlTemplate();
        this.defaultSchema = targetDatabase.getDefaultSchema();
        this.schemaRequired = targetDatabase.isSchemaRequiredFlgTrue();
        this.schemaUpperCase = targetDatabase.isSchemaUpperCaseFlgTrue();
        this.userInputAssist = targetDatabase.isUserInputAssistFlgTrue();
        this.embeddedJar = targetDatabase.isEmbeddedJarFlgTrue();
    }
}
