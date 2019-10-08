package org.dbflute.intro.app.logic.playsql.migrate;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.document.AlterSqlBean;
import org.dbflute.intro.app.logic.document.CheckedZipBean;
import org.dbflute.intro.app.logic.document.UnreleasedDirBean;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.IntroFileOperationException;
import org.dbflute.intro.bizfw.util.AssertUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;

/**
 * @author cabos
 * @author subaru
 */
public class PlaysqlMigrateLogic {

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
    //                                       alter directory
    //                                       ---------------
    /**
     * Load sql files in alter directory.
     *
     * @param clientProject dbflute client project name (NotEmpty)
     * @return list of alter files in alter directory. (NotNull)
     */
    public List<AlterSqlBean> loadAlterSqlFiles(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        return OptionalThing.ofNullable(new File(buildAlterDirectoryPath(clientProject)).listFiles(), () -> {})
                .map(files -> Arrays.stream(files))
                .orElse(Stream.empty())
                .filter(file -> DfStringUtil.endsWith(file.getName(), ".sql"))
                .map(file -> new AlterSqlBean(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                  unreleased directory
    //                                  --------------------
    /**
     * Load unreleased directory info.
     *
     * @param clientProject dbflute client project name (NotEmpty)
     * @return unreleased directory bean (Maybe empty). (NotNull)
     */
    public OptionalThing<UnreleasedDirBean> loadUnreleasedDir(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(clientProject);
        final File alterDir = new File(unreleasedAlterDirPath);
        if (!alterDir.exists() || alterDir.isFile()) {
            return OptionalThing.empty();
        }
        return OptionalThing.of(new UnreleasedDirBean(loadFilesInUnreleasedDirectory(alterDir)));
    }

    /**
     * Load files in unreleased directory.
     *
     * @param unreleasedDir unreleased directory (NotNull)
     * @return file list in unreleased directory (NotNull)
     */
    private List<AlterSqlBean> loadFilesInUnreleasedDirectory(File unreleasedDir) {
        AssertUtil.assertNotNull(unreleasedDir);
        return OptionalThing.ofNullable(unreleasedDir.listFiles(), () -> {})
                .map(files -> Arrays.stream(files))
                .orElse(Stream.empty())
                .map(file -> new AlterSqlBean(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    /**
     * Load alter ng mark if exists.
     *
     * @param clientProject dbflute client project name (NotEmpty)
     * @return Ng mark file (Maybe empty)
     */
    public OptionalThing<CDef.NgMark> loadAlterCheckNgMarkFile(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            if (new File(buildMigrationPath(clientProject, ngMark.code() + ".dfmark")).exists()) {
                return OptionalThing.of(ngMark);
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
     * @param clientProject dbflute client project name (NotEmpty)
     * @return checked zip file bean (Maybe empty)
     */
    public OptionalThing<CheckedZipBean> loadCheckedZip(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        return loadNewestCheckedZipFile(clientProject).map(checkedZip -> {
            return new CheckedZipBean(checkedZip.getName(), loadCheckedSqlList(checkedZip));
        });
    }

    /**
     * Load sql files in checked zip file.
     *
     * @param checkedZipFile checked zip file
     * @return sql files in checked zip file (NotNull)
     */
    private List<AlterSqlBean> loadCheckedSqlList(File checkedZipFile) {
        AssertUtil.assertNotNull(checkedZipFile);
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
    private List<AlterSqlBean> loadAlterSqlFilesInCheckedZip(File checkedZipFile) {
        // TODO cabos resolve duplicate unzip load (2019-10-08)
        AssertUtil.assertNotNull(checkedZipFile);
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
                            final AlterSqlBean bean = new AlterSqlBean(zipEntry.getName(), content);
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
     * @param clientProject dbflute client project name (NotEmpty)
     */
    public void openAlterDir(String clientProject) {
        File alterDir = new File(buildAlterDirectoryPath(clientProject));
        if (alterDir.exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(alterDir);
            } catch (IOException e) {
                throw new UncheckedIOException("fail to open alter directory of" + clientProject, e);
            }
        }
    }

    // ===================================================================================
    //                                                      Check to exists Same Name file
    //                                                      ==============================
    /**
     * Check to exist same name alter SQL file before release.
     * @param clientProject dbflute client project name (NotEmpty)
     * @param sqlFileName checked sql file name (NotEmpty)
     * @return true if exists same name file.
     */
    public boolean existsSameNameAlterSqlFile(String clientProject, String sqlFileName) {
        AssertUtil.assertNotEmpty(clientProject, sqlFileName);
        return existsSameNameFileInAlterDir(clientProject, sqlFileName)           // exists editing sql
                || existsSameNameFileInUnreleasedDir(clientProject, sqlFileName)  // exists sql in unreleased directory
                || existsSameNameFileInCheckedZip(clientProject, sqlFileName);    // exists sql in checked zip
    }

    /**
     * Check to exist same name alter SQL file in alter directory.
     * @param clientProject dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in alter directory.
     */
    private boolean existsSameNameFileInAlterDir(String clientProject, String alterFileName) {
        final String alterPath = buildMigrationPath(clientProject, "alter");
        return Files.exists(Paths.get(alterPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in unreleased directory.
     * @param clientProject dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in unreleased directory.
     */
    private boolean existsSameNameFileInUnreleasedDir(String clientProject, String alterFileName) {
        final String unreleasedDIrPath = buildUnreleasedAlterDirPath(clientProject);
        return Files.exists(Paths.get(unreleasedDIrPath, alterFileName));
    }

    /**
     * Check to exist same name alter SQL file in checked zip.
     * @param clientProject dbflute client project name (NotEmpty)
     * @param alterFileName checked sql file name (NotEmpty)
     * @return true if exists same name file in checked zip.
     */
    private boolean existsSameNameFileInCheckedZip(String clientProject, String alterFileName) {
        final String historyPath = buildMigrationPath(clientProject, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return false;
        }
        return loadNewestCheckedZipFile(clientProject).map(zipFile -> {
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
        final ZipFile zipFile = new ZipFile(zipPath);
        return Collections.list(zipFile.entries())
                .stream()
                .filter(zipEntry -> DfStringUtil.endsWith(zipEntry.getName(), ".sql"))
                .map(zipEntry -> zipEntry.getName())
                .collect(Collectors.toList());
    }

    // TODO cabos write test about newest (2019-10-08)
    private OptionalThing<File> loadNewestCheckedZipFile(String clientProject) {
        final Path historyPath = new File(buildMigrationPath(clientProject, "history")).toPath();
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

    private int compareFileCreationTime(File file1, File file2) {
        try {
            final BasicFileAttributes file1Attributes = Files.readAttributes(file1.toPath(), BasicFileAttributes.class);
            final BasicFileAttributes file2Attributes = Files.readAttributes(file2.toPath(), BasicFileAttributes.class);
            return file1Attributes.creationTime().compareTo(file2Attributes.creationTime());
        } catch (IOException e) {
            return 1;
        }
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    /**
     * Create sql file in alter directory.
     * @param clientProject dbflute client project name (NotEmpty)
     * @param alterFileName file name (NotEmpry)
     */
    public void createAlterSql(String clientProject, String alterFileName) {
        AssertUtil.assertNotEmpty(clientProject, alterFileName);
        createAlterDirIfNotExists(clientProject);
        File file = new File(introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "alter", alterFileName));
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
     * @param clientProject dbflute client project name (NotEmpty)
     * @return alter directory path (NotEmpty)
     */
    private String createAlterDirIfNotExists(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        final File alterDir = new File(buildAlterDirectoryPath(clientProject));
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
     * @param clientProject dbflute client project name (NotEmpty)
     */
    public void unzipCheckedAlterZip(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        createAlterDirIfNotExists(clientProject);
        final String historyPath = buildMigrationPath(clientProject, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return;
        }
        loadNewestCheckedZipFile(clientProject).ifPresent(zipFile -> {
            final String zipPath = zipFile.getPath();
            final String alterDirPath = buildAlterDirectoryPath(clientProject);
            try {
                unzipAlterSqlZipIfNeeds(zipPath, alterDirPath);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to unzip checked alter sql zip file: " + zipPath, e);
            }
        });
    }

    private void unzipAlterSqlZipIfNeeds(String zipPath, String dstPath) throws IOException {
        if (!existsSqlFiles(dstPath)) {
            doUnzipAlterSqlZip(zipPath, dstPath);
        }
    }

    private boolean existsSqlFiles(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        if (Objects.isNull(files)) {
            return false;
        }
        return Arrays.stream(files).anyMatch(file -> DfStringUtil.endsWith(file.getName(), ".sql"));
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
    //                                                                                Copy
    //                                                                                ====
    /**
     * Copy from unreleased directory to alter directory only unreleased sql files.
     * Do nothing if unreleased directory is not exists.
     *
     * @param clientProject dbflute client project name (NotEmpty)
     */
    public void copyUnreleasedAlterDir(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        final String alterDirPath = createAlterDirIfNotExists(clientProject);
        final String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(clientProject);
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
        AssertUtil.assertNotEmpty(alterDirPath);
        AssertUtil.assertNotNull(sourceFile);
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
        AssertUtil.assertNotNull(file);
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
        AssertUtil.assertNotEmpty(fileName);
        return DfStringUtil.replace(fileName, "READONLY_", "");
    }

    // ===================================================================================
    //                                                               File/Directory Helper
    //                                                               =====================
    // -----------------------------------------------------
    //                                  Build Directory Path
    //                                  --------------------
    private String buildAlterDirectoryPath(String clientProject) {
        return buildMigrationPath(clientProject, "alter");
    }

    private String buildUnreleasedAlterDirPath(String clientProject) {
        return buildMigrationPath(clientProject, "history", "unreleased-checked-alter");
    }

    private String buildMigrationPath(String clientProject, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", pureName);
    }

    private String buildMigrationPath(String clientProject, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", type, pureName);
    }
}
