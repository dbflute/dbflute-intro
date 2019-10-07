package org.dbflute.intro.app.web.alter;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 index
    //                                                 -----
    @Execute
    public JsonResponse<AlterSQLResult> index(String clientProject) {
        return asJson(generateAlterSQLResult(clientProject));
    }

    private AlterSQLResult generateAlterSQLResult(String clientProject) {
        AlterSQLResult result = new AlterSQLResult();
        result.ngMark = playsqlMigrateLogic.findAlterCheckNgMark(clientProject);
        result.editingFiles = generateAlterEditingFilePart(clientProject);
        result.checkedZip = generateCheckedZipPart(clientProject);
        result.unreleasedDir = generateUnreleasedDirPart(clientProject);
        return result;
    }

    private List<AlterSQLResult.AlterDirFilePart> generateAlterEditingFilePart(String clientProject) {
        return playsqlMigrateLogic.findAlterFiles(clientProject).stream().map(editingFile -> {
            AlterSQLResult.AlterDirFilePart filePart = new AlterSQLResult.AlterDirFilePart();
            filePart.fileName = editingFile.fileName;
            filePart.content = editingFile.content;
            return filePart;
        }).collect(Collectors.toList());
    }

    private AlterSQLResult.CheckedZipPart generateCheckedZipPart(String clientProject) {
        return playsqlMigrateLogic.findCheckedZip(clientProject).map(checkedZipBean -> {
            AlterSQLResult.CheckedZipPart checkedZipPart = new AlterSQLResult.CheckedZipPart();
            checkedZipPart.fileName = checkedZipBean.getFileName();
            checkedZipPart.checkedFiles = checkedZipBean.getCheckedSqlList().stream().map(checkedFile -> {
                AlterSQLResult.AlterDirFilePart filePart = new AlterSQLResult.AlterDirFilePart();
                filePart.fileName = checkedFile.fileName;
                filePart.content = checkedFile.content;
                return filePart;
            }).collect(Collectors.toList());
            return checkedZipPart;
        }).orElse(null);
    }

    private AlterSQLResult.UnreleasedDirPart generateUnreleasedDirPart(String clientProject) {
        return playsqlMigrateLogic.findUnreleasedAlterDir(clientProject).map(dirBean -> {
            AlterSQLResult.UnreleasedDirPart dirPart = new AlterSQLResult.UnreleasedDirPart();
            dirPart.checkedFiles = dirBean.getCheckedSqlList().stream().map(alterSqlBean -> {
                AlterSQLResult.AlterDirFilePart filePart = new AlterSQLResult.AlterDirFilePart();
                filePart.fileName = alterSqlBean.fileName;
                filePart.content = alterSqlBean.content;
                return filePart;
            }).collect(Collectors.toList());
            return dirPart;
        }).orElse(null);
    }

    // -----------------------------------------------------
    //                                               prepare
    //                                               -------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> prepare(String clientProject) {
        playsqlMigrateLogic.unzipAlterSqlZip(clientProject);
        playsqlMigrateLogic.copyUnreleasedAlterDir(clientProject);
        return JsonResponse.asEmptyBody();
    }

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
