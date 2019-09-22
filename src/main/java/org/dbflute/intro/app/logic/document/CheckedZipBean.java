package org.dbflute.intro.app.logic.document;

import java.util.List;

/**
 * @author cabos
 */
public class CheckedZipBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private String fileName;
    private List<AlterSqlBean> checkedSqlList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public CheckedZipBean(String fileName, List<AlterSqlBean> checkedSqlList) {
        this.fileName = fileName;
        this.checkedSqlList = checkedSqlList;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getFileName() {
        return fileName;
    }

    public List<AlterSqlBean> getCheckedSqlList() {
        return checkedSqlList;
    }
}
