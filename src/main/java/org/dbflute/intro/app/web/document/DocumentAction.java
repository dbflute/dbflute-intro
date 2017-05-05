/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.web.document;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.StreamResponse;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;

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
        File schemaHtml = documentPhysicalLogic.findSchemaHtml(clientProject);
        if (!schemaHtml.exists()) {
            return StreamResponse.asEmptyBody();
        }
        return createHtmlStreamResponse(schemaHtml);
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse historyhtml(String clientProject) {
        File historyHtml = documentPhysicalLogic.findHistoryHtml(clientProject);
        if (!historyHtml.exists()) {
            return StreamResponse.asEmptyBody();
        }
        return createHtmlStreamResponse(historyHtml);
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse synccheckresulthtml(String clientProject) {
        File syncCheckResultHtml = documentPhysicalLogic.findSyncCheckResultHtml(clientProject);
        if (!syncCheckResultHtml.exists()) {
            return null;
        }
        return createHtmlStreamResponse(syncCheckResultHtml);
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private StreamResponse createHtmlStreamResponse(File file) {
        // done deco dummy name or comment about it by jflute (2017/01/12)
        StreamResponse stream = asStream("schema-...html"); // dummy name because unused (not download)
        stream.headerContentDispositionInline();
        return stream.contentType("text/html; encoding=\"UTF-8\"").stream(out -> {
            try (InputStream ins = FileUtils.openInputStream(file)) {
                out.write(ins);
            }
        });
    }
}
