package org.dbflute.intro.app.logic.playsql.migration;

import java.util.List;

import org.dbflute.optional.OptionalThing;

/**
 * @author cabos
 */
public class PlaysqlMigrationDirReturn {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final PlaysqlMigrationNgMarkFileReturn ngMark;
    private final List<PlaysqlMigrationAlterSqlReturn> alterSqlBeanList;
    private final PlaysqlMigrationCheckedZipReturn checkedZipBean;
    private final PlaysqlMigrationUnreleasedDirReturn unreleasedDirBean;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PlaysqlMigrationDirReturn(PlaysqlMigrationNgMarkFileReturn ngMark, List<PlaysqlMigrationAlterSqlReturn> alterSqlBeanList,
            PlaysqlMigrationCheckedZipReturn checkedZipBean, PlaysqlMigrationUnreleasedDirReturn unreleasedDirBean) {
        this.ngMark = ngMark;
        this.alterSqlBeanList = alterSqlBeanList;
        this.checkedZipBean = checkedZipBean;
        this.unreleasedDirBean = unreleasedDirBean;
    }

    public OptionalThing<PlaysqlMigrationNgMarkFileReturn> getNgMark() {
        return OptionalThing.ofNullable(ngMark, () -> {});
    }

    public List<PlaysqlMigrationAlterSqlReturn> getAlterSqlBeanList() {
        return alterSqlBeanList;
    }

    public OptionalThing<PlaysqlMigrationCheckedZipReturn> getCheckedZipBean() {
        return OptionalThing.ofNullable(checkedZipBean, () -> {});
    }

    public OptionalThing<PlaysqlMigrationUnreleasedDirReturn> getUnreleasedDirBean() {
        return OptionalThing.ofNullable(unreleasedDirBean, () -> {});
    }
}
