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
package org.dbflute.intro.app.logic.dfprop.database;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.intro.app.logic.client.ClientPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv;

/**
 * The logic of database information.
 * @author ryohei
 * @author jflute
 */
public class DatabaseInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClsTargetDatabaseBhv databaseBhv;
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;

    // ===================================================================================
    //                                                                        Embedded Jar
    //                                                                        ============
    public boolean isEmbeddedJar(CDef.TargetDatabase target) {
        // done hakiba pri.B orElseTranslatingThrow() is better by jflute (2017/04/27)
        if (target == null) {
            // #needs_fix anyone can it be not null? (should be strict) by jflute (2021/04/18)
            return false;
        }
        return databaseBhv.selectEntity(cb -> cb.query().setDatabaseCode_Equal_AsTargetDatabase(target))
                .map(database -> database.isEmbeddedJarFlgTrue())
                .orElseTranslatingThrow(cause -> new IllegalStateException("not found target database:" + target.alias(), cause));
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    public void updateDatabaseInfoMap(ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        replaceDfpropDatabaseInfoMap(databaseInfoMap, clientModel.getProjectInfra().getProjectName());
    }

    public void replaceDfpropDatabaseInfoMap(DatabaseInfoMap databaseInfoMap, String projectName) {
        final File dfpropDatabaseInfoMap = clientPhysicalLogic.findDfpropDatabaseInfoMap(projectName);

        // #needs_fix anyone switch toString() to getPath() by jflute (2021/04/16)
        // File objects that are made in DBFlute intro uses slack as file separator
        // so you can getPath() here (no problem) however be careful with Windows headache
        // (while, FileTextIO should have rewrite methods that can accept File...?) 
        final String databaseInfoMapPath = dfpropDatabaseInfoMap.toString();
        final DbConnectionBox box = databaseInfoMap.getDbConnectionBox();

        // depends on the format of the dbflute_dfclient template (basically no change so almost no problem)
        new FileTextIO().encodeAsUTF8().rewriteFilteringLine(databaseInfoMapPath, line -> {
            String trimmedLine = line.trim(); // for determination
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
