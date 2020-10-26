/*
 * Copyright 2014-2020 the original author or authors.
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
import org.dbflute.util.DfStringUtil;
import org.lastaflute.core.message.UserMessages;
import org.lastaflute.web.Execute;
import org.lastaflute.web.exception.Forced404NotFoundException;
import org.lastaflute.web.response.JsonResponse;

/**
 * Endpoint for reading log files.
 *
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
    public JsonResponse<LogBean> index(LogBody logBody) {
        validate(logBody, (moreValidator) -> {});
        return logPhysicalLogic.findLogFile(logBody.project, logBody.fileName).map((file) -> {
            return asJson(new LogBean(file.getName(), flutyFileLogic.readFile(file)));
        }).orElseGet(() -> {
            String debugMsg = "file not found fileName : " + logBody.fileName;
            throw new Forced404NotFoundException(debugMsg, UserMessages.empty());
        });
    }

    // TODO cabos implements all log (2019-10-20)
    @Execute(urlPattern = "{}/{}/@word")
    public JsonResponse<LogBean> latest(String clientName, String task) {
        return logPhysicalLogic.findLatestResultFile(clientName, task).map((file) -> {
            return asJson(new LogBean(file.getName(), cutOffErrorLogIfNeeds(flutyFileLogic.readFile(file))));
        }).orElseGet(() -> {
            return JsonResponse.asEmptyBody();
        });
    }

    // TODO cabos create log logic and move this method (2019-10-20)
    private String cutOffErrorLogIfNeeds(String content) {
        final String delimiter = "Look! Read the message below.";
        if (DfStringUtil.contains(content, delimiter)) {
            return DfStringUtil.substringLastRear(content, delimiter);
        } else {
            return content;
        }
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<LogBean>> list(String clientName) {
        List<File> logFileList = logPhysicalLogic.findLogFileAllList(clientName);
        List<LogBean> beans = logFileList.stream()
                .map(logFile -> new LogBean(logFile.getName(), flutyFileLogic.readFile(logFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }
}
