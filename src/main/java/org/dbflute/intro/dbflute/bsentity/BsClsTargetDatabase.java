/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.dbflute.bsentity;

import java.util.List;
import java.util.ArrayList;

import org.dbflute.dbmeta.DBMeta;
import org.dbflute.dbmeta.AbstractEntity;
import org.dbflute.dbmeta.accessory.DomainEntity;
import org.dbflute.intro.dbflute.allcommon.DBMetaInstanceHandler;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.exentity.*;

/**
 * The entity of CLS_TARGET_DATABASE as TABLE. <br>
 * <pre>
 * [primary-key]
 *     DATABASE_CODE
 *
 * [column]
 *     DATABASE_CODE, DATABASE_NAME, JDBC_DRIVER_FQCN, URL_TEMPLATE, DEFAULT_SCHEMA, SCHEMA_REQUIRED_FLG, SCHEMA_UPPER_CASE_FLG, USER_INPUT_ASSIST_FLG, EMBEDDED_JAR_FLG, DISPLAY_ORDER
 *
 * [sequence]
 *     
 *
 * [identity]
 *     
 *
 * [version-no]
 *     
 *
 * [foreign table]
 *     
 *
 * [referrer table]
 *     
 *
 * [foreign property]
 *     
 *
 * [referrer property]
 *     
 *
 * [get/set template]
 * /= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
 * String databaseCode = entity.getDatabaseCode();
 * String databaseName = entity.getDatabaseName();
 * String jdbcDriverFqcn = entity.getJdbcDriverFqcn();
 * String urlTemplate = entity.getUrlTemplate();
 * String defaultSchema = entity.getDefaultSchema();
 * Integer schemaRequiredFlg = entity.getSchemaRequiredFlg();
 * Integer schemaUpperCaseFlg = entity.getSchemaUpperCaseFlg();
 * Integer userInputAssistFlg = entity.getUserInputAssistFlg();
 * Integer embeddedJarFlg = entity.getEmbeddedJarFlg();
 * Integer displayOrder = entity.getDisplayOrder();
 * entity.setDatabaseCode(databaseCode);
 * entity.setDatabaseName(databaseName);
 * entity.setJdbcDriverFqcn(jdbcDriverFqcn);
 * entity.setUrlTemplate(urlTemplate);
 * entity.setDefaultSchema(defaultSchema);
 * entity.setSchemaRequiredFlg(schemaRequiredFlg);
 * entity.setSchemaUpperCaseFlg(schemaUpperCaseFlg);
 * entity.setUserInputAssistFlg(userInputAssistFlg);
 * entity.setEmbeddedJarFlg(embeddedJarFlg);
 * entity.setDisplayOrder(displayOrder);
 * = = = = = = = = = =/
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsClsTargetDatabase extends AbstractEntity implements DomainEntity {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} */
    protected String _databaseCode;

    /** DATABASE_NAME: {NotNull, VARCHAR(100)} */
    protected String _databaseName;

    /** JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)} */
    protected String _jdbcDriverFqcn;

    /** URL_TEMPLATE: {NotNull, VARCHAR(100)} */
    protected String _urlTemplate;

    /** DEFAULT_SCHEMA: {VARCHAR(10)} */
    protected String _defaultSchema;

    /** SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} */
    protected Integer _schemaRequiredFlg;

    /** SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} */
    protected Integer _schemaUpperCaseFlg;

    /** USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} */
    protected Integer _userInputAssistFlg;

    /** EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} */
    protected Integer _embeddedJarFlg;

    /** DISPLAY_ORDER: {NotNull, INTEGER(10)} */
    protected Integer _displayOrder;

    // ===================================================================================
    //                                                                             DB Meta
    //                                                                             =======
    /** {@inheritDoc} */
    public DBMeta asDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(asTableDbName());
    }

    /** {@inheritDoc} */
    public String asTableDbName() {
        return "CLS_TARGET_DATABASE";
    }

    // ===================================================================================
    //                                                                        Key Handling
    //                                                                        ============
    /** {@inheritDoc} */
    public boolean hasPrimaryKeyValue() {
        if (_databaseCode == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Get the value of databaseCode as the classification of TargetDatabase. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.TargetDatabase getDatabaseCodeAsTargetDatabase() {
        return CDef.TargetDatabase.codeOf(getDatabaseCode());
    }

    /**
     * Set the value of databaseCode as the classification of TargetDatabase. <br>
     * DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase cdef) {
        setDatabaseCode(cdef != null ? cdef.code() : null);
    }

    /**
     * Get the value of schemaRequiredFlg as the classification of Flg. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.Flg getSchemaRequiredFlgAsFlg() {
        return CDef.Flg.codeOf(getSchemaRequiredFlg());
    }

    /**
     * Set the value of schemaRequiredFlg as the classification of Flg. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setSchemaRequiredFlgAsFlg(CDef.Flg cdef) {
        setSchemaRequiredFlg(cdef != null ? toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Set the value of schemaRequiredFlg as boolean. <br>
     * SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (NullAllowed: if null, null value is set to the column)
     */
    public void setSchemaRequiredFlgAsBoolean(Boolean determination) {
        setSchemaRequiredFlgAsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Get the value of schemaUpperCaseFlg as the classification of Flg. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.Flg getSchemaUpperCaseFlgAsFlg() {
        return CDef.Flg.codeOf(getSchemaUpperCaseFlg());
    }

    /**
     * Set the value of schemaUpperCaseFlg as the classification of Flg. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setSchemaUpperCaseFlgAsFlg(CDef.Flg cdef) {
        setSchemaUpperCaseFlg(cdef != null ? toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Set the value of schemaUpperCaseFlg as boolean. <br>
     * SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (NullAllowed: if null, null value is set to the column)
     */
    public void setSchemaUpperCaseFlgAsBoolean(Boolean determination) {
        setSchemaUpperCaseFlgAsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Get the value of userInputAssistFlg as the classification of Flg. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.Flg getUserInputAssistFlgAsFlg() {
        return CDef.Flg.codeOf(getUserInputAssistFlg());
    }

    /**
     * Set the value of userInputAssistFlg as the classification of Flg. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setUserInputAssistFlgAsFlg(CDef.Flg cdef) {
        setUserInputAssistFlg(cdef != null ? toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Set the value of userInputAssistFlg as boolean. <br>
     * USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (NullAllowed: if null, null value is set to the column)
     */
    public void setUserInputAssistFlgAsBoolean(Boolean determination) {
        setUserInputAssistFlgAsFlg(CDef.Flg.codeOf(determination));
    }

    /**
     * Get the value of embeddedJarFlg as the classification of Flg. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.Flg getEmbeddedJarFlgAsFlg() {
        return CDef.Flg.codeOf(getEmbeddedJarFlg());
    }

    /**
     * Set the value of embeddedJarFlg as the classification of Flg. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setEmbeddedJarFlgAsFlg(CDef.Flg cdef) {
        setEmbeddedJarFlg(cdef != null ? toNumber(cdef.code(), Integer.class) : null);
    }

    /**
     * Set the value of embeddedJarFlg as boolean. <br>
     * EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * general boolean classification for every flg-column
     * @param determination The determination, true or false. (NullAllowed: if null, null value is set to the column)
     */
    public void setEmbeddedJarFlgAsBoolean(Boolean determination) {
        setEmbeddedJarFlgAsFlg(CDef.Flg.codeOf(determination));
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of databaseCode as MySQL (mysql). <br>
     * MySQL
     */
    public void setDatabaseCode_MySQL() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.MySQL);
    }

    /**
     * Set the value of databaseCode as PostgreSQL (postgresql). <br>
     * PostgreSQL
     */
    public void setDatabaseCode_PostgreSQL() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.PostgreSQL);
    }

    /**
     * Set the value of databaseCode as Oracle (oracle). <br>
     * Oracle
     */
    public void setDatabaseCode_Oracle() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.Oracle);
    }

    /**
     * Set the value of databaseCode as Db2 (db2). <br>
     * DB2
     */
    public void setDatabaseCode_Db2() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.Db2);
    }

    /**
     * Set the value of databaseCode as SQLServer (sqlserver). <br>
     * SQLServer
     */
    public void setDatabaseCode_SQLServer() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.SQLServer);
    }

    /**
     * Set the value of databaseCode as H2Database (h2). <br>
     * H2 Database
     */
    public void setDatabaseCode_H2Database() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.H2Database);
    }

    /**
     * Set the value of databaseCode as ApacheDerby (derby). <br>
     * Apache Derby
     */
    public void setDatabaseCode_ApacheDerby() {
        setDatabaseCodeAsTargetDatabase(CDef.TargetDatabase.ApacheDerby);
    }

    /**
     * Set the value of schemaRequiredFlg as True (1). <br>
     * Checked: means yes
     */
    public void setSchemaRequiredFlg_True() {
        setSchemaRequiredFlgAsFlg(CDef.Flg.True);
    }

    /**
     * Set the value of schemaRequiredFlg as False (0). <br>
     * Unchecked: means no
     */
    public void setSchemaRequiredFlg_False() {
        setSchemaRequiredFlgAsFlg(CDef.Flg.False);
    }

    /**
     * Set the value of schemaUpperCaseFlg as True (1). <br>
     * Checked: means yes
     */
    public void setSchemaUpperCaseFlg_True() {
        setSchemaUpperCaseFlgAsFlg(CDef.Flg.True);
    }

    /**
     * Set the value of schemaUpperCaseFlg as False (0). <br>
     * Unchecked: means no
     */
    public void setSchemaUpperCaseFlg_False() {
        setSchemaUpperCaseFlgAsFlg(CDef.Flg.False);
    }

    /**
     * Set the value of userInputAssistFlg as True (1). <br>
     * Checked: means yes
     */
    public void setUserInputAssistFlg_True() {
        setUserInputAssistFlgAsFlg(CDef.Flg.True);
    }

    /**
     * Set the value of userInputAssistFlg as False (0). <br>
     * Unchecked: means no
     */
    public void setUserInputAssistFlg_False() {
        setUserInputAssistFlgAsFlg(CDef.Flg.False);
    }

    /**
     * Set the value of embeddedJarFlg as True (1). <br>
     * Checked: means yes
     */
    public void setEmbeddedJarFlg_True() {
        setEmbeddedJarFlgAsFlg(CDef.Flg.True);
    }

    /**
     * Set the value of embeddedJarFlg as False (0). <br>
     * Unchecked: means no
     */
    public void setEmbeddedJarFlg_False() {
        setEmbeddedJarFlgAsFlg(CDef.Flg.False);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of databaseCode MySQL? <br>
     * MySQL
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeMySQL() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.MySQL) : false;
    }

    /**
     * Is the value of databaseCode PostgreSQL? <br>
     * PostgreSQL
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodePostgreSQL() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.PostgreSQL) : false;
    }

    /**
     * Is the value of databaseCode Oracle? <br>
     * Oracle
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeOracle() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.Oracle) : false;
    }

    /**
     * Is the value of databaseCode Db2? <br>
     * DB2
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeDb2() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.Db2) : false;
    }

    /**
     * Is the value of databaseCode SQLServer? <br>
     * SQLServer
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeSQLServer() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.SQLServer) : false;
    }

    /**
     * Is the value of databaseCode H2Database? <br>
     * H2 Database
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeH2Database() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.H2Database) : false;
    }

    /**
     * Is the value of databaseCode ApacheDerby? <br>
     * Apache Derby
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isDatabaseCodeApacheDerby() {
        CDef.TargetDatabase cdef = getDatabaseCodeAsTargetDatabase();
        return cdef != null ? cdef.equals(CDef.TargetDatabase.ApacheDerby) : false;
    }

    /**
     * Is the value of schemaRequiredFlg True? <br>
     * Checked: means yes
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isSchemaRequiredFlgTrue() {
        CDef.Flg cdef = getSchemaRequiredFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.True) : false;
    }

    /**
     * Is the value of schemaRequiredFlg False? <br>
     * Unchecked: means no
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isSchemaRequiredFlgFalse() {
        CDef.Flg cdef = getSchemaRequiredFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.False) : false;
    }

    /**
     * Is the value of schemaUpperCaseFlg True? <br>
     * Checked: means yes
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isSchemaUpperCaseFlgTrue() {
        CDef.Flg cdef = getSchemaUpperCaseFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.True) : false;
    }

    /**
     * Is the value of schemaUpperCaseFlg False? <br>
     * Unchecked: means no
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isSchemaUpperCaseFlgFalse() {
        CDef.Flg cdef = getSchemaUpperCaseFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.False) : false;
    }

    /**
     * Is the value of userInputAssistFlg True? <br>
     * Checked: means yes
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isUserInputAssistFlgTrue() {
        CDef.Flg cdef = getUserInputAssistFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.True) : false;
    }

    /**
     * Is the value of userInputAssistFlg False? <br>
     * Unchecked: means no
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isUserInputAssistFlgFalse() {
        CDef.Flg cdef = getUserInputAssistFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.False) : false;
    }

    /**
     * Is the value of embeddedJarFlg True? <br>
     * Checked: means yes
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isEmbeddedJarFlgTrue() {
        CDef.Flg cdef = getEmbeddedJarFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.True) : false;
    }

    /**
     * Is the value of embeddedJarFlg False? <br>
     * Unchecked: means no
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isEmbeddedJarFlgFalse() {
        CDef.Flg cdef = getEmbeddedJarFlgAsFlg();
        return cdef != null ? cdef.equals(CDef.Flg.False) : false;
    }

    // ===================================================================================
    //                                                           Classification Name/Alias
    //                                                           =========================
    /**
     * Get the value of the column 'schemaRequiredFlg' as classification name.
     * @return The string of classification name. (NullAllowed: when the column value is null)
     */
    public String getSchemaRequiredFlgName() {
        CDef.Flg cdef = getSchemaRequiredFlgAsFlg();
        return cdef != null ? cdef.name() : null;
    }

    /**
     * Get the value of the column 'schemaRequiredFlg' as classification alias.
     * @return The string of classification alias. (NullAllowed: when the column value is null)
     */
    public String getSchemaRequiredFlgAlias() {
        CDef.Flg cdef = getSchemaRequiredFlgAsFlg();
        return cdef != null ? cdef.alias() : null;
    }

    /**
     * Get the value of the column 'schemaUpperCaseFlg' as classification name.
     * @return The string of classification name. (NullAllowed: when the column value is null)
     */
    public String getSchemaUpperCaseFlgName() {
        CDef.Flg cdef = getSchemaUpperCaseFlgAsFlg();
        return cdef != null ? cdef.name() : null;
    }

    /**
     * Get the value of the column 'schemaUpperCaseFlg' as classification alias.
     * @return The string of classification alias. (NullAllowed: when the column value is null)
     */
    public String getSchemaUpperCaseFlgAlias() {
        CDef.Flg cdef = getSchemaUpperCaseFlgAsFlg();
        return cdef != null ? cdef.alias() : null;
    }

    /**
     * Get the value of the column 'userInputAssistFlg' as classification name.
     * @return The string of classification name. (NullAllowed: when the column value is null)
     */
    public String getUserInputAssistFlgName() {
        CDef.Flg cdef = getUserInputAssistFlgAsFlg();
        return cdef != null ? cdef.name() : null;
    }

    /**
     * Get the value of the column 'userInputAssistFlg' as classification alias.
     * @return The string of classification alias. (NullAllowed: when the column value is null)
     */
    public String getUserInputAssistFlgAlias() {
        CDef.Flg cdef = getUserInputAssistFlgAsFlg();
        return cdef != null ? cdef.alias() : null;
    }

    /**
     * Get the value of the column 'embeddedJarFlg' as classification name.
     * @return The string of classification name. (NullAllowed: when the column value is null)
     */
    public String getEmbeddedJarFlgName() {
        CDef.Flg cdef = getEmbeddedJarFlgAsFlg();
        return cdef != null ? cdef.name() : null;
    }

    /**
     * Get the value of the column 'embeddedJarFlg' as classification alias.
     * @return The string of classification alias. (NullAllowed: when the column value is null)
     */
    public String getEmbeddedJarFlgAlias() {
        CDef.Flg cdef = getEmbeddedJarFlgAsFlg();
        return cdef != null ? cdef.alias() : null;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    protected <ELEMENT> List<ELEMENT> newReferrerList() { // overriding to import
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    protected boolean doEquals(Object obj) {
        if (obj instanceof BsClsTargetDatabase) {
            BsClsTargetDatabase other = (BsClsTargetDatabase)obj;
            if (!xSV(_databaseCode, other._databaseCode)) { return false; }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int doHashCode(int initial) {
        int hs = initial;
        hs = xCH(hs, asTableDbName());
        hs = xCH(hs, _databaseCode);
        return hs;
    }

    @Override
    protected String doBuildStringWithRelation(String li) {
        return "";
    }

    @Override
    protected String doBuildColumnString(String dm) {
        StringBuilder sb = new StringBuilder();
        sb.append(dm).append(xfND(_databaseCode));
        sb.append(dm).append(xfND(_databaseName));
        sb.append(dm).append(xfND(_jdbcDriverFqcn));
        sb.append(dm).append(xfND(_urlTemplate));
        sb.append(dm).append(xfND(_defaultSchema));
        sb.append(dm).append(xfND(_schemaRequiredFlg));
        sb.append(dm).append(xfND(_schemaUpperCaseFlg));
        sb.append(dm).append(xfND(_userInputAssistFlg));
        sb.append(dm).append(xfND(_embeddedJarFlg));
        sb.append(dm).append(xfND(_displayOrder));
        if (sb.length() > dm.length()) {
            sb.delete(0, dm.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    @Override
    protected String doBuildRelationString(String dm) {
        return "";
    }

    @Override
    public ClsTargetDatabase clone() {
        return (ClsTargetDatabase)super.clone();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * @return The value of the column 'DATABASE_CODE'. (basically NotNull if selected: for the constraint)
     */
    public String getDatabaseCode() {
        checkSpecifiedProperty("databaseCode");
        return convertEmptyToNull(_databaseCode);
    }

    /**
     * [set] DATABASE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetDatabase} <br>
     * @param databaseCode The value of the column 'DATABASE_CODE'. (basically NotNull if update: for the constraint)
     */
    protected void setDatabaseCode(String databaseCode) {
        checkClassificationCode("DATABASE_CODE", CDef.DefMeta.TargetDatabase, databaseCode);
        registerModifiedProperty("databaseCode");
        _databaseCode = databaseCode;
    }

    /**
     * [get] DATABASE_NAME: {NotNull, VARCHAR(100)} <br>
     * @return The value of the column 'DATABASE_NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getDatabaseName() {
        checkSpecifiedProperty("databaseName");
        return convertEmptyToNull(_databaseName);
    }

    /**
     * [set] DATABASE_NAME: {NotNull, VARCHAR(100)} <br>
     * @param databaseName The value of the column 'DATABASE_NAME'. (basically NotNull if update: for the constraint)
     */
    public void setDatabaseName(String databaseName) {
        registerModifiedProperty("databaseName");
        _databaseName = databaseName;
    }

    /**
     * [get] JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)} <br>
     * @return The value of the column 'JDBC_DRIVER_FQCN'. (basically NotNull if selected: for the constraint)
     */
    public String getJdbcDriverFqcn() {
        checkSpecifiedProperty("jdbcDriverFqcn");
        return convertEmptyToNull(_jdbcDriverFqcn);
    }

    /**
     * [set] JDBC_DRIVER_FQCN: {NotNull, VARCHAR(100)} <br>
     * @param jdbcDriverFqcn The value of the column 'JDBC_DRIVER_FQCN'. (basically NotNull if update: for the constraint)
     */
    public void setJdbcDriverFqcn(String jdbcDriverFqcn) {
        registerModifiedProperty("jdbcDriverFqcn");
        _jdbcDriverFqcn = jdbcDriverFqcn;
    }

    /**
     * [get] URL_TEMPLATE: {NotNull, VARCHAR(100)} <br>
     * @return The value of the column 'URL_TEMPLATE'. (basically NotNull if selected: for the constraint)
     */
    public String getUrlTemplate() {
        checkSpecifiedProperty("urlTemplate");
        return convertEmptyToNull(_urlTemplate);
    }

    /**
     * [set] URL_TEMPLATE: {NotNull, VARCHAR(100)} <br>
     * @param urlTemplate The value of the column 'URL_TEMPLATE'. (basically NotNull if update: for the constraint)
     */
    public void setUrlTemplate(String urlTemplate) {
        registerModifiedProperty("urlTemplate");
        _urlTemplate = urlTemplate;
    }

    /**
     * [get] DEFAULT_SCHEMA: {VARCHAR(10)} <br>
     * @return The value of the column 'DEFAULT_SCHEMA'. (NullAllowed even if selected: for no constraint)
     */
    public String getDefaultSchema() {
        checkSpecifiedProperty("defaultSchema");
        return convertEmptyToNull(_defaultSchema);
    }

    /**
     * [set] DEFAULT_SCHEMA: {VARCHAR(10)} <br>
     * @param defaultSchema The value of the column 'DEFAULT_SCHEMA'. (NullAllowed: null update allowed for no constraint)
     */
    public void setDefaultSchema(String defaultSchema) {
        registerModifiedProperty("defaultSchema");
        _defaultSchema = defaultSchema;
    }

    /**
     * [get] SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @return The value of the column 'SCHEMA_REQUIRED_FLG'. (basically NotNull if selected: for the constraint)
     */
    public Integer getSchemaRequiredFlg() {
        checkSpecifiedProperty("schemaRequiredFlg");
        return _schemaRequiredFlg;
    }

    /**
     * [set] SCHEMA_REQUIRED_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @param schemaRequiredFlg The value of the column 'SCHEMA_REQUIRED_FLG'. (basically NotNull if update: for the constraint)
     */
    protected void setSchemaRequiredFlg(Integer schemaRequiredFlg) {
        checkClassificationCode("SCHEMA_REQUIRED_FLG", CDef.DefMeta.Flg, schemaRequiredFlg);
        registerModifiedProperty("schemaRequiredFlg");
        _schemaRequiredFlg = schemaRequiredFlg;
    }

    /**
     * [get] SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @return The value of the column 'SCHEMA_UPPER_CASE_FLG'. (basically NotNull if selected: for the constraint)
     */
    public Integer getSchemaUpperCaseFlg() {
        checkSpecifiedProperty("schemaUpperCaseFlg");
        return _schemaUpperCaseFlg;
    }

    /**
     * [set] SCHEMA_UPPER_CASE_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @param schemaUpperCaseFlg The value of the column 'SCHEMA_UPPER_CASE_FLG'. (basically NotNull if update: for the constraint)
     */
    protected void setSchemaUpperCaseFlg(Integer schemaUpperCaseFlg) {
        checkClassificationCode("SCHEMA_UPPER_CASE_FLG", CDef.DefMeta.Flg, schemaUpperCaseFlg);
        registerModifiedProperty("schemaUpperCaseFlg");
        _schemaUpperCaseFlg = schemaUpperCaseFlg;
    }

    /**
     * [get] USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @return The value of the column 'USER_INPUT_ASSIST_FLG'. (basically NotNull if selected: for the constraint)
     */
    public Integer getUserInputAssistFlg() {
        checkSpecifiedProperty("userInputAssistFlg");
        return _userInputAssistFlg;
    }

    /**
     * [set] USER_INPUT_ASSIST_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @param userInputAssistFlg The value of the column 'USER_INPUT_ASSIST_FLG'. (basically NotNull if update: for the constraint)
     */
    protected void setUserInputAssistFlg(Integer userInputAssistFlg) {
        checkClassificationCode("USER_INPUT_ASSIST_FLG", CDef.DefMeta.Flg, userInputAssistFlg);
        registerModifiedProperty("userInputAssistFlg");
        _userInputAssistFlg = userInputAssistFlg;
    }

    /**
     * [get] EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @return The value of the column 'EMBEDDED_JAR_FLG'. (basically NotNull if selected: for the constraint)
     */
    public Integer getEmbeddedJarFlg() {
        checkSpecifiedProperty("embeddedJarFlg");
        return _embeddedJarFlg;
    }

    /**
     * [set] EMBEDDED_JAR_FLG: {NotNull, INTEGER(10), classification=Flg} <br>
     * @param embeddedJarFlg The value of the column 'EMBEDDED_JAR_FLG'. (basically NotNull if update: for the constraint)
     */
    protected void setEmbeddedJarFlg(Integer embeddedJarFlg) {
        checkClassificationCode("EMBEDDED_JAR_FLG", CDef.DefMeta.Flg, embeddedJarFlg);
        registerModifiedProperty("embeddedJarFlg");
        _embeddedJarFlg = embeddedJarFlg;
    }

    /**
     * [get] DISPLAY_ORDER: {NotNull, INTEGER(10)} <br>
     * @return The value of the column 'DISPLAY_ORDER'. (basically NotNull if selected: for the constraint)
     */
    public Integer getDisplayOrder() {
        checkSpecifiedProperty("displayOrder");
        return _displayOrder;
    }

    /**
     * [set] DISPLAY_ORDER: {NotNull, INTEGER(10)} <br>
     * @param displayOrder The value of the column 'DISPLAY_ORDER'. (basically NotNull if update: for the constraint)
     */
    public void setDisplayOrder(Integer displayOrder) {
        registerModifiedProperty("displayOrder");
        _displayOrder = displayOrder;
    }

    /**
     * For framework so basically DON'T use this method.
     * @param databaseCode The value of the column 'DATABASE_CODE'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingDatabaseCode(String databaseCode) {
        setDatabaseCode(databaseCode);
    }

    /**
     * For framework so basically DON'T use this method.
     * @param schemaRequiredFlg The value of the column 'SCHEMA_REQUIRED_FLG'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingSchemaRequiredFlg(Integer schemaRequiredFlg) {
        setSchemaRequiredFlg(schemaRequiredFlg);
    }

    /**
     * For framework so basically DON'T use this method.
     * @param schemaUpperCaseFlg The value of the column 'SCHEMA_UPPER_CASE_FLG'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingSchemaUpperCaseFlg(Integer schemaUpperCaseFlg) {
        setSchemaUpperCaseFlg(schemaUpperCaseFlg);
    }

    /**
     * For framework so basically DON'T use this method.
     * @param userInputAssistFlg The value of the column 'USER_INPUT_ASSIST_FLG'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingUserInputAssistFlg(Integer userInputAssistFlg) {
        setUserInputAssistFlg(userInputAssistFlg);
    }

    /**
     * For framework so basically DON'T use this method.
     * @param embeddedJarFlg The value of the column 'EMBEDDED_JAR_FLG'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingEmbeddedJarFlg(Integer embeddedJarFlg) {
        setEmbeddedJarFlg(embeddedJarFlg);
    }
}
