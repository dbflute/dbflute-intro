package org.dbflute.intro.app.web.alter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.AlterSqlBean;
import org.dbflute.intro.app.logic.playsql.migrate.PlaysqlMigrateLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
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
    private PlaysqlMigrateLogic playsqlMigrateLogic;

    // ===================================================================================
    //                                                                               Index
    //                                                                               =====
    @Execute
    public JsonResponse<AlterSQLResult> index(String clientProject) {
        return asJson(mappingAlterSQLResult(clientProject));
    }

    // -----------------------------------------------------
    //                                               mapping
    //                                               -------
    private AlterSQLResult mappingAlterSQLResult(String clientProject) {
        AlterSQLResult result = new AlterSQLResult();
        result.ngMark = playsqlMigrateLogic.findAlterCheckNgMark(clientProject);
        result.editingFiles = mappingAlterEditingFilePart(clientProject);
        result.checkedZip = mappingCheckedZipPart(clientProject);
        result.unreleasedDir = mappingUnreleasedDirPart(clientProject);
        return result;
    }

    private List<AlterSQLResult.SQLFilePart> mappingAlterEditingFilePart(String clientProject) {
        return playsqlMigrateLogic.findAlterFiles(clientProject).stream().map(editingFile -> {
            AlterSQLResult.SQLFilePart filePart = new AlterSQLResult.SQLFilePart();
            filePart.fileName = editingFile.fileName;
            filePart.content = editingFile.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    private AlterSQLResult.CheckedZipPart mappingCheckedZipPart(String clientProject) {
        return playsqlMigrateLogic.loadCheckedZip(clientProject).map(checkedZipBean -> {
            AlterSQLResult.CheckedZipPart checkedZipPart = new AlterSQLResult.CheckedZipPart();
            checkedZipPart.fileName = checkedZipBean.getFileName();
            checkedZipPart.checkedFiles = mappingSqlFileBean(checkedZipBean.getCheckedSqlList());
            return checkedZipPart;
        }).orElse(null);
    }

    private AlterSQLResult.UnreleasedDirPart mappingUnreleasedDirPart(String clientProject) {
        return playsqlMigrateLogic.findUnreleasedAlterDir(clientProject).map(dirBean -> {
            AlterSQLResult.UnreleasedDirPart dirPart = new AlterSQLResult.UnreleasedDirPart();
            dirPart.checkedFiles = mappingSqlFileBean(dirBean.getCheckedSqlList());
            return dirPart;
        }).orElse(null);
    }

    private List<AlterSQLResult.SQLFilePart> mappingSqlFileBean(List<AlterSqlBean> sqlBeanList) {
        return sqlBeanList.stream().map(alterSqlBean -> {
            AlterSQLResult.SQLFilePart filePart = new AlterSQLResult.SQLFilePart();
            filePart.fileName = alterSqlBean.fileName;
            filePart.content = alterSqlBean.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                             Prepare
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> prepare(String clientProject) {
        playsqlMigrateLogic.unzipCheckedAlterZip(clientProject);
        playsqlMigrateLogic.copyUnreleasedAlterDir(clientProject);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> create(String clientProject, AlterCreateBody body) {
        validate(body, messages -> moreValidate(clientProject, body, messages));
        playsqlMigrateLogic.createAlterSql(clientProject, body.alterFileName);
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
        if (!containsInvalidCharacter && playsqlMigrateLogic.existsAlterFileAlready(clientProject, body.alterFileName)) {
            messages.addErrorsDuplicateFileName(alterFileName);
        }
    }

    private boolean containsInvalidCharacter(String alterFileName) {
        return alterFileName != null && DfStringUtil.containsAny(alterFileName, "/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0",
                " ");
    }
}
