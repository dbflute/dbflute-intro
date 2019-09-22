package org.dbflute.intro.app.logic.document;

import java.util.List;

/**
 * @author cabos
 */
public class UnreleasedDirBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private List<AlterSqlBean> checkedSqlList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public UnreleasedDirBean(List<AlterSqlBean> checkedSqlList) {
        this.checkedSqlList = checkedSqlList;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public List<AlterSqlBean> getCheckedSqlList() {
        return checkedSqlList;
    }
}
