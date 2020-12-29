/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.app.web.log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 */
public class LogActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String LOG_FILE_PATH = "dbflute_" + TEST_CLIENT_PROJECT + "/log/";

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_index_success_no_files() throws Exception {
        // ## Arrange ##
        deleteLogFiles();

        LogAction action = new LogAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<LogBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<LogBean>> jsonData = validateJsonData(response);
        List<LogBean> logList = jsonData.getJsonResult(); // might be empty
        logList.forEach(log -> { // show only
            log(log);
        });
    }

    @Test
    public void test_index_success_have_files() throws IOException {
        // ## Arrange ##
        deleteLogFiles();

        String content = "content";
        createLogFile("test.log", content);
        createLogFile("test.txt", content);

        LogAction action = new LogAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<LogBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<LogBean>> jsonData = validateJsonData(response);
        List<LogBean> jsonBean = jsonData.getJsonResult();
        assertHasAnyElement(jsonBean);
        jsonBean.forEach(bean -> {
            assertTrue(bean.fileName.endsWith(".log"));
            assertEquals(content, bean.content);
        });
    }

    // ===================================================================================
    //                                                                         Test Helper
    //                                                                         ===========
    private void deleteLogFiles() {
        File logDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, LOG_FILE_PATH);
        File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (logFiles != null) {
            for (File file : logFiles) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    private void createLogFile(String fileName, String content) throws IOException {
        File logFile = new File(IntroPhysicalLogic.BASE_DIR_PATH, LOG_FILE_PATH + fileName);
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        logFile.createNewFile();
        FileUtils.write(logFile, content, StandardCharsets.UTF_8);
    }
}
