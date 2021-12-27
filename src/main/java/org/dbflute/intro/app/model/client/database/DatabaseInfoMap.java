/*
 * Copyright 2014-2021 the original author or authors.
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
    //                                                                          Definition
    //                                                                          ==========
    public static final String DFPROP_NAME = "databaseInfoMap.dfprop";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String driver; // not null
    protected final DbConnectionBox dbConnectionInfo; // not null
    protected final AdditionalSchemaMap additionalSchemaMap; // not null, empty allowed

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DatabaseInfoMap(String driver, DbConnectionBox dbConnectionInfo, AdditionalSchemaMap additionalSchemaMap) {
        if (driver == null) {
            throw new IllegalArgumentException("The argument 'driver' should be not null.");
        }
        if (dbConnectionInfo == null) {
            throw new IllegalArgumentException("The argument 'dbConnectionInfo' should be not null.");
        }
        if (additionalSchemaMap == null) {
            throw new IllegalArgumentException("The argument 'additionalSchemaMap' should be not null.");
        }
        this.driver = driver;
        this.dbConnectionInfo = dbConnectionInfo;
        this.additionalSchemaMap = additionalSchemaMap;
    }

    public static DatabaseInfoMap createWithoutAdditional(String driver, DbConnectionBox dbConnectionInfo) {
        return new DatabaseInfoMap(driver, dbConnectionInfo);
    }

    protected DatabaseInfoMap(String driver, DbConnectionBox dbConnectionInfo) {
        this(driver, dbConnectionInfo, AdditionalSchemaMap.empty());
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

    public AdditionalSchemaMap getAdditionalSchemaMap() {
        return additionalSchemaMap;
    }
}
