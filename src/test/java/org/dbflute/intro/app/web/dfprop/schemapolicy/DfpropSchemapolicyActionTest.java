/*
 * Copyright 2014-2026 the original author or authors.
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
package org.dbflute.intro.app.web.dfprop.schemapolicy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;
import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author cabos
 */
public class DfpropSchemapolicyActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    /**
     * themeListとstatementListの両方に設定がある場合、エラーなくレスポンスが返ること。
     */
    public void test_index_hasSetting() throws Exception {
        // ## Arrange ##
        DfpropSchemapolicyAction action = new DfpropSchemapolicyAction();
        inject(action);
        prepareSchemaPolicyMap("dfprop/schemaPolicyMap.dfprop");

        // ## Act ##
        action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // 例外が発生しないこと
    }

    /**
     * schemaPolicyMap.dfpropの設定が空の場合、エラーなくレスポンスが返ること。
     */
    public void test_index_emptySettings() throws Exception {
        // ## Arrange ##
        DfpropSchemapolicyAction action = new DfpropSchemapolicyAction();
        inject(action);
        prepareSchemaPolicyMap("dfprop/noSetting_schemaPolicyMap.dfprop");

        // ## Act ##
        action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // 例外が発生しないこと
    }

    /**
     * schemaPolicyMap.dfpropが存在しない場合、DfpropFileNotFoundExceptionが発生すること。
     */
    public void test_index_fileNotExists() throws Exception {
        // ## Arrange ##
        DfpropSchemapolicyAction action = new DfpropSchemapolicyAction();
        inject(action);
        File schemaPolicyMapFile = findTestClientFile("dfprop/schemaPolicyMap.dfprop");
        schemaPolicyMapFile.delete();

        // ## Act & Assert ##
        assertException(DfpropFileNotFoundException.class, () -> action.index(TEST_CLIENT_PROJECT));
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private void prepareSchemaPolicyMap(String filePath) {
        File srcFile = findTestResourceFile(filePath);
        File destFile = findTestClientFile("dfprop/schemaPolicyMap.dfprop");
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy file: src=" + srcFile + ", dest=" + destFile, e);
        }
    }
}
