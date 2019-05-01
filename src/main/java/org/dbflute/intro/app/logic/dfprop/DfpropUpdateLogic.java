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
package org.dbflute.intro.app.logic.dfprop;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.util.Srl;
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

    public void replaceSchemaPolicyMap(String project, SchemaPolicyMap inputSchemaPolicyMap) {
        File schemaPolicyMapFile = new File(dfpropPhysicalLogic.buildDfpropFilePath(project, "schemaPolicyMap.dfprop"));
        doReplaceSchemaPolicyMap(schemaPolicyMapFile, inputSchemaPolicyMap);
    }

    protected void doReplaceSchemaPolicyMap(File file, SchemaPolicyMap input) {
        SchemaPolicyMap base = dfpropInfoLogic.parseSchemePolicyMap(file);
        flutyFileLogic.writeFile(file, buildSchemaPolicyMap(mergeSchemaPolicyMap(base, input)));
    }

    protected SchemaPolicyMap mergeSchemaPolicyMap(SchemaPolicyMap base, SchemaPolicyMap input) {
        SchemaPolicyWholeMap mergedWholeMap = mergeWholeMap(base.wholeMap, input.wholeMap);
        SchemaPolicyTableMap mergedTableMap = mergeTableMap(base.tableMap, input.tableMap);
        SchemaPolicyColumnMap mergedColumnMap = mergeColumnMap(base.columnMap, input.columnMap);

        return new SchemaPolicyMap(base.targetSetting, mergedWholeMap, mergedTableMap, mergedColumnMap, base.comments);
    }

    private SchemaPolicyWholeMap mergeWholeMap(SchemaPolicyWholeMap base, SchemaPolicyWholeMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(t -> t.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyWholeMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyWholeMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyWholeMap(themeList);
    }

    private SchemaPolicyTableMap mergeTableMap(SchemaPolicyTableMap base, SchemaPolicyTableMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(t -> t.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyTableMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyTableMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyTableMap(themeList, base.statementList);
    }

    private SchemaPolicyColumnMap mergeColumnMap(SchemaPolicyColumnMap base, SchemaPolicyColumnMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(t -> t.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyColumnMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyColumnMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyColumnMap(themeList, base.statementList);
    }

    protected String buildSchemaPolicyMap(SchemaPolicyMap schemaPolicyMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("# /---------------------------------------------------------------------------\n");
        sb.append("map:{\n");
        sb.append(buildSchemaPolicyTargetSetting(schemaPolicyMap.targetSetting, schemaPolicyMap.comments));
        sb.append(buildWholeMap(schemaPolicyMap.wholeMap, schemaPolicyMap.comments));
        sb.append(buildTableMap(schemaPolicyMap.tableMap, schemaPolicyMap.comments));
        sb.append(buildColumnMap(schemaPolicyMap.columnMap, schemaPolicyMap.comments));
        sb.append("}\n");
        sb.append("# ----------------/\n");

        return sb.toString();
    }

    private String buildSchemaPolicyTargetSetting(SchemaPolicyTargetSetting setting, Map<String, Object> comments) {
        StringBuilder sb = new StringBuilder();
        // tableExceptList
        sb.append(buildLinesWithComments("tableExceptList", "    ; %s = list:{\n", comments));
        setting.tableExceptList.forEach(element -> {
            sb.append(buildLinesWithComments(element, "        ; %s\n", comments));
        });
        sb.append("    }\n");
        // tableTargetList
        sb.append(buildLinesWithComments("tableTargetList", "    ; %s = list:{\n", comments));
        setting.tableTargetList.forEach(element -> {
            sb.append(buildLinesWithComments(element, "        ; %s\n", comments));
        });
        sb.append("    }\n");
        // columnExceptMap
        sb.append(buildLinesWithComments("columnExceptMap", "    ; %s = map:{\n", comments));
        setting.columnExceptMap.forEach((key, value) -> {
            sb.append(buildLinesWithComments(key, "        ; %s = list:{\n", comments));
            value.forEach(element -> {
                sb.append(buildLinesWithComments(element, "            ; %s\n", comments));
            });
            sb.append("        }\n");
        });
        sb.append("    }\n");
        // isMainSchemaOnly
        sb.append(buildLinesWithComments("isMainSchemaOnly", "    ; %s = " + setting.isMainSchemaOnly + "\n", comments));

        return sb.toString();
    }

    private String buildWholeMap(SchemaPolicyWholeMap wholeMap, Map<String, Object> comments) {
        List<String> themeTypeCodeList =
                wholeMap.themeList.stream().map(theme -> theme.type).map(type -> type.code).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(buildLinesWithComments("wholeMap", "    ; %s = map:{\n", comments));
        sb.append(buildSchemaPolicyThemeList(themeTypeCodeList, comments));
        sb.append("    }\n");

        return sb.toString();
    }

    private String buildTableMap(SchemaPolicyTableMap tableMap, Map<String, Object> comments) {
        List<String> themeTypeCodeList =
                tableMap.themeList.stream().map(theme -> theme.type).map(type -> type.code).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(buildLinesWithComments("tableMap", "    ; %s = map:{\n", comments));
        sb.append(buildSchemaPolicyThemeList(themeTypeCodeList, comments));
        sb.append(buildSchemaPolicyStatementList(tableMap.statementList, comments));
        sb.append("    }\n");

        return sb.toString();
    }

    protected String buildColumnMap(SchemaPolicyColumnMap columnMap, Map<String, Object> comments) {
        List<String> themeTypeCodeList =
                columnMap.themeList.stream().map(theme -> theme.type).map(type -> type.code).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(buildLinesWithComments("columnMap", "    ; %s = map:{\n", comments));
        sb.append(buildSchemaPolicyThemeList(themeTypeCodeList, comments));
        sb.append(buildSchemaPolicyStatementList(columnMap.statementList, comments));
        sb.append("    }\n");

        return sb.toString();
    }

    private String buildSchemaPolicyThemeList(List<String> themeTypeCodeList, Map<String, Object> comments) {
        StringBuilder sb = new StringBuilder();

        // themeList
        sb.append(buildLinesWithComments("themeList", "        ; %s = list:{\n", comments));
        themeTypeCodeList.forEach(themeTypeCode -> {
            sb.append(buildLinesWithComments(themeTypeCode, "            ; %s\n", comments));
        });
        sb.append("        }\n");

        return sb.toString();
    }

    private String buildSchemaPolicyStatementList(List<String> statementList, Map<String, Object> comments) {
        StringBuilder sb = new StringBuilder();
        // statementList
        sb.append(buildLinesWithComments("statementList", "        ; %s = list:{\n", comments));
        statementList.forEach(themeTypeCode -> {
            sb.append(buildLinesWithComments(themeTypeCode, "            ; %s\n", comments));
        });
        sb.append("        }\n");

        return sb.toString();
    }

    private String buildLinesWithComments(String key, String format, Map<String, Object> comments) {
        String lines = "";
        if (comments.containsKey(key)) {
            final String space = Srl.substringFirstFront(format, format.trim());
            lines += space + "# " + comments.get(key) + "\n";
        }
        lines += String.format(format, key);
        return lines;
    }
}
