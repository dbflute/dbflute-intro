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
package org.dbflute.intro.app.logic.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroSystemLogic;
import org.lastaflute.core.exception.LaSystemException;

/**
 * @author deco
 * @author jflute
 */
public class DocumentDisplayLogic {

    private static final Pattern LASTADOC_FILENAME_PATTERN = Pattern.compile("lastadoc-(.*)\\.html\"");

    @Resource
    private IntroSystemLogic introSystemLogic;

    public String modifyHtmlForIntroOpening(String clientName, File file) {
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            boolean addedIntroExecuteTag = false;
            boolean addedIntroServerTag = false;
            boolean isDecommentServer = introSystemLogic.isDecommentServer();
            final StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (!addedIntroExecuteTag && line.contains("<script>")) {
                    line = "<input id=\"intro_opening\" type=\"hidden\" />" + line;
                    addedIntroExecuteTag = true;
                }
                if (!addedIntroServerTag && isDecommentServer && line.contains("<script>")) {
                    line = "<input id=\"decomment_server\" type=\"hidden\" />" + line;
                    addedIntroServerTag = true;
                }
                if (line.contains("<a href=\"./history-" + clientName + ".html\">to HistoryHTML</a>")) {
                    line = "<a href=\"/api/document/" + clientName + "/historyhtml\">to HistoryHTML</a>";
                }
                if (line.contains("<a href=\"./schema-" + clientName + ".html\">to SchemaHTML</a>")) {
                    line = "<a href=\"/api/document/" + clientName + "/schemahtml\">to SchemaHTML</a>";
                }
                if (line.contains("<a href=\"./properties-" + clientName + ".html\">to PropertiesHTML</a>")) {
                    line = "<a href=\"/api/document/" + clientName + "/propertieshtml\">to PropertiesHTML</a>";
                }
                Matcher lastadocUrlMatcher = LASTADOC_FILENAME_PATTERN.matcher(line);
                if (lastadocUrlMatcher.find()) {
                    String moduleName = lastadocUrlMatcher.group(1);
                    String prefix = line.contains("|") ? "| " : "";
                    line = prefix + "<a href=\"/api/document/" + clientName + "/lastadochtml/" + moduleName + "\">to " + moduleName
                            + "</a>";
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new LaSystemException("Cannot mark intro opening at document HTML: " + file, e);
        }
    }
}
