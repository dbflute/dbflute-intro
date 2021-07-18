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
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author jflute
 * @author hakiba
 * @since 0.5.0 split from DfpropSchemaPolicyReadLogicTest (2021/06/24 Thursday at roppongi japanese)
 */
public class DfpropSchemaPolicyUpdateLogicTest extends UnitIntroTestCase {

    @Test
    public void test_replaceSchemaPolicyMap_NotExistDfpropFile() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        final SchemaPolicyWholeMap.ThemeType targetType = SchemaPolicyWholeMap.ThemeType.SameColumnDbTypeIfSameColumnName;
        File notExistFile = prepareFileForTestClient("not/exist/schemaPolicyMap.dfprop");
        assertFalse(notExistFile.exists());

        // ## Act ##
        logic.doReplaceSchemaPolicyMapWithInput(notExistFile, createInputWholeMap(targetType, true));

        // ## Assert ##
        File schemaPolicyMapFile = prepareFileForTestClient("not/exist/schemaPolicyMap.dfprop");
        assertTrue(schemaPolicyMapFile.exists());
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapTheme(schemaPolicyMapFile, targetType, readLogic);
        assertTrue(afterTheme.isActive);
    }

    @Test
    public void test_doReplaceSchemaPolicyMap_noChange() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        File inputFile = prepareFileForTestClient("dfprop/unittest/schemaPolicyMap.dfprop");
        File outputFile = prepareFileForTestClient("dfprop/unittest/schemaPolicyMap_output.dfprop");

        Files.copy(inputFile.toPath(), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // ## Act ##
        SchemaPolicyMap inputMap = readLogic.parseSchemePolicyMap(inputFile);
        logic.doReplaceSchemaPolicyMapWithInput(outputFile, inputMap);

        // ## Assert ##
        BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
        BufferedReader outputReader = new BufferedReader(new FileReader(outputFile));

        String inputLine;
        String outputLine;

        while ((inputLine = inputReader.readLine()) != null | (outputLine = outputReader.readLine()) != null) {
            log("inputLine: \t{}", inputLine);
            log("outputLine:\t{}", outputLine);
            assertEquals(inputLine, outputLine);
        }

        inputReader.close();
        outputReader.close();
    }

    // -----------------------------------------------------
    //                                             Whole Map
    //                                             ---------
    @Test
    public void test_replaceWholeMapTheme_ChangeToActive() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final SchemaPolicyWholeMap.ThemeType targetType = SchemaPolicyWholeMap.ThemeType.SameColumnDbTypeIfSameColumnName;
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputWholeMap(targetType, true));

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, readLogic);
        assertTrue(afterTheme.isActive);
        assertWholeMapNotChangeOtherThemeType(readLogic, targetType, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToNotActive() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final SchemaPolicyWholeMap.ThemeType targetType = SchemaPolicyWholeMap.ThemeType.SameColumnDbTypeIfSameColumnName;
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputWholeMap(targetType, false));

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, readLogic);
        assertFalse(afterTheme.isActive);
        assertWholeMapNotChangeOtherThemeType(readLogic, targetType, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputWholeMap(themeType, true));

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, readLogic);
            assertTrue(afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputWholeMap(themeType, false));
            log(themeType.code + " was replaced.");

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, readLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceWholeMapThemeList_ChangeToActiveAllType_In_NoSettingFile() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        copyNoSettingSchemaPolicyMap();

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/noSetting_schemaPolicyMap.dfprop");
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputWholeMap(themeType, true));

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapTheme(schemaPolicyMapFile, themeType, readLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    // -----------------------------------------------------
    //                                             Table Map
    //                                             ---------
    @Test
    public void test_replaceTableMapThemeList_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputTableMap(themeType, true));

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, readLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceTableMapThemeList_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputTableMap(themeType, false));

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, readLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    // -----------------------------------------------------
    //                                            Column Map
    //                                            ----------
    @Test
    public void test_replaceColumnMapThemeList_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputColumnMap(themeType, true));
            log(themeType + "was replaced to true.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, readLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceColumnMapThemeList_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropSchemaPolicyUpdateLogic logic = new DfpropSchemaPolicyUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropSchemaPolicyReadLogic readLogic = new DfpropSchemaPolicyReadLogic();
        inject(readLogic);

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.doReplaceSchemaPolicyMapWithInput(schemaPolicyMapFile, createInputColumnMap(themeType, false));
            log(themeType + "was replaced to false.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, readLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(readLogic, themeType, beforeThemeList);
        }
    }

    // ===================================================================================
    //                                                                             Private
    //                                                                             =======
    // -----------------------------------------------------
    //                                                Assert
    //                                                ------
    private void assertWholeMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic, SchemaPolicyWholeMap.ThemeType targetType,
            List<SchemaPolicyWholeMap.Theme> beforeThemeList) {
        assertWholeMapNotChangeOtherThemeType(readLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertWholeMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic,
            List<SchemaPolicyWholeMap.ThemeType> targetTypeList, List<SchemaPolicyWholeMap.Theme> beforeThemeList) {
        Map<SchemaPolicyWholeMap.ThemeType, SchemaPolicyWholeMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyWholeMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, readLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    private void assertTableMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic, SchemaPolicyTableMap.ThemeType targetType,
            List<SchemaPolicyTableMap.Theme> beforeThemeList) {
        assertTableMapNotChangeOtherThemeType(readLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertTableMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic,
            List<SchemaPolicyTableMap.ThemeType> targetTypeList, List<SchemaPolicyTableMap.Theme> beforeThemeList) {
        Map<SchemaPolicyTableMap.ThemeType, SchemaPolicyTableMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyTableMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyTableMap.Theme afterTheme = fetchTableMapThemeByTestClient(themeType, readLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    private void assertColumnMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic, SchemaPolicyColumnMap.ThemeType targetType,
            List<SchemaPolicyColumnMap.Theme> beforeThemeList) {
        assertColumnMapNotChangeOtherThemeType(readLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertColumnMapNotChangeOtherThemeType(DfpropSchemaPolicyReadLogic readLogic,
            List<SchemaPolicyColumnMap.ThemeType> targetTypeList, List<SchemaPolicyColumnMap.Theme> beforeThemeList) {
        Map<SchemaPolicyColumnMap.ThemeType, SchemaPolicyColumnMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyColumnMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapThemeByTestClient(themeType, readLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    // -----------------------------------------------------
    //                                               Prepare
    //                                               -------
    private SchemaPolicyWholeMap.Theme fetchWholeMapThemeByTestClient(SchemaPolicyWholeMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyTableMap.Theme fetchTableMapThemeByTestClient(SchemaPolicyTableMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyColumnMap.Theme fetchColumnMapThemeByTestClient(SchemaPolicyColumnMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyWholeMap.Theme fetchWholeMapTheme(File schemaPolicyMap, SchemaPolicyWholeMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.parseSchemePolicyMap(schemaPolicyMap).wholeMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyTableMap.Theme fetchTableMapTheme(File schemaPolicyMap, SchemaPolicyTableMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.parseSchemePolicyMap(schemaPolicyMap).tableMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyColumnMap.Theme fetchColumnMapTheme(File schemaPolicyMap, SchemaPolicyColumnMap.ThemeType targetType,
            DfpropSchemaPolicyReadLogic readLogic) {
        return readLogic.parseSchemePolicyMap(schemaPolicyMap).columnMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private File fetchSchemaPolicyMapFromTestClient(DfpropPhysicalLogic physicalLogic) {
        return physicalLogic.findDfpropFile(TEST_CLIENT_PROJECT, "schemaPolicyMap.dfprop");
    }

    private void copyNoSettingSchemaPolicyMap() throws IOException {
        copyFile("noSetting_schemaPolicyMap.dfprop");
    }

    private void copyFile(String fileName) throws IOException {
        File srcFile = prepareFileForTestResource(fileName);
        File destFile = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/" + fileName);
        FileUtils.copyFile(srcFile, destFile);
    }

    private File prepareFileForTestResource(String fileName) {
        return new File(getProjectDir(), TEST_RESOURCE_BASE + "/dfprop/" + fileName);
    }

    private SchemaPolicyMap createInputWholeMap(SchemaPolicyWholeMap.ThemeType themeType, boolean isActive) {
        SchemaPolicyWholeMap inputWholeMap =
                new SchemaPolicyWholeMap(Collections.singletonList(new SchemaPolicyWholeMap.Theme(themeType, isActive)));
        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), inputWholeMap, SchemaPolicyTableMap.noSettingInstance(),
                SchemaPolicyColumnMap.noSettingInstance());
    }

    private SchemaPolicyMap createInputTableMap(SchemaPolicyTableMap.ThemeType themeType, boolean isActive) {
        SchemaPolicyTableMap input = new SchemaPolicyTableMap(
                Collections.singletonList(new SchemaPolicyTableMap.Theme(themeType, isActive)), Collections.emptyList());
        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), SchemaPolicyWholeMap.noSettingInstance(), input,
                SchemaPolicyColumnMap.noSettingInstance());
    }

    private SchemaPolicyMap createInputColumnMap(SchemaPolicyColumnMap.ThemeType themeType, boolean isActive) {
        SchemaPolicyColumnMap input = new SchemaPolicyColumnMap(
                Collections.singletonList(new SchemaPolicyColumnMap.Theme(themeType, isActive)), Collections.emptyList());
        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), SchemaPolicyWholeMap.noSettingInstance(),
                SchemaPolicyTableMap.noSettingInstance(), input);
    }
}
