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
package org.dbflute.intro.app.logic.client;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ExtlibFile;
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
    private String buildDfpropDirPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName, "dfprop");
    }

    private String buildDfpropFilePath(String clientName, String fileName) {
        return buildDfpropDirPath(clientName) + "/" + fileName;
    }

    public File findDfpropBasicInfoMap(String clientName) {
        return new File(buildDfpropFilePath(clientName, BASIC_INFO_MAP_DFPROP));
    }

    public File findDfpropDatabaseInfoMap(String clientName) {
        return new File(buildDfpropFilePath(clientName, DATABASE_INFO_MAP_DFPROP));
    }

    // ===================================================================================
    //                                                                              extlib
    //                                                                              ======
    public ExtlibFile createExtlibFile(String clientName, String fileName, String jdbcDriverFileDataBase64) {
        String filePath = buildExtlibDirPath(clientName) + "/" + fileName;
        return new ExtlibFile(filePath, jdbcDriverFileDataBase64);
    }

    private String buildExtlibDirPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName, "extlib");
    }

    public File findExtlibDir(String clientName) {
        return new File(buildExtlibDirPath(clientName));
    }

    // ===================================================================================
    //                                                                             playsql
    //                                                                             =======
    private String buildPlaysqlDirPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName, "playsql");
    }

    public File findPlaysqlDir(String clientName) {
        return new File(buildPlaysqlDirPath(clientName));
    }

    // ===================================================================================
    //                                                                               Meta
    //                                                                              ======
    public File findProjectBat(String clientName) {
        return new File(introPhysicalLogic.buildClientPath(clientName, "_project.bat"));
    }

    public File findProjectSh(String clientName) {
        return new File(introPhysicalLogic.buildClientPath(clientName, "_project.sh"));
    }

    public File findBuildProperties(String clientName) {
        return new File(introPhysicalLogic.buildClientPath(clientName, "build.properties"));
    }

    // ===================================================================================
    //                                                                        Unzip Client
    //                                                                        ============
    public void locateUnzippedClient(String dbfluteVersion, File clientDir) {
        ZipUtil.decrypt(enginePhysicalLogic.buildDfClientZipPath(dbfluteVersion), introPhysicalLogic.buildIntroPath());
        introPhysicalLogic.findClientDir("dfclient").renameTo(clientDir);
    }
}
