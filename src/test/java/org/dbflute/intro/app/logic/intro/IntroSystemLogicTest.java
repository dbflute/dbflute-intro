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
package org.dbflute.intro.app.logic.intro;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos (at jjug2018 spring)
 */
public class IntroSystemLogicTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                             Pre and Post Processing
    //                                                             =======================
    // -----------------------------------------------------
    //                                             Attribute
    //                                             ---------
    private String decommentServerKey;

    // -----------------------------------------------------
    //                                            Processing
    //                                            ----------
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.decommentServerKey = System.getProperty(IntroSystemLogic.DECOMMENT_SERVER_KEY);
    }

    @Override
    public void tearDown() throws Exception {
        recoverSystemProperty(IntroSystemLogic.DECOMMENT_SERVER_KEY, decommentServerKey);
        super.tearDown();
    }

    private void recoverSystemProperty(String key, String cachedProperty) {
        if (cachedProperty != null) {
            System.setProperty(key, cachedProperty);
        } else {
            System.clearProperty(key);
        }
    }

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_isDecommentServer() throws Exception {
        // ### Arrange ###
        final IntroSystemLogic logic = new IntroSystemLogic();
        inject(logic);

        final String key = IntroSystemLogic.DECOMMENT_SERVER_KEY;
        System.setProperty(key, "true");

        // ### Act ###
        // ### Assert ###
        assertTrue(logic.isDecommentServer());
    }

    public void test_isNotDecommentServer() throws Exception {
        // ### Arrange ###
        final IntroSystemLogic logic = new IntroSystemLogic();
        inject(logic);

        final String key = IntroSystemLogic.DECOMMENT_SERVER_KEY;
        System.setProperty(key, "false");

        // ### Act ###
        // ### Assert ###
        assertFalse(logic.isDecommentServer());
    }

    public void test_isDecommentServer_illegalString() throws Exception {
        // ### Arrange ###
        final IntroSystemLogic logic = new IntroSystemLogic();
        inject(logic);

        final String key = IntroSystemLogic.DECOMMENT_SERVER_KEY;
        System.setProperty(key, "jjug2018");

        // ### Act ###
        // ### Assert ###
        assertFalse(logic.isDecommentServer());
    }
}
