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
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
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
        List<String> actualThemeList = extractThemeList(fetchSchemaPolicyWholeMap());
        wholeMapThemeList.forEach(theme -> {
            if (actualThemeList.contains(theme.type.code)) {
                assertTrue(theme.isActive);
            } else {
                assertFalse(theme.isActive);
            }
        });
    }

    @Test
    public void test_findSchemaPolicyMap_TableMap_containsAllThemeType() throws Exception {
        // ## Arrange ##
        DfpropInfoLogic logic = new DfpropInfoLogic();
        inject(logic);

        // ## Act ##
        SchemaPolicyTableMap tableMap = logic.findSchemaPolicyMap(TEST_CLIENT_PROJECT).tableMap;
        Set<SchemaPolicyTableMap.ThemeType> resultThemeTypeList =
                tableMap.themeList.stream().map(theme -> theme.type).collect(Collectors.toSet());

        // ## Assert ##
        assertTrue(resultThemeTypeList.containsAll(Arrays.asList(SchemaPolicyTableMap.ThemeType.values())));
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
            if (theme.isActive) {
                assertTrue(containsActualThemeList);
            } else {
                assertFalse(containsActualThemeList);
            }
        });
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
        Map<String, Object> wholeMap = (Map<String, Object>) schemaPolicyMapFromDfProp.get("tableMap");

        return wholeMap;
    }

    private List<String> extractThemeList(Map<String, Object> actualWholeMap) {
        @SuppressWarnings("unchecked")
        List<String> themeList = (List<String>) actualWholeMap.get("themeList");

        return themeList;
    }

    private Map<String, Object> fetchSchemaPolicyMap() {
        final File dfpropFile = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/schemaPolicyMap.dfprop");
        return new DfPropFile().readMap(dfpropFile.getAbsolutePath(), null);
    }
}
