package org.dbflute.intro.app.web.document;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.response.StreamResponse;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author deco
 * @author jflute
 */
public class DocumentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DocumentPhysicalLogic documentPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<SchemaHtmlResult> schemahtml(String clientProject) {
        File schemaHtml = documentPhysicalLogic.findSchemaHtml(clientProject);
        if (!schemaHtml.exists()) {
            return JsonResponse.asEmptyBody();
        }
        SchemaHtmlResult result = new SchemaHtmlResult(flutyFileLogic.readFile(schemaHtml));
        return asJson(result);
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<HistoryHtmlResult> historyhtml(String clientProject) {
        File historyHtml = documentPhysicalLogic.findHistoryHtml(clientProject);
        if (!historyHtml.exists()) {
            return JsonResponse.asEmptyBody();
        }
        HistoryHtmlResult result = new HistoryHtmlResult(flutyFileLogic.readFile(historyHtml));
        return asJson(result);
    }
}
