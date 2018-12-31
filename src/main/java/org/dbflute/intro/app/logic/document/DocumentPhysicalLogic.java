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
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;

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
    public boolean existsSchemaHtml(String clientProject) {
        return findSchemaHtml(clientProject).exists();
    }

    public boolean existsHistoryHtml(String clientProject) {
        return findHistoryHtml(clientProject).exists();
    }

    public boolean existsSyncCheckResultHtml(String clientProject) {
        return findSyncCheckResultHtml(clientProject).exists();
    }

    public boolean existsAlterCheckResultHtml(String clientProject) {
        return findAlterCheckResultHtml(clientProject).exists();
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

    public File findAlterCheckResultHtml(String clientProject) {
        return toProjectNamedMigrationFile(clientProject, "schema");
    }

    public File findLastaDocHtml(String clientProject, String moduleName) {
        return toProjectNamedDocumentFile(clientProject, moduleName, "lastadoc");
    }

    public OptionalThing<File> findAlterDir(String clientProject) {
        File file = new File(buildMigrationPath(clientProject, "", "alter"));
        if (file.exists()) {
            return OptionalThing.of(file);
        }
        return OptionalThing.empty();
    }

    public CDef.NgMark findAlterCheckNgMark(String clientProject) {
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            if (new File(buildMigrationPath(clientProject, ngMark.code() + ".dfmark")).exists()) {
                return ngMark;
            }
        }
        return null;
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private File toProjectNamedDocumentFile(String clientProject, String module, String type) {
        String pureName = type + "-" + module + ".html";
        return toFixedNamedDocumentFile(clientProject, pureName);
    }

    private File toProjectNamedDocumentFile(String clientProject, String type) { // e.g. SchemaHtml
        final String pureName = type + "-" + clientProject + ".html";
        return toFixedNamedDocumentFile(clientProject, pureName);
    }

    private File toFixedNamedDocumentFile(String clientProject, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildDocumentPath(clientProject, pureName));
    }

    private File toProjectNamedMigrationFile(String clientProject, String type) { // e.g. AlterCheck
        final String pureName;

        switch (type) {
        case "schema":
            pureName = "alter-check-result.html";
            break;
        default:
            return null;
        }
        return toFixedNamedMigrationFile(clientProject, type, pureName);
    }

    private File toFixedNamedMigrationFile(String clientProject, String type, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildMigrationPath(clientProject, type, pureName));
    }

    private String buildDocumentPath(String clientProject, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "output", "doc", pureName);
    }

    private String buildMigrationPath(String clientProject, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", pureName);
    }

    private String buildMigrationPath(String clientProject, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", type, pureName);
    }
}
