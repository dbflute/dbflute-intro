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
package org.dbflute.intro.app.model.client.basic;

import org.dbflute.intro.dbflute.allcommon.CDef;

/**
 * @author jflute
 */
public class BasicInfoMap {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String DFPROP_NAME = "basicInfoMap.dfprop";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final CDef.TargetDatabase database;
    private final CDef.TargetLanguage targetLanguage;
    private final CDef.TargetContainer targetContainer;
    private final String packageBase;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public BasicInfoMap(CDef.TargetDatabase database, CDef.TargetLanguage targetLanguage, CDef.TargetContainer targetContainer,
            String packageBase) {
        this.database = database;
        this.targetLanguage = targetLanguage;
        this.targetContainer = targetContainer;
        this.packageBase = packageBase;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public CDef.TargetDatabase getDatabase() {
        return database;
    }

    public CDef.TargetLanguage getTargetLanguage() {
        return targetLanguage;
    }

    public CDef.TargetContainer getTargetContainer() {
        return targetContainer;
    }

    public String getPackageBase() {
        return packageBase;
    }
}
