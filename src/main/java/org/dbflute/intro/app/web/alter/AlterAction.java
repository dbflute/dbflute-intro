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
package org.dbflute.intro.app.web.alter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationAlterSqlReturn;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationDirReturn;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.util.DfStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * Api for alter check support.
 *
 * @author subaru
 * @author cabos
 */
public class AlterAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PlaysqlMigrationLogic playsqlMigrationLogic;

    // ===================================================================================
    //                                                                               Index
    //                                                                               =====
    /**
     * Respond migration directory information of dbflute_client/playsql/migration.
     * @param clientName client project (NotNull)
     * @return migration directory information (NotNull)
     */
    @Execute
    public JsonResponse<AlterSQLResult> index(String clientName) {
        return asJson(mappingAlterSQLResult(playsqlMigrationLogic.loadPlaysqlMigrationDir(clientName)));
    }

    // -----------------------------------------------------
    //                                               mapping
    //                                               -------
    private AlterSQLResult mappingAlterSQLResult(PlaysqlMigrationDirReturn bean) {
        AlterSQLResult result = new AlterSQLResult();
        result.ngMarkFile = mappingNgMarkFileResult(bean);
        result.editingFiles = mappingAlterEditingFilePart(bean);
        result.checkedZip = mappingCheckedZipPart(bean);
        result.unreleasedDir = mappingUnreleasedDirPart(bean);
        return result;
    }

    private AlterSQLResult.NgMarkFilePart mappingNgMarkFileResult(PlaysqlMigrationDirReturn bean) {
        return bean.getNgMark().map(ngMark -> {
            AlterSQLResult.NgMarkFilePart part = new AlterSQLResult.NgMarkFilePart();
            part.ngMark = ngMark.getNgMark();
            part.content = ngMark.getContent();
            return part;
        }).orElse(null);
    }

    private List<AlterSQLResult.SQLFilePart> mappingAlterEditingFilePart(PlaysqlMigrationDirReturn bean) {
        return bean.getAlterSqlBeanList().stream().map(editingFile -> {
            AlterSQLResult.SQLFilePart filePart = new AlterSQLResult.SQLFilePart();
            filePart.fileName = editingFile.fileName;
            filePart.content = editingFile.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    private AlterSQLResult.CheckedZipPart mappingCheckedZipPart(PlaysqlMigrationDirReturn bean) {
        return bean.getCheckedZipBean().map(checkedZipBean -> {
            AlterSQLResult.CheckedZipPart checkedZipPart = new AlterSQLResult.CheckedZipPart();
            checkedZipPart.fileName = checkedZipBean.getFileName();
            checkedZipPart.checkedFiles = mappingSqlFileBean(checkedZipBean.getCheckedSqlList());
            return checkedZipPart;
        }).orElse(null);
    }

    private AlterSQLResult.UnreleasedDirPart mappingUnreleasedDirPart(PlaysqlMigrationDirReturn bean) {
        return bean.getUnreleasedDirBean().map(dirBean -> {
            AlterSQLResult.UnreleasedDirPart dirPart = new AlterSQLResult.UnreleasedDirPart();
            dirPart.checkedFiles = mappingSqlFileBean(dirBean.getCheckedSqlList());
            return dirPart;
        }).orElse(null);
    }

    private List<AlterSQLResult.SQLFilePart> mappingSqlFileBean(List<PlaysqlMigrationAlterSqlReturn> sqlBeanList) {
        return sqlBeanList.stream().map(playsqlMigrateAlterSqlBean -> {
            AlterSQLResult.SQLFilePart filePart = new AlterSQLResult.SQLFilePart();
            filePart.fileName = playsqlMigrateAlterSqlBean.fileName;
            filePart.content = playsqlMigrateAlterSqlBean.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                             Prepare
    //                                                                             =======
    @Execute
    @NotAvailableDecommentServer
    public JsonResponse<Void> prepare(String clientName) {
        playsqlMigrationLogic.unzipCheckedAlterZip(clientName);
        playsqlMigrationLogic.copyUnreleasedAlterDir(clientName);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    @Execute
    @NotAvailableDecommentServer
    public JsonResponse<Void> create(String clientName, AlterCreateBody body) {
        validate(body, messages -> moreValidate(clientName, body, messages));
        playsqlMigrationLogic.createAlterSql(clientName, body.alterFileName);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    private void moreValidate(String clientName, AlterCreateBody body, IntroMessages messages) {
        final String alterFileName = body.alterFileName;
        if (alterFileName != null && !alterFileName.endsWith(".sql")) {
            messages.addErrorsInvalidFileExtension(alterFileName);
        }
        if (alterFileName != null && !alterFileName.startsWith("alter-schema")) {
            messages.addErrorsInvalidFileName(alterFileName);
        }
        boolean containsInvalidCharacter = containsInvalidCharacter(alterFileName);
        if (containsInvalidCharacter) {
            messages.addErrorsInvalidFileName(alterFileName);
        }
        if (!containsInvalidCharacter && playsqlMigrationLogic.existsSameNameAlterSqlFile(clientName, body.alterFileName)) {
            messages.addErrorsDuplicateFileName(alterFileName);
        }
    }

    private boolean containsInvalidCharacter(String alterFileName) {
        return alterFileName != null
                && DfStringUtil.containsAny(alterFileName, "/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0", " ");
    }
}
