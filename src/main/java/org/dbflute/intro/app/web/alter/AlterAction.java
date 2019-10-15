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
package org.dbflute.intro.app.web.alter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationLogic;
import org.dbflute.intro.app.logic.playsql.migration.bean.PlaysqlMigrationAlterSqlBean;
import org.dbflute.intro.app.logic.playsql.migration.bean.PlaysqlMigrationDirBean;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.util.DfStringUtil;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
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
    @Execute
    public JsonResponse<AlterSQLResult> index(String clientProject) {
        return asJson(mappingAlterSQLResult(playsqlMigrationLogic.loadPlaysqlMigrationDir(clientProject)));
    }

    // -----------------------------------------------------
    //                                               mapping
    //                                               -------
    private AlterSQLResult mappingAlterSQLResult(PlaysqlMigrationDirBean bean) {
        AlterSQLResult result = new AlterSQLResult();
        result.ngMark = bean.getNgMark().orElse(null);
        result.editingFiles = mappingAlterEditingFilePart(bean);
        result.checkedZip = mappingCheckedZipPart(bean);
        result.unreleasedDir = mappingUnreleasedDirPart(bean);
        return result;
    }

    private List<AlterSQLResult.SQLFilePart> mappingAlterEditingFilePart(PlaysqlMigrationDirBean bean) {
        return bean.getAlterSqlBeanList().stream().map(editingFile -> {
            AlterSQLResult.SQLFilePart filePart = new AlterSQLResult.SQLFilePart();
            filePart.fileName = editingFile.fileName;
            filePart.content = editingFile.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    private AlterSQLResult.CheckedZipPart mappingCheckedZipPart(PlaysqlMigrationDirBean bean) {
        return bean.getCheckedZipBean().map(checkedZipBean -> {
            AlterSQLResult.CheckedZipPart checkedZipPart = new AlterSQLResult.CheckedZipPart();
            checkedZipPart.fileName = checkedZipBean.getFileName();
            checkedZipPart.checkedFiles = mappingSqlFileBean(checkedZipBean.getCheckedSqlList());
            return checkedZipPart;
        }).orElse(null);
    }

    private AlterSQLResult.UnreleasedDirPart mappingUnreleasedDirPart(PlaysqlMigrationDirBean bean) {
        return bean.getUnreleasedDirBean().map(dirBean -> {
            AlterSQLResult.UnreleasedDirPart dirPart = new AlterSQLResult.UnreleasedDirPart();
            dirPart.checkedFiles = mappingSqlFileBean(dirBean.getCheckedSqlList());
            return dirPart;
        }).orElse(null);
    }

    private List<AlterSQLResult.SQLFilePart> mappingSqlFileBean(List<PlaysqlMigrationAlterSqlBean> sqlBeanList) {
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
    @Execute(urlPattern = "{}/@word")
    @NotAvailableDecommentServer
    public JsonResponse<Void> prepare(String clientProject) {
        playsqlMigrationLogic.unzipCheckedAlterZip(clientProject);
        playsqlMigrationLogic.copyUnreleasedAlterDir(clientProject);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    @Execute(urlPattern = "{}/@word")
    @NotAvailableDecommentServer
    public JsonResponse<Void> create(String clientProject, AlterCreateBody body) {
        validate(body, messages -> moreValidate(clientProject, body, messages));
        playsqlMigrationLogic.createAlterSql(clientProject, body.alterFileName);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    private void moreValidate(String clientProject, AlterCreateBody body, IntroMessages messages) {
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
        if (!containsInvalidCharacter && playsqlMigrationLogic.existsSameNameAlterSqlFile(clientProject, body.alterFileName)) {
            messages.addErrorsDuplicateFileName(alterFileName);
        }
    }

    private boolean containsInvalidCharacter(String alterFileName) {
        return alterFileName != null && DfStringUtil.containsAny(alterFileName, "/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0",
                " ");
    }
}
