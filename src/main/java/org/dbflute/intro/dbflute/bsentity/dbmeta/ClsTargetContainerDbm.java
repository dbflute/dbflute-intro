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
 * The DB meta of CLS_TARGET_CONTAINER. (Singleton)
 * @author DBFlute(AutoGenerator)
 */
public class ClsTargetContainerDbm extends AbstractDBMeta {

    // ===================================================================================
    //                                                                           Singleton
    //                                                                           =========
    private static final ClsTargetContainerDbm _instance = new ClsTargetContainerDbm();
    private ClsTargetContainerDbm() {}
    public static ClsTargetContainerDbm getInstance() { return _instance; }

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
        setupEpg(_epgMap, et -> ((ClsTargetContainer)et).getContainerCode(), (et, vl) -> {
            CDef.TargetContainer cls = (CDef.TargetContainer)gcls(et, columnContainerCode(), vl);
            if (cls != null) {
                ((ClsTargetContainer)et).setContainerCodeAsTargetContainer(cls);
            } else {
                ((ClsTargetContainer)et).mynativeMappingContainerCode((String)vl);
            }
        }, "containerCode");
        setupEpg(_epgMap, et -> ((ClsTargetContainer)et).getContainerName(), (et, vl) -> ((ClsTargetContainer)et).setContainerName((String)vl), "containerName");
        setupEpg(_epgMap, et -> ((ClsTargetContainer)et).getDisplayOrder(), (et, vl) -> ((ClsTargetContainer)et).setDisplayOrder(cti(vl)), "displayOrder");
    }
    public PropertyGateway findPropertyGateway(String prop)
    { return doFindEpg(_epgMap, prop); }

    // ===================================================================================
    //                                                                          Table Info
    //                                                                          ==========
    protected final String _tableDbName = "CLS_TARGET_CONTAINER";
    protected final String _tableDispName = "CLS_TARGET_CONTAINER";
    protected final String _tablePropertyName = "clsTargetContainer";
    protected final TableSqlName _tableSqlName = new TableSqlName("CLS_TARGET_CONTAINER", _tableDbName);
    { _tableSqlName.xacceptFilter(DBFluteConfig.getInstance().getTableSqlNameFilter()); }
    public String getTableDbName() { return _tableDbName; }
    public String getTableDispName() { return _tableDispName; }
    public String getTablePropertyName() { return _tablePropertyName; }
    public TableSqlName getTableSqlName() { return _tableSqlName; }

    // ===================================================================================
    //                                                                         Column Info
    //                                                                         ===========
    protected final ColumnInfo _columnContainerCode = cci("CONTAINER_CODE", "CONTAINER_CODE", null, null, String.class, "containerCode", null, true, false, true, "VARCHAR", 10, 0, null, null, false, null, null, null, null, CDef.DefMeta.TargetContainer, false);
    protected final ColumnInfo _columnContainerName = cci("CONTAINER_NAME", "CONTAINER_NAME", null, null, String.class, "containerName", null, false, false, true, "VARCHAR", 100, 0, null, null, false, null, null, null, null, null, false);
    protected final ColumnInfo _columnDisplayOrder = cci("DISPLAY_ORDER", "DISPLAY_ORDER", null, null, Integer.class, "displayOrder", null, false, false, true, "INTEGER", 10, 0, null, null, false, null, null, null, null, null, false);

    /**
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnContainerCode() { return _columnContainerCode; }
    /**
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnContainerName() { return _columnContainerName; }
    /**
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return The information object of specified column. (NotNull)
     */
    public ColumnInfo columnDisplayOrder() { return _columnDisplayOrder; }

    protected List<ColumnInfo> ccil() {
        List<ColumnInfo> ls = newArrayList();
        ls.add(columnContainerCode());
        ls.add(columnContainerName());
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
    protected UniqueInfo cpui() { return hpcpui(columnContainerCode()); }
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
    public String getEntityTypeName() { return "org.dbflute.intro.dbflute.exentity.ClsTargetContainer"; }
    public String getConditionBeanTypeName() { return "org.dbflute.intro.dbflute.cbean.ClsTargetContainerCB"; }
    public String getBehaviorTypeName() { return "org.dbflute.intro.dbflute.exbhv.ClsTargetContainerBhv"; }

    // ===================================================================================
    //                                                                         Object Type
    //                                                                         ===========
    public Class<ClsTargetContainer> getEntityType() { return ClsTargetContainer.class; }

    // ===================================================================================
    //                                                                     Object Instance
    //                                                                     ===============
    public ClsTargetContainer newEntity() { return new ClsTargetContainer(); }

    // ===================================================================================
    //                                                                   Map Communication
    //                                                                   =================
    public void acceptPrimaryKeyMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptPrimaryKeyMap((ClsTargetContainer)et, mp); }
    public void acceptAllColumnMap(Entity et, Map<String, ? extends Object> mp)
    { doAcceptAllColumnMap((ClsTargetContainer)et, mp); }
    public Map<String, Object> extractPrimaryKeyMap(Entity et) { return doExtractPrimaryKeyMap(et); }
    public Map<String, Object> extractAllColumnMap(Entity et) { return doExtractAllColumnMap(et); }
}
