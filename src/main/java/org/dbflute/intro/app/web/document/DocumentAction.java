package org.dbflute.intro.app.web.document;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dbfluteclient.DocumentLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.StreamResponse;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author deco
 */
public class DocumentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private DocumentLogic documentLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public StreamResponse schemahtml(String project) {
        return createHtmlStreamResponse(documentLogic.findDocumentFile(project, "schema"));
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse historyhtml(String project) {
        return createHtmlStreamResponse(documentLogic.findDocumentFile(project, "history"));
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private StreamResponse createHtmlStreamResponse(File file) {
        StreamResponse streamResponse = new StreamResponse("");
        streamResponse.contentType("text/html; charset=UTF-8");
        streamResponse.stream(writtenStream -> writtenStream.write(FileUtils.openInputStream(file)));
        return streamResponse;
    }
}
