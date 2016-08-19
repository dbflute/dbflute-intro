package org.dbflute.intro.app.web.client;

import java.util.List;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author jflute
 */
public class ClientActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                            Settgins
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
    //                                                                               Test
    //                                                                              ======
    public void test_list_success() throws Exception {
        // ## Arrange ##
        ClientAction action = new ClientAction();
        inject(action);

        // ## Act ##
        JsonResponse<List<ClientDetailResult>> response = action.list();

        // ## Assert ##
        showJson(response);
        TestingJsonData<List<ClientDetailResult>> jsonData = validateJsonData(response);
        List<ClientDetailResult> detailBeanList = jsonData.getJsonBean();
        assertHasAnyElement(detailBeanList);
    }
}
