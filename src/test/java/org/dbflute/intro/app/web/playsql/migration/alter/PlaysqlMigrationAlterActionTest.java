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
package org.dbflute.intro.app.web.playsql.migration.alter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.web.playsql.migration.alter.AlterSQLResult.CheckedZipPart;
import org.dbflute.intro.app.web.playsql.migration.alter.AlterSQLResult.SQLFilePart;
import org.dbflute.intro.app.web.playsql.migration.alter.AlterSQLResult.UnreleasedDirPart;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author cabos
 * @author jflute
 */
public class PlaysqlMigrationAlterActionTest extends UnitIntroTestCase {

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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        {
            SQLFilePart checkedAlterSQLFile = checkedFileList.get(0);
            assertEquals(checkedAlterSQLFile.fileName, "alter-schema_001.sql");
        }
        {
            SQLFilePart checkedAlterSQLFile = checkedFileList.get(1);
            assertEquals(checkedAlterSQLFile.fileName, "alter-schema_003.sql");
        }
    }

    public void test_index_checkedUnreleasedState() throws IOException {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        List<String> fileNames = checkedFiles.stream().map(part -> part.fileName).collect(Collectors.toList());
        assertTrue(fileNames.containsAll(
                Arrays.asList("for-previous-20120804-1746.dfmark", "READONLY_alter-schema_sample.sql", "DONT_EDIT_HERE.dfmark")));
    }

    // -----------------------------------------------------
    //                                               prepare
    //                                               -------
    public void test_prepare_existsAlterDir() {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
        inject(alterAction);

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(3, fileList.size());
        List<String> fileNames = fileList.stream().map(file -> file.getName()).collect(Collectors.toList());
        assertTrue(fileNames.containsAll(Arrays.asList("alter-schema_sample.sql", "alter-schema_003.sql", "alter-schema_001.sql")));
    }

    public void test_prepare_notExistsAlterDir() throws IOException {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
        inject(alterAction);

        removeAlterDir();

        // ## Act ##
        alterAction.prepare(TEST_CLIENT_PROJECT);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(3, fileList.size());
        List<String> fileNames = fileList.stream().map(file -> file.getName()).collect(Collectors.toList());
        assertTrue(fileNames.containsAll(Arrays.asList("alter-schema_sample.sql", "alter-schema_003.sql", "alter-schema_001.sql")));
    }

    public void test_prepare_notExistsHistoryDir() throws IOException {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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

    public void test_prepare_alreadyExistsAlterDir() throws IOException {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema_creatable.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        List<File> fileList = loadAlterDir();
        assertEquals(2, fileList.size());
        List<String> fileNames = fileList.stream().map(file -> file.getName()).collect(Collectors.toList());
        assertTrue(fileNames.containsAll(Arrays.asList(body.alterFileName, "alter-schema_sample.sql")));
    }

    public void test_prepare_validateInvalidPrefix() {
        // ## Arrange ##
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        PlaysqlMigrationAlterAction alterAction = new PlaysqlMigrationAlterAction();
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
        FileUtils.deleteDirectory(getTestPlaysqlMigrationAlterDir());
    }

    private void removeHistoryDir() throws IOException {
        FileUtils.deleteDirectory(getTestPlaysqlMigrationHistoryDir());
    }

    private void removeUnreleasedDir() throws IOException {
        FileUtils.deleteDirectory(getTestPlaysqlMigrationUnreleasedDir());
    }

    private void removeMigrationDir() throws IOException {
        FileUtils.deleteDirectory(getTestPlaysqlMigrationDir());
    }

    // ===================================================================================
    //                                                                         File Search
    //                                                                         ===========
    private List<File> loadAlterDir() {
        File alterDir = getTestPlaysqlMigrationAlterDir();
        return OptionalThing.ofNullable(alterDir.listFiles(), () -> {}).map(Arrays::asList).orElse(Collections.emptyList());
    }
}
