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
package org.dbflute.intro.app.web.log;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.log.LogPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.core.message.UserMessages;
import org.lastaflute.web.Execute;
import org.lastaflute.web.exception.Forced404NotFoundException;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author jflute
 * @author cabos
 */
public class LogAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private LogPhysicalLogic logPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 index
    //                                                 -----
    @Execute
    public JsonResponse<LogBean> index(LogForm logForm) {
        validate(logForm, (moreValidator) -> {});
        return logPhysicalLogic.findLogFile(logForm.project, logForm.fileName).map((file) -> {
            return asJson(new LogBean(file.getName(), flutyFileLogic.readFile(file)));
        }).orElseGet(() -> {
            String debugMsg = "file not found fileName : " + logForm.fileName;
            throw new Forced404NotFoundException(debugMsg, UserMessages.empty());
        });
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<LogBean>> list(String project) {
        List<File> logFileList = logPhysicalLogic.findLogFileAllList(project);
        List<LogBean> beans = logFileList.stream()
                .map(logFile -> new LogBean(logFile.getName(), flutyFileLogic.readFile(logFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }
}
