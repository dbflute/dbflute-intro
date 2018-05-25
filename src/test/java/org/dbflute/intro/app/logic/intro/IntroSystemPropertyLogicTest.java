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
package org.dbflute.intro.app.logic.intro;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos
 */
public class IntroSystemPropertyLogicTest extends UnitIntroTestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        clearCache();
    }

    private void clearCache() {
        IntroSystemPropertyLogic.SYSTEM_PROPERTY_MAP.remove(IntroSystemPropertyLogic.DECOMMENT_SERVER_KEY);
    }

    public void test_isDecommentServer() throws Exception {
        // ### Arrange ###
        IntroSystemPropertyLogic logic = new IntroSystemPropertyLogic();
        inject(logic);
        System.clearProperty("intro.decomment.server");
        System.setProperty("intro.decomment.server", "true");

        // ### Act ###
        // ### Assert ###
        assertTrue(logic.isDecommentServer());
    }

    public void test_isNotDecommentServer() throws Exception {
        // ### Arrange ###
        IntroSystemPropertyLogic logic = new IntroSystemPropertyLogic();
        inject(logic);
        System.clearProperty("intro.decomment.server");
        System.setProperty("intro.decomment.server", "false");

        // ### Act ###
        // ### Assert ###
        assertFalse(logic.isDecommentServer());
    }
}
