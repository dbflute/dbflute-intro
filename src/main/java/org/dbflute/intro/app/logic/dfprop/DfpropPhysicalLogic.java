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
package org.dbflute.intro.app.logic.dfprop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.DfpropDirNotFoundException;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;

/**
 * The logic for DBFlute property (dfprop) physical operation.
 * @author deco
 * @author jflute
 */
public class DfpropPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                               Path
    //                                                                              ======
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The path to dfprop directory, basically relative. (NotNull)
     */
    public String buildDfpropDirPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName, "dfprop");
    }

    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param fileName The pure file name for dfprop. (NotNull)
     * @return The path to the dfprop file, basically relative. (NotNull)
     */
    public String buildDfpropFilePath(String projectName, String fileName) {
        return buildDfpropDirPath(projectName) + "/" + fileName;
    }

    // ===================================================================================
    //                                                                               Find
    //                                                                              ======
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param fileName The pure file name for dfprop. (NotNull)
     * @return The file object to the dfprop file. (NotNull)
     * @throws DfpropFileNotFoundException When the file is not found.
     */
    public File findDfpropFile(String projectName, String fileName) {
        final File dfpropFile = new File(buildDfpropFilePath(projectName, fileName));
        if (!dfpropFile.isFile()) {
            throw new DfpropFileNotFoundException("Not found dfprop file: " + dfpropFile.getPath(), fileName);
        }
        return dfpropFile;
    }

    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The list of file object to the dfprop files. (NotNull, NotEmpty)
     * @throws DfpropDirNotFoundException When the directory or file is not found.
     */
    public List<File> findDfpropFileAllList(String projectName) {
        final File dfpropDir = new File(buildDfpropDirPath(projectName));
        final File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) {
            throw new DfpropDirNotFoundException("Not found dfprop directory of files. dfprop dir: " + dfpropDir.getPath(),
                    dfpropDir.getName());
        }
        return Arrays.asList(dfpropFiles);
    }
}
