/*
 * Copyright 2014-2018 the original author or authors.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.document.DocumentDisplayLogic;
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
    @Resource
    private DocumentDisplayLogic documentDisplayLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public StreamResponse schemahtml(String clientProject) {
        File schemaHtml = documentPhysicalLogic.findSchemaHtml(clientProject);
        if (!schemaHtml.exists()) {
            return StreamResponse.asEmptyBody();
        }
        String schemaHtmlContent = documentDisplayLogic.modifyHtmlForIntroOpening(clientProject, schemaHtml);
        return createStringSteamResponse(schemaHtmlContent);
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse historyhtml(String clientProject) {
        File historyHtml = documentPhysicalLogic.findHistoryHtml(clientProject);
        if (!historyHtml.exists()) {
            return StreamResponse.asEmptyBody();
        }
        String historyHtmlContent = documentDisplayLogic.modifyHtmlForIntroOpening(clientProject, historyHtml);
        return createStringSteamResponse(historyHtmlContent);
    }

    @Execute(urlPattern = "{}/@word")
    public StreamResponse propertieshtml(String clientProject) {
        File historyHtml = documentPhysicalLogic.findPropertiesHtml(clientProject);
        if (!historyHtml.exists()) {
            return StreamResponse.asEmptyBody();
        }
        String historyHtmlContent = documentDisplayLogic.modifyHtmlForIntroOpening(clientProject, historyHtml);
        return createStringSteamResponse(historyHtmlContent);
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
    private StreamResponse createStringSteamResponse(String schemaHtmlContent) {
        StreamResponse stream = asStream("schema-...html"); // dummy name because unused (not download)
        stream.headerContentDispositionInline();
        return stream.contentType("text/html; encoding=\"UTF-8\"").stream(out -> {
            try (InputStream ins = new ByteArrayInputStream(schemaHtmlContent.getBytes("UTF-8"))) {
                out.write(ins);
            }
        });
    }

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
