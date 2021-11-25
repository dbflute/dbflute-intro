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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.intro.app.logic.core.MapStringLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv;

/**
 * The logic of database information. (databaseInfoMap.dfprop)
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
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private MapStringLogic mapStringLogic;

    // ===================================================================================
    //                                                                           Find File
    //                                                                           =========
    public File findDfpropFile(String projectName) { // moved from ClientPhysicalLogic (2021/11/18)
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(dfpropPhysicalLogic.buildDfpropFilePath(projectName, DatabaseInfoMap.DFPROP_NAME));
    }

    // ===================================================================================
    //                                                                        Embedded Jar
    //                                                                        ============
    public boolean isEmbeddedJar(CDef.TargetDatabase target) {
        // done anyone can it be not null? (should be strict) by jflute (2021/04/18)
        IntroAssertUtil.assertNotNull(target);
        // done hakiba pri.B orElseTranslatingThrow() is better by jflute (2017/04/27)
        return databaseBhv.selectEntity(cb -> cb.query().setDatabaseCode_Equal_AsTargetDatabase(target))
                .map(database -> database.isEmbeddedJarFlgTrue())
                .orElseTranslatingThrow(cause -> new IllegalStateException("not found target database:" + target.alias(), cause));
    }

    // ===================================================================================
    //                                                                        Init Replace
    //                                                                        ============
    public Map<String, Object> prepareInitReplaceMap(DatabaseInfoMap databaseInfoMap) {
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        final DbConnectionBox dbConnectionBox = databaseInfoMap.getDbConnectionBox();
        replaceMap.put("@driver@", escapeControlMark(databaseInfoMap.getDriver()));
        replaceMap.put("@url@", escapeControlMark(dbConnectionBox.getUrl()));
        replaceMap.put("@schema@", escapeControlMark(dbConnectionBox.getSchema()));
        replaceMap.put("@user@", escapeControlMark(dbConnectionBox.getUser()));
        replaceMap.put("@password@", escapeControlMark(dbConnectionBox.getPassword()));
        return replaceMap;
    }

    private String escapeControlMark(Object value) {
        return mapStringLogic.escapeControlMark(value);
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    public void updateDatabaseInfoMap(ClientModel clientModel) {
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        replaceDfpropDatabaseInfoMap(databaseInfoMap, clientModel.getProjectInfra().getProjectName());
    }

    public void replaceDfpropDatabaseInfoMap(DatabaseInfoMap databaseInfoMap, String projectName) {
        final File dfpropFile = findDfpropFile(projectName);

        // #needs_fix anyone switch toString() to getPath() by jflute (2021/04/16)
        // File objects that are made in DBFlute intro uses slack as file separator
        // so you can getPath() here (no problem) however be careful with Windows headache
        // (while, FileTextIO should have rewrite methods that can accept File...?) 
        final String databaseInfoMapPath = dfpropFile.toString();
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
