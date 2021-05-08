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
package org.dbflute.intro.app.logic.settings;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;

/**
 * The logic for update of settings. (e.g. databaseInfoMap)
 * @author hakiba
 * @author jflute
 */
public class SettingsUpdateLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    // #needs_fix anyone can move to DatabaseInfoLogic? by jflute (2021/05/01)
    public void updateDatabaseInfoMap(ClientModel clientModel) {
        replaceDfpropDatabaseInfoMap(clientModel, clientModel.getProjectInfra().getClientProject());
    }

    // #needs_fix anyone maybe same as DfpropUpdateLogic's one. should be recycle by jflute (2021/05/01)
    private void replaceDfpropDatabaseInfoMap(ClientModel clientModel, String projectName) {
        final File dfpropDatabaseInfoMap = clientPhysicalLogic.findDfpropDatabaseInfoMap(projectName);
        final String databaseInfoMapPath = dfpropDatabaseInfoMap.toString();
        final DbConnectionBox box = clientModel.getDatabaseInfoMap().getDbConnectionBox();

        new FileTextIO().encodeAsUTF8().rewriteFilteringLine(databaseInfoMapPath, line -> {
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("; url") && line.contains("=")) {
                return "    ; url      = " + StringUtils.defaultString(box.getUrl());
            }
            if (trimmedLine.startsWith("; schema") && line.contains("=")) {
                return "    ; schema   = " + StringUtils.defaultString(box.getSchema());
            }
            if (trimmedLine.startsWith("; user") && line.contains("=")) {
                return "    ; user     = " + StringUtils.defaultString(box.getUser());
            }
            if (trimmedLine.startsWith("; password") && line.contains("=")) {
                return "    ; password = " + StringUtils.defaultString(box.getPassword());
            }
            return line;
        });
    }
}
