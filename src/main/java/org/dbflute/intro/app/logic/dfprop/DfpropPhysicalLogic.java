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
package org.dbflute.intro.app.logic.dfprop;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.DfpropDirNotFoundException;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;

/**
 * The logic for DBFlute property (dfprop) physical operation.
 * @author deco
 * @author jflute
 * @author subaru
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
     * dfpropディレクトリのパスを生成する。
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The path to dfprop directory, basically relative. (NotNull)
     */
    public String buildDfpropDirPath(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return introPhysicalLogic.buildClientPath(projectName, "dfprop");
    }

    /**
     * dfpropファイルのパスを生成する。
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param fileName The pure file name for dfprop. (NotNull)
     * @return The path to the dfprop file, basically relative. (NotNull)
     */
    public String buildDfpropFilePath(String projectName, String fileName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        IntroAssertUtil.assertNotEmpty(fileName);
        return buildDfpropDirPath(projectName) + "/" + fileName;
    }

    // ===================================================================================
    //                                                                               Find
    //                                                                              ======
    /**
     * dfpropディレクトリを探す。(存在しなかったら例外)
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The file object to the existing dfprop directory. (NotNull)
     * @throws DfpropDirNotFoundException When the file is not found or not directory.
     */
    public File findDfpropDirExisting(String projectName) {
        final File dfpropFile = new File(buildDfpropDirPath(projectName));
        if (!dfpropFile.isDirectory()) { // means not found or not directory
            throw new DfpropDirNotFoundException("Not found the dfprop directory: " + dfpropFile.getPath(), projectName);
        }
        return dfpropFile;
    }

    /**
     * dfpropファイルのFileオブジェクトを探す。(存在しなかったら例外)
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param fileName The pure file name for dfprop. (NotNull)
     * @return The file object to the existing dfprop file. (NotNull)
     * @throws DfpropFileNotFoundException When the file is not found or not file.
     */
    public File findDfpropFileExisting(String projectName, String fileName) {
        final File dfpropFile = new File(buildDfpropFilePath(projectName, fileName));
        if (!dfpropFile.isFile()) { // means not found or not file
            throw new DfpropFileNotFoundException("Not found the dfprop file: " + dfpropFile.getPath(), fileName);
        }
        return dfpropFile;
    }

    /**
     * 存在するdfpropファイルをすべて探す。(一個もなければ例外)
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The read-only list of file object to the dfprop files. (NotNull, NotEmpty)
     * @throws DfpropDirNotFoundException When the directory or file is not found.
     */
    public List<File> findDfpropFileAllList(String projectName) {
        final File dfpropDir = new File(buildDfpropDirPath(projectName));
        final File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) { // directory not found or empty
            final String debugMsg = "Not found the file on dfprop directory. dfprop dir: " + dfpropDir.getPath();
            throw new DfpropDirNotFoundException(debugMsg, dfpropDir.getName());
        }
        return Arrays.asList(dfpropFiles); // read-only
    }
}
