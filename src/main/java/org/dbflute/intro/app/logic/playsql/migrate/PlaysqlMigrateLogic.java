package org.dbflute.intro.app.logic.playsql.migrate;

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
    public OptionalThing<CheckedZipBean> findCheckedZip(String clientProject) {
        final String zipPath = buildCheckedAlterZipPath(clientProject);
        if (zipPath == null) {
            return OptionalThing.empty();
        }
        return OptionalThing.of(new CheckedZipBean(new File(zipPath).getName(), findStackedAlterSqls(clientProject)));
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

    // TODO cabos fix response type Optional<File> -> File (2019-10-07)
    public OptionalThing<File> findAlterDir(String clientProject) {
        File file = new File(buildMigrationPath(clientProject, "", "alter"));
        if (file.exists()) {
            return OptionalThing.of(file);
        }
        return OptionalThing.empty();
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
        return Arrays.stream(Objects.requireNonNull(files)).map(file -> {
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

        final String zipPath = buildCheckedAlterZipPath(clientProject);
        if (zipPath == null) {
            return false;
        }
        try {
            List<String> alterSqlNames = findAlterSqlNamesFromZip(zipPath);
            return alterSqlNames.contains(alterFileName);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read zip file: " + zipPath, e);
        }
    }

    private boolean existsAlterFileInUnreleasedDir(String clientProject, String alterFileName) {
        final String unreleasedDIrPath = buildMigrationPath(clientProject, "history", "unreleased-checked-alter");
        return Files.exists(Paths.get(unreleasedDIrPath, alterFileName));
    }

    private boolean existsSqlFiles(String dirPath) {
        return Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles()))
                .anyMatch(file -> DfStringUtil.endsWith(file.getName(), ".sql"));
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private String buildCheckedAlterZipPath(String clientProject) {
        final Path historyPath = Paths.get(buildMigrationPath(clientProject, "history"));
        if (Files.notExists(historyPath)) {
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
    /**
     * Create alter directory if not exists alter directory.
     * Do nothing if already alter directory exists.
     *
     * @param clientProject dbflute client project name (NotEmpty)
     * @return alter directory path (NotEmpty)
     */
    public String createAlterDir(String clientProject) {
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

    public void createAlterSql(String clientProject, String alterFileName) {
        File file = new File(introPhysicalLogic.buildClientPath(clientProject, "playsql", "migration", "alter", alterFileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create new alter sql file: " + file.getAbsolutePath(), e);
        }
    }

    // ===================================================================================
    //                                                                                 Zip
    //                                                                                 ===
    public void unzipAlterSqlZip(String clientProject) {
        final String historyPath = buildMigrationPath(clientProject, "history");
        if (Files.notExists(Paths.get(historyPath))) {
            return;
        }

        final String zipPath = buildCheckedAlterZipPath(clientProject);
        if (zipPath == null) {
            return;
        }

        final String alterDirPath = buildMigrationPath(clientProject, "", "alter");
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
        final String alterDirPath = createAlterDir(clientProject);
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
     *
     * @param alterDirPath alter directory (NotEmpty)
     * @param sourceFile source file (NotNull)
     */
    private void copyUnreleasedAlterSqlFile(String alterDirPath, File sourceFile) {
        AssertUtil.assertNotEmpty(alterDirPath);
        AssertUtil.assertNotNull(sourceFile);
        final File targetFile = new File(alterDirPath + "/" + convertEditableAlterSqlFileName(sourceFile.getName()));
        try {
            Files.copy(sourceFile.toPath(), targetFile.toPath());
        } catch (IOException e) {
            throw new IntroFileOperationException("File copy is failure", "from: " + sourceFile.getPath() + ", to: " + targetFile.getPath());
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
