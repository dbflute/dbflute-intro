package org.dbflute.intro.app.model.client.document;

/**
 * @author deco
 */
public class LittleAdjustmentMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public final boolean isTableDispNameUpperCase;
    public final boolean isTableSqlNameUpperCase;
    public final boolean isColumnSqlNameUpperCase;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public LittleAdjustmentMap(boolean isUpperCaseBasic) {
        this.isTableDispNameUpperCase = isUpperCaseBasic;
        this.isTableSqlNameUpperCase = isUpperCaseBasic;
        this.isColumnSqlNameUpperCase = isUpperCaseBasic;
    }

    public LittleAdjustmentMap(boolean isTableDispNameUpperCase, boolean isTableSqlNameUpperCase, boolean isColumnSqlNameUpperCase) {
        this.isTableDispNameUpperCase = isTableDispNameUpperCase;
        this.isTableSqlNameUpperCase = isTableSqlNameUpperCase;
        this.isColumnSqlNameUpperCase = isColumnSqlNameUpperCase;
    }
}
