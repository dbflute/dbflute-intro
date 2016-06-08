package org.dbflute.intro.app.web.client;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.unit.IntroBaseTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.util.List;

/**
 * @author deco
 */
public class ClientDfpropActionTest extends IntroBaseTestCase {

    public void test_index_success() throws Exception {
        // ## Arrange ##
        ClientDfpropAction action = new ClientDfpropAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<ClientDfpropBean>> response = action.index("decodb");

        // ## Assert ##
        TestingJsonData<List<ClientDfpropBean>> jsonData = validateJsonData(response);
        assertHasAnyElement(jsonData.getJsonBean());
        jsonData.getJsonBean().forEach(dfpropBean -> {
            log(dfpropBean.fileName);
            assertTrue(dfpropBean.fileName.endsWith(".dfprop"));
        });
    }

    public void test_update_success() throws Exception {
        // ## Arrange ##
        ClientDfpropAction action = new ClientDfpropAction();
        inject(action);
        String project = "decodb";
        ClientDfpropUpdateForm form = new ClientDfpropUpdateForm();
        form.fileName = "additionalForeignKeyMap.dfprop";
        form.content = "content";

        // ## Act ##
        action.update(project, form);

        // ## Assert ##
        File dfpropFile = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop/" + form.fileName);
        String content = FileUtils.readFileToString(dfpropFile, "utf-8");
        assertEquals(form.content, content);
    }
}
