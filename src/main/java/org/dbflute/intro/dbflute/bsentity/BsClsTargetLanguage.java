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
 * The entity of CLS_TARGET_LANGUAGE as TABLE. <br>
 * <pre>
 * [primary-key]
 *     LANGUAGE_CODE
 *
 * [column]
 *     LANGUAGE_CODE, LANGUAGE_NAME, DISPLAY_ORDER
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
 * String languageCode = entity.getLanguageCode();
 * String languageName = entity.getLanguageName();
 * Integer displayOrder = entity.getDisplayOrder();
 * entity.setLanguageCode(languageCode);
 * entity.setLanguageName(languageName);
 * entity.setDisplayOrder(displayOrder);
 * = = = = = = = = = =/
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsClsTargetLanguage extends AbstractEntity implements DomainEntity {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} */
    protected String _languageCode;

    /** LANGUAGE_NAME: {NotNull, VARCHAR(100)} */
    protected String _languageName;

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
        return "CLS_TARGET_LANGUAGE";
    }

    // ===================================================================================
    //                                                                        Key Handling
    //                                                                        ============
    /** {@inheritDoc} */
    public boolean hasPrimaryKeyValue() {
        if (_languageCode == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Get the value of languageCode as the classification of TargetLanguage. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.TargetLanguage getLanguageCodeAsTargetLanguage() {
        return CDef.TargetLanguage.codeOf(getLanguageCode());
    }

    /**
     * Set the value of languageCode as the classification of TargetLanguage. <br>
     * LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * databases DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setLanguageCodeAsTargetLanguage(CDef.TargetLanguage cdef) {
        setLanguageCode(cdef != null ? cdef.code() : null);
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of languageCode as Java (java). <br>
     * Java
     */
    public void setLanguageCode_Java() {
        setLanguageCodeAsTargetLanguage(CDef.TargetLanguage.Java);
    }

    /**
     * Set the value of languageCode as C (csharp). <br>
     * C#
     */
    public void setLanguageCode_C() {
        setLanguageCodeAsTargetLanguage(CDef.TargetLanguage.C);
    }

    /**
     * Set the value of languageCode as Scala (scala). <br>
     * Scala
     */
    public void setLanguageCode_Scala() {
        setLanguageCodeAsTargetLanguage(CDef.TargetLanguage.Scala);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of languageCode Java? <br>
     * Java
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isLanguageCodeJava() {
        CDef.TargetLanguage cdef = getLanguageCodeAsTargetLanguage();
        return cdef != null ? cdef.equals(CDef.TargetLanguage.Java) : false;
    }

    /**
     * Is the value of languageCode C? <br>
     * C#
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isLanguageCodeC() {
        CDef.TargetLanguage cdef = getLanguageCodeAsTargetLanguage();
        return cdef != null ? cdef.equals(CDef.TargetLanguage.C) : false;
    }

    /**
     * Is the value of languageCode Scala? <br>
     * Scala
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isLanguageCodeScala() {
        CDef.TargetLanguage cdef = getLanguageCodeAsTargetLanguage();
        return cdef != null ? cdef.equals(CDef.TargetLanguage.Scala) : false;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    protected <ELEMENT> List<ELEMENT> newReferrerList() {
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    protected boolean doEquals(Object obj) {
        if (obj instanceof BsClsTargetLanguage) {
            BsClsTargetLanguage other = (BsClsTargetLanguage)obj;
            if (!xSV(_languageCode, other._languageCode)) { return false; }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int doHashCode(int initial) {
        int hs = initial;
        hs = xCH(hs, asTableDbName());
        hs = xCH(hs, _languageCode);
        return hs;
    }

    @Override
    protected String doBuildStringWithRelation(String li) {
        return "";
    }

    @Override
    protected String doBuildColumnString(String dm) {
        StringBuilder sb = new StringBuilder();
        sb.append(dm).append(xfND(_languageCode));
        sb.append(dm).append(xfND(_languageName));
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
    public ClsTargetLanguage clone() {
        return (ClsTargetLanguage)super.clone();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * @return The value of the column 'LANGUAGE_CODE'. (basically NotNull if selected: for the constraint)
     */
    public String getLanguageCode() {
        checkSpecifiedProperty("languageCode");
        return convertEmptyToNull(_languageCode);
    }

    /**
     * [set] LANGUAGE_CODE: {PK, NotNull, VARCHAR(10), classification=TargetLanguage} <br>
     * @param languageCode The value of the column 'LANGUAGE_CODE'. (basically NotNull if update: for the constraint)
     */
    protected void setLanguageCode(String languageCode) {
        checkClassificationCode("LANGUAGE_CODE", CDef.DefMeta.TargetLanguage, languageCode);
        registerModifiedProperty("languageCode");
        _languageCode = languageCode;
    }

    /**
     * [get] LANGUAGE_NAME: {NotNull, VARCHAR(100)} <br>
     * @return The value of the column 'LANGUAGE_NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getLanguageName() {
        checkSpecifiedProperty("languageName");
        return convertEmptyToNull(_languageName);
    }

    /**
     * [set] LANGUAGE_NAME: {NotNull, VARCHAR(100)} <br>
     * @param languageName The value of the column 'LANGUAGE_NAME'. (basically NotNull if update: for the constraint)
     */
    public void setLanguageName(String languageName) {
        registerModifiedProperty("languageName");
        _languageName = languageName;
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
     * @param languageCode The value of the column 'LANGUAGE_CODE'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingLanguageCode(String languageCode) {
        setLanguageCode(languageCode);
    }
}
