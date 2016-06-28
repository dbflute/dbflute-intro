package org.dbflute.intro.app.web.log;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.unit.IntroBaseTestCase;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.util.List;

/**
 * @author deco
 */
public class LogActionTest extends IntroBaseTestCase {

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
        File logDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + TEST_CLIENT_PROJECT + "/log/");
        File[] logFiles = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        if (logFiles != null) {
            for (File file : logFiles) {
                FileUtils.deleteQuietly(file);
            }
        }

        LogAction action = new LogAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<LogBean>> response = action.index(TEST_CLIENT_PROJECT);

        // ## Assert ##
        assertTrue(response.isReturnAsEmptyBody());
    }
}
