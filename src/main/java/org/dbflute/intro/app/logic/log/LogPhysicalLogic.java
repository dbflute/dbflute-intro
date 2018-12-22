/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.logic.log;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.optional.OptionalThing;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author deco
 */
public class LogPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String LOG_DIR_PATH = "log";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private FlutyFileLogic fileLogic;

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    public String buildLogPath(String project) {
        return introPhysicalLogic.buildClientPath(project, LOG_DIR_PATH);
    }

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====
    public List<File> findLogFileAllList(String project) {
        final File logDir = new File(buildLogPath(project));
        final File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (logFiles == null || logFiles.length == 0) {
            return Collections.unmodifiableList(new ArrayList<>());
        }
        return Collections.unmodifiableList(Arrays.asList(logFiles));
    }

    public OptionalThing<File> findLogFile(String project, String fileName) {
        final File logDir = new File(buildLogPath(project));
        final File[] logFiles = logDir.listFiles((dir, name) -> name.equals(fileName));
        if (logFiles == null || logFiles.length == 0) {
            return OptionalThing.empty();
        }
        return OptionalThing.of(logFiles[0]);
    }

    // ===================================================================================
    //                                                                               Write
    //                                                                               =====
    public void logging(String project, String fileName, String log) {
        File file = new File(buildLogPath(project) + "/" + fileName);
        fileLogic.writeFile(file, log);
    }

}
