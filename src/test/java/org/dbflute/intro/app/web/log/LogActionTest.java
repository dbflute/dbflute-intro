package org.dbflute.intro.app.web.log;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.unit.IntroBaseTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author deco
 */
public class LogActionTest extends IntroBaseTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String LOG_FILE_PATH = "dbflute_" + TEST_CLIENT_PROJECT + "/log/";

    // ===================================================================================
    //                                                                            Settings
    //                                                                            ========
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createTestClient();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        deleteTestClient();
    }

    // ===================================================================================
    //                                                                                Test
    //                                                                                ====
    public void test_index_success_no_files() throws Exception {
        // ## Arrange ##
        deleteLogFiles();

        LogAction action = new LogAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<LogBean>> response = action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        assertTrue(response.isReturnAsEmptyBody());
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
        JsonResponse<List<LogBean>> response = action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<LogBean>> jsonData = validateJsonData(response);
        List<LogBean> jsonBean = jsonData.getJsonBean();
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
        File logDir =  new File(DbFluteIntroLogic.BASE_DIR_PATH, LOG_FILE_PATH);
        File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (logFiles != null) {
            for (File file : logFiles) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    private void createLogFile(String fileName, String content) throws IOException {
        File logFile = new File(DbFluteIntroLogic.BASE_DIR_PATH, LOG_FILE_PATH + fileName);
        if (!logFile.getParentFile().exists()) {
            logFile.getParentFile().mkdirs();
        }
        logFile.createNewFile();
        FileUtils.write(logFile, content);
    }
}
