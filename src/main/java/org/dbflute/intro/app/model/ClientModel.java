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
package org.dbflute.intro.app.model;

import java.util.Map;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientModel {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String clientProject;
    private String database;
    private String targetLanguage;
    private String targetContainer;
    private String packageBase;

    private String jdbcDriverFqcn; // common with various database info
    private String jdbcDriverJarPath; // from extlib
    private String dbfluteVersion; // from _project.sh

    private DatabaseModel databaseModel; // contains additional schema
    private DatabaseModel systemUserDatabaseModel; // fromm replaceSchemaMap.dfprop
    private OptionModel optionModel; // e.g. documentMap.dfprop, outsideSqlMap.dfprop
    private Map<String, DatabaseModel> schemaSyncCheckMap; // from documentMap.dfprop

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getClientProject() {
        return clientProject;
    }

    public void setClientProject(String clientProject) {
        this.clientProject = clientProject;
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

    public String getJdbcDriverFqcn() {
        return jdbcDriverFqcn;
    }

    public void setJdbcDriverFqcn(String jdbcDriverFqcn) {
        this.jdbcDriverFqcn = jdbcDriverFqcn;
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

    public DatabaseModel getDatabaseModel() {
        return databaseModel;
    }

    public void setDatabaseModel(DatabaseModel databaseModel) {
        this.databaseModel = databaseModel;
    }

    public DatabaseModel getSystemUserDatabaseModel() {
        return systemUserDatabaseModel;
    }

    public void setSystemUserDatabaseModel(DatabaseModel systemUserDatabaseModel) {
        this.systemUserDatabaseModel = systemUserDatabaseModel;
    }

    public OptionModel getOptionModel() {
        return optionModel;
    }

    public void setOptionModel(OptionModel optionModel) {
        this.optionModel = optionModel;
    }

    public Map<String, DatabaseModel> getSchemaSyncCheckMap() {
        return schemaSyncCheckMap;
    }

    public void setSchemaSyncCheckMap(Map<String, DatabaseModel> schemaSyncCheckMap) {
        this.schemaSyncCheckMap = schemaSyncCheckMap;
    }
}
