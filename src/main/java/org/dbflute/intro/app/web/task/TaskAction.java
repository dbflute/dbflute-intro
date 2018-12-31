/*
 * Copyright 2014-2019 the original author or authors.
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

import org.dbflute.intro.app.logic.log.LogPhysicalLogic;
import org.dbflute.intro.app.logic.task.TaskExecutionLogic;
import org.dbflute.intro.app.logic.task.TaskExecutionLogic.SchemaNotSynchronizedException;
import org.dbflute.intro.app.logic.task.TaskExecutionLogic.TaskErrorResultException;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.bizfw.tellfailure.TaskExecuteFailureException;
import org.dbflute.intro.dbflute.allcommon.CDef.TaskType;
import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 * @author deco
 * @author jflute
 * @author cabos
 */
@NotAvailableDecommentServer
public class TaskAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TaskExecutionLogic taskExecutionLogic;
    @Resource
    private IntroClsAssist introClsAssist;
    @Resource
    private LogPhysicalLogic logPhysicalLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<TaskExecutionResult> execute(String project, AppCDef.TaskInstruction instruction, OptionalThing<String> env) {
        List<TaskType> taskTypeList = introClsAssist.toTaskTypeList(instruction);
        try {
            taskExecutionLogic.execute(project, taskTypeList, env);
        } catch (SchemaNotSynchronizedException e) {
            return asJson(new TaskExecutionResult(false));
        } catch (TaskErrorResultException e) {
            int resultCode = e.getResultCode();
            String processLog = e.getProcessLog();
            loggingLastFailure(project, instruction, processLog);
            String debugMsg =
                    "Failed to execute the tasks: project=" + project + ", taskTypeList=" + taskTypeList + ", resultCode=" + resultCode;
            throw new TaskExecuteFailureException(debugMsg, processLog, e);
        }
        return asJson(new TaskExecutionResult(true));
    }

    private void loggingLastFailure(String project, AppCDef.TaskInstruction instruction, String msg) {
        logPhysicalLogic.logging(project, "intro-last-execute-failure-" + instruction.code() + ".log", msg);
    }
}
