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
package org.dbflute.intro.app.logic.client.projectinfra;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.util.DfStringUtil;

/**
 * The logic for project core information of DBFlute Client. (e.g. version, jdbc)
 * @author hakiba
 * @author jflute
 * @since 0.5.0 split from ClientReadLogic (2021/08/12 at roppongi japanese)
 */
public class ClientProjectInfraReadLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Pattern PROJECT_FILE_VERSION_PATTERN = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;

    // ===================================================================================
    //                                                                       Project Infra
    //                                                                       =============
    public ProjectInfra prepareProjectInfra(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new ProjectInfra(projectName, prepareDBFluteVersion(projectName), prepareJdbcDriverExtlibFile(projectName));
    }

    // ===================================================================================
    //                                                                     DBFlute Version
    //                                                                     ===============
    private String prepareDBFluteVersion(String projectName) {
        // written version in both .sh and .bat should be same
        final File projectFile = new File(introPhysicalLogic.buildClientPath(projectName, "_project.sh"));
        final String data = flutyFileLogic.readFile(projectFile);
        final Matcher matcher = PROJECT_FILE_VERSION_PATTERN.matcher(data);
        if (matcher.find()) {
            return matcher.group(2);
        } else { // almost no way, broken project file
            throw new IllegalStateException("Not found the DBFlute version in _project.sh: " + projectFile);
        }
    }

    // ===================================================================================
    //                                                                         JDBC Driver
    //                                                                         ===========
    // done (by jflute) hakiba confirm allow findFirst by hakiba (2018/04/11)
    // done jflute big problem so make ticket (2020/11/02)
    // https://github.com/dbflute/dbflute-intro/issues/258
    private ExtlibFile prepareJdbcDriverExtlibFile(String projectName) {
        final File extlibDir = clientPhysicalLogic.findExtlibDir(projectName);
        if (!extlibDir.exists()) {
            return null;
        }
        final String[] jdbcExtensions = new String[] { "jar" }; // without dot for the method specification
        final Collection<File> jarFiles = FileUtils.listFiles(extlibDir, jdbcExtensions, /*recursive*/false);
        final Optional<File> certainlyFound = jarFiles.stream().filter(file -> {
            final String fileName = file.getName();
            return isCertainlyJdbcDriver(fileName);
        }).findFirst(); // basically one (not two) here

        if (certainlyFound.isPresent()) {
            return new ExtlibFile(certainlyFound.get());
        } else { // uses old logic, certainly-determination is not perfect so just in case
            return jarFiles.stream().findFirst().map(jar -> new ExtlibFile(jar)).orElse(null);
        }
    }

    private boolean isCertainlyJdbcDriver(String fileName) {
        // determination hint is only name keyword...major JDBC only here
        return DfStringUtil.containsAny(fileName, "mysql", "postgresql", "ojdbc", "db2", "sqljdbc", "h2", "derby", "sqlite");
    }
}
