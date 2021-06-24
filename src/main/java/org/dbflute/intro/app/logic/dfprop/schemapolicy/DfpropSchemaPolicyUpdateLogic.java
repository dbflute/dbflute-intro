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
package org.dbflute.intro.app.logic.dfprop.schemapolicy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.helper.dfmap.DfMapStyle;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;

/**
 * The logic for updating DBFlute property (dfprop).
 * @author deco
 * @author subaru
 * @author jflute
 * @since 0.5.0 split from DfpropInfoLogic (2021/06/24 Thursday at roppongi japanese)
 */
public class DfpropSchemaPolicyUpdateLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;

    // ===================================================================================
    //                                                                         Replace Map
    //                                                                         ===========
    public void replaceSchemaPolicyMap(String project, SchemaPolicyMap inputSchemaPolicyMap) {
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, inputSchemaPolicyMap);
    }

    protected void doReplaceSchemaPolicyMapWithInput(File file, SchemaPolicyMap input) {
        SchemaPolicyMap base = dfpropSchemaPolicyReadLogic.parseSchemePolicyMap(file);
        SchemaPolicyMap merge = mergeSchemaPolicyMap(base, input);
        doReplaceSchemaPolicyMap(file, merge);
    }

    private void doReplaceSchemaPolicyMap(File file, SchemaPolicyMap schemaPolicyMap) {
        try {
            new DfMapFile() {
                protected DfMapStyle newMapStyle() {
                    return new DfMapStyle() {
                        private final List<String> SCOPE_LIST = Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap",
                                "isMainSchemaOnly", "wholeMap", "tableMap", "columnMap");

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
                            if (schemaPolicyMap.comments.containsKey(scope)
                                    && ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).containsKey(key)) {
                                sb.append(((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key));
                            }
                        }

                        @Override
                        protected void doBuildMapStringCurrentEntry(StringBuilder sb, boolean printOneLiner, String previousIndent,
                                String currentIndent, boolean withoutDisplaySideSpace, int index, String key, Object value) {
                            if (SCOPE_LIST.contains(key)) {
                                scope = key;
                            }
                            doBuildCommentStringCurrentElement(sb, currentIndent, key);
                            super.doBuildMapStringCurrentEntry(sb, printOneLiner, previousIndent, currentIndent, withoutDisplaySideSpace,
                                    index, key, value);
                        }

                        @Override
                        protected void doBuildListStringCurrentElement(StringBuilder sb, boolean printOneLiner, String previousIndent,
                                String currentIndent, boolean withoutDisplaySideSpace, int index, Object value) {
                            doBuildCommentStringCurrentElement(sb, currentIndent, (String) value);
                            super.doBuildListStringCurrentElement(sb, printOneLiner, previousIndent, currentIndent, withoutDisplaySideSpace,
                                    index, value);
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

    // ===================================================================================
    //                                                                  Register Statement
    //                                                                  ==================
    public String registerSchemaPolicyStatement(String project, SchemaPolicyStatement statement) {
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.parseSchemePolicyMap(schemaPolicyMapFile);
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

    // ===================================================================================
    //                                                                    Delete Statement
    //                                                                    ================
    public void deleteSchemaPolicyStatement(String project, String mapType, String statement) {
        File schemaPolicyMapFile = findSchemaPolicyMapFile(project);
        SchemaPolicyMap schemaPolicyMap = dfpropSchemaPolicyReadLogic.parseSchemePolicyMap(schemaPolicyMapFile);
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
