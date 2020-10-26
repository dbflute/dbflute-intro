/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.app.model.client.reps;

import java.util.Collections;
import java.util.List;

import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public class AdditionalUserMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final SystemUserMap systemUserMap;
    protected final List<DbConnectionBox> dbConnectionBoxList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AdditionalUserMap(SystemUserMap systemUserMap, List<DbConnectionBox> dbConnectionBoxList) {
        this.systemUserMap = systemUserMap;
        this.dbConnectionBoxList = Collections.unmodifiableList(dbConnectionBoxList);
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public OptionalThing<SystemUserMap> getSystemUserMap() {
        return OptionalThing.ofNullable(systemUserMap, () -> {
            throw new IllegalStateException("Not found the systemUserMap: " + toString());
        });
    }

    public List<DbConnectionBox> getDbConnectionBoxList() {
        return dbConnectionBoxList;
    }
}
