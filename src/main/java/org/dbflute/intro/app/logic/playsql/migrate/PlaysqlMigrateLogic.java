package org.dbflute.intro.app.logic.playsql.migrate;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
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
    //                                                                         Find/Exists
    //                                                                         ===========
    /**
     * Load newest checked zip file info.
     * .
     * @param clientProject dbflute client project name (NotEmpty)
     * @return zip file bean (Maybe empty)
     */
    public OptionalThing<CheckedZipBean> loadCheckedZip(String clientProject) {
        AssertUtil.assertNotEmpty(clientProject);
        return findNewestCheckedZipFile(clientProject).map(checkedZip -> {
            return new CheckedZipBean(checkedZip.getName(), loadCheckedSqlList(checkedZip));
        });
    }

    /**
     * Load sql files in checked zip files.
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

    // TODO cabos make this method private (see PlaysqlMigrationAction) (2019-10-08)
    @Deprecated
    public File findAlterDir(String clientProject) {
        return new File(buildMigrationPath(clientProject, "", "alter"));
    }

    public List<AlterSqlBean> findAlterFiles(String clientProject) {
        File[] files = findAlterDir(clientProject).listFiles();
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }

        return Arrays.stream(files)
                .filter(file -> DfStringUtil.endsWith(file.getName(), ".sql"))
                .map(file -> new AlterSqlBean(file.getName(), flutyFileLogic.readFile(file)))
                .collect(Collectors.toList());
    }

    public OptionalThing<UnreleasedDirBean> findUnreleasedAlterDir(String clientProject) {
        String unreleasedAlterDirPath = buildUnreleasedAlterDirPath(clientProject);
        File dir = new File(unreleasedAlterDirPath);
        File[] files = dir.listFiles();
        if (!dir.exists() || dir.isFile() || files == null) {
            return OptionalThing.empty();
        }
        return OptionalThing.of(new UnreleasedDirBean(getUnreleasedSqlList(files)));
    }

    private List<AlterSqlBean> getUnreleasedSqlList(File[] files) {
        if (Objects.isNull(files)) {
            return Collections.emptyList();
        }
        return Arrays.stream(files).map(file -> {
            return new AlterSqlBean(file.getName(), flutyFileLogic.readFile(file));
        }).collect(Collectors.toList());
    }

    public CDef.NgMark findAlterCheckNgMark(String clientProject) {
        for (CDef.NgMark ngMark : CDef.NgMark.listAll()) {
            if (new File(buildMigrationPath(clientProject, ngMark.code() + ".dfmark")).exists()) {
                return ngMark;
            }
        }
        return null;
    }

    private List<String> findAlterSqlNamesFromZip(String zipPath) throws IOException {
        final List<String> nameList = new ArrayList<>();
        final ZipFile zipFile = new ZipFile(zipPath);
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry entry = entries.nextElement();
            final String fileName = entry.getName();
            if (!DfStringUtil.contains(fileName, ".sql")) {
                continue;
            }
            nameList.add(fileName);
        }
        zipFile.close();
        return nameList;
    }

    public boolean existsAlterFileAlready(String clientProject, String alterFileName) {
        return existsAlterFileInAlterDir(clientProject, alterFileName) || existsAlterFileInCheckedZip(clientProject, alterFileName)
                || existsAlterFileInUnreleasedDir(clientProject, alterFileName);
    }

    private boolean existsAlterFileInAlterDir(String clientProject, String alterFileName) {
        final String alterPath = buildMigrationPath(clientProject, "alter");
        return Files.exists(Paths.get(alterPath, alterFileName));
    }

    private boolean existsAlterFileInCheckedZip(String clientProject, String alterFileName) {
        final String historyPath = buildMigrationPath(clientProject, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return false;
        }

        return findNewestCheckedZipFile(clientProject).map(zipFile -> {
            String zipPath = zipFile.getPath();
            try {
                List<String> alterSqlNames = findAlterSqlNamesFromZip(zipPath);
                return alterSqlNames.contains(alterFileName);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to read zip file: " + zipPath, e);
            }
        }).orElse(false);
    }

    private boolean existsAlterFileInUnreleasedDir(String clientProject, String alterFileName) {
        final String unreleasedDIrPath = buildMigrationPath(clientProject, "history", "unreleased-checked-alter");
        return Files.exists(Paths.get(unreleasedDIrPath, alterFileName));
    }

    private boolean existsSqlFiles(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        if (Objects.isNull(files)) {
            return false;
        }
        return Arrays.stream(files).anyMatch(file -> DfStringUtil.endsWith(file.getName(), ".sql"));
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private OptionalThing<File> findNewestCheckedZipFile(String clientProject) {
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

    private String buildMigrationPath(String clientProject, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", pureName);
    }

    private String buildMigrationPath(String clientProject, String type, String pureName) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", type, pureName);
    }

    private String buildUnreleasedAlterDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "history", "unreleased-checked-alter");
    }

    // ===================================================================================
    //                                                                              Create
    //                                                                              ======
    public void createAlterSql(String clientProject, String alterFileName) {
        createAlterDirIfNotExists(clientProject);
        File file = new File(introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "alter", alterFileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create new alter sql file: " + file.getAbsolutePath(), e);
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
        final File alterDir = new File(buildMigrationPath(clientProject, "", "alter"));
        if (alterDir.exists()) {
            return alterDir.getPath(); // do nothing
        }
        if (!alterDir.mkdirs()) {
            throw new IntroFileOperationException("'Make directory' is failure", "Path : " + alterDir.getPath());
        }
        return alterDir.getPath();
    }

    // ===================================================================================
    //                                                                                 Zip
    //                                                                                 ===
    public void unzipCheckedAlterZip(String clientProject) {
        createAlterDirIfNotExists(clientProject);
        final String historyPath = buildMigrationPath(clientProject, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return;
        }
        findNewestCheckedZipFile(clientProject).ifPresent(zipFile -> {
            final String zipPath = zipFile.getPath();
            final String alterDirPath = buildMigrationPath(clientProject, "", "alter");
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
}
