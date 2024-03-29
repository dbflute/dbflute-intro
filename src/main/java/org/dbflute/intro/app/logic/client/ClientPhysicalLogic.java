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
package org.dbflute.intro.app.logic.client;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.intro.bizfw.util.ZipUtil;

/**
 * The logic for DBFlute Client physical operation.
 * @author jflute
 * @author deco
 * @author hakiba
 * @author cabos
 * @author subaru
 */
public class ClientPhysicalLogic {

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
    // *finding-map methods are moved to thier own logics e.g. BasicInfoLogic by jflute (2021/11/18)
    // *building-path methods are moved to dfprop logic e.g. DfpropPhysicalLogic by jflute (2021/11/18)
    // dfprop is big domain so basically you can make independent logic classes

    // ===================================================================================
    //                                                                              extlib
    //                                                                              ======
    public ExtlibFile createExtlibFile(String projectName, String fileName, String jdbcDriverFileDataBase64) {
        // no used if DBMS that uses embedded JDBC driver, so all variables are required
        IntroAssertUtil.assertNotEmpty(projectName, fileName, jdbcDriverFileDataBase64);
        String filePath = buildExtlibDirPath(projectName) + "/" + fileName;
        return new ExtlibFile(filePath, jdbcDriverFileDataBase64);
    }

    public File findExtlibDir(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(buildExtlibDirPath(projectName));
    }

    private String buildExtlibDirPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName, "extlib");
    }

    // ===================================================================================
    //                                                                             playsql
    //                                                                             =======
    public File findPlaysqlDir(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(buildPlaysqlDirPath(projectName));
    }

    private String buildPlaysqlDirPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql");
    }

    // ===================================================================================
    //                                                                               Meta
    //                                                                              ======
    public File findProjectBat(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(introPhysicalLogic.buildClientPath(projectName, "_project.bat"));
    }

    public File findProjectSh(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(introPhysicalLogic.buildClientPath(projectName, "_project.sh"));
    }

    public File findBuildProperties(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(introPhysicalLogic.buildClientPath(projectName, "build.properties"));
    }

    // ===================================================================================
    //                                                                        Unzip Client
    //                                                                        ============
    public void locateUnzippedClient(String dbfluteVersion, File clientDir) {
        IntroAssertUtil.assertNotEmpty(dbfluteVersion);
        IntroAssertUtil.assertNotNull(clientDir);
        ZipUtil.decrypt(enginePhysicalLogic.buildDfClientZipPath(dbfluteVersion), introPhysicalLogic.buildIntroPath());
        introPhysicalLogic.findClientDir("dfclient").renameTo(clientDir); // e.g. dbflute_dfclient to dbflute_maihamadb
    }
}
