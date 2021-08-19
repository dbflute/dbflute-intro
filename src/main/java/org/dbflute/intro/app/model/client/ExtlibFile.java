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
package org.dbflute.intro.app.model.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.dbflute.util.DfTypeUtil;

/**
 * @author ryohei
 * @author jflute
 */
public class ExtlibFile {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final File file;
    protected final byte[] fileData;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ExtlibFile(String filePath, String fileDataBase64) {
        this.file = new File(filePath);
        this.fileData = Base64.getDecoder().decode(fileDataBase64);
    }

    public ExtlibFile(File file) {
        this.file = file;
        try {
            this.fileData = IOUtils.toByteArray(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy the jar file to extlib: " + file.getName(), e);
        }
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return DfTypeUtil.toClassTitle(this) + ":{file=" + file + ", fileData.length=" + fileData.length + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public File getFile() {
        return file;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileName() {
        return file.getName();
    }

    public String getCanonicalPath() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get the canonicalPath: " + file.getName(), e);
        }
    }

}
