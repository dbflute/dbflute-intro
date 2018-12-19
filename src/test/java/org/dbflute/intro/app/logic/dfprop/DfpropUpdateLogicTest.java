package org.dbflute.intro.app.logic.dfprop;

import static org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap.ThemeType.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMapMeta;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DfpropUpdateLogicTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                           Whole Map
    //                                                                           =========
    @Test
    public void test_replaceWholeMapTheme_ChangeToActive() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final SchemaPolicyWholeMap.ThemeType targetType = SchemaPolicyWholeMap.ThemeType.SameColumnDbTypeIfSameColumnName;
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetType, true);

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, infoLogic);
        assertTrue(afterTheme.isActive);
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetType, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToNotActive() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final SchemaPolicyWholeMap.ThemeType targetType = SchemaPolicyWholeMap.ThemeType.SameColumnDbTypeIfSameColumnName;
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetType, false);

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, infoLogic);
        assertFalse(afterTheme.isActive);
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetType, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, true);

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, infoLogic);
            assertTrue(afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced.");

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceWholeMapTheme_ChangeToNotActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        for (SchemaPolicyWholeMap.ThemeType themeType : SchemaPolicyWholeMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced.");

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertWholeMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceWholeMapTheme_MultipleTheme_ChangeToActive() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final List<SchemaPolicyWholeMap.ThemeType> targetThemeTypeList =
                Arrays.asList(UniqueTableAlias, SameColumnSizeIfSameColumnName, SameColumnDbTypeIfSameColumnName);
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, true);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeListByTestClient(targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertTrue(theme.type.code, theme.isActive);
            log("Assert IsActive: " + theme.type.code);
        });
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetThemeTypeList, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_MultipleTheme_ChangeToNotActive() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        final List<SchemaPolicyWholeMap.ThemeType> targetThemeTypeList =
                Arrays.asList(UniqueTableAlias, SameColumnSizeIfSameColumnName, SameColumnDbTypeIfSameColumnName);
        final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, false);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeListByTestClient(targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertFalse(theme.type.code, theme.isActive);
            log("Assert IsNotActive: " + theme.type.code);
        });
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetThemeTypeList, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_MultipleTheme_ChangeToActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.ThemeType> targetThemeTypeList = Arrays.asList(SchemaPolicyWholeMap.ThemeType.values());
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, true);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeList(schemaPolicyMapFile, targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertTrue(theme.type.code, theme.isActive);
            log("Assert IsActive: " + theme.type.code);
        });
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetThemeTypeList, beforeThemeList);
    }

    @Test
    public void test_replaceWholeMapTheme_MultipleTheme_ChangeToNotActive_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
        final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
        final List<SchemaPolicyWholeMap.ThemeType> targetThemeTypeList = Arrays.asList(SchemaPolicyWholeMap.ThemeType.values());
        final List<SchemaPolicyWholeMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList;

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, false);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeList(schemaPolicyMapFile, targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertFalse(theme.type.code, theme.isActive);
            log("Assert IsNotActive: " + theme.type.code);
        });
        assertWholeMapNotChangeOtherThemeType(infoLogic, targetThemeTypeList, beforeThemeList);
    }

    // ===================================================================================
    //                                                                           Table Map
    //                                                                           =========
    @Test
    public void test_replaceTableMapThemeList_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.replaceTableMapTheme(schemaPolicyMapFile, meta, themeType, true);

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceTableMapThemeList_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.replaceTableMapTheme(schemaPolicyMapFile, meta, themeType, false);

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceTableMapTheme_ChangeToActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.replaceTableMapTheme(schemaPolicyMapFile, meta, themeType, true);
            log(themeType.code + " was replaced to true.");

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceTableapTheme_ChangeToNotActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        for (SchemaPolicyTableMap.ThemeType themeType : SchemaPolicyTableMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyTableMap.Theme> beforeThemeList = infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList;

            // ## Act ##
            logic.replaceTableMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced to false.");

            // ## Assert ##
            SchemaPolicyTableMap.Theme afterTheme = fetchTableMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertTableMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    // ===================================================================================
    //                                                                          Column Map
    //                                                                          ==========
    @Test
    public void test_replaceColumnMapThemeList_ChangeToActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.replaceColumnMapTheme(schemaPolicyMapFile, meta, themeType, true);
            log(themeType + "was replaced to true.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceColumnMapThemeList_ChangeToNotActiveAllType() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = fetchSchemaPolicyMapFromTestClient(physicalLogic);
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.replaceColumnMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType + "was replaced to false.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceColumnMapTheme_ChangeToActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.replaceColumnMapTheme(schemaPolicyMapFile, meta, themeType, true);
            log(themeType.code + " was replaced to true.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertTrue(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    @Test
    public void test_replaceColumnMapTheme_ChangeToNotActive_In_MultipleLineThemeList() throws Exception {
        // ## Arrange ##
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DfpropInfoLogic infoLogic = new DfpropInfoLogic();
        inject(infoLogic);
        DfpropPhysicalLogic physicalLogic = new DfpropPhysicalLogic();
        inject(physicalLogic);

        copyMultipleLineThemeListSchemaPolicyMap();

        for (SchemaPolicyColumnMap.ThemeType themeType : SchemaPolicyColumnMap.ThemeType.values()) {
            final File schemaPolicyMapFile = prepareFileForTestClient("/dfprop/multipleLineThemeList_schemaPolicyMap.dfprop");
            final SchemaPolicyMapMeta meta = logic.extractSchemaPolicyMeta(TEST_CLIENT_PROJECT, schemaPolicyMapFile);
            final List<SchemaPolicyColumnMap.Theme> beforeThemeList =
                    infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList;

            // ## Act ##
            logic.replaceColumnMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced to false.");

            // ## Assert ##
            SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
            assertColumnMapNotChangeOtherThemeType(infoLogic, themeType, beforeThemeList);
        }
    }

    // ===================================================================================
    //                                                                             Private
    //                                                                             =======
    // -----------------------------------------------------
    //                                                Assert
    //                                                ------
    private void assertWholeMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, SchemaPolicyWholeMap.ThemeType targetType,
            List<SchemaPolicyWholeMap.Theme> beforeThemeList) {
        assertWholeMapNotChangeOtherThemeType(infoLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertWholeMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, List<SchemaPolicyWholeMap.ThemeType> targetTypeList,
            List<SchemaPolicyWholeMap.Theme> beforeThemeList) {
        Map<SchemaPolicyWholeMap.ThemeType, SchemaPolicyWholeMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyWholeMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, infoLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    private void assertTableMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, SchemaPolicyTableMap.ThemeType targetType,
            List<SchemaPolicyTableMap.Theme> beforeThemeList) {
        assertTableMapNotChangeOtherThemeType(infoLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertTableMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, List<SchemaPolicyTableMap.ThemeType> targetTypeList,
            List<SchemaPolicyTableMap.Theme> beforeThemeList) {
        Map<SchemaPolicyTableMap.ThemeType, SchemaPolicyTableMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyTableMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyTableMap.Theme afterTheme = fetchTableMapThemeByTestClient(themeType, infoLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    private void assertColumnMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, SchemaPolicyColumnMap.ThemeType targetType,
            List<SchemaPolicyColumnMap.Theme> beforeThemeList) {
        assertColumnMapNotChangeOtherThemeType(infoLogic, Collections.singletonList(targetType), beforeThemeList);
    }

    private void assertColumnMapNotChangeOtherThemeType(DfpropInfoLogic infoLogic, List<SchemaPolicyColumnMap.ThemeType> targetTypeList,
            List<SchemaPolicyColumnMap.Theme> beforeThemeList) {
        Map<SchemaPolicyColumnMap.ThemeType, SchemaPolicyColumnMap.Theme> beforeThemeMap =
                beforeThemeList.stream().collect(Collectors.toMap(v -> v.type, Function.identity()));

        Arrays.stream(SchemaPolicyColumnMap.ThemeType.values())
                .filter(themeType -> !targetTypeList.contains(themeType))
                .forEach(themeType -> {
                    SchemaPolicyColumnMap.Theme afterTheme = fetchColumnMapThemeByTestClient(themeType, infoLogic);
                    log("themeType : " + themeType + "is before isActive = " + beforeThemeMap.get(themeType).isActive
                            + ", and after isActive = " + afterTheme.isActive);
                    assertEquals(beforeThemeMap.get(themeType).isActive, afterTheme.isActive);
                });
    }

    // -----------------------------------------------------
    //                                               Prepare
    //                                               -------
    private SchemaPolicyWholeMap.Theme fetchWholeMapThemeByTestClient(SchemaPolicyWholeMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyTableMap.Theme fetchTableMapThemeByTestClient(SchemaPolicyTableMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyColumnMap.Theme fetchColumnMapThemeByTestClient(SchemaPolicyColumnMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).columnMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyWholeMap.Theme fetchWholeMapTheme(File schemaPolicyMap, SchemaPolicyWholeMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.parseSchemePolicyMap(schemaPolicyMap).wholeMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyTableMap.Theme fetchTableMapTheme(File schemaPolicyMap, SchemaPolicyTableMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.parseSchemePolicyMap(schemaPolicyMap).tableMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private SchemaPolicyColumnMap.Theme fetchColumnMapTheme(File schemaPolicyMap, SchemaPolicyColumnMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.parseSchemePolicyMap(schemaPolicyMap).columnMap.themeList.stream()
                .filter(theme -> targetType == theme.type)
                .findAny()
                .orElseThrow(() -> new IllegalStateException());
    }

    private List<SchemaPolicyWholeMap.Theme> fetchWholeMapThemeListByTestClient(List<SchemaPolicyWholeMap.ThemeType> targetTypeList,
            DfpropInfoLogic infoLogic) {
        return infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList.stream()
                .filter(theme -> targetTypeList.contains(theme.type))
                .collect(Collectors.toList());
    }

    private List<SchemaPolicyWholeMap.Theme> fetchWholeMapThemeList(File schemaPolicyMap,
            List<SchemaPolicyWholeMap.ThemeType> targetTypeList, DfpropInfoLogic infoLogic) {
        return infoLogic.parseSchemePolicyMap(schemaPolicyMap).wholeMap.themeList.stream()
                .filter(theme -> targetTypeList.contains(theme.type))
                .collect(Collectors.toList());
    }

    private File fetchSchemaPolicyMapFromTestClient(DfpropPhysicalLogic physicalLogic) {
        return physicalLogic.findDfpropFile(TEST_CLIENT_PROJECT, "schemaPolicyMap.dfprop");
    }

    private void copyMultipleLineThemeListSchemaPolicyMap() throws IOException {
        copyFile("multipleLineThemeList_schemaPolicyMap.dfprop");
    }

    private void copyFile(String fileName) throws IOException {
        File srcFile = prepareFileForTestResource(fileName);
        File destFile = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/" + fileName);
        FileUtils.copyFile(srcFile, destFile);
    }
    private File prepareFileForTestResource(String fileName) {
        return new File(getProjectDir(), "/src/test/resources/dfprop/" + fileName);
    }

    private File prepareFileForTestClient(String filePath) {
        return new File(getProjectDir(), TEST_CLIENT_PATH + "/" + filePath);
    }
}
