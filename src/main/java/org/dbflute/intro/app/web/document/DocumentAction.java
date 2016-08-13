package org.dbflute.intro.app.web.document;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.StreamResponse;

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

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public StreamResponse schemahtml(String clientProject) {
        return asHtmlStream(documentPhysicalLogic.findSchemaHtml(clientProject));
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse historyhtml(String clientProject) {
        return asHtmlStream(documentPhysicalLogic.findHistoryHtml(clientProject));
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private StreamResponse asHtmlStream(File file) {
        return asStream(file.getName()).contentType("text/html; charset=UTF-8").stream(out -> {
            out.write(FileUtils.openInputStream(file));
        });
    }
}
