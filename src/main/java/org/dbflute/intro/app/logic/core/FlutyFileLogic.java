/*
 * Copyright 2014-2019 the original author or authors.
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

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.apache.commons.io.FileUtils;

/**
 * @author jflute
 * @author deco
 */
public class FlutyFileLogic {

    private static final String BASIC_ENCODING = "UTF-8"; // all DBFlute resources are UTF-8

    public String readFile(File textFile) {
        try {
            return FileUtils.readFileToString(textFile, BASIC_ENCODING);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read the text file: " + textFile, e);
        }
    }

    public void writeFile(File textFile, String content) {
        try {
            FileUtils.write(textFile, content, BASIC_ENCODING);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write the text file: " + textFile, e);
        }
    }
}
