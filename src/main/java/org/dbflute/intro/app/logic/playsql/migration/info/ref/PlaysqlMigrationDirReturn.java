/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.app.logic.playsql.migration.info.ref;

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
