/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.mylasta.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author p1us2er0
 */
public class ZipUtil {

    public static void decrypt(String zipFilenName, String outputDirectory) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilenName))) {
            ZipEntry entry = null;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                final String entryName = entry.getName();
                if (entry.isDirectory()) {
                    makeDirectory(outputDirectory, entryName);
                } else {
                    final String parentEntryName = new File(entryName).getParent();
                    if (parentEntryName != null) {
                        makeDirectory(outputDirectory, parentEntryName);
                    }
                    try(FileOutputStream out = new FileOutputStream(new File(outputDirectory, entryName))) {
                        final byte[] buf = new byte[1024];
                        int size = 0;
                        while ((size = zipInputStream.read(buf)) != -1) {
                            out.write(buf, 0, size);
                        }
                    }
                }
                zipInputStream.closeEntry();
            }
        } catch (IOException e) {
            String msg = "decrypt error. zipFilenName=" + zipFilenName + ",outputDirectory=" + outputDirectory;
            throw new RuntimeException(msg, e);
        }
    }

    private static void makeDirectory(String outputDirectory, String directoryName) {
        new File(outputDirectory, directoryName).mkdirs();
    }
}
