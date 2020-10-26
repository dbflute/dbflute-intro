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
package org.dbflute.intro.app.logic.engine;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author jflute
 */
public class EnginePhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    // *methods for client top directory are implemented on IntroPhysicalLogic

    // ===================================================================================
    //                                                                       lib Directory
    //                                                                       =============
    public File findLibDir(String dbfluteVersion) {
        return new File(introPhysicalLogic.buildEnginePath(dbfluteVersion, "lib"));
    }

    // ===================================================================================
    //                                                                          Client Zip
    //                                                                          ==========
    public File findDfClientZip(String dbfluteVersion) {
        return new File(buildDfClientZipPath(dbfluteVersion));
    }

    public String buildDfClientZipPath(String dbfluteVersion) {
        return introPhysicalLogic.buildEnginePath(dbfluteVersion, "etc", "client-template", "dbflute_dfclient.zip");
    }
}
