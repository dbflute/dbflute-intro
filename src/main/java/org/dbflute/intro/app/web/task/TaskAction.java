/*
 * Copyright 2014-2016 the original author or authors.
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

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.dbflute.intro.app.logic.task.TaskExecutionLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.dbflute.allcommon.CDef.TaskType;
import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 */
public class TaskAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TaskExecutionLogic taskExecutionLogic;
    @Resource
    private IntroClsAssist introClsAssist;
    @Resource
    private ResponseManager responseManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> execute(String project, AppCDef.TaskInstruction instruction, OptionalThing<String> env) {
        List<TaskType> taskTypeList = introClsAssist.toTaskTypeList(instruction);
        HttpServletResponse response = prepareTaskResponse();
        taskExecutionLogic.execute(project, taskTypeList, env, () -> response.getOutputStream());
        return JsonResponse.asEmptyBody();
    }

    private HttpServletResponse prepareTaskResponse() {
        HttpServletResponse response = responseManager.getResponse();
        response.setContentType("text/plain; charset=UTF-8");
        return response;
    }
}
