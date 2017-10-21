package org.dbflute.intro.app.web.task;

import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.optional.OptionalThing;
import org.dbflute.utflute.lastaflute.mock.TestingJsonData;
import org.junit.Test;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco at garden-place-plaza
 */
public class TaskActionTest extends UnitIntroTestCase {

    @Test
    public void test_execute() throws Exception {
        // ## Arrange ##
        TaskAction action = new TaskAction();
        inject(action);

        // ## Act ##
        JsonResponse<TaskExecutionResult> response =
            action.execute(TEST_CLIENT_PROJECT, AppCDef.TaskInstruction.ReplaceSchema, OptionalThing.empty());

        // ## Assert ##
        TestingJsonData<TaskExecutionResult> jsonData = validateJsonData(response);
        assertTrue(jsonData.getJsonResult().success);
    }

}
