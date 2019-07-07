/*
 * Copyright 2014-2019 the original author or authors.
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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.optional.OptionalThing;

/**
 * @author deco
 * @author cabos
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
    public boolean existsViolationSchemaPolicyCheck(String clientProject) {
        return findLatestResultFile(clientProject, "doc").map(file -> fileLogic.readFile(file))
                .map(s -> s.contains("org.dbflute.exception.DfSchemaPolicyCheckViolationException"))
                .orElse(false);
    }

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

    public OptionalThing<File> findLatestResultFile(String project, String task) {
        final File logDir = new File(buildLogPath(project));
        final File[] logFiles = logDir.listFiles((dir, name) -> name.startsWith("intro-last-execute-") && name.endsWith(task + ".log"));
        if (logFiles == null || logFiles.length == 0) {
            return OptionalThing.empty();
        }
        Arrays.sort(logFiles, (f1, f2) -> f1.lastModified() <= f2.lastModified() ? 1 : -1);
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
