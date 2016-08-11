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
package org.dbflute.intro.app.web.dfprop;

import org.dbflute.intro.app.logic.core.FileHandlingLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author deco
 */
public class DfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private FileHandlingLogic fileHandlingLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<DfpropBean>> list(String project) {
        List<File> dfpropFileList = dfpropPhysicalLogic.findDfpropFileAllList(project);
        List<DfpropBean> beans = dfpropFileList.stream()
                .map(dfpropFile -> new DfpropBean(dfpropFile.getName(), fileHandlingLogic.readFile(dfpropFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String project, String fileName, DfpropUpdateBody body) {
        validate(body, messages -> {});

        File dfpropFile = dfpropPhysicalLogic.findDfpropFile(project, fileName);
        fileHandlingLogic.writeFile(body.content, dfpropFile);

        return JsonResponse.asEmptyBody();
    }
}
