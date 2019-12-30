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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.helper.dfmap.DfMapStyle;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.model.client.document.*;
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
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, inputSchemaPolicyMap);
    }

    protected void doReplaceSchemaPolicyMapWithInput(File file, SchemaPolicyMap input) {
        SchemaPolicyMap base = dfpropInfoLogic.parseSchemePolicyMap(file);
        SchemaPolicyMap merge = mergeSchemaPolicyMap(base, input);
        doReplaceSchemaPolicyMap(file, merge);
    }

    private void doReplaceSchemaPolicyMap(File file, SchemaPolicyMap schemaPolicyMap) {
        try {
            new DfMapFile() {
                protected DfMapStyle newMapStyle() {
                    return new DfMapStyle() {
                        private final List<String> SCOPE_LIST =
                                Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap", "isMainSchemaOnly", "wholeMap",
                                        "tableMap", "columnMap");

                        private String scope = "";

                        @Override
                        protected boolean isIgnoreEqualAsEscapeControlMarkInList() {
                            return true;
                        }

                        @Override
                        public String toMapString(Map<String, ? extends Object> map) {
                            final StringBuilder sb = new StringBuilder();
                            doBuildOtherCommentString(sb, "beginningComments");
                            sb.append(super.toMapString(map)).append("\n");
                            doBuildOtherCommentString(sb, "endComments");
                            return sb.toString();
                        }

                        @SuppressWarnings("unchecked")
                        private void doBuildOtherCommentString(StringBuilder sb, String key) {
                            final String scope = "other";
                            if (schemaPolicyMap.comments.containsKey(scope) && ((Map<String, Object>) schemaPolicyMap.comments.get(
                                    scope)).containsKey(key)) {
                                sb.append(((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key));
                            }
                        }

                        @Override
                        protected void doBuildMapStringCurrentEntry(StringBuilder sb, boolean printOneLiner, String previousIndent,
                                String currentIndent, int index, String key, Object value) {
                            if (SCOPE_LIST.contains(key)) {
                                scope = key;
                            }
                            doBuildCommentStringCurrentElement(sb, currentIndent, key);
                            super.doBuildMapStringCurrentEntry(sb, printOneLiner, previousIndent, currentIndent, index, key, value);
                        }

                        @Override
                        protected void doBuildListStringCurrentElement(StringBuilder sb, boolean printOneLiner, String previousIndent,
                                String currentIndent, int index, Object value) {
                            doBuildCommentStringCurrentElement(sb, currentIndent, (String) value);
                            super.doBuildListStringCurrentElement(sb, printOneLiner, previousIndent, currentIndent, index, value);
                        }

                        @SuppressWarnings("unchecked")
                        private void doBuildCommentStringCurrentElement(StringBuilder sb, String currentIndent, String key) {
                            if (schemaPolicyMap.comments.get(scope) != null
                                    && ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key) != null) {
                                String commentString = (String) ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key);
                                String[] comments = commentString.split("\n");
                                if (comments.length == 0) { // case of containing only '\n'
                                    sb.append(commentString);
                                    return;
                                }
                                for (String c : comments) {
                                    sb.append(c.trim().isEmpty() ? "\n" + c : "\n" + currentIndent + c);
                                }
                            }
                        }
                    };
                }
            }.writeMap(FileUtils.openOutputStream(file), schemaPolicyMap.convertToMap());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write dfprop file: " + file.getAbsolutePath(), e);
        }
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

    public String registerSchemaPolicyStatement(String project, SchemaPolicyStatement statement) {
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        SchemaPolicyMap schemaPolicyMap = dfpropInfoLogic.parseSchemePolicyMap(schemaPolicyMapFile);
        String builtStatement = statement.buildStatement();
        if ("tableMap".equals(statement.mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.tableMap.statementList);
            statements.add(builtStatement);
            schemaPolicyMap.tableMap.statementList = statements;
        } else if ("columnMap".equals(statement.mapType)) {
            List<String> statements = new ArrayList<>(schemaPolicyMap.columnMap.statementList);
            statements.add(builtStatement);
            schemaPolicyMap.columnMap.statementList = statements;
        }
        doReplaceSchemaPolicyMap(schemaPolicyMapFile, schemaPolicyMap);
        return builtStatement;
    }

    private File findSchemaPolicyMapFile(String project) {
        return new File(dfpropPhysicalLogic.buildDfpropFilePath(project, "schemaPolicyMap.dfprop"));
    }

    public void deleteSchemaPolicyStatement(String project, String mapType, String statement) {
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        SchemaPolicyMap schemaPolicyMap = dfpropInfoLogic.parseSchemePolicyMap(schemaPolicyMapFile);
        if ("tableMap".equals(mapType)) {
            schemaPolicyMap.tableMap.statementList =
                    schemaPolicyMap.tableMap.statementList.stream().filter(s -> !s.equals(statement)).collect(Collectors.toList());
        } else if ("columnMap".equals(mapType)) {
            schemaPolicyMap.columnMap.statementList =
                    schemaPolicyMap.columnMap.statementList.stream().filter(s -> !s.equals(statement)).collect(Collectors.toList());
        }
        doReplaceSchemaPolicyMap(schemaPolicyMapFile, schemaPolicyMap);
    }
}
