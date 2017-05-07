/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.logic.client;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.util.ZipUtil;

/**
 * @author jflute
 */
public class ClientPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String BASIC_INFO_MAP_DFPROP = "basicInfoMap.dfprop";
    private static final String DATABASE_INFO_MAP_DFPROP = "databaseInfoMap.dfprop";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private EnginePhysicalLogic enginePhysicalLogic;

    // ===================================================================================
    //                                                                               Basic
    //                                                                               =====
    // *methods for client top directory are implemented on IntroPhysicalLogic 
    
    // ===================================================================================
    //                                                                              dfprop
    //                                                                              ======
    private String buildDfpropDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "dfprop");
    }

    private String buildDfpropFilePath(String clientProject, String fileName) {
        return buildDfpropDirPath(clientProject) + "/" + fileName;
    }

    public File findDfpropBasicInfoMap(String clientProject) {
        return new File(buildDfpropFilePath(clientProject, BASIC_INFO_MAP_DFPROP));
    }

    public File findDfpropDatabaseInfoMap(String clientProject) {
        return new File(buildDfpropFilePath(clientProject, DATABASE_INFO_MAP_DFPROP));
    }

    // ===================================================================================
    //                                                                              extlib
    //                                                                              ======
    private String buildExtlibDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "extlib");
    }

    public File findExtlibDir(String clientProject) {
        return new File(buildExtlibDirPath(clientProject));
    }

    // ===================================================================================
    //                                                                             playsql
    //                                                                             =======
    private String buildPlaysqlDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql");
    }

    public File findPlaysqlDir(String clientProject) {
        return new File(buildPlaysqlDirPath(clientProject));
    }

    // ===================================================================================
    //                                                                               Meta
    //                                                                              ======
    public File findProjectBat(String clientProject) {
        return new File(introPhysicalLogic.buildClientPath(clientProject, "_project.bat"));
    }

    public File findProjectSh(String clientProject) {
        return new File(introPhysicalLogic.buildClientPath(clientProject, "_project.sh"));
    }

    public File findBuildProperties(String clientProject) {
        return new File(introPhysicalLogic.buildClientPath(clientProject, "build.properties"));
    }

    // ===================================================================================
    //                                                                        Unzip Client
    //                                                                        ============
    public void locateUnzippedClient(String dbfluteVersion, File clientDir) {
        ZipUtil.decrypt(enginePhysicalLogic.buildDfClientZipPath(dbfluteVersion), introPhysicalLogic.buildIntroPath());
        introPhysicalLogic.findClientDir("dfclient").renameTo(clientDir);
    }
}
