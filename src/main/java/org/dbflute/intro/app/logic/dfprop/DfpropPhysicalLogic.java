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
package org.dbflute.intro.app.logic.dfprop;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.DfpropDirNotFoundException;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;

/**
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
    public String buildDfpropDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "dfprop");
    }

    public String buildDfpropFilePath(String clientProject, String fileName) {
        return buildDfpropDirPath(clientProject) + "/" + fileName;
    }

    // ===================================================================================
    //                                                                                Find
    //                                                                                ====
    public File findDfpropFile(String clientProject, String fileName) {
        final File dfpropFile = new File(buildDfpropFilePath(clientProject, fileName));
        if (!dfpropFile.isFile()) {
            throw new DfpropFileNotFoundException("Not found dfprop file: " + dfpropFile.getPath(), fileName);
        }
        return dfpropFile;
    }

    public List<File> findDfpropFileAllList(String clientProject) {
        final File dfpropDir = new File(buildDfpropDirPath(clientProject));
        final File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) {
            throw new DfpropDirNotFoundException("Not found dfprop files. file dir: " + dfpropDir.getPath(), dfpropDir.getName());
        }
        return Collections.unmodifiableList(Arrays.asList(dfpropFiles));
    }
}
