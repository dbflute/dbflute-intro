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
package org.dbflute.intro.app.logic.core;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.exception.DirNotFoundException;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;

/**
 * The general logic for file. (e.g. read, write)
 * @author jflute
 * @author deco
 * @author cabos
 * @author prprmurakami
 */
public class FlutyFileLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String BASIC_ENCODING = "UTF-8"; // all DBFlute resources are UTF-8

    // ===================================================================================
    //                                                                          Read/Write
    //                                                                          ==========
    public String readFile(File textFile) {
        IntroAssertUtil.assertNotNull(textFile);
        try {
            return FileUtils.readFileToString(textFile, BASIC_ENCODING);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read the text file: " + textFile, e);
        }
    }

    public void writeFile(File textFile, String content) {
        IntroAssertUtil.assertNotNull(textFile);
        // #thinking jflute is content null allowed? (2021/04/17)
        try {
            FileUtils.write(textFile, content, BASIC_ENCODING); // if content is null, no operation
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write the text file: " + textFile, e);
        }
    }

    // ===================================================================================
    //                                                                           Directory
    //                                                                           =========
    /**
     * Open directory of file object. (e.g. finder if mac, explorer if windows) <br>
     * Using OS command.
     *
     * @param dir File object for opened directory. (NotNull)
     * @throws DirNotFoundException When the directory does not exist.
     */
    public void openDir(File dir) throws DirNotFoundException {
        IntroAssertUtil.assertNotNull(dir);
        if (dir.exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(dir);
            } catch (IOException e) {
                throw new UncheckedIOException("fail to open directory." + " directory path: " + dir.getAbsolutePath(), e);
            }
        } else {
            // cannot determine the handling here in case of no directory so checked exception
            String dirPath = dir.getAbsolutePath();
            throw new DirNotFoundException("directory does not exist." + " directory path: " + dirPath, dirPath);
        }
    }
}
