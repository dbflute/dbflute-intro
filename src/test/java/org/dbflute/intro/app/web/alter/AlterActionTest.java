package org.dbflute.intro.app.web.alter;

import static org.dbflute.intro.app.web.alter.AlterSQLResult.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.web.validation.exception.ValidationErrorException;

/**
 * @author cabos
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
    public void test_index_editingState() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);
        removeHistoryDir();

        // ## Act ##
        AlterSQLResult result = alterAction.index(TEST_CLIENT_PROJECT).getJsonResult();

        // ## Assert ##

        // editing
        List<AlterSQLResult.AlterDirFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 1);
        {
            AlterSQLResult.AlterDirFilePart alterSQLFilePart = editingFileList.get(0);
            assertEquals(alterSQLFilePart.fileName, "alter-schema_sample.sql");
            assertEquals(alterSQLFilePart.content, "ALTER TABLE MEMBER ADD INDEX IX_BIRTHDATE(birthdate);");
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
        assertEquals(result.ngMark, CDef.NgMark.AlterNG);

        // editing
        List<AlterSQLResult.AlterDirFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 0);

        // checked
        CheckedZipPart checkedZip = result.checkedZip;
        assertEquals(checkedZip.fileName, "20190831_2249/checked-alter-to-20190422-2332.zip");

        List<AlterSQLResult.AlterDirFilePart> checkedFileList = checkedZip.checkedFiles;
        assertEquals(checkedFileList.size(), 2);
        {
            AlterSQLResult.AlterDirFilePart checkedAlterSQLFile = checkedFileList.get(0);
            assertEquals(checkedAlterSQLFile.fileName, "alter-schema_001.sql");
        }
        {
            AlterSQLResult.AlterDirFilePart checkedAlterSQLFile = checkedFileList.get(1);
            assertEquals(checkedAlterSQLFile.fileName, "alter-schema_002.sql");
        }
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
        assertEquals(result.ngMark, CDef.NgMark.AlterNG);

        // editing
        List<AlterSQLResult.AlterDirFilePart> editingFileList = result.editingFiles;
        assertEquals(editingFileList.size(), 0);

        // checked
        // tested at test_index_checkedZipState

        // unreleased
        UnreleasedDirPart unreleasedDir = result.unreleasedDir;
        assertNotNull(unreleasedDir);

        List<AlterDirFilePart> checkedFiles = unreleasedDir.checkedFiles;
        {
            AlterDirFilePart file = checkedFiles.get(0);
            assertEquals(file.fileName, "DONT_EDIT_HERE.dfmark");
        }
        {
            AlterDirFilePart file = checkedFiles.get(1);
            assertEquals(file.fileName, "for-previous-20120804-1746.dfmark");
        }
        {
            AlterDirFilePart file = checkedFiles.get(2);
            assertEquals(file.fileName, "READONLY_alter-schema_sample.sql");
        }
    }

    // -----------------------------------------------------
    //                                                create
    //                                                ------
    public void test_create_existsAlterDir() {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-BUSINESSINFO.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // do nothing
    }

    public void test_create_notExistsAlterDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeAlterDir();

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-BUSINESSINFO.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // do nothing
    }

    public void test_create_notExistsHistoryDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeHistoryDir();

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-BUSINESSINFO.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // do nothing
    }

    public void test_create_notMigrationDir() throws IOException {
        // ## Arrange ##
        AlterAction alterAction = new AlterAction();
        inject(alterAction);

        removeMigrationDir();

        AlterCreateBody body = new AlterCreateBody();
        body.alterFileName = "alter-schema-BUSINESSINFO.sql";

        // ## Act ##
        alterAction.create(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        // do nothing
    }

    public void test_create_validateInvalidPrefix() {
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

    public void test_create_validateNotSQLFile() {
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

    public void test_create_validateInvalidChar() {
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
    //                                                                       Client Helper
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
}
