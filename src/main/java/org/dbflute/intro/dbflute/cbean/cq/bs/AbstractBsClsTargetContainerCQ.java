/*
 * Copyright 2014-2018 the original author or authors.
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

import java.util.*;

import org.dbflute.cbean.*;
import org.dbflute.cbean.chelper.*;
import org.dbflute.cbean.ckey.*;
import org.dbflute.cbean.coption.*;
import org.dbflute.cbean.cvalue.ConditionValue;
import org.dbflute.cbean.ordering.*;
import org.dbflute.cbean.scoping.*;
import org.dbflute.cbean.sqlclause.SqlClause;
import org.dbflute.dbmeta.DBMetaProvider;
import org.dbflute.intro.dbflute.allcommon.*;
import org.dbflute.intro.dbflute.cbean.*;
import org.dbflute.intro.dbflute.cbean.cq.*;

/**
 * The abstract condition-query of CLS_TARGET_CONTAINER.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsClsTargetContainerCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsClsTargetContainerCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
        super(referrerQuery, sqlClause, aliasName, nestLevel);
    }

    // ===================================================================================
    //                                                                             DB Meta
    //                                                                             =======
    @Override
    protected DBMetaProvider xgetDBMetaProvider() {
        return DBMetaInstanceHandler.getProvider();
    }

    public String asTableDbName() {
        return "CLS_TARGET_CONTAINER";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @param containerCode The value of containerCode as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setContainerCode_Equal(String containerCode) {
        doSetContainerCode_Equal(fRES(containerCode));
    }

    /**
     * Equal(=). As TargetContainer. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer cdef) {
        doSetContainerCode_Equal(cdef != null ? cdef.code() : null);
    }

    /**
     * Equal(=). As LastaDi (lasta_di). And OnlyOnceRegistered. <br>
     * Lasta Di
     */
    public void setContainerCode_Equal_LastaDi() {
        setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer.LastaDi);
    }

    /**
     * Equal(=). As SpringFramework (spring). And OnlyOnceRegistered. <br>
     * Spring Framework
     */
    public void setContainerCode_Equal_SpringFramework() {
        setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer.SpringFramework);
    }

    /**
     * Equal(=). As GoogleGuice (guice). And OnlyOnceRegistered. <br>
     * Google Guice
     */
    public void setContainerCode_Equal_GoogleGuice() {
        setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer.GoogleGuice);
    }

    /**
     * Equal(=). As SeasarS2Container (seasar). And OnlyOnceRegistered. <br>
     * Seasar (S2Container)
     */
    public void setContainerCode_Equal_SeasarS2Container() {
        setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer.SeasarS2Container);
    }

    /**
     * Equal(=). As Cdi (cdi). And OnlyOnceRegistered. <br>
     * CDI
     */
    public void setContainerCode_Equal_Cdi() {
        setContainerCode_Equal_AsTargetContainer(CDef.TargetContainer.Cdi);
    }

    protected void doSetContainerCode_Equal(String containerCode) {
        regContainerCode(CK_EQ, containerCode);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @param containerCode The value of containerCode as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setContainerCode_NotEqual(String containerCode) {
        doSetContainerCode_NotEqual(fRES(containerCode));
    }

    /**
     * NotEqual(&lt;&gt;). As TargetContainer. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer cdef) {
        doSetContainerCode_NotEqual(cdef != null ? cdef.code() : null);
    }

    /**
     * NotEqual(&lt;&gt;). As LastaDi (lasta_di). And OnlyOnceRegistered. <br>
     * Lasta Di
     */
    public void setContainerCode_NotEqual_LastaDi() {
        setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer.LastaDi);
    }

    /**
     * NotEqual(&lt;&gt;). As SpringFramework (spring). And OnlyOnceRegistered. <br>
     * Spring Framework
     */
    public void setContainerCode_NotEqual_SpringFramework() {
        setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer.SpringFramework);
    }

    /**
     * NotEqual(&lt;&gt;). As GoogleGuice (guice). And OnlyOnceRegistered. <br>
     * Google Guice
     */
    public void setContainerCode_NotEqual_GoogleGuice() {
        setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer.GoogleGuice);
    }

    /**
     * NotEqual(&lt;&gt;). As SeasarS2Container (seasar). And OnlyOnceRegistered. <br>
     * Seasar (S2Container)
     */
    public void setContainerCode_NotEqual_SeasarS2Container() {
        setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer.SeasarS2Container);
    }

    /**
     * NotEqual(&lt;&gt;). As Cdi (cdi). And OnlyOnceRegistered. <br>
     * CDI
     */
    public void setContainerCode_NotEqual_Cdi() {
        setContainerCode_NotEqual_AsTargetContainer(CDef.TargetContainer.Cdi);
    }

    protected void doSetContainerCode_NotEqual(String containerCode) {
        regContainerCode(CK_NES, containerCode);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @param containerCodeList The collection of containerCode as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setContainerCode_InScope(Collection<String> containerCodeList) {
        doSetContainerCode_InScope(containerCodeList);
    }

    /**
     * InScope {in ('a', 'b')}. As TargetContainer. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setContainerCode_InScope_AsTargetContainer(Collection<CDef.TargetContainer> cdefList) {
        doSetContainerCode_InScope(cTStrL(cdefList));
    }

    protected void doSetContainerCode_InScope(Collection<String> containerCodeList) {
        regINS(CK_INS, cTL(containerCodeList), xgetCValueContainerCode(), "CONTAINER_CODE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     * @param containerCodeList The collection of containerCode as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setContainerCode_NotInScope(Collection<String> containerCodeList) {
        doSetContainerCode_NotInScope(containerCodeList);
    }

    /**
     * NotInScope {not in ('a', 'b')}. As TargetContainer. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setContainerCode_NotInScope_AsTargetContainer(Collection<CDef.TargetContainer> cdefList) {
        doSetContainerCode_NotInScope(cTStrL(cdefList));
    }

    protected void doSetContainerCode_NotInScope(Collection<String> containerCodeList) {
        regINS(CK_NINS, cTL(containerCodeList), xgetCValueContainerCode(), "CONTAINER_CODE");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     */
    public void setContainerCode_IsNull() { regContainerCode(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer}
     */
    public void setContainerCode_IsNotNull() { regContainerCode(CK_ISNN, DOBJ); }

    protected void regContainerCode(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueContainerCode(), "CONTAINER_CODE"); }
    protected abstract ConditionValue xgetCValueContainerCode();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @param containerName The value of containerName as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setContainerName_Equal(String containerName) {
        doSetContainerName_Equal(fRES(containerName));
    }

    protected void doSetContainerName_Equal(String containerName) {
        regContainerName(CK_EQ, containerName);
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setContainerName_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param containerName The value of containerName as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setContainerName_LikeSearch(String containerName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setContainerName_LikeSearch(containerName, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setContainerName_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param containerName The value of containerName as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setContainerName_LikeSearch(String containerName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(containerName), xgetCValueContainerName(), "CONTAINER_NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @param containerName The value of containerName as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setContainerName_NotLikeSearch(String containerName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setContainerName_NotLikeSearch(containerName, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * CONTAINER_NAME: {NotNull, VARCHAR(100)}
     * @param containerName The value of containerName as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setContainerName_NotLikeSearch(String containerName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(containerName), xgetCValueContainerName(), "CONTAINER_NAME", likeSearchOption);
    }

    protected void regContainerName(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueContainerName(), "CONTAINER_NAME"); }
    protected abstract ConditionValue xgetCValueContainerName();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @param displayOrder The value of displayOrder as equal. (basically NotNull: error as default, or no condition as option)
     */
    public void setDisplayOrder_Equal(Integer displayOrder) {
        doSetDisplayOrder_Equal(displayOrder);
    }

    protected void doSetDisplayOrder_Equal(Integer displayOrder) {
        regDisplayOrder(CK_EQ, displayOrder);
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @param minNumber The min number of displayOrder. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of displayOrder. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param opLambda The callback for option of range-of. (NotNull)
     */
    public void setDisplayOrder_RangeOf(Integer minNumber, Integer maxNumber, ConditionOptionCall<RangeOfOption> opLambda) {
        setDisplayOrder_RangeOf(minNumber, maxNumber, xcROOP(opLambda));
    }

    /**
     * RangeOf with various options. (versatile) <br>
     * {(default) minNumber &lt;= column &lt;= maxNumber} <br>
     * And NullIgnored, OnlyOnceRegistered. <br>
     * DISPLAY_ORDER: {NotNull, INTEGER(10)}
     * @param minNumber The min number of displayOrder. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param maxNumber The max number of displayOrder. (basically NotNull: if op.allowOneSide(), null allowed)
     * @param rangeOfOption The option of range-of. (NotNull)
     */
    protected void setDisplayOrder_RangeOf(Integer minNumber, Integer maxNumber, RangeOfOption rangeOfOption) {
        regROO(minNumber, maxNumber, xgetCValueDisplayOrder(), "DISPLAY_ORDER", rangeOfOption);
    }

    protected void regDisplayOrder(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueDisplayOrder(), "DISPLAY_ORDER"); }
    protected abstract ConditionValue xgetCValueDisplayOrder();

    // ===================================================================================
    //                                                                     ScalarCondition
    //                                                                     ===============
    /**
     * Prepare ScalarCondition as equal. <br>
     * {where FOO = (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_Equal() {
        return xcreateSLCFunction(CK_EQ, ClsTargetContainerCB.class);
    }

    /**
     * Prepare ScalarCondition as equal. <br>
     * {where FOO &lt;&gt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_NotEqual() {
        return xcreateSLCFunction(CK_NES, ClsTargetContainerCB.class);
    }

    /**
     * Prepare ScalarCondition as greaterThan. <br>
     * {where FOO &gt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_GreaterThan() {
        return xcreateSLCFunction(CK_GT, ClsTargetContainerCB.class);
    }

    /**
     * Prepare ScalarCondition as lessThan. <br>
     * {where FOO &lt; (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_LessThan() {
        return xcreateSLCFunction(CK_LT, ClsTargetContainerCB.class);
    }

    /**
     * Prepare ScalarCondition as greaterEqual. <br>
     * {where FOO &gt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().scalar_Equal().<span style="color: #CC4747">avg</span>(<span style="color: #553000">purchaseCB</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">purchaseCB</span>.specify().<span style="color: #CC4747">columnPurchasePrice</span>(); <span style="color: #3F7E5E">// *Point!</span>
     *     <span style="color: #553000">purchaseCB</span>.query().setPaymentCompleteFlg_Equal_True();
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_GreaterEqual() {
        return xcreateSLCFunction(CK_GE, ClsTargetContainerCB.class);
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br>
     * {where FOO &lt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().<span style="color: #CC4747">scalar_LessEqual()</span>.max(new SubQuery&lt;ClsTargetContainerCB&gt;() {
     *     public void query(ClsTargetContainerCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetContainerCB> scalar_LessEqual() {
        return xcreateSLCFunction(CK_LE, ClsTargetContainerCB.class);
    }

    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xscalarCondition(String fn, SubQuery<CB> sq, String rd, HpSLCCustomized<CB> cs, ScalarConditionOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetContainerCB cb = xcreateScalarConditionCB(); sq.query((CB)cb);
        String pp = keepScalarCondition(cb.query()); // for saving query-value
        cs.setPartitionByCBean((CB)xcreateScalarConditionPartitionByCB()); // for using partition-by
        registerScalarCondition(fn, cb.query(), pp, rd, cs, op);
    }
    public abstract String keepScalarCondition(ClsTargetContainerCQ sq);

    protected ClsTargetContainerCB xcreateScalarConditionCB() {
        ClsTargetContainerCB cb = newMyCB(); cb.xsetupForScalarCondition(this); return cb;
    }

    protected ClsTargetContainerCB xcreateScalarConditionPartitionByCB() {
        ClsTargetContainerCB cb = newMyCB(); cb.xsetupForScalarConditionPartitionBy(this); return cb;
    }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public void xsmyselfDerive(String fn, SubQuery<ClsTargetContainerCB> sq, String al, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetContainerCB cb = new ClsTargetContainerCB(); cb.xsetupForDerivedReferrer(this);
        lockCall(() -> sq.query(cb)); String pp = keepSpecifyMyselfDerived(cb.query()); String pk = "CONTAINER_CODE";
        registerSpecifyMyselfDerived(fn, cb.query(), pk, pk, pp, "myselfDerived", al, op);
    }
    public abstract String keepSpecifyMyselfDerived(ClsTargetContainerCQ sq);

    /**
     * Prepare for (Query)MyselfDerived (correlated sub-query).
     * @return The object to set up a function for myself table. (NotNull)
     */
    public HpQDRFunction<ClsTargetContainerCB> myselfDerived() {
        return xcreateQDRFunctionMyselfDerived(ClsTargetContainerCB.class);
    }
    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xqderiveMyselfDerived(String fn, SubQuery<CB> sq, String rd, Object vl, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetContainerCB cb = new ClsTargetContainerCB(); cb.xsetupForDerivedReferrer(this); sq.query((CB)cb);
        String pk = "CONTAINER_CODE";
        String sqpp = keepQueryMyselfDerived(cb.query()); // for saving query-value.
        String prpp = keepQueryMyselfDerivedParameter(vl);
        registerQueryMyselfDerived(fn, cb.query(), pk, pk, sqpp, "myselfDerived", rd, vl, prpp, op);
    }
    public abstract String keepQueryMyselfDerived(ClsTargetContainerCQ sq);
    public abstract String keepQueryMyselfDerivedParameter(Object vl);

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    /**
     * Prepare for MyselfExists (correlated sub-query).
     * @param subCBLambda The implementation of sub-query. (NotNull)
     */
    public void myselfExists(SubQuery<ClsTargetContainerCB> subCBLambda) {
        assertObjectNotNull("subCBLambda", subCBLambda);
        ClsTargetContainerCB cb = new ClsTargetContainerCB(); cb.xsetupForMyselfExists(this);
        lockCall(() -> subCBLambda.query(cb)); String pp = keepMyselfExists(cb.query());
        registerMyselfExists(cb.query(), pp);
    }
    public abstract String keepMyselfExists(ClsTargetContainerCQ sq);

    // ===================================================================================
    //                                                                        Manual Order
    //                                                                        ============
    /**
     * Order along manual ordering information.
     * <pre>
     * cb.query().addOrderBy_Birthdate_Asc().<span style="color: #CC4747">withManualOrder</span>(<span style="color: #553000">op</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_GreaterEqual</span>(priorityDate); <span style="color: #3F7E5E">// e.g. 2000/01/01</span>
     * });
     * <span style="color: #3F7E5E">// order by </span>
     * <span style="color: #3F7E5E">//   case</span>
     * <span style="color: #3F7E5E">//     when BIRTHDATE &gt;= '2000/01/01' then 0</span>
     * <span style="color: #3F7E5E">//     else 1</span>
     * <span style="color: #3F7E5E">//   end asc, ...</span>
     *
     * cb.query().addOrderBy_MemberStatusCode_Asc().<span style="color: #CC4747">withManualOrder</span>(<span style="color: #553000">op</span> <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> {
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Withdrawal);
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Formalized);
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_Equal</span>(CDef.MemberStatus.Provisional);
     * });
     * <span style="color: #3F7E5E">// order by </span>
     * <span style="color: #3F7E5E">//   case</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'WDL' then 0</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'FML' then 1</span>
     * <span style="color: #3F7E5E">//     when MEMBER_STATUS_CODE = 'PRV' then 2</span>
     * <span style="color: #3F7E5E">//     else 3</span>
     * <span style="color: #3F7E5E">//   end asc, ...</span>
     * </pre>
     * <p>This function with Union is unsupported!</p>
     * <p>The order values are bound (treated as bind parameter).</p>
     * @param opLambda The callback for option of manual-order containing order values. (NotNull)
     */
    public void withManualOrder(ManualOrderOptionCall opLambda) { // is user public!
        xdoWithManualOrder(cMOO(opLambda));
    }

    // ===================================================================================
    //                                                                    Small Adjustment
    //                                                                    ================
    // ===================================================================================
    //                                                                       Very Internal
    //                                                                       =============
    protected ClsTargetContainerCB newMyCB() {
        return new ClsTargetContainerCB();
    }
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabUDT() { return Date.class.getName(); }
    protected String xabCQ() { return ClsTargetContainerCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSLCS() { return HpSLCSetupper.class.getName(); }
    protected String xabSCP() { return SubQuery.class.getName(); }
}
