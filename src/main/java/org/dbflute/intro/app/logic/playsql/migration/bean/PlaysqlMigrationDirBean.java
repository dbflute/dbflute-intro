package org.dbflute.intro.app.logic.playsql.migration.bean;

import java.util.List;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;

/**
 * @author cabos
 */
public class PlaysqlMigrationDirBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final CDef.NgMark ngMark;
    private final List<PlaysqlMigrationAlterSqlBean> alterSqlBeanList;
    private final PlaysqlMigrationCheckedZipBean checkedZipBean;
    private final PlaysqlMigrationUnreleasedDirBean unreleasedDirBean;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PlaysqlMigrationDirBean(CDef.NgMark ngMark, List<PlaysqlMigrationAlterSqlBean> alterSqlBeanList,
            PlaysqlMigrationCheckedZipBean checkedZipBean, PlaysqlMigrationUnreleasedDirBean unreleasedDirBean) {
        this.ngMark = ngMark;
        this.alterSqlBeanList = alterSqlBeanList;
        this.checkedZipBean = checkedZipBean;
        this.unreleasedDirBean = unreleasedDirBean;
    }

    public OptionalThing<CDef.NgMark> getNgMark() {
        return OptionalThing.ofNullable(ngMark, () -> {});
    }

    public List<PlaysqlMigrationAlterSqlBean> getAlterSqlBeanList() {
        return alterSqlBeanList;
    }

    public OptionalThing<PlaysqlMigrationCheckedZipBean> getCheckedZipBean() {
        return OptionalThing.ofNullable(checkedZipBean, () -> {});
    }

    public OptionalThing<PlaysqlMigrationUnreleasedDirBean> getUnreleasedDirBean() {
        return OptionalThing.ofNullable(unreleasedDirBean, () -> {});
    }
}
