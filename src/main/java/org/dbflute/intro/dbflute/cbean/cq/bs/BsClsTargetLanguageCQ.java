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
 * The base condition-query of CLS_TARGET_LANGUAGE.
 * @author DBFlute(AutoGenerator)
 */
public class BsClsTargetLanguageCQ extends AbstractBsClsTargetLanguageCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected ClsTargetLanguageCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsClsTargetLanguageCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                 InlineView/OrClause
    //                                                                 ===================
    /**
     * Prepare InlineView query. <br>
     * {select ... from ... left outer join (select * from CLS_TARGET_LANGUAGE) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public ClsTargetLanguageCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = xcreateCIQ(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected ClsTargetLanguageCIQ xcreateCIQ() {
        ClsTargetLanguageCIQ ciq = xnewCIQ();
        ciq.xsetBaseCB(_baseCB);
        return ciq;
    }

    protected ClsTargetLanguageCIQ xnewCIQ() {
        return new ClsTargetLanguageCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this);
    }

    /**
     * Prepare OnClause query. <br>
     * {select ... from ... left outer join CLS_TARGET_LANGUAGE on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public ClsTargetLanguageCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        ClsTargetLanguageCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    protected ConditionValue _languageCode;
    public ConditionValue xdfgetLanguageCode()
    { if (_languageCode == null) { _languageCode = nCV(); }
      return _languageCode; }
    protected ConditionValue xgetCValueLanguageCode() { return xdfgetLanguageCode(); }

    /** 
     * Add order-by as ascend. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @return this. (NotNull)
     */
    public BsClsTargetLanguageCQ addOrderBy_LanguageCode_Asc() { regOBA("LANGUAGE_CODE"); return this; }

    /**
     * Add order-by as descend. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @return this. (NotNull)
     */
    public BsClsTargetLanguageCQ addOrderBy_LanguageCode_Desc() { regOBD("LANGUAGE_CODE"); return this; }

    protected ConditionValue _languageName;
    public ConditionValue xdfgetLanguageName()
    { if (_languageName == null) { _languageName = nCV(); }
      return _languageName; }
    protected ConditionValue xgetCValueLanguageName() { return xdfgetLanguageName(); }

    /** 
     * Add order-by as ascend. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetLanguageCQ addOrderBy_LanguageName_Asc() { regOBA("LANGUAGE_NAME"); return this; }

    /**
     * Add order-by as descend. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetLanguageCQ addOrderBy_LanguageName_Desc() { regOBD("LANGUAGE_NAME"); return this; }

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
    public BsClsTargetLanguageCQ addOrderBy_DisplayOrder_Asc() { regOBA("DISPLAY_ORDER"); return this; }

    /**
     * Add order-by as descend. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetLanguageCQ addOrderBy_DisplayOrder_Desc() { regOBD("DISPLAY_ORDER"); return this; }

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
    public BsClsTargetLanguageCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }

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
    public BsClsTargetLanguageCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

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
    public Map<String, ClsTargetLanguageCQ> xdfgetScalarCondition() { return xgetSQueMap("scalarCondition"); }
    public String keepScalarCondition(ClsTargetLanguageCQ sq) { return xkeepSQue("scalarCondition", sq); }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public Map<String, ClsTargetLanguageCQ> xdfgetSpecifyMyselfDerived() { return xgetSQueMap("specifyMyselfDerived"); }
    public String keepSpecifyMyselfDerived(ClsTargetLanguageCQ sq) { return xkeepSQue("specifyMyselfDerived", sq); }

    public Map<String, ClsTargetLanguageCQ> xdfgetQueryMyselfDerived() { return xgetSQueMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerived(ClsTargetLanguageCQ sq) { return xkeepSQue("queryMyselfDerived", sq); }
    public Map<String, Object> xdfgetQueryMyselfDerivedParameter() { return xgetSQuePmMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerivedParameter(Object pm) { return xkeepSQuePm("queryMyselfDerived", pm); }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    protected Map<String, ClsTargetLanguageCQ> _myselfExistsMap;
    public Map<String, ClsTargetLanguageCQ> xdfgetMyselfExists() { return xgetSQueMap("myselfExists"); }
    public String keepMyselfExists(ClsTargetLanguageCQ sq) { return xkeepSQue("myselfExists", sq); }

    // ===================================================================================
    //                                                                       MyselfInScope
    //                                                                       =============
    public Map<String, ClsTargetLanguageCQ> xdfgetMyselfInScope() { return xgetSQueMap("myselfInScope"); }
    public String keepMyselfInScope(ClsTargetLanguageCQ sq) { return xkeepSQue("myselfInScope", sq); }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return ClsTargetLanguageCB.class.getName(); }
    protected String xCQ() { return ClsTargetLanguageCQ.class.getName(); }
    protected String xCHp() { return HpQDRFunction.class.getName(); }
    protected String xCOp() { return ConditionOption.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
