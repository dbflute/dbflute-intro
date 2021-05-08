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
package org.dbflute.intro.app.logic.playsql.migration.info;

import java.io.File;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos
 * @author jflute
 */
public class PlaysqlMigrationInfoLogicTest extends UnitIntroTestCase {

    public void test_compareFileCreationTime() throws Exception {
        // ## Arrange ##
        final PlaysqlMigrationInfoLogic logic = new PlaysqlMigrationInfoLogic();
        inject(logic);

        String basePath = TEST_RESOURCE_PLAYSQL_DIR_PATH;
        final File f1 = new File(basePath + "/migration/history/201910/20191020_1207/checked-alter-to20190422-2332.zip");
        final File f2 = new File(basePath + "/migration/history/201910/20191021_1207/checked-alter-to20190422-2332.zip");

        // ## Act ##
        int result = logic.compareFileCreationTime(f1, f2); // from compareTo()

        // ## Assert ##
        assertEquals(result, -1);
    }
}
