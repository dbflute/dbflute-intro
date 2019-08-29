package org.dbflute.intro.app.web.alter;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author subaru
 */
public class AlterAction extends IntroBaseAction {
    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DocumentPhysicalLogic documentPhysicalLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> create(String clientProject, AlterCreateBody body) {
        validate(body, messages -> moreValidate(body, messages));
        OptionalThing<File> optAlterDir = documentPhysicalLogic.findAlterDir(clientProject);
        if (!optAlterDir.isPresent()) {
            documentPhysicalLogic.createAlterDir(clientProject);
        }
        documentPhysicalLogic.createAlterSql(clientProject, body.alterFileName);
        return JsonResponse.asEmptyBody();
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> prepare(String clientProject) {
        documentPhysicalLogic.unzipAlterSqlZip(clientProject);
        return JsonResponse.asEmptyBody();
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    private void moreValidate(AlterCreateBody body, IntroMessages messages) {
        if (body.alterFileName != null && !body.alterFileName.endsWith(".sql")) {
            messages.addErrorsInvalidFileExtension(body.alterFileName);
        }
        if (body.alterFileName != null && !body.alterFileName.startsWith("alter_schema_")) {
            messages.addErrorsInvalidFileName(body.alterFileName);
        }
    }
}
