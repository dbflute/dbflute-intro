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
package org.dbflute.intro.app.web.alter;

import static org.dbflute.intro.app.web.alter.AlterSQLResult.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.playsql.migration.PlaysqlMigrationLogic;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author cabos
 * @author hakiba
 */
public class AlterActionTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String MIGRATION_DIR = TEST_CLIENT_PATH + PLAYSQL_DIR_PATH + "/migration";
    private static final String ALTER_DIR = MIGRATION_DIR + "/alter";
    private static final String HISTORY_DIR = MIGRATION_DIR + "/history";
    private static final String UNRELEASED_DIR = HISTORY_DIR + "unreleased-checked-alter";

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    @Override
    public void setUp() throws Exception {
        super.setUp();
        preparePlaysqlFiles();
    }

    // ===================================================================================
    //                                                                               Test
    //                                                                              ======
    // -----------------------------------------------------
    //                                                 index
    //                                                 -----
    public void test_index_afterCreateClientOnly() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);
        removeMigrationDir();

        // ## Act ##
        AlterSQLResult result = alterAction.index(TEST_CLIENT_PROJECT).getJsonResult();

        // ## Assert ##
        assertNull(result.ngMarkFile);
        assertNull(result.checkedZip);
        assertNull(result.unreleasedDir);
        assertTrue(result.editingFiles.isEmpty());
    }

    public void test_index_editingState() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);
        removeHistoryDir();

        // ## Act ##
        AlterSQLResult result = alterAction.index(TEST_CLIENT_PROJECT).getJsonResult();

        // ## Assert ##

        // editing
        List<SQLFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 1);
        {
            SQLFilePart alterSQLFilePart = editingFileList.get(0);
            assertEquals(alterSQLFilePart.fileName, "alter-schema_sample.sql");
            assertEquals(alterSQLFilePart.content, "ALTER TABLE MEMBER ADD INDEX IX_BIRTHDATE(birthdate);\n");
        }

        // checked
        CheckedZipPart checkedFile = result.checkedZip;
        assertNull(checkedFile);
    }

    public void test_index_checkedZipState() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);
        removeAlterDir();
        removeUnreleasedDir();

        // ## Act ##
        AlterSQLResult result = alterAction.index(TEST_CLIENT_PROJECT).getJsonResult();

        // ## Assert ##
        // mark
        assertEquals(result.ngMarkFile.ngMark, CDef.NgMark.AlterNG);

        // editing
        List<SQLFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 0);

        // checked
        CheckedZipPart checkedZip = result.checkedZip;
        assertEquals(checkedZip.fileName, "checked-alter-to-20190422-2332.zip");

        List<SQLFilePart> checkedFileList = checkedZip.checkedFiles;
        assertEquals(checkedFileList.size(), 2);
        List<String> fileNameList = checkedFileList.stream().map(sqlFilePart -> sqlFilePart.fileName).collect(Collectors.toList());
        List<String> expected = loadNewestCheckedFileNames();
        assertTrue(String.format("expected=%s, actual=%s", expected, fileNameList), fileNameList.containsAll(expected));
    }

    public void test_index_checkedUnreleasedState() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);
        removeAlterDir();

        // ## Act ##
        AlterSQLResult result = alterAction.index(TEST_CLIENT_PROJECT).getJsonResult();

        // ## Assert ##
        // mark
        assertEquals(result.ngMarkFile.ngMark, CDef.NgMark.AlterNG);

        // editing
        List<SQLFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 0);

        // checked
        // tested at test_index_checkedZipState

        // unreleased
        UnreleasedDirPart unreleasedDir = result.unreleasedDir;
        assertNotNull(unreleasedDir);

        List<SQLFilePart> checkedFiles = unreleasedDir.checkedFiles;
        List<String> fileNameList = checkedFiles.stream().map(sqlFilePart -> sqlFilePart.fileName).collect(Collectors.toList());
        List<String> expected =
                Arrays.asList("for-previous-20120804-1746.dfmark", "READONLY_alter-schema_sample.sql", "DONT_EDIT_HERE.dfmark");
        assertTrue(String.format("expected=%s, actual=%s", expected, fileNameList), fileNameList.containsAll(expected));
    }

    // -----------------------------------------------------
    //                                               prepare
    //                                               -------
    public void test_prepare_existsAlterDir() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(3, fileList.size());
        List<String> fileNameList = fileList.stream().map(File::getName).collect(Collectors.toList());
        List<String> expected = loadNewestCheckedFileNames();
        assertTrue(String.format("expected=%s, actual=%s", expected, fileNameList), fileNameList.containsAll(expected));
    }

    public void test_prepare_notExistsAlterDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeAlterDir();

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(3, fileList.size());
        List<String> fileNameList = fileList.stream().map(File::getName).collect(Collectors.toList());
        List<String> expected = loadNewestCheckedFileNames();
        assertTrue(String.format("expected=%s, actual=%s", expected, fileNameList), fileNameList.containsAll(expected));
    }

    public void test_prepare_notExistsHistoryDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeHistoryDir();

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(1, fileList.size());
        {
            File file = fileList.get(0);
            assertEquals(file.getName(), "alter-schema_sample.sql");
        }
    }

    public void test_prepare_notExistsMigrationDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeMigrationDir();

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(0, fileList.size());
    }

    // -----------------------------------------------------
    //                                                create
    //                                                ------
    public void test_prepare_success() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeAlterDir();

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema_creatable.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(1, fileList.size());
        {
            File file = fileList.get(0);
            assertEquals(file.getName(), body.alterFileName);
        }
    }

    public void test_prepare_alreadyExistsAlterDir() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema_creatable.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(2, fileList.size());
        List<String> fileNameList = fileList.stream().map(File::getName).collect(Collectors.toList());
        List<String> expected = Arrays.asList(body.alterFileName, "alter-schema_sample.sql");
        assertTrue(String.format("expected=%s, actual=%s", expected, fileNameList), fileNameList.containsAll(expected));
    }

    public void test_prepare_validateInvalidPrefix() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter_schema_sample_business.sql";

        // ## Act, Assert ##
        assertException(ValidationErrorException.class, () -> {
            alterAction.create(TEST_CLIENT_PROJECT, body);
        });
    }

    public void test_prepare_validateNotSQLFile() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-sample_business.txt";

        // ## Act, Assert ##
        assertException(ValidationErrorException.class, () -> {
            alterAction.create(TEST_CLIENT_PROJECT, body);
        });
    }

    public void test_create_validateSameFileName() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema_sample.sql"; // see test/resource/playsql/mygration/alter/alter-schema_sample.sql

        // ## Act, Assert ##
        assertException(ValidationErrorException.class, () -> {
            alterAction.create(TEST_CLIENT_PROJECT, body);
        });
    }

    public void test_prepare_validateInvalidChar() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-/\\<>*?\"|:;\0 .sql";

        // ## Act, Assert ##
        assertException(ValidationErrorException.class, () -> {
            alterAction.create(TEST_CLIENT_PROJECT, body);
        });
    }

    // ===================================================================================
    //                                                                       Client Adjust
    //                                                                       =============
    private void removeAlterDir() throws IOException {
        FileUtils.deleteDirectory(new File(getProjectDir(), ALTER_DIR));
    }

    private void removeHistoryDir() throws IOException {
        FileUtils.deleteDirectory(new File(getProjectDir(), HISTORY_DIR));
    }

    private void removeUnreleasedDir() throws IOException {
        FileUtils.deleteDirectory(new File(getProjectDir(), UNRELEASED_DIR));
    }

    private void removeMigrationDir() throws IOException {
        FileUtils.deleteDirectory(new File(getProjectDir(), MIGRATION_DIR));
    }

    // ===================================================================================
    //                                                                         File Search
    //                                                                         ===========
    private List<File> loadAlterDir() {
        return OptionalThing.ofNullable(new File(getProjectDir(), ALTER_DIR).listFiles(), () -> {})
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    // TODO hakiba delete when assertions can be made in checked zip files for test (2020-01-01)
    private List<String> loadNewestCheckedFileNames() {
        PlaysqlMigrationLogic logic = new PlaysqlMigrationLogic();
        inject(logic);
        return logic.loadNewestCheckedZipFile(TEST_CLIENT_PROJECT).map(zipFile -> {
            List<String> fileNames = new ArrayList<>();
            try (final ZipFile file = new ZipFile(zipFile.getPath())) {
                final Enumeration<? extends ZipEntry> entries = file.entries();
                while (entries.hasMoreElements()) {
                    final ZipEntry entry = entries.nextElement();
                    fileNames.add(entry.getName());
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to unzip checked alter sql zip file: " + zipFile.getPath(), e);
            }
            return fileNames;
        }).orElse(Collections.emptyList());
    }
}
