/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.logic.playsql;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.mylasta.exception.PlaysqlFileNotFoundException;

/**
 * @author deco
 */
public class PlaysqlPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String PLAYSQL_DIR_PATH = "playsql";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    public String buildPlaysqlDirPath(String project) {
        return introPhysicalLogic.toDBFluteClientResourcePath(project, PLAYSQL_DIR_PATH);
    }

    public String buildPlaysqlFilePath(String project, String fileName) {
        String dfpropDirPath = buildPlaysqlDirPath(project);
        return dfpropDirPath + "/" + fileName;
    }

    // ===================================================================================
    //                                                                                Find
    //                                                                                ====
    public File findPlaysqlFile(String project, String fileName) {
        final File playsqlFile = new File(buildPlaysqlFilePath(project, fileName));
        if (!playsqlFile.isFile()) {
            throw new PlaysqlFileNotFoundException("Not found dfprop file: " + playsqlFile.getPath(), playsqlFile.getName());
        }
        return playsqlFile;
    }

    public List<File> findPlaysqlFileAllList(String project) {
        final File playsqlDir = new File(buildPlaysqlDirPath(project));
        final File[] playsqlFiles = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"));
        if (playsqlFiles == null || playsqlFiles.length == 0) {
            throw new PlaysqlFileNotFoundException("Not found playsql files or dir: " + playsqlDir.getPath(), playsqlDir.getName());
        }
        return Collections.unmodifiableList(Arrays.asList(playsqlFiles));
    }
}
