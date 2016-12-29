/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.logic.document;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author deco
 */
public class DocumentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                         Find/Exists
    //                                                                         ===========
    public boolean existsSchemaHtml(String clientProject) {
        return toDocumentFile(clientProject, "schema").exists();
    }

    public boolean existsHistoryHtml(String clientProject) {
        return toDocumentFile(clientProject, "history").exists();
    }

    public File findSchemaHtml(String clientProject) {
        return toDocumentFile(clientProject, "schema");
    }

    public File findHistoryHtml(String clientProject) {
        return toDocumentFile(clientProject, "history");
    }

    public File findSyncCheckResultHtml(String clientProject) {
        return new File(introPhysicalLogic.buildClientPath(clientProject, "output", "doc", "sync-check-result.html"));
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private File toDocumentFile(String clientProject, String type) {
        final String htmlFileName = buildHtmlFileName(clientProject, type);
        return new File(introPhysicalLogic.buildClientPath(clientProject, "output", "doc", htmlFileName));
    }

    private String buildHtmlFileName(String clientProject, String type) {
        return type + "-" + clientProject + ".html";
    }
}
