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
package org.dbflute.intro.app.logic.document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropReadLogic;
import org.dbflute.intro.app.logic.intro.IntroSystemLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.lastaflute.core.exception.LaSystemException;

/**
 * The logic for document display on intro. (e.g. SchemaHTML) <br>
 * For example, it needs to adjust links to other HTMLs in intro-managed HTML.
 * @author deco
 * @author jflute
 * @author hakiba
 */
public class DocumentDisplayLogic {

    private static final Pattern LASTADOC_FILENAME_PATTERN = Pattern.compile("lastadoc-(.*)\\.html\"");
    private static final Pattern SCHEMA_HTML_FILENAME_PATTERN = Pattern.compile("schema-(.*)\\.html");

    @Resource
    private IntroSystemLogic introSystemLogic;
    @Resource
    private DfpropReadLogic dfpropReadLogic;

    /**
     * DBFluteの各htmlの各種URLをDBFluteIntro用に置換します。
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param file DBFlute html file. (NotNull)
     * @return SchemaHtml with each URL replaced for DBFluteIntro (NotNull)
     */
    public String modifyHtmlForIntroOpening(String projectName, File file) {
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

                // add intro-managed mark used in SchemaHTML JavaScript
                if (!addedIntroExecuteTag && line.contains("<script>")) {
                    line = "<input id=\"intro_opening\" type=\"hidden\" />" + line;
                    addedIntroExecuteTag = true;
                }
                if (!addedIntroServerTag && isDecommentServer && line.contains("<script>")) {
                    line = "<input id=\"decomment_server\" type=\"hidden\" />" + line;
                    addedIntroServerTag = true;
                }

                // enable links in intro-managed HTML
                // attention, it depends on SchemaHTML coding
                if (line.contains("<a href=\"./history-" + projectName + ".html\">to HistoryHTML</a>")) {
                    line = "<a href=\"/api/document/" + projectName + "/historyhtml\">to HistoryHTML</a>";
                }
                if (line.contains("<a href=\"./schema-" + projectName + ".html\">to SchemaHTML</a>")) {
                    line = "<a href=\"/api/document/" + projectName + "/schemahtml\">to SchemaHTML</a>";
                }
                if (line.contains("<a href=\"./properties-" + projectName + ".html\">to PropertiesHTML</a>")) {
                    line = "<a href=\"/api/document/" + projectName + "/propertieshtml\">to PropertiesHTML</a>";
                }
                Matcher lastadocUrlMatcher = LASTADOC_FILENAME_PATTERN.matcher(line);
                if (lastadocUrlMatcher.find()) {
                    String moduleName = lastadocUrlMatcher.group(1);
                    String prefix = line.contains("|") ? "| " : "";
                    line = prefix + "<a href=\"/api/document/" + projectName + "/lastadochtml/" + moduleName + "\">to " + moduleName
                            + "</a>";
                }

                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new LaSystemException("Cannot mark intro opening at document HTML: " + file, e);
        }
    }

    /**
     * schemaHtmlの各種URLをDBFluteIntro用に置換します。
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param file SchemaHtml file. (NotNull)
     * @return SchemaHtml with schemaDiagram and NeighborhoodSchemaHtml URL replaced for DBFluteIntro (NotNull)
     */
    public String modifySchemaHtmlForIntroOpening(String projectName, File file) {
        String content = modifyHtmlForIntroOpening(projectName, file);
        DocumentMap documentMap = dfpropReadLogic.findDocumentMap(projectName);

        if (!documentMap.getSchemaDiagramMap().isEmpty()) {
            // schemaDiagramのリンクをIntro用のAPI URLに置換する
            content = documentMap.getSchemaDiagramMap().get().stream().reduce(content, (replacedContent, diagramEntry) -> {
                final String name = diagramEntry.getKey();
                final String path = diagramEntry.getValue().path;
                final String replacedPath = String.format("/api/dfprop/document/schemadiagram/%s/%s", projectName, name);
                return replacedContent.replaceAll(path, replacedPath);
            }, (ignore, lastContent) -> lastContent);
        }

        if (!documentMap.getNeighborhoodSchemaHtmlMap().isEmpty()) {
            // 「schema-{projectName}.html」というファイル名で neighborhood schemaHtml が指定されている場合は Intro用のAPI URLに置換する（厳密には、dfpropでSchemaHTMLの名前が変更できるが、変える人はほぼいないという想定）
            content = documentMap.getNeighborhoodSchemaHtmlMap().get().stream().reduce(content, (replacedContent, neighborhood) -> {
                Matcher matcher = SCHEMA_HTML_FILENAME_PATTERN.matcher(neighborhood.getValue().path);
                if (!matcher.find()) {
                    return replacedContent;
                }
                final String neighborhoodProjectName = matcher.group(1);
                final String path = neighborhood.getValue().path;
                final String replacedPath = String.format("/api/document/%s/schemahtml", neighborhoodProjectName);
                return replacedContent.replaceAll(path, replacedPath);
            }, (ignore, lastContent) -> lastContent);
        }

        return content;
    }
}
