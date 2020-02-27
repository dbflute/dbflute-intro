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
package org.dbflute.intro.app.web.playsql;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.playsql.PlaysqlPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author jflute
 */
public class PlaysqlAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PlaysqlPhysicalLogic playsqlPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    // #forgot deco pri.C with directory? by jflute (2016/07/26)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<PlaysqlBean>> list(String clientName) {
        List<File> playsqlFileList = playsqlPhysicalLogic.findPlaysqlFileAllList(clientName);
        List<PlaysqlBean> beans = playsqlFileList.stream()
                .map(playsqlFile -> new PlaysqlBean(playsqlFile.getName(), flutyFileLogic.readFile(playsqlFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    // #forgot jflute pri.C intro: needs adjustment? (2016/07/26)
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String clientName, String fileName, PlaysqlUpdateBody body) {
        validate(body, messages -> {});

        File playsqlFile = playsqlPhysicalLogic.findPlaysqlFile(clientName, fileName);
        flutyFileLogic.writeFile(playsqlFile, body.content);

        return JsonResponse.asEmptyBody();
    }
}
