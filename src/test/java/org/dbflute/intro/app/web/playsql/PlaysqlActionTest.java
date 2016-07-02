package org.dbflute.intro.app.web.playsql;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.unit.IntroBaseTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.util.List;

/**
 * @author deco
 */
public class PlaysqlActionTest extends IntroBaseTestCase {

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
    @Test
    public void test_list_success() throws Exception {
        // ## Arrange ##
        PlaysqlAction action = new PlaysqlAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<PlaysqlBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<PlaysqlBean>> jsonData = validateJsonData(response);
        List<PlaysqlBean> beans = jsonData.getJsonBean();
        assertHasAnyElement(beans);
        beans.forEach(bean -> {
            log(bean.fileName);
            assertTrue(bean.fileName.endsWith(".sql"));
        });
    }

    @Test
    public void test_update_success() throws Exception {
        // ## Arrange ##
        PlaysqlAction action = new PlaysqlAction();
        inject(action);

        PlaysqlUpdateForm form = new PlaysqlUpdateForm();
        File playsqlDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/");
        File playsqlBefore = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"))[0];
        String fileName = playsqlBefore.getName();
        form.content = "content";

        // ## Act ##
        action.update(TEST_CLIENT_PROJECT, fileName, form);

        // ## Assert ##
        File playsqlAfter = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/" + fileName);
        String content = FileUtils.readFileToString(playsqlAfter, "UTF-8");
        log(fileName, content);
        assertEquals(form.content, content);
    }
}
