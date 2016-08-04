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
 * The base condition-query of CLS_TARGET_CONTAINER.
 * @author DBFlute(AutoGenerator)
 */
public class BsClsTargetContainerCQ extends AbstractBsClsTargetContainerCQ {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected ClsTargetContainerCIQ _inlineQuery;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BsClsTargetContainerCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                 InlineView/OrClause
    //                                                                 ===================
    /**
     * Prepare InlineView query. <br>
     * {select ... from ... left outer join (select * from CLS_TARGET_CONTAINER) where FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">inline()</span>.setFoo...;
     * </pre>
     * @return The condition-query for InlineView query. (NotNull)
     */
    public ClsTargetContainerCIQ inline() {
        if (_inlineQuery == null) { _inlineQuery = xcreateCIQ(); }
        _inlineQuery.xsetOnClause(false); return _inlineQuery;
    }

    protected ClsTargetContainerCIQ xcreateCIQ() {
        ClsTargetContainerCIQ ciq = xnewCIQ();
        ciq.xsetBaseCB(_baseCB);
        return ciq;
    }

    protected ClsTargetContainerCIQ xnewCIQ() {
        return new ClsTargetContainerCIQ(xgetReferrerQuery(), xgetSqlClause(), xgetAliasName(), xgetNestLevel(), this);
    }

    /**
     * Prepare OnClause query. <br>
     * {select ... from ... left outer join CLS_TARGET_CONTAINER on ... and FOO = [value] ...}
     * <pre>
     * cb.query().queryMemberStatus().<span style="color: #CC4747">on()</span>.setFoo...;
     * </pre>
     * @return The condition-query for OnClause query. (NotNull)
     * @throws IllegalConditionBeanOperationException When this condition-query is base query.
     */
    public ClsTargetContainerCIQ on() {
        if (isBaseQuery()) { throw new IllegalConditionBeanOperationException("OnClause for local table is unavailable!"); }
        ClsTargetContainerCIQ inlineQuery = inline(); inlineQuery.xsetOnClause(true); return inlineQuery;
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    protected ConditionValue _containerCode;
    public ConditionValue xdfgetContainerCode()
    { if (_containerCode == null) { _containerCode = nCV(); }
      return _containerCode; }
    protected ConditionValue xgetCValueContainerCode() { return xdfgetContainerCode(); }

    /** 
     * Add order-by as ascend. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @return this. (NotNull)
     */
    public BsClsTargetContainerCQ addOrderBy_ContainerCode_Asc() { regOBA("CONTAINER_CODE"); return this; }

    /**
     * Add order-by as descend. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @return this. (NotNull)
     */
    public BsClsTargetContainerCQ addOrderBy_ContainerCode_Desc() { regOBD("CONTAINER_CODE"); return this; }

    protected ConditionValue _containerName;
    public ConditionValue xdfgetContainerName()
    { if (_containerName == null) { _containerName = nCV(); }
      return _containerName; }
    protected ConditionValue xgetCValueContainerName() { return xdfgetContainerName(); }

    /** 
     * Add order-by as ascend. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetContainerCQ addOrderBy_ContainerName_Asc() { regOBA("CONTAINER_NAME"); return this; }

    /**
     * Add order-by as descend. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @return this. (NotNull)
     */
    public BsClsTargetContainerCQ addOrderBy_ContainerName_Desc() { regOBD("CONTAINER_NAME"); return this; }

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
    public BsClsTargetContainerCQ addOrderBy_DisplayOrder_Asc() { regOBA("DISPLAY_ORDER"); return this; }

    /**
     * Add order-by as descend. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @return this. (NotNull)
     */
    public BsClsTargetContainerCQ addOrderBy_DisplayOrder_Desc() { regOBD("DISPLAY_ORDER"); return this; }

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
    public BsClsTargetContainerCQ addSpecifiedDerivedOrderBy_Asc(String aliasName) { registerSpecifiedDerivedOrderBy_Asc(aliasName); return this; }

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
    public BsClsTargetContainerCQ addSpecifiedDerivedOrderBy_Desc(String aliasName) { registerSpecifiedDerivedOrderBy_Desc(aliasName); return this; }

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
    public Map<String, ClsTargetContainerCQ> xdfgetScalarCondition() { return xgetSQueMap("scalarCondition"); }
    public String keepScalarCondition(ClsTargetContainerCQ sq) { return xkeepSQue("scalarCondition", sq); }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public Map<String, ClsTargetContainerCQ> xdfgetSpecifyMyselfDerived() { return xgetSQueMap("specifyMyselfDerived"); }
    public String keepSpecifyMyselfDerived(ClsTargetContainerCQ sq) { return xkeepSQue("specifyMyselfDerived", sq); }

    public Map<String, ClsTargetContainerCQ> xdfgetQueryMyselfDerived() { return xgetSQueMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerived(ClsTargetContainerCQ sq) { return xkeepSQue("queryMyselfDerived", sq); }
    public Map<String, Object> xdfgetQueryMyselfDerivedParameter() { return xgetSQuePmMap("queryMyselfDerived"); }
    public String keepQueryMyselfDerivedParameter(Object pm) { return xkeepSQuePm("queryMyselfDerived", pm); }

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    protected Map<String, ClsTargetContainerCQ> _myselfExistsMap;
    public Map<String, ClsTargetContainerCQ> xdfgetMyselfExists() { return xgetSQueMap("myselfExists"); }
    public String keepMyselfExists(ClsTargetContainerCQ sq) { return xkeepSQue("myselfExists", sq); }

    // ===================================================================================
    //                                                                       MyselfInScope
    //                                                                       =============
    public Map<String, ClsTargetContainerCQ> xdfgetMyselfInScope() { return xgetSQueMap("myselfInScope"); }
    public String keepMyselfInScope(ClsTargetContainerCQ sq) { return xkeepSQue("myselfInScope", sq); }

    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xCB() { return ClsTargetContainerCB.class.getName(); }
    protected String xCQ() { return ClsTargetContainerCQ.class.getName(); }
    protected String xCHp() { return HpQDRFunction.class.getName(); }
    protected String xCOp() { return ConditionOption.class.getName(); }
    protected String xMap() { return Map.class.getName(); }
}
