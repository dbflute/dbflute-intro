package org.dbflute.intro.app.logic.dfprop;

import static org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap.ThemeType.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMapMeta;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DfpropUpdateLogicTest extends UnitIntroTestCase {

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

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetType, true);

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, infoLogic);
        assertTrue(afterTheme.isActive);
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

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetType, false);

        // ## Assert ##
        SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(targetType, infoLogic);
        assertFalse(afterTheme.isActive);
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

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, true);

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, infoLogic);
            assertTrue(afterTheme.isActive);
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

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced.");

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapThemeByTestClient(themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
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

            // ## Act ##
            logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeType, false);
            log(themeType.code + " was replaced.");

            // ## Assert ##
            SchemaPolicyWholeMap.Theme afterTheme = fetchWholeMapTheme(schemaPolicyMapFile, themeType, infoLogic);
            assertFalse(afterTheme.type.code, afterTheme.isActive);
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

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, true);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeListByTestClient(targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertTrue(theme.type.code, theme.isActive);
            log("Assert IsActive: " + theme.type.code);
        });
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

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, targetThemeTypeList, false);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeListByTestClient(targetThemeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertFalse(theme.type.code, theme.isActive);
            log("Assert IsNotActive: " + theme.type.code);
        });
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
        List<SchemaPolicyWholeMap.ThemeType> themeTypeList = Arrays.asList(SchemaPolicyWholeMap.ThemeType.values());

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeTypeList, true);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeList(schemaPolicyMapFile, themeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertTrue(theme.type.code, theme.isActive);
            log("Assert IsActive: " + theme.type.code);
        });
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
        List<SchemaPolicyWholeMap.ThemeType> themeTypeList = Arrays.asList(SchemaPolicyWholeMap.ThemeType.values());

        // ## Act ##
        logic.replaceWholeMapTheme(schemaPolicyMapFile, meta, themeTypeList, false);

        // ## Assert ##
        List<SchemaPolicyWholeMap.Theme> afterThemeList = fetchWholeMapThemeList(schemaPolicyMapFile, themeTypeList, infoLogic);
        afterThemeList.forEach(theme -> {
            assertFalse(theme.type.code, theme.isActive);
            log("Assert IsNotActive: " + theme.type.code);
        });
    }

    private SchemaPolicyWholeMap.Theme fetchWholeMapThemeByTestClient(SchemaPolicyWholeMap.ThemeType targetType,
            DfpropInfoLogic infoLogic) {
        return infoLogic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList.stream()
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
