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
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author hakiba
 */
public class DfpropInfoLogicTest extends UnitIntroTestCase {

    @Test
    public void test_findSchemaPolicyMap() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyMap schemaPolicyMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT);

        // ## Assert ##
        SchemaPolicyWholeMap wholeMap = schemaPolicyMap.wholeMap;
        List<SchemaPolicyWholeMap.Theme> wholeMapThemeList = wholeMap.themeList;

        // assert wholeMap theme not null
        assertNotNull(wholeMapThemeList);
        wholeMapThemeList.forEach(theme -> {
            assertNotNull(theme.type);
        });

        // assert contain all wholeMap Theme
        Set<SchemaPolicyWholeMap.ThemeType> actualWholeMapTypeList =
                wholeMapThemeList.stream().map(theme -> theme.type).collect(Collectors.toSet());
        boolean containAllWholeMapThemeType = actualWholeMapTypeList.containsAll(Arrays.asList(SchemaPolicyWholeMap.ThemeType.values()));
        assertTrue(containAllWholeMapThemeType);

        // assert fetched wholeMap theme status is correct
        final File dfpropFile =
                new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + TEST_CLIENT_PROJECT + "/dfprop/schemaPolicyMap.dfprop");
        Map<String, Object> schemaPolicyMapFromDfProp = new DfPropFile().readMap(dfpropFile.getAbsolutePath(), null);
        @SuppressWarnings("unchecked")
        Map<String, Object> wholeMapFromDfProp = (Map<String, Object>) schemaPolicyMapFromDfProp.get("wholeMap");
        @SuppressWarnings("unchecked")
        List<String> themeListFromDfProp = (List<String>) wholeMapFromDfProp.get("themeList");
        wholeMapThemeList.forEach(theme -> {
            if (themeListFromDfProp.contains(theme.type.code)) {
                assertTrue(theme.isActive);
            } else {
                assertFalse(theme.isActive);
            }
        });
    }

    @Test
    public void test_editSchemaPolicy() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        SchemaPolicyWholeMap.ThemeType testTargetThemeType = SchemaPolicyWholeMap.ThemeType.SameColumnAliasIfSameColumnName;
        boolean isActive = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).wholeMap.themeList.stream()
                .filter(theme -> testTargetThemeType == theme.type)
                .findAny()
                .map(theme -> theme.isActive)
                .orElseThrow(() -> new IllegalStateException());
        boolean toggledActive = !isActive;

        // ## Act ##
        logic.editSchemePolicyTheme(TEST_CLIENT_PROJECT, testTargetThemeType, toggledActive);

        // ## Assert ##
        SchemaPolicyMap schemaPolicyMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT);
        schemaPolicyMap.wholeMap.themeList.stream()
                .filter(theme -> testTargetThemeType == theme.type)
                .forEach(theme -> assertEquals(theme.isActive, toggledActive));
    }
}
