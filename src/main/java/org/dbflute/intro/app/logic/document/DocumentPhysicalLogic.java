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
package org.dbflute.intro.app.logic.document;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author deco
 * @author jflute
 */
public class DocumentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                         Find/Exists
    //                                                                         ===========
    public boolean existsSchemaHtml(String clientProject) {
        return findSchemaHtml(clientProject).exists();
    }

    public boolean existsHistoryHtml(String clientProject) {
        return findHistoryHtml(clientProject).exists();
    }

    public boolean existsSyncCheckResultHtml(String clientProject) {
        return findSyncCheckResultHtml(clientProject).exists();
    }

    public File findSchemaHtml(String clientProject) {
        return toProjectNamedDocumentFile(clientProject, "schema");
    }

    public File findHistoryHtml(String clientProject) {
        return toProjectNamedDocumentFile(clientProject, "history");
    }

    public File findPropertiesHtml(String clientProject) {
        return toProjectNamedDocumentFile(clientProject, "properties");
    }

    public File findSyncCheckResultHtml(String clientProject) {
        return toFixedNamedDocumentFile(clientProject, "sync-check-result.html");
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private File toProjectNamedDocumentFile(String clientProject, String type) { // e.g. SchemaHtml
        final String pureName = type + "-" + clientProject + ".html";
        return toFixedNamedDocumentFile(clientProject, pureName);
    }

    private File toFixedNamedDocumentFile(String clientProject, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildDocumentPath(clientProject, pureName));
    }

    private String buildDocumentPath(String clientProject, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "output", "doc", pureName);
    }
}
