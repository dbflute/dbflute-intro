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
package org.dbflute.intro.app.logic.dbfluteclient;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author p1us2er0
 */
public class ClientParam {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String project;

    private String database;

    private String targetLanguage;

    private String targetContainer;

    private String packageBase;

    private String jdbcDriver;

    private DatabaseParam databaseParam;

    private DatabaseParam systemUserDatabaseParam;

    private String jdbcDriverJarPath;

    private String dbfluteVersion;

    private OptionParam optionParam;

    private Map<String, DatabaseParam> schemaSyncCheckMap;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getProject() {
        return project;
    }

    public void setProject(String projectName) {
        this.project = projectName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getTargetContainer() {
        return targetContainer;
    }

    public void setTargetContainer(String targetContainer) {
        this.targetContainer = targetContainer;
    }

    public String getPackageBase() {
        return packageBase;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public DatabaseParam getDatabaseParam() {
        if (databaseParam == null) {
            databaseParam = new DatabaseParam();
        }

        return databaseParam;
    }

    public void setDatabaseParam(DatabaseParam databaseParam) {
        this.databaseParam = databaseParam;
    }

    public DatabaseParam getSystemUserDatabaseParam() {
        if (systemUserDatabaseParam == null) {
            systemUserDatabaseParam = new DatabaseParam();
        }

        return systemUserDatabaseParam;
    }

    public void setSystemUserDatabaseParam(DatabaseParam systemUserDatabaseParam) {
        this.systemUserDatabaseParam = systemUserDatabaseParam;
    }

    public String getJdbcDriverJarPath() {
        return jdbcDriverJarPath;
    }

    public void setJdbcDriverJarPath(String jdbcDriverJarPath) {
        this.jdbcDriverJarPath = jdbcDriverJarPath;
    }

    public String getDbfluteVersion() {
        return dbfluteVersion;
    }

    public void setDbfluteVersion(String versionInfoDBFlute) {
        this.dbfluteVersion = versionInfoDBFlute;
    }

    public OptionParam getOptionParam() {
        if (optionParam == null) {
            optionParam = new OptionParam();
        }

        return optionParam;
    }

    public void setOptionParam(OptionParam optionParam) {
        this.optionParam = optionParam;
    }

    public Map<String, DatabaseParam> getSchemaSyncCheckMap() {
        if (schemaSyncCheckMap == null) {
            schemaSyncCheckMap = new LinkedHashMap<String, DatabaseParam>();
        }

        return schemaSyncCheckMap;
    }

    public void setSchemaSyncCheckMap(Map<String, DatabaseParam> schemaSyncCheckMap) {
        this.schemaSyncCheckMap = schemaSyncCheckMap;
    }
}
