package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapFile;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.bizfw.tellfailure.PhysicalDecoMapFileException;
import org.dbflute.util.DfStringUtil;
import org.lastaflute.core.time.TimeManager;

/**
 * @author cabos
 * @author hakiba
 */
public class DocumentDecommentPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String PICKUP_FILE_NAME = "decomment-pickup.dfmap";
    private static final Map<String, String> REPLACE_CHAR_MAP;

    static {
        List<String> notAvailableCharList = Arrays.asList("/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0");
        String replaceChar = "x";
        REPLACE_CHAR_MAP = notAvailableCharList.stream().collect(Collectors.toMap(ch -> ch, ch -> replaceChar));
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private DocumentAuthorLogic documentAuthorLogic;
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void saveDecommentPieceMap(String clientProject, DfDecoMapPiece decoMapPiece) {
        String tableName = decoMapPiece.getDecoMap().getTableName();
        String author = decoMapPiece.getAuthor();
        File pieceMapFile = new File(buildDecommentPiecePath(clientProject, buildPieceFileName(tableName, author)));
        createPieceMapFile(pieceMapFile);
        try (OutputStream outputStream = new FileOutputStream(pieceMapFile)) {
            // done cabos remove 'df' from variable name by jflute (2017/08/10)
            DfDecoMapFile decoMapFile = new DfDecoMapFile();
            decoMapFile.writeMap(outputStream, decoMapPiece.convertMap());
            // done cabos make and throw PhysicalCabosException (application exception) see ClientNotFoundException by jflute (2017/08/10)
        } catch (FileNotFoundException | SecurityException e) {
            throw new PhysicalDecoMapFileException("fail to open decomment piece map file, file path : " + pieceMapFile.getAbsolutePath(),
                    pieceMapFile.getAbsolutePath(), e);
        } catch (IOException e) {
            throw new PhysicalDecoMapFileException("maybe... fail to execute \"outputStream.close()\".", pieceMapFile.getAbsolutePath(), e);
        }
    }

    private String buildPieceFileName(String tableName, String author) { // e.g decomment-piece-TABLE_NAME-20170316-123456-789-jflute.dfmap
        return "decomment-piece-" + tableName + "-" + getCurrentDateStr() + "-" + author + ".dfmap";
    }

    private String getCurrentDateStr() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS").format(timeManager.currentDateTime());
    }

    private void createPieceMapFile(File pieceMapFile) {
        try {
            Files.createDirectories(Paths.get(pieceMapFile.getParentFile().getAbsolutePath()));
            Files.createFile(Paths.get(pieceMapFile.getAbsolutePath()));
        } catch (IOException e) {
            throw new PhysicalDecoMapFileException("fail to create decomment piece map file, file path : " + pieceMapFile.getAbsolutePath(),
                    pieceMapFile.getAbsolutePath(), e);
        }
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // done hakiba tag comment: Pickup Map by jflute (2017/08/17)
    public DfDecoMapPickup readMergedDecommentPickupMap(String clientProject) {
        List<DfDecoMapPiece> pieces = readAllDecommentPieceMap(clientProject);
        DfDecoMapPickup pickup = readDecommentPickupMap(clientProject);
        DfDecoMapFile decoMapFile = new DfDecoMapFile();
        return decoMapFile.merge(pickup, pieces);
    }

    // done hakoba public on demand, so private now by jflute (2017/08/17)
    private List<DfDecoMapPiece> readAllDecommentPieceMap(String clientProject) {
        String dirPath = buildDecommentPieceDirPath(clientProject);
        try {
            return Files.list(Paths.get(dirPath)).map(path -> {
                try {
                    String fileName = path.toFile().getName();
                    DfDecoMapFile decoMapFile = new DfDecoMapFile();
                    return decoMapFile.readPiece(fileName, Files.newInputStream(path));
                } catch (IOException e) {
                    throw new UncheckedIOException("fail to read decomment piece map file. path : " + path.toAbsolutePath(), e);
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("fail to read decomment piece map directory. path : " + dirPath, e);
        }
    }

    private DfDecoMapPickup readDecommentPickupMap(String clientProject) {
        String filePath = buildDecommentPickupPath(clientProject);
        try {
            DfDecoMapFile decoMapFile = new DfDecoMapFile();
            return decoMapFile.readPickup(PICKUP_FILE_NAME, Files.newInputStream(Paths.get(filePath)));
        } catch (IOException e) {
            throw new UncheckedIOException("fail to read decomment pickup map. path : " + filePath, e);
        }
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public String getAuthor() {
        return filterAuthor(documentAuthorLogic.getAuthor());
    }

    private String filterAuthor(String author) {
        return DfStringUtil.replaceBy(author, REPLACE_CHAR_MAP);
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private String buildDecommentPieceDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "piece");
    }

    private String buildDecommentPiecePath(String clientProject, String fileName) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "piece", fileName);
    }

    private String buildDecommentPickupPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "pickup", PICKUP_FILE_NAME);
    }
}
