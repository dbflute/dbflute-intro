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
package org.dbflute.intro.app.web.playsql.migration.alter;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.exception.DirNotFoundException;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationPhysicalLogic;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationUpdateLogic;
import org.dbflute.intro.app.logic.playsql.migration.info.PlaysqlMigrationReadLogic;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationDirReturn;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.bizfw.tellfailure.OpenDirNotFoundException;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 * @author prprmurakami
 * @author jflute
 */
public class PlaysqlMigrationAlterAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PlaysqlMigrationReadLogic playsqlMigrationReadLogic;
    @Resource
    private PlaysqlMigrationUpdateLogic playsqlMigrationUpdateLogic;
    @Resource
    private PlaysqlMigrationPhysicalLogic playsqlMigrationPhysicalLogic;
    @Resource
    private PlaysqlMigrationAlterAssist playsqlMigrationAlterAssist;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    /**
     * Respond migration directory information of dbflute_client/playsql/migration.
     * @param projectName name of client project (NotNull)
     * @return migration directory information (NotNull)
     */
    @Execute
    public JsonResponse<AlterSQLResult> index(String projectName) {
        PlaysqlMigrationDirReturn migrationDirReturn = playsqlMigrationReadLogic.loadPlaysqlMigrationDir(projectName);
        AlterSQLResult alterSQLResult = playsqlMigrationAlterAssist.mappingAlterSQLResult(migrationDirReturn);
        return asJson(alterSQLResult);
    }

    @Execute
    public JsonResponse<Void> open(String projectName) {
        try {
            playsqlMigrationPhysicalLogic.openAlterDir(projectName);
        } catch (DirNotFoundException e) {
            throw new OpenDirNotFoundException("alter directory is not found. dirPath: " + e.getDirPath(), e.getDirPath());
        }
        return JsonResponse.asEmptyBody();
    }

    @Execute
    @NotAvailableDecommentServer
    public JsonResponse<Void> prepare(String projectName) {
        playsqlMigrationUpdateLogic.unzipCheckedAlterZip(projectName);
        playsqlMigrationUpdateLogic.copyUnreleasedAlterDir(projectName);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    @NotAvailableDecommentServer
    public JsonResponse<Void> create(String projectName, AlterCreateBody body) {
        validate(body, messages -> moreValidate(projectName, body, messages));
        playsqlMigrationUpdateLogic.createAlterSql(projectName, body.alterFileName);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    private void moreValidate(String projectName, AlterCreateBody body, IntroMessages messages) {
        playsqlMigrationAlterAssist.moreValidateCreate(projectName, body, messages);
    }
}
