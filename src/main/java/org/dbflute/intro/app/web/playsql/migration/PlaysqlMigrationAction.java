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

import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author cabos
 */
public class PlaysqlMigrationAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DocumentPhysicalLogic documentPhysicalLogic;

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> alter(String clientProject) {
        OptionalThing<File> optAlterDir = documentPhysicalLogic.findAlterDir(clientProject);
        if (optAlterDir.isPresent()) {
            File file = optAlterDir.get();
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);
            } catch (IOException e) {
                throw new UncheckedIOException("fail to open alter directory of" + clientProject, e);
            }
            return JsonResponse.asEmptyBody();
        } else {
            throwVerifiedClientError(clientProject + " has not alter directory");
            return  null; // not reached
        }
    }
}
