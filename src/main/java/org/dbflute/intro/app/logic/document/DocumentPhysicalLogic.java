/*
 * Copyright 2014-2021 the original author or authors.
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
 * The logic for document physical operation. (e.g. SchemaHTML, HistoryHTML) <br>
 * ドキュメントファイルの存在を判定したり、Fileオブジェクトを取得したりできる。
 * @author deco
 * @author jflute
 * @author cabos
 * @author subaru
 * @author hakiba
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
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 該当のファイルが存在するかどうか？
    // ファイルの場所はそれぞれ決まっているので、クライアントプロジェクト名を入れれば特定できる。
    // _/_/_/_/_/_/_/_/_/_/
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
    //                                                                           Find File
    //                                                                           =========
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 該当のファイルのFileオブジェクトを探す。
    // ファイル自体が存在しなくても、想定されるパスのFileオブジェクトが戻る。(NotNull)
    // _/_/_/_/_/_/_/_/_/_/
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
        return toFixedNamedMigrationFile(projectName, "schema", "alter-check-result.html");
    }

    public File findLastaDocHtml(String projectName, String moduleName) {
        return toProjectNamedDocumentFile(projectName, moduleName, "lastadoc");
    }

    public String buildDocumentDirPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName, "output", "doc");
    }

    // ===================================================================================
    //                                                                        Path to File
    //                                                                        ============
    // -----------------------------------------------------
    //                                         Project Named
    //                                         -------------
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // クライアントプロジェクト名が付いてるファイルのFileオブジェクトを探す。
    // 例えば、SchemaHTMLであれば schema-[プロジェクト名].html となる。
    // _/_/_/_/_/_/_/_/_/_/
    private File toProjectNamedDocumentFile(String projectName, String docType) { // e.g. SchemaHtml
        final String pureName = docType + "-" + projectName + ".html";
        return toFixedNamedDocumentFile(projectName, pureName);
    }

    private File toProjectNamedDocumentFile(String projectName, String moduleName, String docType) { // LastaDoc
        // #for_now jflute not project name but module name (2021/12/11)
        String pureName = docType + "-" + moduleName + ".html";
        return toFixedNamedDocumentFile(projectName, pureName);
    }

    // -----------------------------------------------------
    //                                           Fixed Named
    //                                           -----------
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // 固定ファイル名のFileオブジェクトを探す。
    // 例えば、SchemaSyncCheckであれば sync-check-result.html となる。
    // _/_/_/_/_/_/_/_/_/_/
    private File toFixedNamedDocumentFile(String projectName, String pureName) { // e.g. SchemaSyncCheck
        return new File(buildDocumentPath(projectName, pureName));
    }

    private File toFixedNamedMigrationFile(String projectName, String subDir, String pureName) { // e.g. AlterCheck
        return new File(buildMigrationPath(projectName, subDir, pureName));
    }

    // -----------------------------------------------------
    //                                            Build Path
    //                                            ----------
    private String buildDocumentPath(String projectName, String pureName) {
        return buildDocumentDirPath(projectName) + "/" + pureName;
    }

    private String buildMigrationPath(String projectName, String subDir, String pureName) {
        return introPhysicalLogic.buildClientPath(projectName, "playsql", "migration", subDir, pureName);
    }
}
