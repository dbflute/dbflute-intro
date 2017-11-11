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
import java.util.Collections;
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
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;
import org.lastaflute.core.time.TimeManager;

/**
 * @author cabos
 * @author hakiba
 * @author jflute
 */
public class DocumentDecommentPhysicalLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String PICKUP_FILE_NAME = "decomment-pickup.dfmap";
    protected static final Map<String, String> REPLACE_CHAR_MAP;

    static {
        // done cabos add spaces and replaceChar should be underscore? by jflute (2017/09/07)
        List<String> notAvailableCharList = Arrays.asList("/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0", " ");
        String replaceChar = "_";
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
        String tableName = decoMapPiece.getTableName();
        String owner = decoMapPiece.getPieceOwner();
        String pieceCode = decoMapPiece.getPieceCode();
        File pieceMapFile = new File(buildDecommentPiecePath(clientProject, buildPieceFileName(tableName, owner, pieceCode)));
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

    protected String buildPieceFileName(String tableName, String owner,
        String pieceCode) { // e.g decomment-piece-TABLE_NAME-20170316-123456-789-jflute.dfmap
        return "decomment-piece-" + tableName + "-" + getCurrentDateStr() + "-" + owner + "-" + pieceCode + ".dfmap";
    }

    protected String getCurrentDateStr() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS").format(timeManager.currentDateTime());
    }

    protected void createPieceMapFile(File pieceMapFile) {
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
        List<DfDecoMapPiece> pieces =
            readAllDecommentPieceMap(clientProject).stream().filter(piece -> !piece.isMerged()).collect(Collectors.toList());
        OptionalThing<DfDecoMapPickup> pickupOpt = readDecommentPickupMap(clientProject);
        DfDecoMapFile decoMapFile = new DfDecoMapFile();
        return decoMapFile.merge(pickupOpt, pieces);
    }

    // done hakoba public on demand, so private now by jflute (2017/08/17)
    private List<DfDecoMapPiece> readAllDecommentPieceMap(String clientProject) {
        String dirPath = buildDecommentPieceDirPath(clientProject);
        // done hakiba support no-existing directory by jflute (2017/09/28)
        if (Files.notExists(Paths.get(dirPath))) {
            return Collections.emptyList();
        }
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

    private OptionalThing<DfDecoMapPickup> readDecommentPickupMap(String clientProject) {
        String filePath = buildDecommentPickupPath(clientProject);
        // done hakiba support no-existing directory or file by jflute (2017/09/28)
        if (Files.notExists(Paths.get(filePath))) {
            // done hakiba null pointer so use optional thing and stream empty by jflute (2017/10/05)
            return OptionalThing.empty();
        }
        try {
            DfDecoMapFile decoMapFile = new DfDecoMapFile();
            return OptionalThing.of(decoMapFile.readPickup(Files.newInputStream(Paths.get(filePath))));
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
    protected String buildDecommentPieceDirPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "piece");
    }

    protected String buildDecommentPiecePath(String clientProject, String fileName) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "piece", fileName);
    }

    private String buildDecommentPickupPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "pickup", PICKUP_FILE_NAME);
    }
}
