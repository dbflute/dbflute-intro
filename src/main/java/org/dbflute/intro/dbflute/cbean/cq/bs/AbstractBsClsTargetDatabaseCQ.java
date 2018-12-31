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
 * The abstract condition-query of CLS_TARGET_DATABASE.
 * @author DBFlute(AutoGenerator)
 */
public abstract class AbstractBsClsTargetDatabaseCQ extends AbstractConditionQuery {

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AbstractBsClsTargetDatabaseCQ(ConditionQuery referrerQuery, SqlClause sqlClause, String aliasName, int nestLevel) {
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
        return "CLS_TARGET_DATABASE";
    }

    // ===================================================================================
    //                                                                               Query
    //                                                                               =====
    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @param databaseCode The value of databaseCode as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setDatabaseCode_Equal(String databaseCode) {
        doSetDatabaseCode_Equal(fRES(databaseCode));
    }

    /**
     * Equal(=). As TargetDatabase. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase cdef) {
        doSetDatabaseCode_Equal(cdef != null ? cdef.code() : null);
    }

    /**
     * Equal(=). As MySQL (mysql). And OnlyOnceRegistered. <br>
     * MySQL
     */
    public void setDatabaseCode_Equal_MySQL() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.MySQL);
    }

    /**
     * Equal(=). As PostgreSQL (postgresql). And OnlyOnceRegistered. <br>
     * PostgreSQL
     */
    public void setDatabaseCode_Equal_PostgreSQL() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.PostgreSQL);
    }

    /**
     * Equal(=). As Oracle (oracle). And OnlyOnceRegistered. <br>
     * Oracle
     */
    public void setDatabaseCode_Equal_Oracle() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.Oracle);
    }

    /**
     * Equal(=). As Db2 (db2). And OnlyOnceRegistered. <br>
     * DB2
     */
    public void setDatabaseCode_Equal_Db2() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.Db2);
    }

    /**
     * Equal(=). As SQLServer (sqlserver). And OnlyOnceRegistered. <br>
     * SQLServer
     */
    public void setDatabaseCode_Equal_SQLServer() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.SQLServer);
    }

    /**
     * Equal(=). As H2Database (h2). And OnlyOnceRegistered. <br>
     * H2 Database
     */
    public void setDatabaseCode_Equal_H2Database() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.H2Database);
    }

    /**
     * Equal(=). As ApacheDerby (derby). And OnlyOnceRegistered. <br>
     * Apache Derby
     */
    public void setDatabaseCode_Equal_ApacheDerby() {
        setDatabaseCode_Equal_AsTargetDatabase(CDef.TargetDatabase.ApacheDerby);
    }

    protected void doSetDatabaseCode_Equal(String databaseCode) {
        regDatabaseCode(CK_EQ, databaseCode);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @param databaseCode The value of databaseCode as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setDatabaseCode_NotEqual(String databaseCode) {
        doSetDatabaseCode_NotEqual(fRES(databaseCode));
    }

    /**
     * NotEqual(&lt;&gt;). As TargetDatabase. And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase cdef) {
        doSetDatabaseCode_NotEqual(cdef != null ? cdef.code() : null);
    }

    /**
     * NotEqual(&lt;&gt;). As MySQL (mysql). And OnlyOnceRegistered. <br>
     * MySQL
     */
    public void setDatabaseCode_NotEqual_MySQL() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.MySQL);
    }

    /**
     * NotEqual(&lt;&gt;). As PostgreSQL (postgresql). And OnlyOnceRegistered. <br>
     * PostgreSQL
     */
    public void setDatabaseCode_NotEqual_PostgreSQL() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.PostgreSQL);
    }

    /**
     * NotEqual(&lt;&gt;). As Oracle (oracle). And OnlyOnceRegistered. <br>
     * Oracle
     */
    public void setDatabaseCode_NotEqual_Oracle() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.Oracle);
    }

    /**
     * NotEqual(&lt;&gt;). As Db2 (db2). And OnlyOnceRegistered. <br>
     * DB2
     */
    public void setDatabaseCode_NotEqual_Db2() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.Db2);
    }

    /**
     * NotEqual(&lt;&gt;). As SQLServer (sqlserver). And OnlyOnceRegistered. <br>
     * SQLServer
     */
    public void setDatabaseCode_NotEqual_SQLServer() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.SQLServer);
    }

    /**
     * NotEqual(&lt;&gt;). As H2Database (h2). And OnlyOnceRegistered. <br>
     * H2 Database
     */
    public void setDatabaseCode_NotEqual_H2Database() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.H2Database);
    }

    /**
     * NotEqual(&lt;&gt;). As ApacheDerby (derby). And OnlyOnceRegistered. <br>
     * Apache Derby
     */
    public void setDatabaseCode_NotEqual_ApacheDerby() {
        setDatabaseCode_NotEqual_AsTargetDatabase(CDef.TargetDatabase.ApacheDerby);
    }

    protected void doSetDatabaseCode_NotEqual(String databaseCode) {
        regDatabaseCode(CK_NES, databaseCode);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @param databaseCodeList The collection of databaseCode as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setDatabaseCode_InScope(Collection<String> databaseCodeList) {
        doSetDatabaseCode_InScope(databaseCodeList);
    }

    /**
     * InScope {in ('a', 'b')}. As TargetDatabase. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDatabaseCode_InScope_AsTargetDatabase(Collection<CDef.TargetDatabase> cdefList) {
        doSetDatabaseCode_InScope(cTStrL(cdefList));
    }

    protected void doSetDatabaseCode_InScope(Collection<String> databaseCodeList) {
        regINS(CK_INS, cTL(databaseCodeList), xgetCValueDatabaseCode(), "DATABASE_CODE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     * @param databaseCodeList The collection of databaseCode as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setDatabaseCode_NotInScope(Collection<String> databaseCodeList) {
        doSetDatabaseCode_NotInScope(databaseCodeList);
    }

    /**
     * NotInScope {not in ('a', 'b')}. As TargetDatabase. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDatabaseCode_NotInScope_AsTargetDatabase(Collection<CDef.TargetDatabase> cdefList) {
        doSetDatabaseCode_NotInScope(cTStrL(cdefList));
    }

    protected void doSetDatabaseCode_NotInScope(Collection<String> databaseCodeList) {
        regINS(CK_NINS, cTL(databaseCodeList), xgetCValueDatabaseCode(), "DATABASE_CODE");
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     */
    public void setDatabaseCode_IsNull() { regDatabaseCode(CK_ISN, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase}
     */
    public void setDatabaseCode_IsNotNull() { regDatabaseCode(CK_ISNN, DOBJ); }

    protected void regDatabaseCode(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueDatabaseCode(), "DATABASE_CODE"); }
    protected abstract ConditionValue xgetCValueDatabaseCode();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @param databaseName The value of databaseName as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDatabaseName_Equal(String databaseName) {
        doSetDatabaseName_Equal(fRES(databaseName));
    }

    protected void doSetDatabaseName_Equal(String databaseName) {
        regDatabaseName(CK_EQ, databaseName);
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setDatabaseName_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param databaseName The value of databaseName as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setDatabaseName_LikeSearch(String databaseName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setDatabaseName_LikeSearch(databaseName, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setDatabaseName_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param databaseName The value of databaseName as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setDatabaseName_LikeSearch(String databaseName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(databaseName), xgetCValueDatabaseName(), "DATABASE_NAME", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @param databaseName The value of databaseName as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setDatabaseName_NotLikeSearch(String databaseName, ConditionOptionCall<LikeSearchOption> opLambda) {
        setDatabaseName_NotLikeSearch(databaseName, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DATABASE_NAME: {NotNull, VARCHAR(100)}
     * @param databaseName The value of databaseName as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setDatabaseName_NotLikeSearch(String databaseName, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(databaseName), xgetCValueDatabaseName(), "DATABASE_NAME", likeSearchOption);
    }

    protected void regDatabaseName(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueDatabaseName(), "DATABASE_NAME"); }
    protected abstract ConditionValue xgetCValueDatabaseName();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setJdbcDriverFqcn_Equal(String jdbcDriverFqcn) {
        doSetJdbcDriverFqcn_Equal(fRES(jdbcDriverFqcn));
    }

    protected void doSetJdbcDriverFqcn_Equal(String jdbcDriverFqcn) {
        regJdbcDriverFqcn(CK_EQ, jdbcDriverFqcn);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setJdbcDriverFqcn_NotEqual(String jdbcDriverFqcn) {
        doSetJdbcDriverFqcn_NotEqual(fRES(jdbcDriverFqcn));
    }

    protected void doSetJdbcDriverFqcn_NotEqual(String jdbcDriverFqcn) {
        regJdbcDriverFqcn(CK_NES, jdbcDriverFqcn);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcnList The collection of jdbcDriverFqcn as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setJdbcDriverFqcn_InScope(Collection<String> jdbcDriverFqcnList) {
        doSetJdbcDriverFqcn_InScope(jdbcDriverFqcnList);
    }

    protected void doSetJdbcDriverFqcn_InScope(Collection<String> jdbcDriverFqcnList) {
        regINS(CK_INS, cTL(jdbcDriverFqcnList), xgetCValueJdbcDriverFqcn(), "JDBC_DRIVER_FQCN");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcnList The collection of jdbcDriverFqcn as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setJdbcDriverFqcn_NotInScope(Collection<String> jdbcDriverFqcnList) {
        doSetJdbcDriverFqcn_NotInScope(jdbcDriverFqcnList);
    }

    protected void doSetJdbcDriverFqcn_NotInScope(Collection<String> jdbcDriverFqcnList) {
        regINS(CK_NINS, cTL(jdbcDriverFqcnList), xgetCValueJdbcDriverFqcn(), "JDBC_DRIVER_FQCN");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setJdbcDriverFqcn_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setJdbcDriverFqcn_LikeSearch(String jdbcDriverFqcn, ConditionOptionCall<LikeSearchOption> opLambda) {
        setJdbcDriverFqcn_LikeSearch(jdbcDriverFqcn, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setJdbcDriverFqcn_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setJdbcDriverFqcn_LikeSearch(String jdbcDriverFqcn, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(jdbcDriverFqcn), xgetCValueJdbcDriverFqcn(), "JDBC_DRIVER_FQCN", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setJdbcDriverFqcn_NotLikeSearch(String jdbcDriverFqcn, ConditionOptionCall<LikeSearchOption> opLambda) {
        setJdbcDriverFqcn_NotLikeSearch(jdbcDriverFqcn, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)}
     * @param jdbcDriverFqcn The value of jdbcDriverFqcn as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setJdbcDriverFqcn_NotLikeSearch(String jdbcDriverFqcn, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(jdbcDriverFqcn), xgetCValueJdbcDriverFqcn(), "JDBC_DRIVER_FQCN", likeSearchOption);
    }

    protected void regJdbcDriverFqcn(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueJdbcDriverFqcn(), "JDBC_DRIVER_FQCN"); }
    protected abstract ConditionValue xgetCValueJdbcDriverFqcn();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplate The value of urlTemplate as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUrlTemplate_Equal(String urlTemplate) {
        doSetUrlTemplate_Equal(fRES(urlTemplate));
    }

    protected void doSetUrlTemplate_Equal(String urlTemplate) {
        regUrlTemplate(CK_EQ, urlTemplate);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplate The value of urlTemplate as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUrlTemplate_NotEqual(String urlTemplate) {
        doSetUrlTemplate_NotEqual(fRES(urlTemplate));
    }

    protected void doSetUrlTemplate_NotEqual(String urlTemplate) {
        regUrlTemplate(CK_NES, urlTemplate);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplateList The collection of urlTemplate as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUrlTemplate_InScope(Collection<String> urlTemplateList) {
        doSetUrlTemplate_InScope(urlTemplateList);
    }

    protected void doSetUrlTemplate_InScope(Collection<String> urlTemplateList) {
        regINS(CK_INS, cTL(urlTemplateList), xgetCValueUrlTemplate(), "URL_TEMPLATE");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplateList The collection of urlTemplate as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUrlTemplate_NotInScope(Collection<String> urlTemplateList) {
        doSetUrlTemplate_NotInScope(urlTemplateList);
    }

    protected void doSetUrlTemplate_NotInScope(Collection<String> urlTemplateList) {
        regINS(CK_NINS, cTL(urlTemplateList), xgetCValueUrlTemplate(), "URL_TEMPLATE");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setUrlTemplate_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param urlTemplate The value of urlTemplate as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setUrlTemplate_LikeSearch(String urlTemplate, ConditionOptionCall<LikeSearchOption> opLambda) {
        setUrlTemplate_LikeSearch(urlTemplate, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)} <br>
     * <pre>e.g. setUrlTemplate_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param urlTemplate The value of urlTemplate as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setUrlTemplate_LikeSearch(String urlTemplate, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(urlTemplate), xgetCValueUrlTemplate(), "URL_TEMPLATE", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplate The value of urlTemplate as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setUrlTemplate_NotLikeSearch(String urlTemplate, ConditionOptionCall<LikeSearchOption> opLambda) {
        setUrlTemplate_NotLikeSearch(urlTemplate, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * URL_TEMPLATE: {NotNull, VARCHAR(100)}
     * @param urlTemplate The value of urlTemplate as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setUrlTemplate_NotLikeSearch(String urlTemplate, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(urlTemplate), xgetCValueUrlTemplate(), "URL_TEMPLATE", likeSearchOption);
    }

    protected void regUrlTemplate(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueUrlTemplate(), "URL_TEMPLATE"); }
    protected abstract ConditionValue xgetCValueUrlTemplate();

    /**
     * Equal(=). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchema The value of defaultSchema as equal. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDefaultSchema_Equal(String defaultSchema) {
        doSetDefaultSchema_Equal(fRES(defaultSchema));
    }

    protected void doSetDefaultSchema_Equal(String defaultSchema) {
        regDefaultSchema(CK_EQ, defaultSchema);
    }

    /**
     * NotEqual(&lt;&gt;). And NullOrEmptyIgnored, OnlyOnceRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchema The value of defaultSchema as notEqual. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDefaultSchema_NotEqual(String defaultSchema) {
        doSetDefaultSchema_NotEqual(fRES(defaultSchema));
    }

    protected void doSetDefaultSchema_NotEqual(String defaultSchema) {
        regDefaultSchema(CK_NES, defaultSchema);
    }

    /**
     * InScope {in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchemaList The collection of defaultSchema as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDefaultSchema_InScope(Collection<String> defaultSchemaList) {
        doSetDefaultSchema_InScope(defaultSchemaList);
    }

    protected void doSetDefaultSchema_InScope(Collection<String> defaultSchemaList) {
        regINS(CK_INS, cTL(defaultSchemaList), xgetCValueDefaultSchema(), "DEFAULT_SCHEMA");
    }

    /**
     * NotInScope {not in ('a', 'b')}. And NullOrEmptyIgnored, NullOrEmptyElementIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchemaList The collection of defaultSchema as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setDefaultSchema_NotInScope(Collection<String> defaultSchemaList) {
        doSetDefaultSchema_NotInScope(defaultSchemaList);
    }

    protected void doSetDefaultSchema_NotInScope(Collection<String> defaultSchemaList) {
        regINS(CK_NINS, cTL(defaultSchemaList), xgetCValueDefaultSchema(), "DEFAULT_SCHEMA");
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)} <br>
     * <pre>e.g. setDefaultSchema_LikeSearch("xxx", op <span style="color: #90226C; font-weight: bold"><span style="font-size: 120%">-</span>&gt;</span> op.<span style="color: #CC4747">likeContain()</span>);</pre>
     * @param defaultSchema The value of defaultSchema as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setDefaultSchema_LikeSearch(String defaultSchema, ConditionOptionCall<LikeSearchOption> opLambda) {
        setDefaultSchema_LikeSearch(defaultSchema, xcLSOP(opLambda));
    }

    /**
     * LikeSearch with various options. (versatile) {like '%xxx%' escape ...}. And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)} <br>
     * <pre>e.g. setDefaultSchema_LikeSearch("xxx", new <span style="color: #CC4747">LikeSearchOption</span>().likeContain());</pre>
     * @param defaultSchema The value of defaultSchema as likeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of like-search. (NotNull)
     */
    protected void setDefaultSchema_LikeSearch(String defaultSchema, LikeSearchOption likeSearchOption) {
        regLSQ(CK_LS, fRES(defaultSchema), xgetCValueDefaultSchema(), "DEFAULT_SCHEMA", likeSearchOption);
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchema The value of defaultSchema as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param opLambda The callback for option of like-search. (NotNull)
     */
    public void setDefaultSchema_NotLikeSearch(String defaultSchema, ConditionOptionCall<LikeSearchOption> opLambda) {
        setDefaultSchema_NotLikeSearch(defaultSchema, xcLSOP(opLambda));
    }

    /**
     * NotLikeSearch with various options. (versatile) {not like 'xxx%' escape ...} <br>
     * And NullOrEmptyIgnored, SeveralRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     * @param defaultSchema The value of defaultSchema as notLikeSearch. (basically NotNull, NotEmpty: error as default, or no condition as option)
     * @param likeSearchOption The option of not-like-search. (NotNull)
     */
    protected void setDefaultSchema_NotLikeSearch(String defaultSchema, LikeSearchOption likeSearchOption) {
        regLSQ(CK_NLS, fRES(defaultSchema), xgetCValueDefaultSchema(), "DEFAULT_SCHEMA", likeSearchOption);
    }

    /**
     * IsNull {is null}. And OnlyOnceRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     */
    public void setDefaultSchema_IsNull() { regDefaultSchema(CK_ISN, DOBJ); }

    /**
     * IsNullOrEmpty {is null or empty}. And OnlyOnceRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     */
    public void setDefaultSchema_IsNullOrEmpty() { regDefaultSchema(CK_ISNOE, DOBJ); }

    /**
     * IsNotNull {is not null}. And OnlyOnceRegistered. <br>
     * DEFAULT_SCHEMA: {VARCHAR(10)}
     */
    public void setDefaultSchema_IsNotNull() { regDefaultSchema(CK_ISNN, DOBJ); }

    protected void regDefaultSchema(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueDefaultSchema(), "DEFAULT_SCHEMA"); }
    protected abstract ConditionValue xgetCValueDefaultSchema();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaRequiredFlg The value of schemaRequiredFlg as equal. (basically NotNull: error as default, or no condition as option)
     */
    protected void setSchemaRequiredFlg_Equal(Integer schemaRequiredFlg) {
        doSetSchemaRequiredFlg_Equal(schemaRequiredFlg);
    }

    /**
     * Equal(=). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaRequiredFlg_Equal_AsFlg(CDef.Flg cdef) {
        doSetSchemaRequiredFlg_Equal(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * Equal(=). As boolean for Flg. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaRequiredFlg_Equal_AsBoolean(Boolean determination) {
        setSchemaRequiredFlg_Equal_AsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Equal(=). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setSchemaRequiredFlg_Equal_True() {
        setSchemaRequiredFlg_Equal_AsFlg(CDef.Flg.True);
    }

    /**
     * Equal(=). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setSchemaRequiredFlg_Equal_False() {
        setSchemaRequiredFlg_Equal_AsFlg(CDef.Flg.False);
    }

    protected void doSetSchemaRequiredFlg_Equal(Integer schemaRequiredFlg) {
        regSchemaRequiredFlg(CK_EQ, schemaRequiredFlg);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaRequiredFlg The value of schemaRequiredFlg as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    protected void setSchemaRequiredFlg_NotEqual(Integer schemaRequiredFlg) {
        doSetSchemaRequiredFlg_NotEqual(schemaRequiredFlg);
    }

    /**
     * NotEqual(&lt;&gt;). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaRequiredFlg_NotEqual_AsFlg(CDef.Flg cdef) {
        doSetSchemaRequiredFlg_NotEqual(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * NotEqual(&lt;&gt;). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setSchemaRequiredFlg_NotEqual_True() {
        setSchemaRequiredFlg_NotEqual_AsFlg(CDef.Flg.True);
    }

    /**
     * NotEqual(&lt;&gt;). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setSchemaRequiredFlg_NotEqual_False() {
        setSchemaRequiredFlg_NotEqual_AsFlg(CDef.Flg.False);
    }

    protected void doSetSchemaRequiredFlg_NotEqual(Integer schemaRequiredFlg) {
        regSchemaRequiredFlg(CK_NES, schemaRequiredFlg);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaRequiredFlgList The collection of schemaRequiredFlg as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setSchemaRequiredFlg_InScope(Collection<Integer> schemaRequiredFlgList) {
        doSetSchemaRequiredFlg_InScope(schemaRequiredFlgList);
    }

    /**
     * InScope {in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setSchemaRequiredFlg_InScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetSchemaRequiredFlg_InScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetSchemaRequiredFlg_InScope(Collection<Integer> schemaRequiredFlgList) {
        regINS(CK_INS, cTL(schemaRequiredFlgList), xgetCValueSchemaRequiredFlg(), "SCHEMA_REQUIRED_FLG");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaRequiredFlgList The collection of schemaRequiredFlg as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setSchemaRequiredFlg_NotInScope(Collection<Integer> schemaRequiredFlgList) {
        doSetSchemaRequiredFlg_NotInScope(schemaRequiredFlgList);
    }

    /**
     * NotInScope {not in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setSchemaRequiredFlg_NotInScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetSchemaRequiredFlg_NotInScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetSchemaRequiredFlg_NotInScope(Collection<Integer> schemaRequiredFlgList) {
        regINS(CK_NINS, cTL(schemaRequiredFlgList), xgetCValueSchemaRequiredFlg(), "SCHEMA_REQUIRED_FLG");
    }

    protected void regSchemaRequiredFlg(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueSchemaRequiredFlg(), "SCHEMA_REQUIRED_FLG"); }
    protected abstract ConditionValue xgetCValueSchemaRequiredFlg();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaUpperCaseFlg The value of schemaUpperCaseFlg as equal. (basically NotNull: error as default, or no condition as option)
     */
    protected void setSchemaUpperCaseFlg_Equal(Integer schemaUpperCaseFlg) {
        doSetSchemaUpperCaseFlg_Equal(schemaUpperCaseFlg);
    }

    /**
     * Equal(=). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaUpperCaseFlg_Equal_AsFlg(CDef.Flg cdef) {
        doSetSchemaUpperCaseFlg_Equal(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * Equal(=). As boolean for Flg. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaUpperCaseFlg_Equal_AsBoolean(Boolean determination) {
        setSchemaUpperCaseFlg_Equal_AsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Equal(=). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setSchemaUpperCaseFlg_Equal_True() {
        setSchemaUpperCaseFlg_Equal_AsFlg(CDef.Flg.True);
    }

    /**
     * Equal(=). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setSchemaUpperCaseFlg_Equal_False() {
        setSchemaUpperCaseFlg_Equal_AsFlg(CDef.Flg.False);
    }

    protected void doSetSchemaUpperCaseFlg_Equal(Integer schemaUpperCaseFlg) {
        regSchemaUpperCaseFlg(CK_EQ, schemaUpperCaseFlg);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaUpperCaseFlg The value of schemaUpperCaseFlg as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    protected void setSchemaUpperCaseFlg_NotEqual(Integer schemaUpperCaseFlg) {
        doSetSchemaUpperCaseFlg_NotEqual(schemaUpperCaseFlg);
    }

    /**
     * NotEqual(&lt;&gt;). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setSchemaUpperCaseFlg_NotEqual_AsFlg(CDef.Flg cdef) {
        doSetSchemaUpperCaseFlg_NotEqual(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * NotEqual(&lt;&gt;). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setSchemaUpperCaseFlg_NotEqual_True() {
        setSchemaUpperCaseFlg_NotEqual_AsFlg(CDef.Flg.True);
    }

    /**
     * NotEqual(&lt;&gt;). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setSchemaUpperCaseFlg_NotEqual_False() {
        setSchemaUpperCaseFlg_NotEqual_AsFlg(CDef.Flg.False);
    }

    protected void doSetSchemaUpperCaseFlg_NotEqual(Integer schemaUpperCaseFlg) {
        regSchemaUpperCaseFlg(CK_NES, schemaUpperCaseFlg);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaUpperCaseFlgList The collection of schemaUpperCaseFlg as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setSchemaUpperCaseFlg_InScope(Collection<Integer> schemaUpperCaseFlgList) {
        doSetSchemaUpperCaseFlg_InScope(schemaUpperCaseFlgList);
    }

    /**
     * InScope {in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setSchemaUpperCaseFlg_InScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetSchemaUpperCaseFlg_InScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetSchemaUpperCaseFlg_InScope(Collection<Integer> schemaUpperCaseFlgList) {
        regINS(CK_INS, cTL(schemaUpperCaseFlgList), xgetCValueSchemaUpperCaseFlg(), "SCHEMA_UPPER_CASE_FLG");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param schemaUpperCaseFlgList The collection of schemaUpperCaseFlg as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setSchemaUpperCaseFlg_NotInScope(Collection<Integer> schemaUpperCaseFlgList) {
        doSetSchemaUpperCaseFlg_NotInScope(schemaUpperCaseFlgList);
    }

    /**
     * NotInScope {not in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setSchemaUpperCaseFlg_NotInScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetSchemaUpperCaseFlg_NotInScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetSchemaUpperCaseFlg_NotInScope(Collection<Integer> schemaUpperCaseFlgList) {
        regINS(CK_NINS, cTL(schemaUpperCaseFlgList), xgetCValueSchemaUpperCaseFlg(), "SCHEMA_UPPER_CASE_FLG");
    }

    protected void regSchemaUpperCaseFlg(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueSchemaUpperCaseFlg(), "SCHEMA_UPPER_CASE_FLG"); }
    protected abstract ConditionValue xgetCValueSchemaUpperCaseFlg();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param userInputAssistFlg The value of userInputAssistFlg as equal. (basically NotNull: error as default, or no condition as option)
     */
    protected void setUserInputAssistFlg_Equal(Integer userInputAssistFlg) {
        doSetUserInputAssistFlg_Equal(userInputAssistFlg);
    }

    /**
     * Equal(=). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setUserInputAssistFlg_Equal_AsFlg(CDef.Flg cdef) {
        doSetUserInputAssistFlg_Equal(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * Equal(=). As boolean for Flg. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (basically NotNull: error as default, or no condition as option)
     */
    public void setUserInputAssistFlg_Equal_AsBoolean(Boolean determination) {
        setUserInputAssistFlg_Equal_AsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Equal(=). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setUserInputAssistFlg_Equal_True() {
        setUserInputAssistFlg_Equal_AsFlg(CDef.Flg.True);
    }

    /**
     * Equal(=). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setUserInputAssistFlg_Equal_False() {
        setUserInputAssistFlg_Equal_AsFlg(CDef.Flg.False);
    }

    protected void doSetUserInputAssistFlg_Equal(Integer userInputAssistFlg) {
        regUserInputAssistFlg(CK_EQ, userInputAssistFlg);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param userInputAssistFlg The value of userInputAssistFlg as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    protected void setUserInputAssistFlg_NotEqual(Integer userInputAssistFlg) {
        doSetUserInputAssistFlg_NotEqual(userInputAssistFlg);
    }

    /**
     * NotEqual(&lt;&gt;). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setUserInputAssistFlg_NotEqual_AsFlg(CDef.Flg cdef) {
        doSetUserInputAssistFlg_NotEqual(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * NotEqual(&lt;&gt;). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setUserInputAssistFlg_NotEqual_True() {
        setUserInputAssistFlg_NotEqual_AsFlg(CDef.Flg.True);
    }

    /**
     * NotEqual(&lt;&gt;). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setUserInputAssistFlg_NotEqual_False() {
        setUserInputAssistFlg_NotEqual_AsFlg(CDef.Flg.False);
    }

    protected void doSetUserInputAssistFlg_NotEqual(Integer userInputAssistFlg) {
        regUserInputAssistFlg(CK_NES, userInputAssistFlg);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param userInputAssistFlgList The collection of userInputAssistFlg as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setUserInputAssistFlg_InScope(Collection<Integer> userInputAssistFlgList) {
        doSetUserInputAssistFlg_InScope(userInputAssistFlgList);
    }

    /**
     * InScope {in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUserInputAssistFlg_InScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetUserInputAssistFlg_InScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetUserInputAssistFlg_InScope(Collection<Integer> userInputAssistFlgList) {
        regINS(CK_INS, cTL(userInputAssistFlgList), xgetCValueUserInputAssistFlg(), "USER_INPUT_ASSIST_FLG");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param userInputAssistFlgList The collection of userInputAssistFlg as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setUserInputAssistFlg_NotInScope(Collection<Integer> userInputAssistFlgList) {
        doSetUserInputAssistFlg_NotInScope(userInputAssistFlgList);
    }

    /**
     * NotInScope {not in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setUserInputAssistFlg_NotInScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetUserInputAssistFlg_NotInScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetUserInputAssistFlg_NotInScope(Collection<Integer> userInputAssistFlgList) {
        regINS(CK_NINS, cTL(userInputAssistFlgList), xgetCValueUserInputAssistFlg(), "USER_INPUT_ASSIST_FLG");
    }

    protected void regUserInputAssistFlg(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueUserInputAssistFlg(), "USER_INPUT_ASSIST_FLG"); }
    protected abstract ConditionValue xgetCValueUserInputAssistFlg();

    /**
     * Equal(=). And NullIgnored, OnlyOnceRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param embeddedJarFlg The value of embeddedJarFlg as equal. (basically NotNull: error as default, or no condition as option)
     */
    protected void setEmbeddedJarFlg_Equal(Integer embeddedJarFlg) {
        doSetEmbeddedJarFlg_Equal(embeddedJarFlg);
    }

    /**
     * Equal(=). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setEmbeddedJarFlg_Equal_AsFlg(CDef.Flg cdef) {
        doSetEmbeddedJarFlg_Equal(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * Equal(=). As boolean for Flg. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (basically NotNull: error as default, or no condition as option)
     */
    public void setEmbeddedJarFlg_Equal_AsBoolean(Boolean determination) {
        setEmbeddedJarFlg_Equal_AsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Equal(=). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setEmbeddedJarFlg_Equal_True() {
        setEmbeddedJarFlg_Equal_AsFlg(CDef.Flg.True);
    }

    /**
     * Equal(=). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setEmbeddedJarFlg_Equal_False() {
        setEmbeddedJarFlg_Equal_AsFlg(CDef.Flg.False);
    }

    protected void doSetEmbeddedJarFlg_Equal(Integer embeddedJarFlg) {
        regEmbeddedJarFlg(CK_EQ, embeddedJarFlg);
    }

    /**
     * NotEqual(&lt;&gt;). And NullIgnored, OnlyOnceRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param embeddedJarFlg The value of embeddedJarFlg as notEqual. (basically NotNull: error as default, or no condition as option)
     */
    protected void setEmbeddedJarFlg_NotEqual(Integer embeddedJarFlg) {
        doSetEmbeddedJarFlg_NotEqual(embeddedJarFlg);
    }

    /**
     * NotEqual(&lt;&gt;). As Flg. And NullIgnored, OnlyOnceRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (basically NotNull: error as default, or no condition as option)
     */
    public void setEmbeddedJarFlg_NotEqual_AsFlg(CDef.Flg cdef) {
        doSetEmbeddedJarFlg_NotEqual(cTNum(cdef != null ? cdef.code() : null, Integer.class));
    }

    /**
     * NotEqual(&lt;&gt;). As True (1). And NullIgnored, OnlyOnceRegistered. <br>
     * Checked: means yes
     */
    public void setEmbeddedJarFlg_NotEqual_True() {
        setEmbeddedJarFlg_NotEqual_AsFlg(CDef.Flg.True);
    }

    /**
     * NotEqual(&lt;&gt;). As False (0). And NullIgnored, OnlyOnceRegistered. <br>
     * Unchecked: means no
     */
    public void setEmbeddedJarFlg_NotEqual_False() {
        setEmbeddedJarFlg_NotEqual_AsFlg(CDef.Flg.False);
    }

    protected void doSetEmbeddedJarFlg_NotEqual(Integer embeddedJarFlg) {
        regEmbeddedJarFlg(CK_NES, embeddedJarFlg);
    }

    /**
     * InScope {in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param embeddedJarFlgList The collection of embeddedJarFlg as inScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setEmbeddedJarFlg_InScope(Collection<Integer> embeddedJarFlgList) {
        doSetEmbeddedJarFlg_InScope(embeddedJarFlgList);
    }

    /**
     * InScope {in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setEmbeddedJarFlg_InScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetEmbeddedJarFlg_InScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetEmbeddedJarFlg_InScope(Collection<Integer> embeddedJarFlgList) {
        regINS(CK_INS, cTL(embeddedJarFlgList), xgetCValueEmbeddedJarFlg(), "EMBEDDED_JAR_FLG");
    }

    /**
     * NotInScope {not in (1, 2)}. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg}
     * @param embeddedJarFlgList The collection of embeddedJarFlg as notInScope. (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    protected void setEmbeddedJarFlg_NotInScope(Collection<Integer> embeddedJarFlgList) {
        doSetEmbeddedJarFlg_NotInScope(embeddedJarFlgList);
    }

    /**
     * NotInScope {not in (1, 2)}. As Flg. And NullIgnored, NullElementIgnored, SeveralRegistered. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdefList The list of classification definition (as ENUM type). (basically NotNull, NotEmpty: error as default, or no condition as option)
     */
    public void setEmbeddedJarFlg_NotInScope_AsFlg(Collection<CDef.Flg> cdefList) {
        doSetEmbeddedJarFlg_NotInScope(cTNumL(cdefList, Integer.class));
    }

    protected void doSetEmbeddedJarFlg_NotInScope(Collection<Integer> embeddedJarFlgList) {
        regINS(CK_NINS, cTL(embeddedJarFlgList), xgetCValueEmbeddedJarFlg(), "EMBEDDED_JAR_FLG");
    }

    protected void regEmbeddedJarFlg(ConditionKey ky, Object vl) { regQ(ky, vl, xgetCValueEmbeddedJarFlg(), "EMBEDDED_JAR_FLG"); }
    protected abstract ConditionValue xgetCValueEmbeddedJarFlg();

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
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_Equal() {
        return xcreateSLCFunction(CK_EQ, ClsTargetDatabaseCB.class);
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
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_NotEqual() {
        return xcreateSLCFunction(CK_NES, ClsTargetDatabaseCB.class);
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
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_GreaterThan() {
        return xcreateSLCFunction(CK_GT, ClsTargetDatabaseCB.class);
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
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_LessThan() {
        return xcreateSLCFunction(CK_LT, ClsTargetDatabaseCB.class);
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
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_GreaterEqual() {
        return xcreateSLCFunction(CK_GE, ClsTargetDatabaseCB.class);
    }

    /**
     * Prepare ScalarCondition as lessEqual. <br>
     * {where FOO &lt;= (select max(BAR) from ...)}
     * <pre>
     * cb.query().<span style="color: #CC4747">scalar_LessEqual()</span>.max(new SubQuery&lt;ClsTargetDatabaseCB&gt;() {
     *     public void query(ClsTargetDatabaseCB subCB) {
     *         subCB.specify().setFoo... <span style="color: #3F7E5E">// derived column for function</span>
     *         subCB.query().setBar...
     *     }
     * });
     * </pre>
     * @return The object to set up a function. (NotNull)
     */
    public HpSLCFunction<ClsTargetDatabaseCB> scalar_LessEqual() {
        return xcreateSLCFunction(CK_LE, ClsTargetDatabaseCB.class);
    }

    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xscalarCondition(String fn, SubQuery<CB> sq, String rd, HpSLCCustomized<CB> cs, ScalarConditionOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetDatabaseCB cb = xcreateScalarConditionCB(); sq.query((CB)cb);
        String pp = keepScalarCondition(cb.query()); // for saving query-value
        cs.setPartitionByCBean((CB)xcreateScalarConditionPartitionByCB()); // for using partition-by
        registerScalarCondition(fn, cb.query(), pp, rd, cs, op);
    }
    public abstract String keepScalarCondition(ClsTargetDatabaseCQ sq);

    protected ClsTargetDatabaseCB xcreateScalarConditionCB() {
        ClsTargetDatabaseCB cb = newMyCB(); cb.xsetupForScalarCondition(this); return cb;
    }

    protected ClsTargetDatabaseCB xcreateScalarConditionPartitionByCB() {
        ClsTargetDatabaseCB cb = newMyCB(); cb.xsetupForScalarConditionPartitionBy(this); return cb;
    }

    // ===================================================================================
    //                                                                       MyselfDerived
    //                                                                       =============
    public void xsmyselfDerive(String fn, SubQuery<ClsTargetDatabaseCB> sq, String al, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetDatabaseCB cb = new ClsTargetDatabaseCB(); cb.xsetupForDerivedReferrer(this);
        lockCall(() -> sq.query(cb)); String pp = keepSpecifyMyselfDerived(cb.query()); String pk = "DATABASE_CODE";
        registerSpecifyMyselfDerived(fn, cb.query(), pk, pk, pp, "myselfDerived", al, op);
    }
    public abstract String keepSpecifyMyselfDerived(ClsTargetDatabaseCQ sq);

    /**
     * Prepare for (Query)MyselfDerived (correlated sub-query).
     * @return The object to set up a function for myself table. (NotNull)
     */
    public HpQDRFunction<ClsTargetDatabaseCB> myselfDerived() {
        return xcreateQDRFunctionMyselfDerived(ClsTargetDatabaseCB.class);
    }
    @SuppressWarnings("unchecked")
    protected <CB extends ConditionBean> void xqderiveMyselfDerived(String fn, SubQuery<CB> sq, String rd, Object vl, DerivedReferrerOption op) {
        assertObjectNotNull("subQuery", sq);
        ClsTargetDatabaseCB cb = new ClsTargetDatabaseCB(); cb.xsetupForDerivedReferrer(this); sq.query((CB)cb);
        String pk = "DATABASE_CODE";
        String sqpp = keepQueryMyselfDerived(cb.query()); // for saving query-value.
        String prpp = keepQueryMyselfDerivedParameter(vl);
        registerQueryMyselfDerived(fn, cb.query(), pk, pk, sqpp, "myselfDerived", rd, vl, prpp, op);
    }
    public abstract String keepQueryMyselfDerived(ClsTargetDatabaseCQ sq);
    public abstract String keepQueryMyselfDerivedParameter(Object vl);

    // ===================================================================================
    //                                                                        MyselfExists
    //                                                                        ============
    /**
     * Prepare for MyselfExists (correlated sub-query).
     * @param subCBLambda The implementation of sub-query. (NotNull)
     */
    public void myselfExists(SubQuery<ClsTargetDatabaseCB> subCBLambda) {
        assertObjectNotNull("subCBLambda", subCBLambda);
        ClsTargetDatabaseCB cb = new ClsTargetDatabaseCB(); cb.xsetupForMyselfExists(this);
        lockCall(() -> subCBLambda.query(cb)); String pp = keepMyselfExists(cb.query());
        registerMyselfExists(cb.query(), pp);
    }
    public abstract String keepMyselfExists(ClsTargetDatabaseCQ sq);

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
    protected ClsTargetDatabaseCB newMyCB() {
        return new ClsTargetDatabaseCB();
    }
    // very internal (for suppressing warn about 'Not Use Import')
    protected String xabUDT() { return Date.class.getName(); }
    protected String xabCQ() { return ClsTargetDatabaseCQ.class.getName(); }
    protected String xabLSO() { return LikeSearchOption.class.getName(); }
    protected String xabSLCS() { return HpSLCSetupper.class.getName(); }
    protected String xabSCP() { return SubQuery.class.getName(); }
}
