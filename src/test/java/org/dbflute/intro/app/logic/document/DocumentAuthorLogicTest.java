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
package org.dbflute.intro.app.logic.document;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos (at sumida jazz festival)
 */
public class DocumentAuthorLogicTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                             Pre and Post Processing
    //                                                             =======================
    // -----------------------------------------------------
    //                                             Attribute
    //                                             ---------
    private String originalUserName;

    // -----------------------------------------------------
    //                                            Processing
    //                                            ----------
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.originalUserName = System.getProperty(DocumentAuthorLogic.USER_NAME_KEY);
    }

    @Override
    public void tearDown() throws Exception {
        recoverSystemProperty(DocumentAuthorLogic.USER_NAME_KEY, originalUserName);
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
    public void test_getAuthor() throws Exception {
        // ## Arrange ##
        final DocumentAuthorLogic logic = new DocumentAuthorLogic();
        inject(logic);
        final String userName = "nanako.kono";
        System.setProperty(DocumentAuthorLogic.USER_NAME_KEY, userName);

        // ## Act ##
        String author = logic.getAuthor();

        // ## Assert ##
        assertEquals(userName, author);
    }
}
