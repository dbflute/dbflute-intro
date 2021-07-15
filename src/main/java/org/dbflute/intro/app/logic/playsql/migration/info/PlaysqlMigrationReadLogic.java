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
package org.dbflute.intro.app.logic.playsql.migration.info;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationPhysicalLogic;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationAlterSqlReturn;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationCheckedZipReturn;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationDirReturn;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationNgMarkFileReturn;
import org.dbflute.intro.app.logic.playsql.migration.info.ref.PlaysqlMigrationUnreleasedDirReturn;
import org.dbflute.intro.bizfw.tellfailure.IntroFileOperationException;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;

/**
 * The logic for playsql.migration (in DBFlute Client).
 * @author cabos
 * @author subaru
 * @author prprmurakami
 * @author jflute
 */
public class PlaysqlMigrationReadLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // This pattern is same as DBFlute specification.
    private static final DateTimeFormatter ZIP_PATH_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private PlaysqlMigrationPhysicalLogic playsqlMigrationPhysicalLogic;

    // ===================================================================================
    //                                                                          Whole Info
    //                                                                          ==========
    /**
     * Load migration directory.
     * @param projectName The project name of the DBFlute client. (NotNull, NotEmpty)
     * @return dbflute_client/playsql/migration directory information (NotNull)
     */
    public PlaysqlMigrationDirReturn loadPlaysqlMigrationDir(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new PlaysqlMigrationDirReturn( //
                loadAlterCheckNgMarkFile(projectName).orElse(null), //
                loadAlterSqlFiles(projectName), //
                loadCheckedZip(projectName).orElse(null), //
                loadUnreleasedDir(projectName).orElse(null) //
        );
    }

    // -----------------------------------------------------
    //                                       alter directory
    //                                       ---------------
    /**
     * Load sql files in alter directory.
     *
     * @param projectName dbflute client project name (NotEmpty)
     * @return list of alter files in alter directory. (NotNull)
     */
    private List<PlaysqlMigrationAlterSqlReturn> loadAlterSqlFiles(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return OptionalThing.ofNullable(new File(buildAlterDirectoryPath(projectName)).listFiles(), () -> {})
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
     * @param projectName dbflute client project name (NotEmpty)
     * @return unreleased directory bean (Maybe empty). (NotNull)
     */
    private OptionalThing<PlaysqlMigrationUnreleasedDirReturn> loadUnreleasedDir(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(projectName);
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
     * @param projectName dbflute client project name (NotEmpty)
     * @return Ng mark file (Maybe empty)
     */
    private OptionalThing<PlaysqlMigrationNgMarkFileReturn> loadAlterCheckNgMarkFile(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            File file = new File(buildMigrationPath(projectName, ngMark.code() + ".dfmark"));
            if (file.exists()) {
                return OptionalThing.of(new PlaysqlMigrationNgMarkFileReturn(ngMark, flutyFileLogic.readFile(file)));
            }
        }
        return OptionalThing.empty();
    }

    // ===================================================================================
    //                                                                         Checked Zip
    //                                                                         ===========
    /**
     * Load whole information of newest checked zip file.
     *
     * @param projectName dbflute client project name (NotEmpty)
     * @return checked zip file bean (Maybe empty)
     */
    public OptionalThing<PlaysqlMigrationCheckedZipReturn> loadCheckedZip(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return loadNewestCheckedZipFile(projectName).map(checkedZip -> {
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
        // done cabos resolve duplicate unzip load (2019-10-08)
        // fix this issue https://github.com/dbflute/dbflute-intro/issues/257
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

    // -----------------------------------------------------
    //                               Newest Checked Zip File
    //                               -----------------------
    public OptionalThing<File> loadNewestCheckedZipFile(String projectName) { // called for general purpose
        final Path historyPath = new File(buildMigrationPath(projectName, "history")).toPath();
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
    //                                                      Check to exists Same Name file
    //                                                      ==============================
    /**
     * Check to exist same name alter SQL file before release.
     * @param projectName dbflute client project name (NotEmpty)
     * @param sqlFileName checked sql file name (NotEmpty)
     * @return true if exists same name file.
     */
    public boolean existsSameNameAlterSqlFile(String projectName, String sqlFileName) {
        IntroAssertUtil.assertNotEmpty(projectName, sqlFileName);
        return existsSameNameFileInAlterDir(projectName, sqlFileName) // exists editing sql
                || existsSameNameFileInUnreleasedDir(projectName, sqlFileName) // exists sql in unreleased directory
                || existsSameNameFileInCheckedZip(projectName, sqlFileName); // exists sql in checked zip
    }

    /**
     * Check to exist same name alter SQL file in alter directory.
     * @param projectName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in alter directory.
     */
    private boolean existsSameNameFileInAlterDir(String projectName, String alterFileName) {
        final String alterPath = buildMigrationPath(projectName, "alter");
        return Files.exists(Paths.get(alterPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in unreleased directory.
     * @param projectName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in unreleased directory.
     */
    private boolean existsSameNameFileInUnreleasedDir(String projectName, String alterFileName) {
        final String unreleasedDIrPath = buildUnreleasedAlterDirPath(projectName);
        return Files.exists(Paths.get(unreleasedDIrPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in checked zip.
     * @param projectName dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in checked zip.
     */
    private boolean existsSameNameFileInCheckedZip(String projectName, String alterFileName) {
        final String historyPath = buildMigrationPath(projectName, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return false;
        }
        return loadNewestCheckedZipFile(projectName).map(zipFile -> {
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

    // ===================================================================================
    //                                                               File/Directory Helper
    //                                                               =====================
    private String buildAlterDirectoryPath(String projectName) {
        return playsqlMigrationPhysicalLogic.buildAlterDirectoryPath(projectName);
    }

    private String buildUnreleasedAlterDirPath(String projectName) {
        return playsqlMigrationPhysicalLogic.buildUnreleasedAlterDirPath(projectName);
    }

    private String buildMigrationPath(String projectName, String pureName) {
        return playsqlMigrationPhysicalLogic.buildMigrationPath(projectName, pureName);
    }
}
