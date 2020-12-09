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
package org.dbflute.intro.app.web.playsql.migration;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.exception.DirNotFoundException;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.tellfailure.OpenDirNotFoundException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author cabos
 * @author prprmurakami
 */
public class PlaysqlMigrationAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PlaysqlMigrationLogic playsqlMigrationLogic;
    @Resource
    private ResponseManager responseManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> alter(String clientName) {
        try {
            playsqlMigrationLogic.openAlterDir(clientName);
        } catch (DirNotFoundException e) {
            String alterDirPath = playsqlMigrationLogic.buildAlterDirectoryPath(clientName);
            throw new OpenDirNotFoundException("alter directory is not found. dirPath: " + alterDirPath, alterDirPath);
        }
        return JsonResponse.asEmptyBody();
    }
}
