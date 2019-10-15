package org.dbflute.intro.app.logic.playsql.migration.retrun;

import java.util.List;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;

/**
 * @author cabos
 */
public class PlaysqlMigrationDirReturn {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final CDef.NgMark ngMark;
    private final List<PlaysqlMigrationAlterSqlReturn> alterSqlBeanList;
    private final PlaysqlMigrationCheckedZipRetrun checkedZipBean;
    private final PlaysqlMigrationUnreleasedDirReturn unreleasedDirBean;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PlaysqlMigrationDirReturn(CDef.NgMark ngMark, List<PlaysqlMigrationAlterSqlReturn> alterSqlBeanList,
            PlaysqlMigrationCheckedZipRetrun checkedZipBean, PlaysqlMigrationUnreleasedDirReturn unreleasedDirBean) {
        this.ngMark = ngMark;
        this.alterSqlBeanList = alterSqlBeanList;
        this.checkedZipBean = checkedZipBean;
        this.unreleasedDirBean = unreleasedDirBean;
    }

    public OptionalThing<CDef.NgMark> getNgMark() {
        return OptionalThing.ofNullable(ngMark, () -> {});
    }

    public List<PlaysqlMigrationAlterSqlReturn> getAlterSqlBeanList() {
        return alterSqlBeanList;
    }

    public OptionalThing<PlaysqlMigrationCheckedZipRetrun> getCheckedZipBean() {
        return OptionalThing.ofNullable(checkedZipBean, () -> {});
    }

    public OptionalThing<PlaysqlMigrationUnreleasedDirReturn> getUnreleasedDirBean() {
        return OptionalThing.ofNullable(unreleasedDirBean, () -> {});
    }
}
