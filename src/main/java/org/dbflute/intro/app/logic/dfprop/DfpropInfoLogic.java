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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.util.DfCollectionUtil;
import org.dbflute.util.Srl;

/**
 * @author jflute
 * @author deco
 * @author cabos
 * @author subaru
 */
public class DfpropInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

    // ===================================================================================
    //                                                                                Find
    //                                                                                ====
    // -----------------------------------------------------
    //                                                Dfprop
    //                                                ------
    public Map<String, Map<String, Object>> findDfpropMap(String clientProject) {
        final Map<String, Map<String, Object>> dfpropMap = new LinkedHashMap<String, Map<String, Object>>();
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + clientProject + "/dfprop");
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) {
            return dfpropMap;
        }
        Stream.of(dfpropFiles).forEach(file -> {
            if (!file.getName().endsWith("Map.dfprop")) {
                return;
            }

            final String fileNameKey;
            if (file.getName().equals("classificationDefinitionMap.dfprop")) {
                fileNameKey = file.getName();
            } else {
                fileNameKey = file.getName().replace("DefinitionMap.dfprop", "Map.dfprop");
            }
            dfpropMap.put(fileNameKey, readMap(file));

            final File plusFile = new File(file.getName().replace("Map.dfprop", "Map+.dfprop"));
            if (plusFile.exists()) {
                dfpropMap.get(fileNameKey).putAll(readMap(plusFile));
            }
        });
        final Map<String, Object> basicInfoMap = dfpropMap.get("basicInfoMap.dfprop");
        if (basicInfoMap == null) {
            throw new RuntimeException("Not found the basicInfoMap.dfprop: " + dfpropMap.keySet());
        }
        final Map<String, Object> databaseInfoMap = dfpropMap.get("databaseInfoMap.dfprop");
        if (databaseInfoMap == null) {
            throw new RuntimeException("Not found the databaseInfoMap.dfprop: " + dfpropMap.keySet());
        }
        return dfpropMap;
    }

    // -----------------------------------------------------
    //                                       SchemaSyncCheck
    //                                       ---------------
    public Optional<SchemaSyncCheckMap> findSchemaSyncCheckMap(String projectName) {
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + projectName + "/dfprop");
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) {
            return Optional.empty();
        }
        return Arrays.stream(dfpropFiles).filter(file -> StringUtils.equals(file.getName(), "documentMap.dfprop")).findAny().map(file -> {
            Map<String, Object> readMap = readMap(file);
            @SuppressWarnings("unchecked")
            Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");
            return schemaSyncCheckMap;
        }).map(schemaSyncCheckMap -> {
            final String url = convertSettingToString(schemaSyncCheckMap.get("url"));
            final String schema = convertSettingToString(schemaSyncCheckMap.get("schema"));
            final String user = convertSettingToString(schemaSyncCheckMap.get("user"));
            final String password = convertSettingToString(schemaSyncCheckMap.get("password"));
            final DbConnectionBox dbConnectionBox = new DbConnectionBox(url, schema, user, password);
            final Boolean isSuppressCraftDiff = Boolean.valueOf(convertSettingToString(schemaSyncCheckMap.get("isSuppressCraftDiff")));
            return new SchemaSyncCheckMap(dbConnectionBox, isSuppressCraftDiff);
        });
    }
    // -----------------------------------------------------
    //                                          SchemaPolicy
    //                                          ------------
    public SchemaPolicyMap findSchemaPolicyMap(String projectName) {
        File schemaPolicyMapFile = new File(dfpropPhysicalLogic.buildDfpropFilePath(projectName, "schemaPolicyMap.dfprop"));
        return parseSchemePolicyMap(schemaPolicyMapFile);
    }

    protected SchemaPolicyMap parseSchemePolicyMap(File schemaPolicyMapFile) {
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

    private SchemaPolicyTargetSetting parseSchemaPolicyTargetSetting(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.isEmpty()) {
            return SchemaPolicyTargetSetting.noSettingInstance();
        }

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

    private SchemaPolicyWholeMap parseWholeMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("wholeMap") == null) {
            return SchemaPolicyWholeMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalWholeMap = (Map<String, Object>) schemaPolicyMap.get("wholeMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalWholeMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyWholeMap.Theme> themeList = Arrays.stream(SchemaPolicyWholeMap.ThemeType.values()).map(themeType -> {
            boolean isOn = originalThemeList.contains(themeType.code);
            return new SchemaPolicyWholeMap.Theme(themeType, isOn);
        }).collect(Collectors.toList());

        return new SchemaPolicyWholeMap(themeList);
    }

    private SchemaPolicyTableMap parseTableMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("tableMap") == null) {
            return SchemaPolicyTableMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalTableMap = (Map<String, Object>) schemaPolicyMap.get("tableMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalTableMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyTableMap.Theme> themeList = Arrays.stream(SchemaPolicyTableMap.ThemeType.values()).map(themeType -> {
            boolean isOn = originalThemeList.contains(themeType.code);
            return new SchemaPolicyTableMap.Theme(themeType, isOn);
        }).collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<String> originalStatementList =
                Optional.ofNullable((List<String>) originalTableMap.get("statementList")).orElse(Collections.emptyList());

        return new SchemaPolicyTableMap(themeList, originalStatementList);
    }

    private SchemaPolicyColumnMap parseColumnMap(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.get("columnMap") == null) {
            return SchemaPolicyColumnMap.noSettingInstance();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> originalColumnMap = (Map<String, Object>) schemaPolicyMap.get("columnMap");
        @SuppressWarnings("unchecked")
        List<String> originalThemeList =
                Optional.ofNullable((List<String>) originalColumnMap.get("themeList")).orElse(Collections.emptyList());
        List<SchemaPolicyColumnMap.Theme> themeList = Arrays.stream(SchemaPolicyColumnMap.ThemeType.values()).map(themeType -> {
            boolean isOn = originalThemeList.contains(themeType.code);
            return new SchemaPolicyColumnMap.Theme(themeType, isOn);
        }).collect(Collectors.toList());
        @SuppressWarnings("unchecked")
        List<String> originalStatementList =
                Optional.ofNullable((List<String>) originalColumnMap.get("statementList")).orElse(Collections.emptyList());

        return new SchemaPolicyColumnMap(themeList, originalStatementList);
    }

    // -----------------------------------------------------
    //                                      LittleAdjustment
    //                                      ----------------
    public LittleAdjustmentMap findLittleAdjustmentMap(String projectName) {
        final File littleAdjustmentMap = dfpropPhysicalLogic.findDfpropFile(projectName, "littleAdjustmentMap.dfprop");
        final Map<String, Object> readMap = readMap(littleAdjustmentMap);
        final boolean isTableDispNameUpperCase = convertSettingToBoolean(readMap.get("isTableDispNameUpperCase"));
        final boolean isTableSqlNameUpperCase = convertSettingToBoolean(readMap.get("isTableSqlNameUpperCase"));
        final boolean isColumnSqlNameUpperCase = convertSettingToBoolean(readMap.get("isColumnSqlNameUpperCase"));
        return new LittleAdjustmentMap(isTableDispNameUpperCase, isTableSqlNameUpperCase, isColumnSqlNameUpperCase);
    }

    // -----------------------------------------------------
    //                                              Document
    //                                              --------
    public DocumentMap findDocumentMap(String projectName) {
        final File documentDefinitionMap = dfpropPhysicalLogic.findDfpropFile(projectName, "documentMap.dfprop");
        final Map<String, Object> readMap = readMap(documentDefinitionMap);
        return prepareDocumentMapInner(readMap);
    }

    private DocumentMap prepareDocumentMapInner(Map<String, Object> readMap) {
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setDbCommentOnAliasBasis(convertSettingToBoolean(readMap.get("isDbCommentOnAliasBasis")));
        documentMap.setAliasDelimiterInDbComment(convertSettingToString(readMap.get("aliasDelimiterInDbComment")));
        documentMap.setCheckColumnDefOrderDiff(convertSettingToBoolean(readMap.get("isCheckColumnDefOrderDiff")));
        documentMap.setCheckDbCommentDiff(convertSettingToBoolean(readMap.get("isCheckDbCommentDiff")));
        documentMap.setCheckProcedureDiff(convertSettingToBoolean(readMap.get("isCheckProcedureDiff")));
        return documentMap;
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

    private Map<String, Object> readComments(File targetFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return new DfMapFile() {
                private final List<String> SCOPE_LIST =
                        Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap", "isMainSchemaOnly", "wholeMap", "tableMap",
                                "columnMap");
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
                    } catch (IOException ignored) {
                    }
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

    private String convertSettingToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    private boolean convertSettingToBoolean(Object obj) {
        return obj != null && Boolean.parseBoolean(convertSettingToString(obj));
    }
}
