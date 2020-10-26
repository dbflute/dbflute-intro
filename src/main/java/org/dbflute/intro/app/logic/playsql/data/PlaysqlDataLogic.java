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
package org.dbflute.intro.app.logic.playsql.data;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author prprmurakami
 */
public class PlaysqlDataLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                 Open data directory
    //                                                                 ===================
    /**
     * Open data directory by filer. (e.g. finder if mac, explorer if windows)
     * Use OS command.
     *
     * @param clientName dbflute client project name (NotEmpty)
     */
    public void openDataDir(String clientName) {
        File dataDir = new File(buildDataDirectoryPath(clientName));
        if (dataDir.exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(dataDir);
            } catch (IOException e) {
                throw new UncheckedIOException("fail to open alter directory of" + clientName, e);
            }
        }
    }

    // ===================================================================================
    //                                                               File/Directory Helper
    //                                                               =====================
    // -----------------------------------------------------
    //                                  Build Directory Path
    //                                  --------------------
    private String buildDataDirectoryPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName, "playsql", "data");
    }
}
