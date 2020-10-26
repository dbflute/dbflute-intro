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
package org.dbflute.intro.app.model.client.database;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.logic.core.MapStringLogic;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.lastaflute.core.util.ContainerUtil;

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

    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;
    @Resource
    private MapStringLogic mapStringLogic;

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
        ContainerUtil.injectSimply(this);
    }

    public static DatabaseInfoMap createWithoutAdditional(String driver, DbConnectionBox dbConnectionInfo) {
        return new DatabaseInfoMap(driver, dbConnectionInfo);
    }

    protected DatabaseInfoMap(String driver, DbConnectionBox dbConnectionInfo) {
        this(driver, dbConnectionInfo, AdditionalSchemaMap.empty());
    }

    // ===================================================================================
    //                                                                             Replace
    //                                                                             =======
    public Map<String, Object> prepareInitReplaceMap() {
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        replaceMap.put("@driver@", escapeControlMark(driver));
        replaceMap.put("@url@", escapeControlMark(dbConnectionInfo.getUrl()));
        replaceMap.put("@schema@", escapeControlMark(dbConnectionInfo.getSchema()));
        replaceMap.put("@user@", escapeControlMark(dbConnectionInfo.getUser()));
        replaceMap.put("@password@", escapeControlMark(dbConnectionInfo.getPassword()));
        return replaceMap;
    }

    // ===================================================================================
    //                                                                            Physical
    //                                                                            ========
    public File findDfpropFile(String clientName) {
        return clientPhysicalLogic.findDfpropDatabaseInfoMap(clientName);
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private String escapeControlMark(Object value) {
        return mapStringLogic.escapeControlMark(value);
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
