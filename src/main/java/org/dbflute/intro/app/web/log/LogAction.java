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
package org.dbflute.intro.app.web.log;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.log.LogPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.core.exception.LaSystemException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author deco
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

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 index
    //                                                 -----
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<LogBean>> list(String project) {
        File[] logFiles = findLogFiles(project);
        if (logFiles == null) {
            return JsonResponse.asEmptyBody();
        }
        List<LogBean> beans = mappingToBeans(logFiles);
        return asJson(beans);
    }

    private File[] findLogFiles(String project) {
        File logDir = new File(logPhysicalLogic.buildLogPath(project));
        return logDir.listFiles((dir, name) -> name.endsWith(".log"));
    }

    private List<LogBean> mappingToBeans(File[] logFiles) {
        return Stream.of(logFiles).map(logFile -> {
            String content = "";
            try {
                content = FileUtils.readFileToString(logFile, "UTF-8");
            } catch (IOException e) {
                throw new LaSystemException("Cannot read the file: " + logFile);
            }
            return new LogBean(logFile.getName(), content);
        }).collect(Collectors.toList());
    }
}
