/*
 * Copyright 2014-2021 the original author or authors.
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

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author cabos
 * @author subaru
 * @author prprmurakami
 * @author jflute
 */
public class DfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<DfpropBean>> list(String projectName) {
        List<File> dfpropFileList = dfpropPhysicalLogic.findDfpropFileAllList(projectName);
        List<DfpropBean> beans = dfpropFileList.stream()
                .map(dfpropFile -> new DfpropBean(dfpropFile.getName(), flutyFileLogic.readFile(dfpropFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> update(String projectName, String fileName, DfpropUpdateBody body) {
        validate(body, messages -> {});

        File dfpropFile = dfpropPhysicalLogic.findDfpropFile(projectName, fileName);
        flutyFileLogic.writeFile(dfpropFile, body.content);

        return JsonResponse.asEmptyBody();
    }
}
