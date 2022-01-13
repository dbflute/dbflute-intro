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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.logic.dfprop.schemapolicy.file.DfpropSchemaPolicyFileCommentLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;

/**
 * The logic for reading Schema Policy (dfprop).
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
    @Resource
    private DfpropSchemaPolicyFileCommentLogic schemaPolicyFileCommentLogic;

    // ===================================================================================
    //                                                                           Find File
    //                                                                           =========
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The file object of schema-policy. (NotNull)
     */
    public File findSchemaPolicyFile(String projectName) {
        return dfpropPhysicalLogic.findDfpropFileExisting(projectName, SchemaPolicyMap.DFPROP_NAME);
    }

    // ===================================================================================
    //                                                                            Find Map
    //                                                                            ========
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The map of schema-policy. (NotNull, NoSettingsInstance if not found)
     */
    public SchemaPolicyMap findSchemaPolicyMap(String projectName) {
        return parseSchemePolicyMap(findSchemaPolicyFile(projectName));
    }

    // ===================================================================================
    //                                                                           Parse Map
    //                                                                           =========
    // -----------------------------------------------------
    //                                           Entry Point
    //                                           -----------
    // done jflute need to be public? Can the UpdateLogic use findSchemaPolicyMap() instead of this? (2021/12/02)
    //  => for general use as file so no problem (2021/12/25)
    public SchemaPolicyMap parseSchemePolicyMap(File schemaPolicyMapFile) { // called by e.g. replace logic
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

    private Map<String, Object> readComments(File targetFile) {
        return schemaPolicyFileCommentLogic.readComments(targetFile);
    }

    // -----------------------------------------------------
    //                                        Target Setting
    //                                        --------------
    private SchemaPolicyTargetSetting parseSchemaPolicyTargetSetting(Map<String, Object> schemaPolicyMap) {
        if (schemaPolicyMap.isEmpty()) {
            return SchemaPolicyTargetSetting.noSettingInstance();
        }

        // done anyone resolve ofNullable() headache by jflute (2021/04/29)
        final List<String> tableExceptList = extractListFromDfpropMap(schemaPolicyMap, "tableExceptList");
        final List<String> tableTargetList = extractListFromDfpropMap(schemaPolicyMap, "tableTargetList");
        final Map<String, List<String>> columnExceptMap = extractListMapFromDfpropMap(schemaPolicyMap, "columnExceptMap");
        final boolean isMainSchemaOnly = extractBooleanFromDfpropMap(schemaPolicyMap, "isMainSchemaOnly");

        return new SchemaPolicyTargetSetting(tableExceptList, tableTargetList, columnExceptMap, isMainSchemaOnly);
    }

    // -----------------------------------------------------
    //                                             Whole Map
    //                                             ---------
    private SchemaPolicyWholeMap parseWholeMap(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        Map<String, Object> originalWholeMap = (Map<String, Object>) schemaPolicyMap.get("wholeMap");
        if (originalWholeMap == null) {
            return SchemaPolicyWholeMap.noSettingInstance();
        }
        List<String> originalThemeList = extractListFromDfpropMap(originalWholeMap, "themeList");
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
        @SuppressWarnings("unchecked")
        Map<String, Object> originalTableMap = (Map<String, Object>) schemaPolicyMap.get("tableMap");
        if (originalTableMap == null) {
            return SchemaPolicyTableMap.noSettingInstance();
        }
        List<String> originalThemeList = extractListFromDfpropMap(originalTableMap, "themeList");
        List<SchemaPolicyTableMap.Theme> themeList = originalThemeList.stream()
                .map(code -> new SchemaPolicyTableMap.Theme(SchemaPolicyTableMap.ThemeType.valueByCode(code), true))
                .collect(Collectors.toList());
        List<SchemaPolicyTableMap.Theme> notExistsThemeList = Arrays.stream(SchemaPolicyTableMap.ThemeType.values())
                .filter(themeType -> !originalThemeList.contains(themeType.code))
                .map(themeType -> new SchemaPolicyTableMap.Theme(themeType, false))
                .collect(Collectors.toList());
        themeList.addAll(notExistsThemeList);
        List<String> originalStatementList = extractListFromDfpropMap(originalTableMap, "statementList");

        return new SchemaPolicyTableMap(themeList, originalStatementList);
    }

    // -----------------------------------------------------
    //                                            Column Map
    //                                            ----------
    private SchemaPolicyColumnMap parseColumnMap(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        Map<String, Object> originalColumnMap = (Map<String, Object>) schemaPolicyMap.get("columnMap");
        if (originalColumnMap == null) {
            return SchemaPolicyColumnMap.noSettingInstance();
        }
        List<String> originalThemeList = extractListFromDfpropMap(originalColumnMap, "themeList");
        List<SchemaPolicyColumnMap.Theme> themeList = originalThemeList.stream()
                .map(code -> new SchemaPolicyColumnMap.Theme(SchemaPolicyColumnMap.ThemeType.valueByCode(code), true))
                .collect(Collectors.toList());
        List<SchemaPolicyColumnMap.Theme> notExistsThemeList = Arrays.stream(SchemaPolicyColumnMap.ThemeType.values())
                .filter(themeType -> !originalThemeList.contains(themeType.code))
                .map(themeType -> new SchemaPolicyColumnMap.Theme(themeType, false))
                .collect(Collectors.toList());
        themeList.addAll(notExistsThemeList);
        List<String> originalStatementList = extractListFromDfpropMap(originalColumnMap, "statementList");

        return new SchemaPolicyColumnMap(themeList, originalStatementList);
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

    // -----------------------------------------------------
    //                                      Extract from Map
    //                                      ----------------
    // with default value as empty, false
    private List<String> extractListFromDfpropMap(Map<String, Object> dfpropMap, String key) {
        @SuppressWarnings("unchecked")
        final List<String> plainList = (List<String>) dfpropMap.get(key);
        return Optional.ofNullable(plainList).orElse(Collections.emptyList());
    }

    private Map<String, List<String>> extractListMapFromDfpropMap(Map<String, Object> dfpropMap, String key) {
        @SuppressWarnings("unchecked")
        final Map<String, List<String>> plainMap = (Map<String, List<String>>) dfpropMap.get(key);
        return Optional.ofNullable(plainMap).orElse(Collections.emptyMap());
    }

    private boolean extractBooleanFromDfpropMap(Map<String, Object> dfpropMap, String key) {
        final String plainValue = (String) dfpropMap.get(key);
        return Optional.ofNullable(plainValue).map(value -> Boolean.valueOf(value)).orElse(false);
    }
}
