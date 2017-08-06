package org.dbflute.intro.app.logic.document.decomment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapFile;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.lastaflute.core.time.TimeManager;

/**
 * @author cabos
 */
public class DocumentDecommentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
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
            DfDecoMapFile dfDecoMapFile = new DfDecoMapFile();
            dfDecoMapFile.writeMap(outputStream, decoMapPiece.convertMap());
        } catch (IOException e) {
            // TODO cabos throw more detail exception (2017/07/29)
            throw new UncheckedIOException(e);
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
            // TODO cabos throw more detail exception (2017/07/29)
            throw new UncheckedIOException("fail to create piece map file " + pieceMapFile.getAbsolutePath(), e);
        }
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public String getAuthorFromGitSystem() {
        // get user name from git
        Runtime runtime = Runtime.getRuntime();
        Process p;
        try {
            p = runtime.exec("git config user.name");
        } catch (IOException e) {
            throw new UncheckedIOException("fail to execute git command", e);
        }

        // read user name
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("UTF-8")))) {
            return reader.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException("fail to read execute command result", e);
        }
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private String buildDecommentPiecePath(String clientProject, String fileName) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "piece", fileName);
    }

    private String buildDecommentPickupPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject, "schema", "decomment", "pickup");
    }
}
