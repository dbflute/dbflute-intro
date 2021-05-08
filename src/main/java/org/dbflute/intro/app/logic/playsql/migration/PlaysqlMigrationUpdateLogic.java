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
package org.dbflute.intro.app.logic.playsql.migration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.logic.playsql.migration.info.PlaysqlMigrationInfoLogic;
import org.dbflute.intro.bizfw.tellfailure.IntroFileOperationException;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.util.DfStringUtil;

/**
 * The logic for playsql.migration (in DBFlute Client).
 * @author cabos
 * @author subaru
 * @author prprmurakami
 * @author jflute (rename and split it to several classes)
 */
public class PlaysqlMigrationUpdateLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private PlaysqlMigrationInfoLogic playsqlMigrationInfoLogic;
    @Resource
    private PlaysqlMigrationPhysicalLogic playsqlMigrationPhysicalLogic;

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    /**
     * Create sql file in alter directory.
     * @param projectName dbflute client project name (NotEmpty)
     * @param alterFileName file name (NotEmpry)
     */
    public void createAlterSql(String projectName, String alterFileName) {
        IntroAssertUtil.assertNotEmpty(projectName, alterFileName);
        createAlterDirIfNotExists(projectName);
        File file = new File(introPhysicalLogic.buildClientPath(projectName, "playsql", "migration", "alter", alterFileName));
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
     * @param projectName dbflute client project name (NotEmpty)
     * @return alter directory path (NotEmpty)
     */
    private String createAlterDirIfNotExists(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        final File alterDir = new File(buildAlterDirectoryPath(projectName));
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
    // done cabos use zip util (2019-10-08)
    // fix this issue https://github.com/dbflute/dbflute-intro/issues/257
    /**
     * Unzip checked file and copy sql files to alter directory.
     * Do nothing if checked zip not exists.
     *
     * @param projectName dbflute client project name (NotEmpty)
     */
    public void unzipCheckedAlterZip(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        createAlterDirIfNotExists(projectName);
        final String historyPath = buildMigrationPath(projectName, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return;
        }
        playsqlMigrationInfoLogic.loadNewestCheckedZipFile(projectName).ifPresent(zipFile -> {
            final String zipPath = zipFile.getPath();
            final String alterDirPath = buildAlterDirectoryPath(projectName);
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
     * @param projectName dbflute client project name (NotEmpty)
     */
    public void copyUnreleasedAlterDir(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        final String alterDirPath = createAlterDirIfNotExists(projectName);
        final String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(projectName);
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
