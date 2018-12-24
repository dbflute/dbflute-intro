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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DfpropInfoLogicTest extends UnitIntroTestCase {

    @Test
    public void test_findSchemaPolicyMap_WholeMap_containsAllThemeType() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyWholeMap wholeMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap;
        Set<SchemaPolicyWholeMap.ThemeType> resultThemeTypeList =
                wholeMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toSet());

        // ## Assert ##
        assertTrue(resultThemeTypeList.containsAll(Arrays.asList(SchemaPolicyWholeMap.ThemeType.values())));
    }

    @Test
    public void test_findSchemaPolicyMap_WholeMap_correctThemeStatus() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyMap schemaPolicyMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> result = schemaPolicyMap.wholeMap.themeList;

        List<String> actualThemeList = extractThemeList(fetchSchemaPolicyWholeMap());
        result.forEach(theme -> {
            boolean containsActualThemeList = actualThemeList.contains(theme.type.code);
            if (containsActualThemeList) {
                assertTrue(theme.isActive);
            } else {
                assertFalse(theme.isActive);
            }
        });
    }

    @Test
    public void test_findSchemaPolicyMap_TableMap_containsAllProperty() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyTableMap tableMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap;
        Set<SchemaPolicyTableMap.ThemeType> resultThemeTypeList =
                tableMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toSet());

        // ## Assert ##
        assertTrue(resultThemeTypeList.containsAll(Arrays.asList(SchemaPolicyTableMap.ThemeType.values())));
        List<String> actualStatementList = extractStatementList(fetchSchemaPolicyTableMap());
        assertTrue(tableMap.statementList.containsAll(actualStatementList));

    }

    @Test
    public void test_findSchemaPolicyMap_TableMap_correctThemeStatus() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        List<SchemaPolicyTableMap.Theme> result = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

        // ## Assert ##
        List<String> actualThemeList = extractThemeList(fetchSchemaPolicyTableMap());
        result.forEach(theme -> {
            boolean containsActualThemeList = actualThemeList.contains(theme.type.code);
            if (containsActualThemeList) {
                assertTrue(theme.isActive);
            } else {
                assertFalse(theme.isActive);
            }
        });
    }

    @Test
    public void test_findSchemaPolicyMap_ColumnMap_containsAllThemeType() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyColumnMap columnMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap;
        Set<SchemaPolicyColumnMap.ThemeType> resultThemeTypeList =
                columnMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toSet());

        // ## Assert ##
        assertTrue(resultThemeTypeList.containsAll(Arrays.asList(SchemaPolicyColumnMap.ThemeType.values())));
        List<String> actualStatementList = extractStatementList(fetchSchemaPolicyColumnMap());
        assertTrue(columnMap.statementList.containsAll(actualStatementList));
    }

    @Test
    public void test_findSchemaPolicyMap_ColumnMap_correctThemeStatus() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        List<SchemaPolicyColumnMap.Theme> result = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

        // ## Assert ##
        List<String> actualThemeList = extractThemeList(fetchSchemaPolicyColumnMap());
        result.forEach(theme -> {
            boolean containsActualThemeList = actualThemeList.contains(theme.type.code);
            if (containsActualThemeList) {
                assertTrue(theme.isActive);
            } else {
                assertFalse(theme.isActive);
            }
        });
    }

    @Test
    public void test_parseSchemePolicyMap_TargetSetting() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        File schemaPolicyMapFile = prepareFileForTestResource("schemaPolicyMap.dfprop");
        SchemaPolicyTargetSetting result = logic.parseSchemePolicyMap(schemaPolicyMapFile).targetSetting;

        // ## Assert ##
        Map<String, Object> acutualSchemaPolicyMap = readDfpropFile(schemaPolicyMapFile);
        assertTrue(result.tableExceptList.containsAll(extractTableExceptList(acutualSchemaPolicyMap)));
        assertTrue(result.tableTargetList.containsAll(extractTableTargetList(acutualSchemaPolicyMap)));
        Map<String, List<String>> actualColumnExceptMap = extractColumnExceptMap(acutualSchemaPolicyMap);
        result.columnExceptMap.forEach((key, value) -> {
            List<String> actualColumnExcept = actualColumnExceptMap.get(key);
            assertTrue(value.containsAll(actualColumnExcept));
        });
        assertTrue(result.tableExceptList.containsAll(extractTableExceptList(acutualSchemaPolicyMap)));
    }

    @Test
    public void test_parseSchemaPolicyMap_noSettingSchemaPolicyMapFile() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/noSetting_schemaPolicyMap.dfprop");
        SchemaPolicyMap schemaPolicyMap = logic.parseSchemePolicyMap(schemaPolicyMapFile);

        // ## Assert ##
        // basic settings
        assertNotNull(schemaPolicyMap.targetSetting.tableExceptList);
        assertNotNull(schemaPolicyMap.targetSetting.tableTargetList);
        assertNotNull(schemaPolicyMap.targetSetting.columnExceptMap);
        assertFalse(schemaPolicyMap.targetSetting.isMainSchemaOnly);

        // wholeMap
        List<SchemaPolicyWholeMap.ThemeType> wholeMapThemeTypeList =
                schemaPolicyMap.wholeMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toList());
        Arrays.stream(SchemaPolicyWholeMap.ThemeType.values()).forEach(themeType -> {
            assertTrue(wholeMapThemeTypeList.contains(themeType));
        });
        schemaPolicyMap.wholeMap.themeList.forEach(theme -> assertFalse(theme.isActive));
        // tableMap
        List<SchemaPolicyTableMap.ThemeType> tableMapThemeTypeList =
                schemaPolicyMap.tableMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toList());
        Arrays.stream(SchemaPolicyTableMap.ThemeType.values()).forEach(themeType -> {
            assertTrue(tableMapThemeTypeList.contains(themeType));
        });
        assertNotNull(schemaPolicyMap.tableMap.statementList);
        schemaPolicyMap.tableMap.themeList.forEach(theme -> assertFalse(theme.isActive));
        // columnMap
        List<SchemaPolicyColumnMap.ThemeType> columnMapThemeTypeList =
                schemaPolicyMap.columnMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toList());
        Arrays.stream(SchemaPolicyColumnMap.ThemeType.values()).forEach(themeType -> {
            assertTrue(columnMapThemeTypeList.contains(themeType));
        });
        assertNotNull(schemaPolicyMap.columnMap.statementList);
        schemaPolicyMap.columnMap.themeList.forEach(theme -> assertFalse(theme.isActive));
    }

    private Map<String, Object> fetchSchemaPolicyWholeMap() {
        Map<String, Object> schemaPolicyMapFromDfProp = fetchSchemaPolicyMap();
        @SuppressWarnings("unchecked")
        Map<String, Object> wholeMap = (Map<String, Object>) schemaPolicyMapFromDfProp.get("wholeMap");

        return wholeMap;
    }

    private Map<String, Object> fetchSchemaPolicyTableMap() {
        Map<String, Object> schemaPolicyMapFromDfProp = fetchSchemaPolicyMap();
        @SuppressWarnings("unchecked")
        Map<String, Object> tableMap = (Map<String, Object>) schemaPolicyMapFromDfProp.get("tableMap");

        return tableMap;
    }

    private Map<String, Object> fetchSchemaPolicyColumnMap() {
        Map<String, Object> schemaPolicyMapFromDfProp = fetchSchemaPolicyMap();
        @SuppressWarnings("unchecked")
        Map<String, Object> wholeMap = (Map<String, Object>) schemaPolicyMapFromDfProp.get("columnMap");

        return wholeMap;
    }

    private List<String> extractTableExceptList(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        List<String> tableExceptList = (List<String>) schemaPolicyMap.get("tableExceptList");

        return tableExceptList;
    }

    private List<String> extractTableTargetList(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        List<String> tableExceptList = (List<String>) schemaPolicyMap.get("tableTargetList");

        return tableExceptList;
    }

    private Map<String, List<String>> extractColumnExceptMap(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> columnExceptMap = (Map<String, List<String>>) schemaPolicyMap.get("columnExceptMap");

        return columnExceptMap;
    }

    private Boolean extractIsMainSchemaOnly(Map<String, Object> schemaPolicyMap) {
        @SuppressWarnings("unchecked")
        String isMainSchemaOnly = (String) schemaPolicyMap.get("isMainSchemaOnly");

        return Boolean.valueOf(isMainSchemaOnly);
    }

    private List<String> extractThemeList(Map<String, Object> actualWholeMap) {
        @SuppressWarnings("unchecked")
        List<String> themeList = (List<String>) actualWholeMap.get("themeList");

        return themeList;
    }

    private List<String> extractStatementList(Map<String, Object> map) {
        @SuppressWarnings("unchecked")
        List<String> themeList = (List<String>) map.get("statementList");

        return themeList;
    }

    private Map<String, Object> fetchSchemaPolicyMap() {
        final File dfpropFile = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/schemaPolicyMap.dfprop");
        return new DfPropFile().readMap(dfpropFile.getAbsolutePath(), null);
    }

    private Map<String, Object> readDfpropFile(File file) {
        return new DfPropFile().readMap(file.getAbsolutePath(), null);
    }

    private File prepareFileForTestResource(String fileName) {
        return new File(getProjectDir(), "/src/test/resources/dfprop/" + fileName);
    }

    private File prepareFileForTestClient(String filePath) {
        return new File(getProjectDir(), TEST_CLIENT_PATH + "/" + filePath);
    }
}
