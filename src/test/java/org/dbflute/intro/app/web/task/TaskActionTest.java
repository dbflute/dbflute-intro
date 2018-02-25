/*
 * Copyright 2014-2018 the original author or authors.
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
