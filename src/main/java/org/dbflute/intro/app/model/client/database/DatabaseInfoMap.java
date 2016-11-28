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
package org.dbflute.intro.app.model.client.database;

import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;

/**
 * @author jflute
 */
public class DatabaseInfoMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String driver;
    protected final DbConnectionBox dbConnectionInfo;
    protected final AdditionalSchemaMap additionalSchemaMap; // not null, empty allowed

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseInfoMap(String driver, DbConnectionBox dbConnectionInfo, AdditionalSchemaMap additionalSchemaMap) {
        this.driver = driver;
        this.dbConnectionInfo = dbConnectionInfo;
        this.additionalSchemaMap = additionalSchemaMap;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return "database:{" + driver + ", " + dbConnectionInfo + ", " + additionalSchemaMap.getSchemaBoxMap().keySet() + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDriver() {
        return driver;
    }

    public DbConnectionBox getDbConnectionBox() {
        return dbConnectionInfo;
    }
}
