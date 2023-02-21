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
package org.dbflute.intro.dbflute.bsbhv.loader;

import java.util.List;

import org.dbflute.bhv.*;
import org.dbflute.intro.dbflute.exbhv.*;
import org.dbflute.intro.dbflute.exentity.*;

/**
 * The referrer loader of CLS_TARGET_DATABASE as TABLE. <br>
 * <pre>
 * [primary key]
 *     DATABASE_CODE
 *
 * [column]
 *     DATABASE_CODE, DATABASE_NAME, JDBC_DRIVER_FQCN, URL_TEMPLATE, DEFAULT_SCHEMA, SCHEMA_REQUIRED_FLG, SCHEMA_UPPER_CASE_FLG, USER_INPUT_ASSIST_FLG, EMBEDDED_JAR_FLG, DISPLAY_ORDER
 *
 * [sequence]
 *     
 *
 * [identity]
 *     
 *
 * [version-no]
 *     
 *
 * [foreign table]
 *     
 *
 * [referrer table]
 *     
 *
 * [foreign property]
 *     
 *
 * [referrer property]
 *     
 * </pre>
 * @author DBFlute(AutoGenerator)
 */
public class LoaderOfClsTargetDatabase {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected List<ClsTargetDatabase> _selectedList;
    protected BehaviorSelector _selector;
    protected ClsTargetDatabaseBhv _myBhv; // lazy-loaded

    // ===================================================================================
    //                                                                   Ready for Loading
    //                                                                   =================
    public LoaderOfClsTargetDatabase ready(List<ClsTargetDatabase> selectedList, BehaviorSelector selector)
    { _selectedList = selectedList; _selector = selector; return this; }

    protected ClsTargetDatabaseBhv myBhv()
    { if (_myBhv != null) { return _myBhv; } else { _myBhv = _selector.select(ClsTargetDatabaseBhv.class); return _myBhv; } }

    // ===================================================================================
    //                                                                    Pull out Foreign
    //                                                                    ================
    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public List<ClsTargetDatabase> getSelectedList() { return _selectedList; }
    public BehaviorSelector getSelector() { return _selector; }
}
