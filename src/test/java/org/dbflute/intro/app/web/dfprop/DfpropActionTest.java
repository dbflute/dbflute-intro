package org.dbflute.intro.app.web.dfprop;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.util.List;

/**
 * @author deco
 */
public class DfpropActionTest extends UnitIntroTestCase {

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
    public void test_index_success() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<DfpropBean>> response = action.list(TEST_CLIENT_PROJECT);

        // ## Assert ##
        TestingJsonData<List<DfpropBean>> jsonData = validateJsonData(response);
        assertHasAnyElement(jsonData.getJsonBean());
        jsonData.getJsonBean().forEach(dfpropBean -> {
            log(dfpropBean.fileName);
            assertTrue(dfpropBean.fileName.endsWith(".dfprop"));
        });
    }

    public void test_update_success() throws Exception {
        // ## Arrange ##
        DfpropAction action = new DfpropAction();
        inject(action);

        DfpropUpdateBody body = new DfpropUpdateBody();
        File dfpropDir = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/");
        File dfpropBefore = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"))[0];
        String fileName = dfpropBefore.getName();
        body.content = "content";

        // ## Act ##
        action.update(TEST_CLIENT_PROJECT, fileName, body);

        // ## Assert ##
        File dfpropAfter = new File(getProjectDir(), TEST_CLIENT_PATH + "/dfprop/" + fileName);
        String content = FileUtils.readFileToString(dfpropAfter, "UTF-8");
        log(fileName, content);
        assertEquals(body.content, content);
    }
}
