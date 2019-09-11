package org.dbflute.intro.app.web.alter;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

    private void removeMigrationDir() throws IOException {
        FileUtils.deleteDirectory(new File(getProjectDir(), MIGRATION_DIR));
    }
}
