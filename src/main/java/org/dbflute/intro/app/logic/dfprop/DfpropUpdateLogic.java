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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMapMeta;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.lastaflute.core.exception.LaSystemException;

/**
 * @author deco
 * @author subaru
 */
public class DfpropUpdateLogic {

    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    public void replaceSchemaSyncCheckMap(String project, SchemaSyncCheckMap schemaSyncCheckMap) {
        final File documentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");
        try (BufferedReader br = Files.newBufferedReader(documentMapFile.toPath())) {
            boolean inSetting = false;
            boolean inSyncSchemeSetting = false;
            final StringBuilder sb = new StringBuilder();

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    inSetting = true;
                }
                if (inSetting) {
                    if (line.contains("; schemaSyncCheckMap = map:{")) {
                        inSyncSchemeSetting = true;
                    }
                    if (inSyncSchemeSetting) {
                        if (line.contains("# - - - - - - - - - -/")) {
                            inSyncSchemeSetting = false;
                            line = schemaSyncCheckMap.convertToDfpropStr() + "\n" + line;
                        } else {
                            continue;
                        }
                    }
                }
                sb.append(line).append("\n");
            }
            flutyFileLogic.writeFile(documentMapFile, sb.toString());
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace schema sync check map", e);
        }
    }

    public void replaceDocumentMap(String project, DocumentMap documentMap) {
        final Function<String, String> lineReplacer = line -> {
            if (line.contains("; aliasDelimiterInDbComment") && line.contains("=")) {
                return "    ; aliasDelimiterInDbComment = " + documentMap.getAliasDelimiterInDbComment().orElse("");
            }
            if (line.contains("; isDbCommentOnAliasBasis") && line.contains("=")) {
                return "    ; isDbCommentOnAliasBasis = " + String.valueOf(documentMap.isDbCommentOnAliasBasis());
            }
            if (line.contains("; isCheckColumnDefOrderDiff") && line.contains("=")) {
                return "    ; isCheckColumnDefOrderDiff = " + String.valueOf(documentMap.isCheckColumnDefOrderDiff());
            }
            if (line.contains("; isCheckDbCommentDiff") && line.contains("=")) {
                return "    ; isCheckDbCommentDiff = " + String.valueOf(documentMap.isCheckDbCommentDiff());
            }
            if (line.contains("; isCheckProcedureDiff") && line.contains("=")) {
                return "    ; isCheckProcedureDiff = " + String.valueOf(documentMap.isCheckProcedureDiff());
            }
            return line;
        };
        final File documentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");
        final String fileContent = replaceDfpropFileContent(documentMapFile, lineReplacer);
        flutyFileLogic.writeFile(documentMapFile, fileContent);
    }

    public void replaceLittleAdjustmentMap(String project, LittleAdjustmentMap littleAdjustmentMap) {
        final Function<String, String> lineReplacer = line -> {
            final Boolean isTableDispNameUpperCase = littleAdjustmentMap.isTableDispNameUpperCase;
            if (isTableDispNameUpperCase != null && line.contains("; isTableDispNameUpperCase") && line.contains("=")) {
                return "    ; isTableDispNameUpperCase = " + String.valueOf(isTableDispNameUpperCase);
            }
            final Boolean isTableSqlNameUpperCase = littleAdjustmentMap.isTableSqlNameUpperCase;
            if (isTableSqlNameUpperCase != null && line.contains("; isTableSqlNameUpperCase") && line.contains("=")) {
                return "    ; isTableSqlNameUpperCase = " + String.valueOf(isTableSqlNameUpperCase);
            }
            final Boolean isColumnSqlNameUpperCase = littleAdjustmentMap.isColumnSqlNameUpperCase;
            if (isColumnSqlNameUpperCase != null && line.contains("; isColumnSqlNameUpperCase") && line.contains("=")) {
                return "    ; isColumnSqlNameUpperCase = " + String.valueOf(isColumnSqlNameUpperCase);
            }
            return line;
        };
        final File littleAdjustmentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "littleAdjustmentMap.dfprop");
        final String fileContent = replaceDfpropFileContent(littleAdjustmentMapFile, lineReplacer);
        flutyFileLogic.writeFile(littleAdjustmentMapFile, fileContent);
    }

    private String replaceDfpropFileContent(File file, Function<String, String> lineReplacer) {
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            boolean endComment = false;
            final StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    endComment = true;
                }
                if (endComment) {
                    line = lineReplacer.apply(line);
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace dfprop", e);
        }
    }
    public void replaceWholeMap(String project, SchemaPolicyWholeMap wholeMap) {

    }

    protected void replaceWholeMapTheme(File schemaPolicyMapFile, SchemaPolicyMapMeta meta, SchemaPolicyWholeMap.ThemeType themeType,
            final Boolean isActive) {
        replaceWholeMapTheme(schemaPolicyMapFile, meta, Collections.singletonList(themeType), isActive);
    }

    protected void replaceWholeMapTheme(File schemaPolicyMapFile, SchemaPolicyMapMeta meta,
            List<SchemaPolicyWholeMap.ThemeType> themeTypeList, final Boolean isActive) {
        final String targetMapAlias = "wholeMap";
        final String themeListAlias = "themeList";
        final List<String> themeTypeCodeList = themeTypeList.stream().map(themeType -> themeType.code).collect(Collectors.toList());
        final List<String> originalThemeCodeList = meta.wholeMapMeta.themeListMeta.originalThemeCodeList;

        replaceSchemaPolicyInnerMap(schemaPolicyMapFile, isActive, targetMapAlias, themeListAlias, themeTypeCodeList,
                originalThemeCodeList);
    }

    private void replaceSchemaPolicyInnerMap(File schemaPolicyMapFile, Boolean isActive, String targetMapAlias, String themeListAlias,
            List<String> themeTypeCodeList, List<String> originalThemeCodeList) {
        boolean inTargetMap = false;
        boolean inTargetMapThemeList = false;
        boolean closeListElement = false;

        try (BufferedReader br = Files.newBufferedReader(schemaPolicyMapFile.toPath())) {
            final StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                boolean isNotComment = !StringUtils.startsWith(line.trim(), "#");

                // Check in
                if (isNotComment && StringUtils.contains(line, targetMapAlias)) {
                    inTargetMap = true;
                }
                if (inTargetMap && isNotComment && StringUtils.contains(line, themeListAlias)) {
                    inTargetMapThemeList = true;
                }

                // Change Property
                if (inTargetMapThemeList) {
                    // change to Active
                    if (isActive) {
                        // append new element
                        boolean closeThemeList = StringUtils.contains(line, "}");
                        if (closeThemeList) {
                            String startThemeList = line.contains(themeListAlias) ? "        ; themeList = list:{" : "";
                            line = Stream.concat(originalThemeCodeList.stream(), themeTypeCodeList.stream())
                                    .distinct()
                                    .map(themeTypeCode -> String.format("            ; %s", themeTypeCode))
                                    .reduce((v1, v2) -> v1 + "\n" + v2)
                                    .map(themeTypeCodeLines -> startThemeList + themeTypeCodeLines + "\n" + "        }")
                                    .orElse(line);
                        }
                    }
                    // change to NotActive
                    if (!isActive) {
                        boolean closeThemeList = StringUtils.contains(line, "}");
                        if (closeThemeList) {
                            String startThemeList = "        ; themeList = list:{\n";
                            line = originalThemeCodeList.stream()
                                    .filter(themeTypeCode -> !themeTypeCodeList.contains(themeTypeCode))
                                    .map(themeTypeCode -> String.format("            ; %s", themeTypeCode))
                                    .reduce((v1, v2) -> v1 + "\n" + v2)
                                    .map(themeTypeCodeLines -> startThemeList + themeTypeCodeLines + "\n" + "        }")
                                    .orElse("        ; themeList = list:{ }");
                        } else {
                            line = "";
                        }
                    }
                }

                // Check out
                if (inTargetMapThemeList && StringUtils.contains(line, "}")) {
                    inTargetMapThemeList = false;
                    closeListElement = true;
                }
                if (inTargetMap && closeListElement) {
                    inTargetMap = false;
                }
                sb.append(line).append("\n");
            }
            flutyFileLogic.writeFile(schemaPolicyMapFile, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected SchemaPolicyMapMeta extractSchemaPolicyMeta(String project, File file) throws IOException {
        SchemaPolicyMapMeta schemaPolicyMeta = new SchemaPolicyMapMeta();

        boolean inWholeMap = false;

        // check written by one line.
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                boolean isNotComment = !StringUtils.startsWith(line.trim(), "#");

                // check in
                if (isNotComment && StringUtils.contains(line, "wholeMap")) {
                    inWholeMap = true;
                }
                if (inWholeMap && isNotComment && StringUtils.contains(line, "themeList")) {
                    inWholeMap = false;
                }
            }
        }

        // save original value
        SchemaPolicyMap schemaPolicyMap = dfpropInfoLogic.parseSchemePolicyMap(file);
        List<String> originalCodeList = schemaPolicyMap.wholeMap.themeList.stream().
                filter(theme -> theme.isActive).map(theme -> theme.type.code).collect(Collectors.toList());
        schemaPolicyMeta.wholeMapMeta.themeListMeta.originalThemeCodeList.addAll(originalCodeList);

        return schemaPolicyMeta;
    }
}
