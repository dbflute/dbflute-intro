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
package org.dbflute.intro.app.logic.playsql.data;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.exception.DirNotFoundException;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * The logic for playsql.data (in DBFlute Client).
 * @author prprmurakami
 * @author jflute
 */
public class PlaysqlDataLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                 Open data directory
    //                                                                 ===================
    /**
     * Open data directory by filer. (e.g. finder if mac, explorer if windows)
     * Use OS command.
     *
     * @param projectName dbflute client project name (NotNull, NotEmpty)
     * @throws DirNotFoundException playsqlのディレクトリが無かったとき
     */
    public void openDataDir(String projectName) throws DirNotFoundException {
        File dataDir = new File(buildDataDirectoryPath(projectName));
        flutyFileLogic.openDir(dataDir);
    }

    // ===================================================================================
    //                                                               File/Directory Helper
    //                                                               =====================
    // -----------------------------------------------------
    //                                  Build Directory Path
    //                                  --------------------
    private String buildDataDirectoryPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql", "data");
    }
}
