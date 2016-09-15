package org.dbflute.intro.app.web.playsql;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author jflute
 */
public class PlaysqlActionTest extends UnitIntroTestCase {

    @Resource
    private FlutyFileLogic flutyFileLogic;

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

        PlaysqlUpdateBody body = new PlaysqlUpdateBody();
        File playsqlDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/");
        File playsqlBefore = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"))[0];
        String fileName = playsqlBefore.getName();
        body.content = "content";

        // ## Act ##
        action.update(TEST_CLIENT_PROJECT, fileName, body);

        // ## Assert ##
        File playsqlAfter = new File(getProjectDir(), TEST_CLIENT_PATH + "/playsql/" + fileName);
        String content = flutyFileLogic.readFile(playsqlAfter);
        log(fileName, content);
        assertEquals(body.content, content);
    }
}
