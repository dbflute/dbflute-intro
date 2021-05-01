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
package org.dbflute.intro.app.logic.dfprop.database;

import javax.annotation.Resource;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv;

/**
 * The logic of database information.
 * @author ryohei
 * @author jflute
 */
public class DatabaseInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClsTargetDatabaseBhv databaseBhv;

    // ===================================================================================
    //                                                                        Embedded Jar
    //                                                                        ============
    public boolean isEmbeddedJar(CDef.TargetDatabase target) {
        // done hakiba pri.B orElseTranslatingThrow() is better by jflute (2017/04/27)
        if (target == null) {
            // #needs_fix anyone can it be not null? (should be strict) by jflute (2021/04/18)
            return false;
        }
        return databaseBhv.selectEntity(cb -> cb.query().setDatabaseCode_Equal_AsTargetDatabase(target))
                .map(database -> database.isEmbeddedJarFlgTrue())
                .orElseTranslatingThrow(cause -> new IllegalStateException("not found target database:" + target.alias(), cause));
    }
}