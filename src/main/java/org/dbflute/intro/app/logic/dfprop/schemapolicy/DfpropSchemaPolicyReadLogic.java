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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.util.DfCollectionUtil;
import org.dbflute.util.Srl;

/**
 * The logic for reading DBFlute property (dfprop).
 * @author jflute
 * @author deco
 * @author cabos
 * @author hakiba
 * @author subaru
 * @author prprmurakami
 * @since 0.5.0 split from DfpropInfoLogic (2021/06/24 Thursday at roppongi japanese)
 */
public class DfpropSchemaPolicyReadLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

    // ===================================================================================
    //                                                                            Find Map
    //                                                                            ========
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The map of schema-policy. (NotNull, NoSettingsInstance if not found)
     */
    public SchemaPolicyMap findSchemaPolicyMap(String projectName) {
        File schemaPolicyMapFile = dfpropPhysicalLogic.findDfpropFile(projectName, "schemaPolicyMap.dfprop");
        return parseSchemePolicyMap(schemaPolicyMapFile);
    }

    // ===================================================================================
    //                                                                               Parse
    //                                                                               =====
    // -----------------------------------------------------
    //                                           Entry Point
    //                                           -----------
    public SchemaPolicyMap parseSchemePolicyMap(File schemaPolicyMapFile) { // called by e.g. update logic
        if (!schemaPolicyMapFile.exists()) {
            return SchemaPolicyMap.noSettingsInstance();
        }

        Map<String, Object> schemaPolicyMap = readMap(schemaPolicyMapFile);
        Map<String, Object> comments = readComments(schemaPolicyMapFile);

        SchemaPolicyTargetSetting targetSetting = parseSchemaPolicyTargetSetting(schemaPolicyMap);
        SchemaPolicyWholeMap wholeMap = parseWholeMap(schemaPolicyMap);
        SchemaPolicyTableMap tableMap = parseTableMap(schemaPolicyMap);
        SchemaPolicyColumnMap columnMap = parseColumnMap(schemaPolicyMap);

        return new SchemaPolicyMap(targetSetting, wholeMap, tableMap, columnMap, comments);
    }

    // -----------------------------------------------------
    //                                        Target Setting
    //                                        --------------
    private SchemaPolicyTargetSetting parseSchemaPolicyTargetSetting(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.isEmpty()) {
            return SchemaPolicyTargetSetting.noSettingInstance();
        }

        // #needs_fix anyone resolve ofNullable() headache by jflute (2021/04/29)
        @SuppressWarnings("unchecked")
        List<String> tableExceptList =
                Optional.ofNullable((List<String>) schemaPolicyMap.get("tableExceptList")).orElse(Collections.emptyList());
        @SuppressWarnings("unchecked")
        List<String> tableTargetList =
                Optional.ofNullable((List<String>) schemaPolicyMap.get("tableTargetList")).orElse(Collections.emptyList());
        @SuppressWarnings("unchecked")
        Map<String, List<String>> columnExceptMap =
                Optional.ofNullable((Map<String, List<String>>) schemaPolicyMap.get("columnExceptMap")).orElse(Collections.emptyMap());
        boolean isMainSchemaOnly =
                Optional.ofNullable((String) schemaPolicyMap.get("isMainSchemaOnly")).map(value -> Boolean.valueOf(value)).orElse(false);

        return new SchemaPolicyTargetSetting(tableExceptList, tableTargetList, columnExceptMap, isMainSchemaOnly);
    }

    // -----------------------------------------------------
    //                                             Whole Map
    //                                             ---------
    private SchemaPolicyWholeMap parseWholeMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("wholeMap") == null) {
            return SchemaPolicyWholeMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalWholeMap = (Map<String, Object>) schemaPolicyMap.get("wholeMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalWholeMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyWholeMap.Theme> themeList = originalThemeList.stream()
                .map(code -> new SchemaPolicyWholeMap.Theme(SchemaPolicyWholeMap.ThemeType.valueByCode(code), true))
                .collect(Collectors.toList());
        List<SchemaPolicyWholeMap.Theme> notExistsThemeList = Arrays.stream(SchemaPolicyWholeMap.ThemeType.values())
                .filter(themeType -> !originalThemeList.contains(themeType.code))
                .map(themeType -> new SchemaPolicyWholeMap.Theme(themeType, false))
                .collect(Collectors.toList());
        themeList.addAll(notExistsThemeList);

        return new SchemaPolicyWholeMap(themeList);
    }

    // -----------------------------------------------------
    //                                             Table Map
    //                                             ---------
    private SchemaPolicyTableMap parseTableMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("tableMap") == null) {
            return SchemaPolicyTableMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalTableMap = (Map<String, Object>) schemaPolicyMap.get("tableMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalTableMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyTableMap.Theme> themeList = originalThemeList.stream()
                .map(code -> new SchemaPolicyTableMap.Theme(SchemaPolicyTableMap.ThemeType.valueByCode(code), true))
                .collect(Collectors.toList());
        List<SchemaPolicyTableMap.Theme> notExistsThemeList = Arrays.stream(SchemaPolicyTableMap.ThemeType.values())
                .filter(themeType -> !originalThemeList.contains(themeType.code))
                .map(themeType -> new SchemaPolicyTableMap.Theme(themeType, false))
                .collect(Collectors.toList());
        themeList.addAll(notExistsThemeList);
        @SuppressWarnings("unchecked")
        List<String> originalStatementList =
                Optional.ofNullable((List<String>) originalTableMap.get("statementList")).orElse(Collections.emptyList());

        return new SchemaPolicyTableMap(themeList, originalStatementList);
    }

    // -----------------------------------------------------
    //                                            Column Map
    //                                            ----------
    private SchemaPolicyColumnMap parseColumnMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("columnMap") == null) {
            return SchemaPolicyColumnMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalColumnMap = (Map<String, Object>) schemaPolicyMap.get("columnMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalColumnMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyColumnMap.Theme> themeList = originalThemeList.stream()
                .map(code -> new SchemaPolicyColumnMap.Theme(SchemaPolicyColumnMap.ThemeType.valueByCode(code), true))
                .collect(Collectors.toList());
        List<SchemaPolicyColumnMap.Theme> notExistsThemeList = Arrays.stream(SchemaPolicyColumnMap.ThemeType.values())
                .filter(themeType -> !originalThemeList.contains(themeType.code))
                .map(themeType -> new SchemaPolicyColumnMap.Theme(themeType, false))
                .collect(Collectors.toList());
        themeList.addAll(notExistsThemeList);
        @SuppressWarnings("unchecked")
        List<String> originalStatementList =
                Optional.ofNullable((List<String>) originalColumnMap.get("statementList")).orElse(Collections.emptyList());

        return new SchemaPolicyColumnMap(themeList, originalStatementList);
    }

    // ===================================================================================
    //                                                                        Subject List
    //                                                                        ============
    public List<TableMapSubject> getStatementTableMapSubjectList() {
        // Create subject list here because contents does not change frequently
        return Arrays.asList(TableMapSubject.values());
    }

    public List<ColumnMapSubject> getStatementColumnMapSubjectList() {
        // Create subject list here because contents does not change frequently
        return Arrays.asList(ColumnMapSubject.values());
    }

    // 以下のページの内容と一致しており、項目が増えた場合は追加する。 by prprmurakami (2021/05/27)
    // http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatement
    public enum TableMapSubject {
        TABLE_NAME("tableName"), //
        ALIAS("alias"), //
        FIRST_DATE("firstDate"), //
        PK_COLUMN_NAME("pk_columnName"), //
        PK_DB_TYPE("pk_dbType"), //
        PK_SIZE("pk_size"), //
        PK_DB_TYPE_WITH_SIZE("pk_dbType_with_size"); //

        private final String title;

        private TableMapSubject(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    // 以下のページの内容と一致しており、項目が増えた場合は追加する。 by prprmurakami (2021/05/27)
    // http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatement
    public enum ColumnMapSubject {
        TABLE_NAME("tableName"), //
        ALIAS("alias"), //
        FIRST_DATE("firstDate"), //
        COLUMN("column"), //
        COLUMN_NAME("columnName"), //
        TABLE_COLUMN_NAME("tableColumnName"), //
        DB_TYPE_WITH_SIZE("dbType_with_size"), //
        SIZE("size"), //
        DB_TYPE("dbType");

        private final String title;

        private ColumnMapSubject(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    // ===================================================================================
    //                                                                            Comments
    //                                                                            ========
    private Map<String, Object> readComments(File targetFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return new DfMapFile() {
                private final List<String> SCOPE_LIST = Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap",
                        "isMainSchemaOnly", "wholeMap", "tableMap", "columnMap");
                private final String OTHER_SCOPE = "other";
                private final String BEGINNING_KEY = "beginningComments";
                private final String END_KEY = "endComments";

                public Map<String, Object> readComments(InputStream ins) throws IOException {
                    assertObjectNotNull("ins", ins);
                    final Map<String, Object> keyCommentMap = DfCollectionUtil.newLinkedHashMap();
                    final String encoding = "UTF-8";
                    BufferedReader br = new BufferedReader(new InputStreamReader(ins, encoding));
                    String previousComment = "";
                    String scope = null;
                    while (true) {
                        final String line = br.readLine();
                        if (line == null) {
                            if (!previousComment.isEmpty()) {
                                addComments(keyCommentMap, OTHER_SCOPE, END_KEY, previousComment);
                            }
                            break;
                        }
                        final String ltrimmedLine = Srl.ltrim(line);
                        if (ltrimmedLine.startsWith(_lineCommentMark) || "".equals(ltrimmedLine)) {
                            previousComment += ltrimmedLine + "\n";
                            continue;
                        }
                        // key value here
                        String key = extractKey(ltrimmedLine);
                        if (SCOPE_LIST.contains(key)) {
                            scope = key;
                        }
                        if ("".equals(previousComment)) {
                            continue;
                        }

                        if (scope == null && key.startsWith("map:{")) {
                            scope = OTHER_SCOPE;
                            key = BEGINNING_KEY;
                        }
                        addComments(keyCommentMap, scope, key, previousComment);
                        previousComment = "";
                    }
                    try {
                        br.close();
                    } catch (IOException ignored) {}
                    return keyCommentMap;
                }

                private String extractKey(String line) {
                    String key;
                    if (line.contains("=>")) {
                        key = line.trim();
                    } else if (line.contains("=")) {
                        key = Srl.substringFirstFront(line, "=").trim();
                    } else {
                        key = line.trim();
                    }

                    if (key.startsWith(";")) {
                        return Srl.substringFirstRear(key, ";").trim();
                    }
                    return key;
                }

                @SuppressWarnings("unchecked")
                private void addComments(Map<String, Object> map, String scope, String key, String comments) {
                    if (map.containsKey(scope)) {
                        ((Map<String, Object>) map.get(scope)).put(key, comments);
                    } else {
                        map.put(scope, DfCollectionUtil.newLinkedHashMap(key, comments));
                    }
                }
            }.readComments(new FileInputStream(targetFile));
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop comments: " + absolutePath, e);
        }
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private Map<String, Object> readMap(File targetFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return new DfMapFile().readMap(new FileInputStream(targetFile));
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop as map: " + absolutePath, e);
        }
    }
}
