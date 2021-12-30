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
package org.dbflute.intro.app.logic.playsql.migration;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.exception.DirNotFoundException;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * The logic for physical operation to playsql.migration (in DBFlute Client).
 * @author jflute (split it from large logic) (at shin-urayasu)
 */
public class PlaysqlMigrationPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                     alter Directory
    //                                                                     ===============
    public String buildAlterDirectoryPath(String projectName) {
        return buildMigrationPath(projectName, "alter");
    }

    // ===================================================================================
    //                                                                unreleased Directory
    //                                                                ====================
    public String buildUnreleasedAlterDirPath(String projectName) {
        return buildMigrationPath(projectName, "history", "unreleased-checked-alter");
    }

    // ===================================================================================
    //                                                         General migration Directory
    //                                                         ===========================
    public String buildMigrationPath(String projectName, String pureName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql", "migration", pureName);
    }

    public String buildMigrationPath(String projectName, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql", "migration", type, pureName);
    }

    // ===================================================================================
    //                                                                Open alter directory
    //                                                                ====================
    /**
     * Open alter directory by filter. (e.g. finder if mac, explorer if windows)
     * Use OS command.
     *
     * @param projectName dbflute client project name (NotEmpty)
     * @throws DirNotFoundException When the directory is not found.
     */
    public void openAlterDir(String projectName) throws DirNotFoundException {
        File alterDir = new File(buildAlterDirectoryPath(projectName));
        flutyFileLogic.openDir(alterDir);
    }
}
