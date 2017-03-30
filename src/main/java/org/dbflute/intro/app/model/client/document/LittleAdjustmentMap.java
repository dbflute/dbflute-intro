package org.dbflute.intro.app.model.client.document;

/**
 * @author deco
 */
public class LittleAdjustmentMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public final Boolean isTableDispNameUpperCase;
    public final Boolean isTableSqlNameUpperCase;
    public final Boolean isColumnSqlNameUpperCase;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    // TODO deco factory method by jflute (2017/03/30)
    public LittleAdjustmentMap(Boolean isUpperCaseBasic) {
        this.isTableDispNameUpperCase = isUpperCaseBasic;
        this.isTableSqlNameUpperCase = isUpperCaseBasic;
        this.isColumnSqlNameUpperCase = isUpperCaseBasic;
    }

    public LittleAdjustmentMap(Boolean isTableDispNameUpperCase, Boolean isTableSqlNameUpperCase, Boolean isColumnSqlNameUpperCase) {
        this.isTableDispNameUpperCase = isTableDispNameUpperCase;
        this.isTableSqlNameUpperCase = isTableSqlNameUpperCase;
        this.isColumnSqlNameUpperCase = isColumnSqlNameUpperCase;
    }
}
