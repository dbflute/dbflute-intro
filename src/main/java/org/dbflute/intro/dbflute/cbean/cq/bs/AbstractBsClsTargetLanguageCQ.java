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
 * The abstract condition-query of CLS_TARGET_LANGUAGE.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsClsTargetLanguageCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsClsTargetLanguageCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
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
        return "CLS_TARGET_LANGUAGE";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @param languageCode The value of languageCode as equal. (NullAllowed: if null (or empty), no condition)
     */
    protected void setLanguageCode_Equal(String languageCode) {
        doSetLanguageCode_Equal(fRES(languageCode));
    }

    /**
     * Equal(=). As TargetLanguage. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setLanguageCode_Equal_AsTargetLanguage(CDef.TargetLanguage cdef) {
        doSetLanguageCode_Equal(cdef != null ? cdef.code() : null);
    }

    /**
     * Equal(=). As Java (java). And OnlyOnceRegistered. <br>
     * Java
     */
    public void setLanguageCode_Equal_Java() {
        setLanguageCode_Equal_AsTargetLanguage(CDef.TargetLanguage.Java);
    }

    /**
     * Equal(=). As C (csharp). And OnlyOnceRegistered. <br>
     * C#
     */
    public void setLanguageCode_Equal_C() {
        setLanguageCode_Equal_AsTargetLanguage(CDef.TargetLanguage.C);
    }

    /**
     * Equal(=). As Scala (scala). And OnlyOnceRegistered. <br>
     * Scala
     */
    public void setLanguageCode_Equal_Scala() {
        setLanguageCode_Equal_AsTargetLanguage(CDef.TargetLanguage.Scala);
    }

    protected void doSetLanguageCode_Equal(String languageCode) {
        regLanguageCode(CK_EQ, languageCode);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @param languageCode The value of languageCode as notEqual. (NullAllowed: if null (or empty), no condition)
     */
    protected void setLanguageCode_NotEqual(String languageCode) {
        doSetLanguageCode_NotEqual(fRES(languageCode));
    }

    /**
     * NotEqual(&lt;&gt;). As TargetLanguage. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setLanguageCode_NotEqual_AsTargetLanguage(CDef.TargetLanguage cdef) {
        doSetLanguageCode_NotEqual(cdef != null ? cdef.code() : null);
    }

    /**
     * NotEqual(&lt;&gt;). As Java (java). And OnlyOnceRegistered. <br>
     * Java
     */
    public void setLanguageCode_NotEqual_Java() {
        setLanguageCode_NotEqual_AsTargetLanguage(CDef.TargetLanguage.Java);
    }

    /**
     * NotEqual(&lt;&gt;). As C (csharp). And OnlyOnceRegistered. <br>
     * C#
     */
    public void setLanguageCode_NotEqual_C() {
        setLanguageCode_NotEqual_AsTargetLanguage(CDef.TargetLanguage.C);
    }

    /**
     * NotEqual(&lt;&gt;). As Scala (scala). And OnlyOnceRegistered. <br>
     * Scala
     */
    public void setLanguageCode_NotEqual_Scala() {
        setLanguageCode_NotEqual_AsTargetLanguage(CDef.TargetLanguage.Scala);
    }

    protected void doSetLanguageCode_NotEqual(String languageCode) {
        regLanguageCode(CK_NES, languageCode);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @param languageCodeList The collection of languageCode as inScope. (NullAllowed: if null (or empty), no condition)
     */
    protected void setLanguageCode_InScope(Collection<String> languageCodeList) {
        doSetLanguageCode_InScope(languageCodeList);
    }

    /**
     * InScope {in ('a', 'b')}. As TargetLanguage. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (NullAllowed: if null (or empty), no condition)
     */
    public void setLanguageCode_InScope_AsTargetLanguage(Collection<CDef.TargetLanguage> cdefList) {
        doSetLanguageCode_InScope(cTStrL(cdefList));
    }

    protected void doSetLanguageCode_InScope(Collection<String> languageCodeList) {
        regINS(CK_INS, cTL(languageCodeList), xgetCValueLanguageCode(), "LANGUAGE_CODE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     * @param languageCodeList The collection of languageCode as notInScope. (NullAllowed: if null (or empty), no condition)
     */
    public void setLanguageCode_NotInScope(Collection<String> languageCodeList) {
        doSetLanguageCode_NotInScope(languageCodeList);
    }

    /**
     * NotInScope {not in ('a', 'b')}. As TargetLanguage. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (NullAllowed: if null (or empty), no condition)
     */
    public void setLanguageCode_NotInScope_AsTargetLanguage(Collection<CDef.TargetLanguage> cdefList) {
        doSetLanguageCode_NotInScope(cTStrL(cdefList));
    }

    protected void doSetLanguageCode_NotInScope(Collection<String> languageCodeList) {
        regINS(CK_NINS, cTL(languageCodeList), xgetCValueLanguageCode(), "LANGUAGE_CODE");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     */
    public void setLanguageCode_IsNull() { regLanguageCode(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage}
     */
    public void setLanguageCode_IsNotNull() { regLanguageCode(CK_ISNN, DOBJ); }

    protected void regLanguageCode(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueLanguageCode(), "LANGUAGE_CODE"); }
    protected abstract ConditionValue xgetCValueLanguageCode();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)}
     * @param languageName The value of languageName as equal. (NullAllowed: if null (or empty), no condition)
     */
    public void setLanguageName_Equal(String languageName) {
        doSetLanguageName_Equal(fRES(languageName));
    }

    protected void doSetLanguageName_Equal(String languageName) {
        regLanguageName(CK_EQ, languageName);
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setLanguageName_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param languageName The value of languageName as likeSearch. (NullAllowed: if null (or empty), no condition)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setLanguageName_LikeSearch(String languageName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setLanguageName_LikeSearch(languageName, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setLanguageName_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param languageName The value of languageName as likeSearch. (NullAllowed: if null (or empty), no condition)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setLanguageName_LikeSearch(String languageName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(languageName), xgetCValueLanguageName(), "LANGUAGE_NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)}
     * @param languageName The value of languageName as notLikeSearch. (NullAllowed: if null (or empty), no condition)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setLanguageName_NotLikeSearch(String languageName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setLanguageName_NotLikeSearch(languageName, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * LANGUAGE_NAME: {NotNull, VARCHAR(100)}
     * @param languageName The value of languageName as notLikeSearch. (NullAllowed: if null (or empty), no condition)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setLanguageName_NotLikeSearch(String languageName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(languageName), xgetCValueLanguageName(), "LANGUAGE_NAME", likeSearchOption);
    }

    protected void regLanguageName(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueLanguageName(), "LANGUAGE_NAME"); }
    protected abstract ConditionValue xgetCValueLanguageName();

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
     * @param minNumber The min number of displayOrder. (NullAllowed: if null, no from-condition)
     * @param maxNumber The max number of displayOrder. (NullAllowed: if null, no to-condition)
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
     * @param minNumber The min number of displayOrder. (NullAllowed: if null, no from-condition)
     * @param maxNumber The max number of displayOrder. (NullAllowed: if null, no to-condition)
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
    public HpSLCFunction<ClsTargetLanguageCB> scalar_Equal() {
        return xcreateSLCFunction(CK_EQ, ClsTargetLanguageCB.class);
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
    public HpSLCFunction<ClsTargetLanguageCB> scalar_NotEqual() {
        return xcreateSLCFunction(CK_NES, ClsTargetLanguageCB.class);
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
    public HpSLCFunction<ClsTargetLanguageCB> scalar_GreaterThan() {
        return xcreateSLCFunction(CK_GT, ClsTargetLanguageCB.class);
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
    public HpSLCFunction<ClsTargetLanguageCB> scalar_LessThan() {
        return xcreateSLCFunction(CK_LT, ClsTargetLanguageCB.class);
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
    public HpSLCFunction<ClsTargetLanguageCB> scalar_GreaterEqual() {
        return xcreateSLCFunction(CK_GE, ClsTargetLanguageCB.class);
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br>
     * {where FOO &lt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().<span style="color: #CC4747">scalar_LessEqual()</span>.max(new SubQuery&lt;ClsTargetLanguageCB&gt;() {
     *     public void query(ClsTargetLanguageCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetLanguageCB> scalar_LessEqual() {
        return xcreateSLCFunction(CK_LE, ClsTargetLanguageCB.class);
    }

    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xscalarCondition(String fn, SubQuery<CB> sq, String rd, HpSLCCustomized<CB> cs, ScalarConditionOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetLanguageCB cb = xcreateScalarConditionCB(); sq.query((CB)cb);
        String pp = keepScalarCondition(cb.query()); // for saving query-value
        cs.setPartitionByCBean((CB)xcreateScalarConditionPartitionByCB()); // for using partition-by
        registerScalarCondition(fn, cb.query(), pp, rd, cs, op);
    }
    public abstract String keepScalarCondition(ClsTargetLanguageCQ sq);

    protected ClsTargetLanguageCB xcreateScalarConditionCB() {
        ClsTargetLanguageCB cb = newMyCB(); cb.xsetupForScalarCondition(this); return cb;
    }

    protected ClsTargetLanguageCB xcreateScalarConditionPartitionByCB() {
        ClsTargetLanguageCB cb = newMyCB(); cb.xsetupForScalarConditionPartitionBy(this); return cb;
    }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public void xsmyselfDerive(String fn, SubQuery<ClsTargetLanguageCB> sq, String al, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetLanguageCB cb = new ClsTargetLanguageCB(); cb.xsetupForDerivedReferrer(this);
        lockCall(() -> sq.query(cb)); String pp = keepSpecifyMyselfDerived(cb.query()); String pk = "LANGUAGE_CODE";
        registerSpecifyMyselfDerived(fn, cb.query(), pk, pk, pp, "myselfDerived", al, op);
    }
    public abstract String keepSpecifyMyselfDerived(ClsTargetLanguageCQ sq);

    /**
     * Prepare for (Query)MyselfDerived (correlated sub-query).
     * @return The object to set up a function for myself table. (NotNull)
     */
    public HpQDRFunction<ClsTargetLanguageCB> myselfDerived() {
        return xcreateQDRFunctionMyselfDerived(ClsTargetLanguageCB.class);
    }
    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xqderiveMyselfDerived(String fn, SubQuery<CB> sq, String rd, Object vl, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetLanguageCB cb = new ClsTargetLanguageCB(); cb.xsetupForDerivedReferrer(this); sq.query((CB)cb);
        String pk = "LANGUAGE_CODE";
        String sqpp = keepQueryMyselfDerived(cb.query()); // for saving query-value.
        String prpp = keepQueryMyselfDerivedParameter(vl);
        registerQueryMyselfDerived(fn, cb.query(), pk, pk, sqpp, "myselfDerived", rd, vl, prpp, op);
    }
    public abstract String keepQueryMyselfDerived(ClsTargetLanguageCQ sq);
    public abstract String keepQueryMyselfDerivedParameter(Object vl);

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    /**
     * Prepare for MyselfExists (correlated sub-query).
     * @param subCBLambda The implementation of sub-query. (NotNull)
     */
    public void myselfExists(SubQuery<ClsTargetLanguageCB> subCBLambda) {
        assertObjectNotNull("subCBLambda", subCBLambda);
        ClsTargetLanguageCB cb = new ClsTargetLanguageCB(); cb.xsetupForMyselfExists(this);
        lockCall(() -> subCBLambda.query(cb)); String pp = keepMyselfExists(cb.query());
        registerMyselfExists(cb.query(), pp);
    }
    public abstract String keepMyselfExists(ClsTargetLanguageCQ sq);

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
     *     <span style="color: #553000">op</span>.<span style="color: #CC4747">when_GreaterEqual</span>(priorityDate); <span style="color: #3F7E5E">// e.g. 2000/01/01</span>
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
    protected ClsTargetLanguageCB newMyCB() {
        return new ClsTargetLanguageCB();
    }
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabUDT() { return Date.class.getName(); }
    protected String xabCQ() { return ClsTargetLanguageCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSLCS() { return HpSLCSetupper.class.getName(); }
    protected String xabSCP() { return SubQuery.class.getName(); }
}
