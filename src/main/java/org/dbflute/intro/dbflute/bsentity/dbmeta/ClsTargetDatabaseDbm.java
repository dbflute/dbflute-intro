/*
 * Copyright 2014-2019 the original author or authors.
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
package org.dbflute.intro.dbflute.bsentity.dbmeta;

import java.util.List;
import java.util.Map;

import org.dbflute.Entity;
import org.dbflute.dbmeta.AbstractDBMeta;
import org.dbflute.dbmeta.info.*;
import org.dbflute.dbmeta.name.*;
import org.dbflute.dbmeta.property.PropertyGateway;
import org.dbflute.dbway.DBDef;
import org.dbflute.intro.dbflute.allcommon.*;
import org.dbflute.intro.dbflute.exentity.*;

/**
 * The DB meta of CLS_TARGET_DATABASE. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class ClsTargetDatabaseDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final ClsTargetDatabaseDbm _instance = new ClsTargetDatabaseDbm();
    private ClsTargetDatabaseDbm() {}
    public static ClsTargetDatabaseDbm getInstance() { return _instance; }

    // ===================================================================================
    //                                                                       Current DBDef
    //                                                                       =============
    public String getProjectName() { return DBCurrent.getInstance().projectName(); }
    public String getProjectPrefix() { return DBCurrent.getInstance().projectPrefix(); }
    public String getGenerationGapBasePrefix() { return DBCurrent.getInstance().generationGapBasePrefix(); }
    public DBDef getCurrentDBDef() { return DBCurrent.getInstance().currentDBDef(); }

    // ===================================================================================
    //                                                                    Property Gateway
    //                                                                    ================
    // -----------------------------------------------------
    //                                       Column Property
    //                                       ---------------
    protected final Map<String, PropertyGateway> _epgMap = newHashMap();
    { xsetupEpg(); }
    protected void xsetupEpg() {
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getDatabaseCode(), (et, vl) -> {
            CDef.TargetDatabase cls = (CDef.TargetDatabase)gcls(et, columnDatabaseCode(), vl);
            if (cls != null) {
                ((ClsTargetDatabase)et).setDatabaseCodeAsTargetDatabase(cls);
            } else {
                ((ClsTargetDatabase)et).mynativeMappingDatabaseCode((String)vl);
            }
        }, "databaseCode");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getDatabaseName(), (et, vl) -> ((ClsTargetDatabase)et).setDatabaseName((String)vl), "databaseName");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getJdbcDriverFqcn(), (et, vl) -> ((ClsTargetDatabase)et).setJdbcDriverFqcn((String)vl), "jdbcDriverFqcn");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getUrlTemplate(), (et, vl) -> ((ClsTargetDatabase)et).setUrlTemplate((String)vl), "urlTemplate");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getDefaultSchema(), (et, vl) -> ((ClsTargetDatabase)et).setDefaultSchema((String)vl), "defaultSchema");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getSchemaRequiredFlg(), (et, vl) -> {
            CDef.Flg cls = (CDef.Flg)gcls(et, columnSchemaRequiredFlg(), vl);
            if (cls != null) {
                ((ClsTargetDatabase)et).setSchemaRequiredFlgAsFlg(cls);
            } else {
                ((ClsTargetDatabase)et).mynativeMappingSchemaRequiredFlg(ctn(vl, Integer.class));
            }
        }, "schemaRequiredFlg");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getSchemaUpperCaseFlg(), (et, vl) -> {
            CDef.Flg cls = (CDef.Flg)gcls(et, columnSchemaUpperCaseFlg(), vl);
            if (cls != null) {
                ((ClsTargetDatabase)et).setSchemaUpperCaseFlgAsFlg(cls);
            } else {
                ((ClsTargetDatabase)et).mynativeMappingSchemaUpperCaseFlg(ctn(vl, Integer.class));
            }
        }, "schemaUpperCaseFlg");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getUserInputAssistFlg(), (et, vl) -> {
            CDef.Flg cls = (CDef.Flg)gcls(et, columnUserInputAssistFlg(), vl);
            if (cls != null) {
                ((ClsTargetDatabase)et).setUserInputAssistFlgAsFlg(cls);
            } else {
                ((ClsTargetDatabase)et).mynativeMappingUserInputAssistFlg(ctn(vl, Integer.class));
            }
        }, "userInputAssistFlg");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getEmbeddedJarFlg(), (et, vl) -> {
            CDef.Flg cls = (CDef.Flg)gcls(et, columnEmbeddedJarFlg(), vl);
            if (cls != null) {
                ((ClsTargetDatabase)et).setEmbeddedJarFlgAsFlg(cls);
            } else {
                ((ClsTargetDatabase)et).mynativeMappingEmbeddedJarFlg(ctn(vl, Integer.class));
            }
        }, "embeddedJarFlg");
        setupEpg(_epgMap, et -> ((ClsTargetDatabase)et).getDisplayOrder(), (et, vl) -> ((ClsTargetDatabase)et).setDisplayOrder(cti(vl)), "displayOrder");
    }
    public PropertyGateway findPropertyGateway(String prop)
    { return doFindEpg(_epgMap, prop); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "CLS_TARGET_DATABASE";
    protected final String _tableDispName = "CLS_TARGET_DATABASE";
    protected final String _tablePropertyName = "clsTargetDatabase";
    protected final TableSqlName _tableSqlName = new TableSqlName("CLS_TARGET_DATABASE", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTableDispName() { return _tableDispName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnDatabaseCode = cci("DATABASE_CODE", "DATABASE_CODE", null, null, String.class, "databaseCode", null, true, false, true, "VARCHAR", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.TargetDatabase, false);
    protected final ColumnInfo _columnDatabaseName = cci("DATABASE_NAME", "DATABASE_NAME", null, null, String.class, "databaseName", null, false, false, true, "VARCHAR", 100, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnJdbcDriverFqcn = cci("JDBC_DRIVER_FQCN", "JDBC_DRIVER_FQCN", null, null, String.class, "jdbcDriverFqcn", null, false, false, true, "VARCHAR", 100, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnUrlTemplate = cci("URL_TEMPLATE", "URL_TEMPLATE", null, null, String.class, "urlTemplate", null, false, false, true, "VARCHAR", 100, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnDefaultSchema = cci("DEFAULT_SCHEMA", "DEFAULT_SCHEMA", null, null, String.class, "defaultSchema", null, false, false, false, "VARCHAR", 10, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnSchemaRequiredFlg = cci("SCHEMA_REQUIRED_FLG", "SCHEMA_REQUIRED_FLG", null, null, Integer.class, "schemaRequiredFlg", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.Flg, false);
    protected final ColumnInfo _columnSchemaUpperCaseFlg = cci("SCHEMA_UPPER_CASE_FLG", "SCHEMA_UPPER_CASE_FLG", null, null, Integer.class, "schemaUpperCaseFlg", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.Flg, false);
    protected final ColumnInfo _columnUserInputAssistFlg = cci("USER_INPUT_ASSIST_FLG", "USER_INPUT_ASSIST_FLG", null, null, Integer.class, "userInputAssistFlg", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.Flg, false);
    protected final ColumnInfo _columnEmbeddedJarFlg = cci("EMBEDDED_JAR_FLG", "EMBEDDED_JAR_FLG", null, null, Integer.class, "embeddedJarFlg", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.Flg, false);
    protected final ColumnInfo _columnDisplayOrder = cci("DISPLAY_ORDER", "DISPLAY_ORDER", null, null, Integer.class, "displayOrder", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, null, false);

    /**
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnDatabaseCode() { return _columnDatabaseCode; }
    /**
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnDatabaseName() { return _columnDatabaseName; }
    /**
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnJdbcDriverFqcn() { return _columnJdbcDriverFqcn; }
    /**
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnUrlTemplate() { return _columnUrlTemplate; }
    /**
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnDefaultSchema() { return _columnDefaultSchema; }
    /**
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnSchemaRequiredFlg() { return _columnSchemaRequiredFlg; }
    /**
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnSchemaUpperCaseFlg() { return _columnSchemaUpperCaseFlg; }
    /**
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnUserInputAssistFlg() { return _columnUserInputAssistFlg; }
    /**
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnEmbeddedJarFlg() { return _columnEmbeddedJarFlg; }
    /**
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnDisplayOrder() { return _columnDisplayOrder; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnDatabaseCode());
        ls.add(columnDatabaseName());
        ls.add(columnJdbcDriverFqcn());
        ls.add(columnUrlTemplate());
        ls.add(columnDefaultSchema());
        ls.add(columnSchemaRequiredFlg());
        ls.add(columnSchemaUpperCaseFlg());
        ls.add(columnUserInputAssistFlg());
        ls.add(columnEmbeddedJarFlg());
        ls.add(columnDisplayOrder());
        return ls;
    }

    { initializeInformationResource(); }

    // ===================================================================================
    //                                                                         Unique Info
    //                                                                         ===========
    // -----------------------------------------------------
    //                                       Primary Element
    //                                       ---------------
    protected UniqueInfo cpui() { return hpcpui(columnDatabaseCode()); }
    public boolean hasPrimaryKey() { return true; }
    public boolean hasCompoundPrimaryKey() { return false; }

    // ===================================================================================
    //                                                                       Relation Info
    //                                                                       =============
    // cannot cache because it uses related DB meta instance while booting
    // (instead, cached by super's collection)
    // -----------------------------------------------------
    //                                      Foreign Property
    //                                      ----------------

    // -----------------------------------------------------
    //                                     Referrer Property
    //                                     -----------------

    // ===================================================================================
    //                                                                        Various Info
    //                                                                        ============

    // ===================================================================================
    //                                                                           Type Name
    //                                                                           =========
    public String getEntityTypeName() { return "org.dbflute.intro.dbflute.exentity.ClsTargetDatabase"; }
    public String getConditionBeanTypeName() { return "org.dbflute.intro.dbflute.cbean.ClsTargetDatabaseCB"; }
    public String getBehaviorTypeName() { return "org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<ClsTargetDatabase> getEntityType() { return ClsTargetDatabase.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public ClsTargetDatabase newEntity() { return new ClsTargetDatabase(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptPrimaryKeyMap((ClsTargetDatabase)et, mp); }
    public void acceptAllColumnMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptAllColumnMap((ClsTargetDatabase)et, mp); }
    public Map<String, Object> extractPrimaryKeyMap(Entity et) { return doExtractPrimaryKeyMap(et); }
    public Map<String, Object> extractAllColumnMap(Entity et) { return doExtractAllColumnMap(et); }
}
