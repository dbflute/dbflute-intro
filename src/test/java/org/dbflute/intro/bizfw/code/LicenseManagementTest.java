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
package org.dbflute.intro.bizfw.code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.dbflute.utflute.core.PlainTestCase;
import org.lastaflute.di.exception.IORuntimeException;

/**
 * @author hakiba
 * @author jflute
 * @author cabos
 */
public class LicenseManagementTest extends PlainTestCase {

    private static final String COPYRIGHT = "Copyright 2014-2021 the original author or authors.";

    // ===================================================================================
    //                                                                      License Header
    //                                                                      ==============
    public void test_license_header_main() {
        doTest_license_header("/src/main/java");
    }

    public void test_license_header_test() {
        doTest_license_header("/src/test/java");
    }

    private void doTest_license_header(String srcPathMark) {
        // ## Arrange ##
        File srcDir = new File(getProjectPath() + srcPathMark);
        assertTrue(srcDir.exists());
        List<File> unlicensedList = new ArrayList<>();

        // ## Act ##
        checkUnlicensed(srcDir, unlicensedList);

        // ## Assert ##
        StringBuilder sb = new StringBuilder();
        for (File unlicensedFile : unlicensedList) {
            String path = unlicensedFile.getPath().replace("\\", "/");
            final String rear;
            final String splitToken = srcPathMark + "/";
            if (path.contains(splitToken)) {
                rear = path.substring(path.indexOf(splitToken) + splitToken.length());
            } else {
                rear = path;
            }
            sb.append(ln()).append(rear);
        }
        sb.append(ln()).append(" count: ").append(unlicensedList.size());
        log(sb.toString());
        assertTrue(unlicensedList.isEmpty());
    }

    // ===================================================================================
    //                                                                             Checker
    //                                                                             =======
    protected void checkUnlicensed(File currentFile, List<File> unlicensedList) {
        if (isPackageDir(currentFile)) {
            File[] subFiles = currentFile.listFiles(file -> isPackageDir(file) || isSourceFile(file));
            if (subFiles == null || subFiles.length == 0) {
                return;
            }
            for (File subFile : subFiles) {
                checkUnlicensed(subFile, unlicensedList);
            }
        } else if (isSourceFile(currentFile)) {
            doCheckUnlicensed(currentFile, unlicensedList);
        } else { // no way
            throw new IllegalStateException("Unknown file: " + currentFile);
        }
    }

    protected boolean isPackageDir(File file) {
        return file.isDirectory() && !file.getName().startsWith(".");
    }

    protected boolean isSourceFile(File file) {
        return file.getName().endsWith(".java");
    }

    protected void doCheckUnlicensed(File srcFile, List<File> unlicensedList) {
        if (srcFile == null) {
            String msg = "The argument 'targetFile' should not be null.";
            throw new IllegalArgumentException(msg);
        }
        if (!srcFile.isFile()) {
            String msg = "The argument 'targetFile' should be file: " + srcFile;
            throw new IllegalArgumentException(msg);
        }
        boolean contains = false;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile), "UTF-8"))) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (line.contains(COPYRIGHT)) {
                    contains = true;
                    break;
                }
            }
        } catch (IOException e) {
            String msg = "Failed to read the file: " + srcFile;
            throw new IllegalStateException(msg, e);
        }
        if (!contains) {
            unlicensedList.add(srcFile);
        }
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private String getProjectPath() {
        try {
            return getProjectDir().getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
