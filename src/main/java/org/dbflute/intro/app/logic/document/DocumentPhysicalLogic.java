/*
 * Copyright 2014-2020 the original author or authors.
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
 * The logic for document physical operation. (e.g. SchemaHTML, HistoryHTML)
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
    //                                                                              Exists
    //                                                                              ======
    public boolean existsSchemaHtml(String projectName) {
        return findSchemaHtml(projectName).exists();
    }

    public boolean existsHistoryHtml(String projectName) {
        return findHistoryHtml(projectName).exists();
    }

    public boolean existsSyncCheckResultHtml(String projectName) {
        return findSyncCheckResultHtml(projectName).exists();
    }

    public boolean existsAlterCheckResultHtml(String projectName) {
        return findAlterCheckResultHtml(projectName).exists();
    }

    // ===================================================================================
    //                                                                               Find
    //                                                                              ======
    public File findSchemaHtml(String projectName) {
        return toProjectNamedDocumentFile(projectName, "schema");
    }

    public File findHistoryHtml(String projectName) {
        return toProjectNamedDocumentFile(projectName, "history");
    }

    public File findPropertiesHtml(String projectName) {
        return toProjectNamedDocumentFile(projectName, "properties");
    }

    public File findSyncCheckResultHtml(String projectName) {
        return toFixedNamedDocumentFile(projectName, "sync-check-result.html");
    }

    public File findAlterCheckResultHtml(String projectName) {
        return toProjectNamedMigrationFile(projectName, "schema");
    }

    public File findLastaDocHtml(String projectName, String moduleName) {
        return toProjectNamedDocumentFile(projectName, moduleName, "lastadoc");
    }

    // ===================================================================================
    //                                                                        Path to File
    //                                                                        ============
    // -----------------------------------------------------
    //                                         Project Named
    //                                         -------------
    private File toProjectNamedDocumentFile(String projectName, String module, String type) {
        String pureName = type + "-" + module + ".html";
        return toFixedNamedDocumentFile(projectName, pureName);
    }

    private File toProjectNamedDocumentFile(String projectName, String type) { // e.g. SchemaHtml
        final String pureName = type + "-" + projectName + ".html";
        return toFixedNamedDocumentFile(projectName, pureName);
    }

    private File toProjectNamedMigrationFile(String projectName, String type) { // e.g. AlterCheck
        // #needs_fix jflute basically only one file so you can remove switch case (2021/05/01)
        final String pureName;
        switch (type) {
        case "schema":
            pureName = "alter-check-result.html";
            break;
        default:
            return null;
        }
        return toFixedNamedMigrationFile(projectName, type, pureName);
    }

    // -----------------------------------------------------
    //                                           Fixed Named
    //                                           -----------
    private File toFixedNamedDocumentFile(String projectName, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildDocumentPath(projectName, pureName));
    }

    private File toFixedNamedMigrationFile(String projectName, String type, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildMigrationPath(projectName, type, pureName));
    }

    // -----------------------------------------------------
    //                                            Build Path
    //                                            ----------
    private String buildDocumentPath(String projectName, String pureName) {
        return introPhysicalLogic.buildClientPath(projectName, "output", "doc", pureName);
    }

    private String buildMigrationPath(String projectName, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql", "migration", type, pureName);
    }
}
