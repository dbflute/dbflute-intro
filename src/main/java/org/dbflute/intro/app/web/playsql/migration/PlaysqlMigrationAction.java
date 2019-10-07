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
package org.dbflute.intro.app.web.playsql.migration;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.playsql.migrate.PlaysqlMigrateLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 */
public class PlaysqlMigrationAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PlaysqlMigrateLogic playsqlMigrateLogic;

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> alter(String clientProject) {
        playsqlMigrateLogic.openAlterDir(clientProject);
        return JsonResponse.asEmptyBody();
    }
}
