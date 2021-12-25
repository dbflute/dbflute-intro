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
package org.dbflute.intro.app.logic.dfprop.basic;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;

/**
 * The logic of basic information. (basicInfoMap.dfprop)
 * @author jflute
 * @since 0.5.0 (2021/11/18 Thursday at brighton)
 */
public class BasicInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

    // ===================================================================================
    //                                                                           Find File
    //                                                                           =========
    public File findDfpropFile(String projectName) { // moved from ClientPhysicalLogic (2021/11/18)
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(dfpropPhysicalLogic.buildDfpropFilePath(projectName, BasicInfoMap.DFPROP_NAME));
    }
}