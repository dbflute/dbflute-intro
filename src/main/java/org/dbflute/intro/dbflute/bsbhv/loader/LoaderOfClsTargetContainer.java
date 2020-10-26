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
package org.dbflute.intro.dbflute.bsbhv.loader;

import java.util.List;

import org.dbflute.bhv.*;
import org.dbflute.intro.dbflute.exbhv.*;
import org.dbflute.intro.dbflute.exentity.*;

/**
 * The referrer loader of CLS_TARGET_CONTAINER as TABLE. <br>
 * <pre>
 * [primary key]
 *     CONTAINER_CODE
 *
 * [column]
 *     CONTAINER_CODE, CONTAINER_NAME, DISPLAY_ORDER
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
public class LoaderOfClsTargetContainer {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected List<ClsTargetContainer> _selectedList;
    protected BehaviorSelector _selector;
    protected ClsTargetContainerBhv _myBhv; // lazy-loaded

    // ===================================================================================
    //                                                                   Ready for Loading
    //                                                                   =================
    public LoaderOfClsTargetContainer ready(List<ClsTargetContainer> selectedList, BehaviorSelector selector)
    { _selectedList = selectedList; _selector = selector; return this; }

    protected ClsTargetContainerBhv myBhv()
    { if (_myBhv != null) { return _myBhv; } else { _myBhv = _selector.select(ClsTargetContainerBhv.class); return _myBhv; } }

    // ===================================================================================
    //                                                                    Pull out Foreign
    //                                                                    ================
    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public List<ClsTargetContainer> getSelectedList() { return _selectedList; }
    public BehaviorSelector getSelector() { return _selector; }
}
