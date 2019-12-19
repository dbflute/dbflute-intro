/*
 * Copyright 2014-2019 the original author or authors.
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

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author deco
 * @author jflute
 * @author cabos
 * @author subaru
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
    public boolean existsSchemaHtml(String clientName) {
        return findSchemaHtml(clientName).exists();
    }

    public boolean existsHistoryHtml(String clientName) {
        return findHistoryHtml(clientName).exists();
    }

    public boolean existsSyncCheckResultHtml(String clientName) {
        return findSyncCheckResultHtml(clientName).exists();
    }

    public boolean existsAlterCheckResultHtml(String clientName) {
        return findAlterCheckResultHtml(clientName).exists();
    }

    public File findSchemaHtml(String clientName) {
        return toProjectNamedDocumentFile(clientName, "schema");
    }

    public File findHistoryHtml(String clientName) {
        return toProjectNamedDocumentFile(clientName, "history");
    }

    public File findPropertiesHtml(String clientName) {
        return toProjectNamedDocumentFile(clientName, "properties");
    }

    public File findSyncCheckResultHtml(String clientName) {
        return toFixedNamedDocumentFile(clientName, "sync-check-result.html");
    }

    public File findAlterCheckResultHtml(String clientName) {
        return toProjectNamedMigrationFile(clientName, "schema");
    }

    public File findLastaDocHtml(String clientName, String moduleName) {
        return toProjectNamedDocumentFile(clientName, moduleName, "lastadoc");
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private File toProjectNamedDocumentFile(String clientName, String module, String type) {
        String pureName = type + "-" + module + ".html";
        return toFixedNamedDocumentFile(clientName, pureName);
    }

    private File toProjectNamedDocumentFile(String clientName, String type) { // e.g. SchemaHtml
        final String pureName = type + "-" + clientName + ".html";
        return toFixedNamedDocumentFile(clientName, pureName);
    }

    private File toFixedNamedDocumentFile(String clientName, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildDocumentPath(clientName, pureName));
    }

    private File toProjectNamedMigrationFile(String clientName, String type) { // e.g. AlterCheck
        final String pureName;

        switch (type) {
        case "schema":
            pureName = "alter-check-result.html";
            break;
        default:
            return null;
        }
        return toFixedNamedMigrationFile(clientName, type, pureName);
    }

    private File toFixedNamedMigrationFile(String clientName, String type, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildMigrationPath(clientName, type, pureName));
    }

    private String buildDocumentPath(String clientName, String pureName) {
        return introPhysicalLogic.buildClientPath(clientName, "output", "doc", pureName);
    }

    private String buildMigrationPath(String clientName, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(clientName, "playsql", "migration", type, pureName);
    }
}
