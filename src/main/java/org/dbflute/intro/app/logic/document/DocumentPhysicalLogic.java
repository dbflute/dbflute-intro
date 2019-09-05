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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;

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
    @Resource
    private FlutyFileLogic flutyFileLogic;

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

    public void createAlterDir(String clientProject) {
        File file = new File(buildMigrationPath(clientProject, "", "alter"));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void createAlterSql(String clientProject, String alterFileName) {
        File file = new File(introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "alter", alterFileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create new alter sql file: " + file.getAbsolutePath(), e);
        }
    }

    public List<AlterSqlBean> findAlterFiles(String clientProject) {
        return findAlterDir(clientProject).map(dir -> dir.listFiles())
                .filter(files -> files != null)
                .map(files -> Arrays.stream(files))
                .orElse(Stream.empty())
                .filter(file -> DfStringUtil.endsWith(file.getName(), ".sql"))
                .map(file -> new AlterSqlBean(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    public CDef.NgMark findAlterCheckNgMark(String clientProject) {
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            if (new File(buildMigrationPath(clientProject, ngMark.code() + ".dfmark")).exists()) {
                return ngMark;
            }
        }
        return null;
    }

    public List<AlterSqlBean> findStackedAlterSqls(String clientProject) {
        final String zipPath = buildCheckedAlterZipPath(clientProject);
        if (zipPath != null) {
            try {
                return findAlterSqlsFromZip(zipPath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read zip file: " + zipPath, e);
            }
        }
        return Collections.emptyList();
    }

    private List<AlterSqlBean> findAlterSqlsFromZip(String zipPath) throws IOException {
        final List<AlterSqlBean> alterSqls = new ArrayList<>();
        final ZipFile zipFile = new ZipFile(zipPath);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String fileName = entry.getName();
            if (!DfStringUtil.contains(fileName, ".sql")) {
                continue;
            }
            final String content = IOUtils.toString(zipFile.getInputStream(entry), StandardCharsets.UTF_8);
            alterSqls.add(new AlterSqlBean(fileName, content));
        }
        zipFile.close();
        return alterSqls;
    }

    public void unzipAlterSqlZip(String clientProject) {
        String zipPath = buildCheckedAlterZipPath(clientProject);
        String alterDirPath = buildMigrationPath(clientProject, "", "alter");
        try {
            unzipAlterSqlZipIfNeeds(zipPath, alterDirPath);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to unzip checked alter sql zip file: " + zipPath, e);
        }
    }

    private void unzipAlterSqlZipIfNeeds(String zipPath, String dstPath) throws IOException {
        if (!existsSqlFiles(dstPath)) {
            doUnzipAlterSqlZip(zipPath, dstPath);
        }
    }

    private boolean existsSqlFiles(String dirPath) {
        return Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles()))
                .anyMatch(file -> DfStringUtil.endsWith(file.getName(), ".sql"));
    }

    private void doUnzipAlterSqlZip(String zipPath, String dstPath) throws IOException {
        final ZipFile zipFile = new ZipFile(zipPath);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String fileName = entry.getName();
            final String content = IOUtils.toString(zipFile.getInputStream(entry), StandardCharsets.UTF_8);
            Files.write(Paths.get(dstPath, fileName), Collections.singletonList(content));
        }
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

    private String buildCheckedAlterZipPath(String clientProject) {
        final Path historyPath = Paths.get(introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "history"));
        if (!Files.exists(historyPath)) {
            return null;
        }
        try {
            return Files.walk(historyPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().startsWith("checked"))
                    .map(path -> path.toString())
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get checked alter schema zip file under the directory: " + historyPath, e);
        }
    }
}
