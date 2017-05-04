/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.dbflute.cbean.cq.bs;

import java.util.Map;

import org.dbflute.cbean.*;
import org.dbflute.cbean.chelper.*;
import org.dbflute.cbean.coption.*;
import org.dbflute.cbean.cvalue.ConditionValue;
import org.dbflute.cbean.sqlclause.SqlClause;
import org.dbflute.exception.IllegalConditionBeanOperationException;
import org.dbflute.intro.dbflute.cbean.cq.ciq.*;
import org.dbflute.intro.dbflute.cbean.*;
import org.dbflute.intro.dbflute.cbean.cq.*;

/**
 * The base condition-query of CLS_TARGET_DATABASE.
 * @author DBFlute(AutoGenerator)
 */
public class BsClsTargetDatabaseCQ extends AbstractBsClsTargetDatabaseCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected ClsTargetDatabaseCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsClsTargetDatabaseCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                 InlineView/OrClause
    //                                                                 ===================
    /**
     * Prepare InlineView query. <br>
     * {select ... from ... left outer join (select * from CLS_TARGET_DATABASE) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public ClsTargetDatabaseCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = xcreateCIQ(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected ClsTargetDatabaseCIQ xcreateCIQ() {
        ClsTargetDatabaseCIQ ciq = xnewCIQ();
        ciq.xsetBaseCB(_baseCB);
        return ciq;
    }

    protected ClsTargetDatabaseCIQ xnewCIQ() {
        return new ClsTargetDatabaseCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this);
    }

    /**
     * Prepare OnClause query. <br>
     * {select ... from ... left outer join CLS_TARGET_DATABASE on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public ClsTargetDatabaseCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        ClsTargetDatabaseCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    protected ConditionValue _databaseCode;
    public ConditionValue xdfgetDatabaseCode()
    { if (_databaseCode == null) { _databaseCode = nCV(); }
      return _databaseCode; }
    protected ConditionValue xgetCValueDatabaseCode() { return xdfgetDatabaseCode(); }

    /** 
     * Add order-by as ascend. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DatabaseCode_Asc() { regOBA("DATABASE_CODE"); return this; }

    /**
     * Add order-by as descend. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DatabaseCode_Desc() { regOBD("DATABASE_CODE"); return this; }

    protected ConditionValue _databaseName;
    public ConditionValue xdfgetDatabaseName()
    { if (_databaseName == null) { _databaseName = nCV(); }
      return _databaseName; }
    protected ConditionValue xgetCValueDatabaseName() { return xdfgetDatabaseName(); }

    /** 
     * Add order-by as ascend. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DatabaseName_Asc() { regOBA("DATABASE_NAME"); return this; }

    /**
     * Add order-by as descend. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DatabaseName_Desc() { regOBD("DATABASE_NAME"); return this; }

    protected ConditionValue _jdbcDriverFqcn;
    public ConditionValue xdfgetJdbcDriverFqcn()
    { if (_jdbcDriverFqcn == null) { _jdbcDriverFqcn = nCV(); }
      return _jdbcDriverFqcn; }
    protected ConditionValue xgetCValueJdbcDriverFqcn() { return xdfgetJdbcDriverFqcn(); }

    /** 
     * Add order-by as ascend. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_JdbcDriverFqcn_Asc() { regOBA("JDBC_DRIVER_FQCN"); return this; }

    /**
     * Add order-by as descend. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_JdbcDriverFqcn_Desc() { regOBD("JDBC_DRIVER_FQCN"); return this; }

    protected ConditionValue _urlTemplate;
    public ConditionValue xdfgetUrlTemplate()
    { if (_urlTemplate == null) { _urlTemplate = nCV(); }
      return _urlTemplate; }
    protected ConditionValue xgetCValueUrlTemplate() { return xdfgetUrlTemplate(); }

    /** 
     * Add order-by as ascend. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_UrlTemplate_Asc() { regOBA("URL_TEMPLATE"); return this; }

    /**
     * Add order-by as descend. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_UrlTemplate_Desc() { regOBD("URL_TEMPLATE"); return this; }

    protected ConditionValue _defaultSchema;
    public ConditionValue xdfgetDefaultSchema()
    { if (_defaultSchema == null) { _defaultSchema = nCV(); }
      return _defaultSchema; }
    protected ConditionValue xgetCValueDefaultSchema() { return xdfgetDefaultSchema(); }

    /** 
     * Add order-by as ascend. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DefaultSchema_Asc() { regOBA("DEFAULT_SCHEMA"); return this; }

    /**
     * Add order-by as descend. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DefaultSchema_Desc() { regOBD("DEFAULT_SCHEMA"); return this; }

    protected ConditionValue _schemaRequiredFlg;
    public ConditionValue xdfgetSchemaRequiredFlg()
    { if (_schemaRequiredFlg == null) { _schemaRequiredFlg = nCV(); }
      return _schemaRequiredFlg; }
    protected ConditionValue xgetCValueSchemaRequiredFlg() { return xdfgetSchemaRequiredFlg(); }

    /** 
     * Add order-by as ascend. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_SchemaRequiredFlg_Asc() { regOBA("SCHEMA_REQUIRED_FLG"); return this; }

    /**
     * Add order-by as descend. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_SchemaRequiredFlg_Desc() { regOBD("SCHEMA_REQUIRED_FLG"); return this; }

    protected ConditionValue _schemaUpperCaseFlg;
    public ConditionValue xdfgetSchemaUpperCaseFlg()
    { if (_schemaUpperCaseFlg == null) { _schemaUpperCaseFlg = nCV(); }
      return _schemaUpperCaseFlg; }
    protected ConditionValue xgetCValueSchemaUpperCaseFlg() { return xdfgetSchemaUpperCaseFlg(); }

    /** 
     * Add order-by as ascend. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_SchemaUpperCaseFlg_Asc() { regOBA("SCHEMA_UPPER_CASE_FLG"); return this; }

    /**
     * Add order-by as descend. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_SchemaUpperCaseFlg_Desc() { regOBD("SCHEMA_UPPER_CASE_FLG"); return this; }

    protected ConditionValue _userInputAssistFlg;
    public ConditionValue xdfgetUserInputAssistFlg()
    { if (_userInputAssistFlg == null) { _userInputAssistFlg = nCV(); }
      return _userInputAssistFlg; }
    protected ConditionValue xgetCValueUserInputAssistFlg() { return xdfgetUserInputAssistFlg(); }

    /** 
     * Add order-by as ascend. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_UserInputAssistFlg_Asc() { regOBA("USER_INPUT_ASSIST_FLG"); return this; }

    /**
     * Add order-by as descend. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_UserInputAssistFlg_Desc() { regOBD("USER_INPUT_ASSIST_FLG"); return this; }

    protected ConditionValue _embeddedJarFlg;
    public ConditionValue xdfgetEmbeddedJarFlg()
    { if (_embeddedJarFlg == null) { _embeddedJarFlg = nCV(); }
      return _embeddedJarFlg; }
    protected ConditionValue xgetCValueEmbeddedJarFlg() { return xdfgetEmbeddedJarFlg(); }

    /** 
     * Add order-by as ascend. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_EmbeddedJarFlg_Asc() { regOBA("EMBEDDED_JAR_FLG"); return this; }

    /**
     * Add order-by as descend. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_EmbeddedJarFlg_Desc() { regOBD("EMBEDDED_JAR_FLG"); return this; }

    protected ConditionValue _displayOrder;
    public ConditionValue xdfgetDisplayOrder()
    { if (_displayOrder == null) { _displayOrder = nCV(); }
      return _displayOrder; }
    protected ConditionValue xgetCValueDisplayOrder() { return xdfgetDisplayOrder(); }

    /** 
     * Add order-by as ascend. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DisplayOrder_Asc() { regOBA("DISPLAY_ORDER"); return this; }

    /**
     * Add order-by as descend. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addOrderBy_DisplayOrder_Desc() { regOBD("DISPLAY_ORDER"); return this; }

    // ===================================================================================
    //                                                             SpecifiedDerivedOrderBy
    //                                                             =======================
    /**
     * Add order-by for specified derived column as ascend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #CC4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] asc</span>
     * cb.<span style="color: #CC4747">addSpecifiedDerivedOrderBy_Asc</span>(<span style="color: #CC4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }

    /**
     * Add order-by for specified derived column as descend.
     * <pre>
     * cb.specify().derivedPurchaseList().max(new SubQuery&lt;PurchaseCB&gt;() {
     *     public void query(PurchaseCB subCB) {
     *         subCB.specify().columnPurchaseDatetime();
     *     }
     * }, <span style="color: #CC4747">aliasName</span>);
     * <span style="color: #3F7E5E">// order by [alias-name] desc</span>
     * cb.<span style="color: #CC4747">addSpecifiedDerivedOrderBy_Desc</span>(<span style="color: #CC4747">aliasName</span>);
     * </pre>
     * @param aliasName The alias name specified at (Specify)DerivedReferrer. (NotNull)
     * @return this. (NotNull)
     */
    public BsClsTargetDatabaseCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

    // ===================================================================================
    //                                                                         Union Query
    //                                                                         ===========
    public void reflectRelationOnUnionQuery(ConditionQuery bqs, ConditionQuery uqs) {
    }

    // ===================================================================================
    //                                                                       Foreign Query
    //                                                                       =============
    protected Map<String, Object> xfindFixedConditionDynamicParameterMap(String property) {
        return null;
    }

    // ===================================================================================
    //                                                                     ScalarCondition
    //                                                                     ===============
    public Map<String, ClsTargetDatabaseCQ> xdfgetScalarCondition() { return xgetSQueMap("scalarCondition"); }
    public String keepScalarCondition(ClsTargetDatabaseCQ sq) { return xkeepSQue("scalarCondition", sq); }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public Map<String, ClsTargetDatabaseCQ> xdfgetSpecifyMyselfDerived() { return xgetSQueMap("specifyMyselfDerived"); }
    public String keepSpecifyMyselfDerived(ClsTargetDatabaseCQ sq) { return xkeepSQue("specifyMyselfDerived", sq); }

    public Map<String, ClsTargetDatabaseCQ> xdfgetQueryMyselfDerived() { return xgetSQueMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerived(ClsTargetDatabaseCQ sq) { return xkeepSQue("queryMyselfDerived", sq); }
    public Map<String, Object> xdfgetQueryMyselfDerivedParameter() { return xgetSQuePmMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerivedParameter(Object pm) { return xkeepSQuePm("queryMyselfDerived", pm); }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    protected Map<String, ClsTargetDatabaseCQ> _myselfExistsMap;
    public Map<String, ClsTargetDatabaseCQ> xdfgetMyselfExists() { return xgetSQueMap("myselfExists"); }
    public String keepMyselfExists(ClsTargetDatabaseCQ sq) { return xkeepSQue("myselfExists", sq); }

    // ===================================================================================
    //                                                                       MyselfInScope
    //                                                                       =============
    public Map<String, ClsTargetDatabaseCQ> xdfgetMyselfInScope() { return xgetSQueMap("myselfInScope"); }
    public String keepMyselfInScope(ClsTargetDatabaseCQ sq) { return xkeepSQue("myselfInScope", sq); }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return ClsTargetDatabaseCB.class.getName(); }
    protected String xCQ() { return ClsTargetDatabaseCQ.class.getName(); }
    protected String xCHp() { return HpQDRFunction.class.getName(); }
    protected String xCOp() { return ConditionOption.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
