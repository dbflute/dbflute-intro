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
 * The entity of CLS_TARGET_CONTAINER as TABLE. <br>
 * <pre>
 * [primary-key]
 *     CONTAINER_CODE
 *
 * [column]
 *     CONTAINER_CODE, CONTAINER_NAME, DISPLAY_ORDER
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
 * String containerCode = entity.getContainerCode();
 * String containerName = entity.getContainerName();
 * Integer displayOrder = entity.getDisplayOrder();
 * entity.setContainerCode(containerCode);
 * entity.setContainerName(containerName);
 * entity.setDisplayOrder(displayOrder);
 * = = = = = = = = = =/
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsClsTargetContainer extends AbstractEntity implements DomainEntity {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} */
    protected String _containerCode;

    /** CONTAINER_NAME: {NotNull, VARCHAR(100)} */
    protected String _containerName;

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
        return "CLS_TARGET_CONTAINER";
    }

    // ===================================================================================
    //                                                                        Key Handling
    //                                                                        ============
    /** {@inheritDoc} */
    public boolean hasPrimaryKeyValue() {
        if (_containerCode == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Get the value of containerCode as the classification of TargetContainer. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.TargetContainer getContainerCodeAsTargetContainer() {
        return CDef.TargetContainer.codeOf(getContainerCode());
    }

    /**
     * Set the value of containerCode as the classification of TargetContainer. <br>
     * CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * containers DBFlute cau use
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setContainerCodeAsTargetContainer(CDef.TargetContainer cdef) {
        setContainerCode(cdef != null ? cdef.code() : null);
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of containerCode as LastaDi (lasta_di). <br>
     * Lasta Di
     */
    public void setContainerCode_LastaDi() {
        setContainerCodeAsTargetContainer(CDef.TargetContainer.LastaDi);
    }

    /**
     * Set the value of containerCode as SpringFramework (spring). <br>
     * Spring Framework
     */
    public void setContainerCode_SpringFramework() {
        setContainerCodeAsTargetContainer(CDef.TargetContainer.SpringFramework);
    }

    /**
     * Set the value of containerCode as GoogleGuice (guice). <br>
     * Google Guice
     */
    public void setContainerCode_GoogleGuice() {
        setContainerCodeAsTargetContainer(CDef.TargetContainer.GoogleGuice);
    }

    /**
     * Set the value of containerCode as SeasarS2Container (seasar). <br>
     * Seasar (S2Container)
     */
    public void setContainerCode_SeasarS2Container() {
        setContainerCodeAsTargetContainer(CDef.TargetContainer.SeasarS2Container);
    }

    /**
     * Set the value of containerCode as Cdi (cdi). <br>
     * CDI
     */
    public void setContainerCode_Cdi() {
        setContainerCodeAsTargetContainer(CDef.TargetContainer.Cdi);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of containerCode LastaDi? <br>
     * Lasta Di
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isContainerCodeLastaDi() {
        CDef.TargetContainer cdef = getContainerCodeAsTargetContainer();
        return cdef != null ? cdef.equals(CDef.TargetContainer.LastaDi) : false;
    }

    /**
     * Is the value of containerCode SpringFramework? <br>
     * Spring Framework
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isContainerCodeSpringFramework() {
        CDef.TargetContainer cdef = getContainerCodeAsTargetContainer();
        return cdef != null ? cdef.equals(CDef.TargetContainer.SpringFramework) : false;
    }

    /**
     * Is the value of containerCode GoogleGuice? <br>
     * Google Guice
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isContainerCodeGoogleGuice() {
        CDef.TargetContainer cdef = getContainerCodeAsTargetContainer();
        return cdef != null ? cdef.equals(CDef.TargetContainer.GoogleGuice) : false;
    }

    /**
     * Is the value of containerCode SeasarS2Container? <br>
     * Seasar (S2Container)
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isContainerCodeSeasarS2Container() {
        CDef.TargetContainer cdef = getContainerCodeAsTargetContainer();
        return cdef != null ? cdef.equals(CDef.TargetContainer.SeasarS2Container) : false;
    }

    /**
     * Is the value of containerCode Cdi? <br>
     * CDI
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isContainerCodeCdi() {
        CDef.TargetContainer cdef = getContainerCodeAsTargetContainer();
        return cdef != null ? cdef.equals(CDef.TargetContainer.Cdi) : false;
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
        if (obj instanceof BsClsTargetContainer) {
            BsClsTargetContainer other = (BsClsTargetContainer)obj;
            if (!xSV(_containerCode, other._containerCode)) { return false; }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int doHashCode(int initial) {
        int hs = initial;
        hs = xCH(hs, asTableDbName());
        hs = xCH(hs, _containerCode);
        return hs;
    }

    @Override
    protected String doBuildStringWithRelation(String li) {
        return "";
    }

    @Override
    protected String doBuildColumnString(String dm) {
        StringBuilder sb = new StringBuilder();
        sb.append(dm).append(xfND(_containerCode));
        sb.append(dm).append(xfND(_containerName));
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
    public ClsTargetContainer clone() {
        return (ClsTargetContainer)super.clone();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * @return The value of the column 'CONTAINER_CODE'. (basically NotNull if selected: for the constraint)
     */
    public String getContainerCode() {
        checkSpecifiedProperty("containerCode");
        return convertEmptyToNull(_containerCode);
    }

    /**
     * [set] CONTAINER_CODE: {PK, NotNull, VARCHAR(10), classification=TargetContainer} <br>
     * @param containerCode The value of the column 'CONTAINER_CODE'. (basically NotNull if update: for the constraint)
     */
    protected void setContainerCode(String containerCode) {
        checkClassificationCode("CONTAINER_CODE", CDef.DefMeta.TargetContainer, containerCode);
        registerModifiedProperty("containerCode");
        _containerCode = containerCode;
    }

    /**
     * [get] CONTAINER_NAME: {NotNull, VARCHAR(100)} <br>
     * @return The value of the column 'CONTAINER_NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getContainerName() {
        checkSpecifiedProperty("containerName");
        return convertEmptyToNull(_containerName);
    }

    /**
     * [set] CONTAINER_NAME: {NotNull, VARCHAR(100)} <br>
     * @param containerName The value of the column 'CONTAINER_NAME'. (basically NotNull if update: for the constraint)
     */
    public void setContainerName(String containerName) {
        registerModifiedProperty("containerName");
        _containerName = containerName;
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
     * @param containerCode The value of the column 'CONTAINER_CODE'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingContainerCode(String containerCode) {
        setContainerCode(containerCode);
    }
}
