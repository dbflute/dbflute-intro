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
package org.dbflute.intro.app.logic.playsql.migration;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.exception.DirectoryDoesNotExsistException;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.IntroFileOperationException;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;

/**
 * @author cabos
 * @author subaru
 */
public class PlaysqlMigrationLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // This pattern is same as DBFlute specification.
    private static final DateTimeFormatter ZIP_PATH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                      load sql files
    //                                                                      ==============
    // -----------------------------------------------------
    //                                             migration
    //                                             ---------
    /**
     * Load migration directory.
     * @return dbflute_client/playsql/migration directory information (NotNull)
     */
    public PlaysqlMigrationDirReturn loadPlaysqlMigrationDir(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        return new PlaysqlMigrationDirReturn( //
                loadAlterCheckNgMarkFile(clientName).orElse(null), //
                loadAlterSqlFiles(clientName), //
                loadCheckedZip(clientName).orElse(null), //
                loadUnreleasedDir(clientName).orElse(null) //
        );

    }

    // -----------------------------------------------------
    //                                       alter directory
    //                                       ---------------
    /**
     * Load sql files in alter directory.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @return list of alter files in alter directory. (NotNull)
     */
    private List<PlaysqlMigrationAlterSqlReturn> loadAlterSqlFiles(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        return OptionalThing.ofNullable(new File(buildAlterDirectoryPath(clientName)).listFiles(), () -> {})
                .map(files -> Arrays.stream(files))
                .orElse(Stream.empty())
                .filter(file -> DfStringUtil.endsWith(file.getName(), ".sql"))
                .map(file -> new PlaysqlMigrationAlterSqlReturn(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                  unreleased directory
    //                                  --------------------
    /**
     * Load unreleased directory info.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @return unreleased directory bean (Maybe empty). (NotNull)
     */
    private OptionalThing<PlaysqlMigrationUnreleasedDirReturn> loadUnreleasedDir(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(clientName);
        final File alterDir = new File(unreleasedAlterDirPath);
        if (!alterDir.exists() || alterDir.isFile()) {
            return OptionalThing.empty();
        }
        return OptionalThing.of(new PlaysqlMigrationUnreleasedDirReturn(loadFilesInUnreleasedDirectory(alterDir)));
    }

    /**
     * Load files in unreleased directory.
     *
     * @param unreleasedDir unreleased directory (NotNull)
     * @return file list in unreleased directory (NotNull)
     */
    private List<PlaysqlMigrationAlterSqlReturn> loadFilesInUnreleasedDirectory(File unreleasedDir) {
        IntroAssertUtil.assertNotNull(unreleasedDir);
        return OptionalThing.ofNullable(unreleasedDir.listFiles(), () -> {})
                .map(files -> Arrays.stream(files))
                .orElse(Stream.empty())
                .map(file -> new PlaysqlMigrationAlterSqlReturn(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    /**
     * Load alter ng mark if exists.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @return Ng mark file (Maybe empty)
     */
    private OptionalThing<PlaysqlMigrationNgMarkFileReturn> loadAlterCheckNgMarkFile(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            File file = new File(buildMigrationPath(clientName, ngMark.code() + ".dfmark"));
            if (file.exists()) {
                return OptionalThing.of(new PlaysqlMigrationNgMarkFileReturn(ngMark, flutyFileLogic.readFile(file)));
            }
        }
        return OptionalThing.empty();
    }

    // -----------------------------------------------------
    //                                           checked zip
    //                                           -----------
    /**
     * Load newest checked zip file info.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @return checked zip file bean (Maybe empty)
     */
    public OptionalThing<PlaysqlMigrationCheckedZipReturn> loadCheckedZip(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        return loadNewestCheckedZipFile(clientName).map(checkedZip -> {
            return new PlaysqlMigrationCheckedZipReturn(checkedZip.getName(), loadCheckedSqlList(checkedZip));
        });
    }

    /**
     * Load sql files in checked zip file.
     *
     * @param checkedZipFile checked zip file
     * @return sql files in checked zip file (NotNull)
     */
    private List<PlaysqlMigrationAlterSqlReturn> loadCheckedSqlList(File checkedZipFile) {
        IntroAssertUtil.assertNotNull(checkedZipFile);
        if (!checkedZipFile.exists()) {
            return Collections.emptyList();
        }
        return loadAlterSqlFilesInCheckedZip(checkedZipFile);
    }

    /**
     * Load alter sql files in checked zip.
     * Return empty list if zip file not exists.
     *
     * @param checkedZipFile checked zip (NotNull)
     * @return sql files in checkedZipFile (NotNull)
     */
    private List<PlaysqlMigrationAlterSqlReturn> loadAlterSqlFilesInCheckedZip(File checkedZipFile) {
        // TODO cabos resolve duplicate unzip load (2019-10-08)
        IntroAssertUtil.assertNotNull(checkedZipFile);
        if (!checkedZipFile.exists()) {
            return Collections.emptyList();
        }

        try (ZipFile zipFile = new ZipFile(checkedZipFile.getPath())) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .filter(zipEntry -> DfStringUtil.endsWith(zipEntry.getName(), ".sql"))
                    .map(zipEntry -> {
                        try {
                            final String content = IOUtils.toString(zipFile.getInputStream(zipEntry), StandardCharsets.UTF_8);
                            final PlaysqlMigrationAlterSqlReturn bean = new PlaysqlMigrationAlterSqlReturn(zipEntry.getName(), content);
                            return bean;
                        } catch (IOException e) {
                            throw new UncheckedIOException(e); // for handle out of lambda
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException | UncheckedIOException e) {
            throw new IntroFileOperationException("Unable load file in zip.", "zipFileName : " + checkedZipFile.getPath(), e);
        }
    }

    // ===================================================================================
    //                                                                Open alter directory
    //                                                                ====================
    /**
     * Open alter directory by filer. (e.g. finder if mac, explorer if windows)
     * Use OS command.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @throws DirectoryDoesNotExsistException 
     */
    public void openAlterDir(String clientName) throws DirectoryDoesNotExsistException {
        File alterDir = new File(buildAlterDirectoryPath(clientName));
        flutyFileLogic.openDir(alterDir);
    }

    // ===================================================================================
    //                                                      Check to exists Same Name file
    //                                                      ==============================
    /**
     * Check to exist same name alter SQL file before release.
     * @param clientName dbflute client project name (NotEmpty)
     * @param sqlFileName checked sql file name (NotEmpty)
     * @return true if exists same name file.
     */
    public boolean existsSameNameAlterSqlFile(String clientName, String sqlFileName) {
        IntroAssertUtil.assertNotEmpty(clientName, sqlFileName);
        return existsSameNameFileInAlterDir(clientName, sqlFileName) // exists editing sql
                || existsSameNameFileInUnreleasedDir(clientName, sqlFileName) // exists sql in unreleased directory
                || existsSameNameFileInCheckedZip(clientName, sqlFileName); // exists sql in checked zip
    }

    /**
     * Check to exist same name alter SQL file in alter directory.
     * @param clientName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in alter directory.
     */
    private boolean existsSameNameFileInAlterDir(String clientName, String alterFileName) {
        final String alterPath = buildMigrationPath(clientName, "alter");
        return Files.exists(Paths.get(alterPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in unreleased directory.
     * @param clientName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in unreleased directory.
     */
    private boolean existsSameNameFileInUnreleasedDir(String clientName, String alterFileName) {
        final String unreleasedDIrPath = buildUnreleasedAlterDirPath(clientName);
        return Files.exists(Paths.get(unreleasedDIrPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in checked zip.
     * @param clientName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in checked zip.
     */
    private boolean existsSameNameFileInCheckedZip(String clientName, String alterFileName) {
        final String historyPath = buildMigrationPath(clientName, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return false;
        }
        return loadNewestCheckedZipFile(clientName).map(zipFile -> {
            String zipPath = zipFile.getPath();
            try {
                final List<String> alterSqlNames = loadFileNamesFromCheckedZip(zipPath);
                return alterSqlNames.contains(alterFileName);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read zip file: " + zipPath, e);
            }
        }).orElse(false);
    }

    private List<String> loadFileNamesFromCheckedZip(String zipPath) throws IOException {
        try (final ZipFile zipFile = new ZipFile(zipPath)) {
            return Collections.list(zipFile.entries())
                    .stream()
                    .filter(zipEntry -> DfStringUtil.endsWith(zipEntry.getName(), ".sql"))
                    .map(zipEntry -> zipEntry.getName())
                    .collect(Collectors.toList());
        }
    }

    private OptionalThing<File> loadNewestCheckedZipFile(String clientName) {
        final Path historyPath = new File(buildMigrationPath(clientName, "history")).toPath();
        if (Files.notExists(historyPath)) {
            return OptionalThing.empty();
        }
        try {
            return Files.walk(historyPath)
                    .filter(Files::isRegularFile)
                    .filter(path -> DfStringUtil.startsWith(path.getFileName().toString(), "checked"))
                    .map(path -> path.toFile())
                    .max((f1, f2) -> compareFileCreationTime(f1, f2))
                    .map(OptionalThing::of)
                    .orElse(OptionalThing.empty());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get checked alter schema zip file under the directory: " + historyPath, e);
        }
    }

    protected int compareFileCreationTime(File file1, File file2) {
        final String file1Dir = file1.getParentFile().getName().replace("_", "");
        final String file2Dir = file2.getParentFile().getName().replace("_", "");
        final LocalDateTime date1 = LocalDateTime.parse(file1Dir, ZIP_PATH_DATE_FORMATTER);
        final LocalDateTime date2 = LocalDateTime.parse(file2Dir, ZIP_PATH_DATE_FORMATTER);
        return date1.compareTo(date2);
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    /**
     * Create sql file in alter directory.
     * @param clientName dbflute client project name (NotEmpty)
     * @param alterFileName file name (NotEmpry)
     */
    public void createAlterSql(String clientName, String alterFileName) {
        IntroAssertUtil.assertNotEmpty(clientName, alterFileName);
        createAlterDirIfNotExists(clientName);
        File file = new File(introPhysicalLogic.buildClientPath(clientName, "playsql", "migration", "alter", alterFileName));
        try {
            if (!file.createNewFile()) {
                throw new IOException("Failure to create alter file, file.createNewFile() returns false"); // catch immediately
            }
        } catch (IOException e) {
            throw new IntroFileOperationException("Failed to create new alter sql file: " + file.getAbsolutePath(),
                    "FileName : " + file.getPath(), e);
        }
    }

    /**
     * Create alter directory if not exists alter directory.
     * Do nothing if already alter directory exists.
     *
     * @param clientName dbflute client project name (NotEmpty)
     * @return alter directory path (NotEmpty)
     */
    private String createAlterDirIfNotExists(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        final File alterDir = new File(buildAlterDirectoryPath(clientName));
        if (alterDir.exists()) {
            return alterDir.getPath(); // do nothing
        }
        if (!alterDir.mkdirs()) {
            throw new IntroFileOperationException("'Make directory' is failure", "Path : " + alterDir.getPath());
        }
        return alterDir.getPath();
    }

    // ===================================================================================
    //                                                                               Unzip
    //                                                                               =====
    // TODO cabos use zip util (2019-10-08)
    /**
     * Unzip checked file and copy sql files to alter directory.
     * Do nothing if checked zip not exists.
     *
     * @param clientName dbflute client project name (NotEmpty)
     */
    public void unzipCheckedAlterZip(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        createAlterDirIfNotExists(clientName);
        final String historyPath = buildMigrationPath(clientName, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return;
        }
        loadNewestCheckedZipFile(clientName).ifPresent(zipFile -> {
            final String zipPath = zipFile.getPath();
            final String alterDirPath = buildAlterDirectoryPath(clientName);
            try {
                unzipAlterSqlZipIfNeeds(zipPath, alterDirPath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to unzip checked alter sql zip file: " + zipPath, e);
            }
        });
    }

    private void unzipAlterSqlZipIfNeeds(String zipPath, String dstPath) throws IOException {
        doUnzipAlterSqlZip(zipPath, dstPath);
    }

    private void doUnzipAlterSqlZip(String zipPath, String dstPath) throws IOException {
        try (final ZipFile zipFile = new ZipFile(zipPath)) {
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                final String fileName = entry.getName();
                final String content = IOUtils.toString(zipFile.getInputStream(entry), StandardCharsets.UTF_8);
                Files.write(Paths.get(dstPath, fileName), Collections.singletonList(content));
            }
        }
    }

    // ===================================================================================
    //                                                                                Copy
    //                                                                                ====
    /**
     * Copy from unreleased directory to alter directory only unreleased sql files.
     * Do nothing if unreleased directory is not exists.
     *
     * @param clientName dbflute client project name (NotEmpty)
     */
    public void copyUnreleasedAlterDir(String clientName) {
        IntroAssertUtil.assertNotEmpty(clientName);
        final String alterDirPath = createAlterDirIfNotExists(clientName);
        final String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(clientName);
        final File unreleasedDir = new File(unreleasedAlterDirPath);
        if (!unreleasedDir.exists()) {
            return;
        }
        final File[] files = unreleasedDir.listFiles();
        if (files == null) {
            return;
        }

        Arrays.asList(files).forEach(sourceFile -> {
            if (!isUnreleasedSql(sourceFile)) {
                return;
            }
            copyUnreleasedAlterSqlFile(alterDirPath, sourceFile);
        });
    }

    /**
     * Copy sql file from unreleased dir to alter dir only.
     * Overwrite if same name sql file exists.
     *
     * @param alterDirPath alter directory (NotEmpty)
     * @param sourceFile source file (NotNull)
     */
    private void copyUnreleasedAlterSqlFile(String alterDirPath, File sourceFile) {
        IntroAssertUtil.assertNotEmpty(alterDirPath);
        IntroAssertUtil.assertNotNull(sourceFile);
        final File targetFile = new File(alterDirPath + "/" + convertEditableAlterSqlFileName(sourceFile.getName()));
        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IntroFileOperationException("File copy is failure",
                    "from: " + sourceFile.getPath() + ", to: " + targetFile.getPath());
        }
    }

    /**
     * Check that file is unreleased sql.
     * o path contains "unreleased"
     * o filename starts with "READONLY_alter-schema"
     * o filename ends with ".sql"
     *
     * @param file file (NotNull)
     * @return true, if file is unreleased sql
     */
    private boolean isUnreleasedSql(File file) {
        IntroAssertUtil.assertNotNull(file);
        if (!file.exists()) {
            return false;
        }
        if (!DfStringUtil.contains(file.getPath(), "unreleased")) {
            return false;
        }

        final String fileName = file.getName();
        return DfStringUtil.startsWith(fileName, "READONLY_alter-schema") && DfStringUtil.endsWith(fileName, ".sql");
    }

    /**
     * Convert checked sql file name to edit. (Remove READONLY_)
     * e.g. READONLY_alter-schema-sample.sql => alter-schema-sample.sql
     * @return editable file name
     */
    private String convertEditableAlterSqlFileName(String fileName) {
        IntroAssertUtil.assertNotEmpty(fileName);
        return DfStringUtil.replace(fileName, "READONLY_", "");
    }

    // ===================================================================================
    //                                                               File/Directory Helper
    //                                                               =====================
    // -----------------------------------------------------
    //                                  Build Directory Path
    //                                  --------------------
    private String buildAlterDirectoryPath(String clientName) {
        return buildMigrationPath(clientName, "alter");
    }

    private String buildUnreleasedAlterDirPath(String clientName) {
        return buildMigrationPath(clientName, "history", "unreleased-checked-alter");
    }

    private String buildMigrationPath(String clientName, String pureName) {
        return introPhysicalLogic.buildClientPath(clientName, "playsql", "migration", pureName);
    }

    private String buildMigrationPath(String clientName, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(clientName, "playsql", "migration", type, pureName);
    }
}
