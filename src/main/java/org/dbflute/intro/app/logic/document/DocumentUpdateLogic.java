/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.logic.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.lastaflute.core.exception.LaSystemException;

/**
 * @author deco
 * @author jflute
 */
public class DocumentUpdateLogic {

    public String markIntroOpeningFileTag(File file) {
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            boolean addIntroExecuteTag = false;
            final StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (!addIntroExecuteTag && line.contains("<script>")) {
                    line = "<input id=\"intro_opening\" type=\"hidden\" />" + line;
                    addIntroExecuteTag = true;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new LaSystemException("Cannot mark intro opening at document HTML: " + file, e);
        }
    }
}
