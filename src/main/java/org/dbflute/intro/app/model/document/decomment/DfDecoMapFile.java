package org.dbflute.intro.app.model.document.decomment;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Map;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.helper.HandyDate;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

// TODO done cabos DfDecoMapFile by jflute (2017/07/27)

/**
 * @author cabos
 * @author hakiba
 */
public class DfDecoMapFile {

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====
    public DfDecoMapPiece readPiece(String fileName, InputStream ins) {
        final MapListFile mapListFile = createMapListFile();
        try {
            Map<String, Object> mapList = mapListFile.readMap(ins);
            return createDecoMapPiece(fileName, mapList);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }

    // done hakiba cast check by hakiba (2017/07/29)
    private DfDecoMapPiece createDecoMapPiece(String fileName, Map<String, Object> mapList) throws Exception {
        String formatVersion = (String) mapList.get("formatVersion");
        String author = (String) mapList.get("author");
        LocalDateTime decommentDatetime = new HandyDate((String) mapList.get("decommentDatetime")).getLocalDateTime();
        Boolean merged = Boolean.valueOf((String) mapList.get("merged"));
        @SuppressWarnings("unchecked")
        DfDecoMapTablePart decoMapTablePart = ((Map<String, Object>) mapList.get("decoMap")).entrySet()
            .stream()
            .map(entry -> DfDecoMapTablePart.createPieceTablePart(entry))
            .findFirst()
            .orElseThrow(() -> {
                // TODO hakiba handle exception by hakiba (2017/07/29)
                return new IllegalStateException();
            });

        DfDecoMapPiece piece = new DfDecoMapPiece();
        piece.setFileName(fileName);
        piece.setFormatVersion(formatVersion);
        piece.setAuthor(author);
        piece.setDecommentDatetime(decommentDatetime);
        piece.setMerged(merged);
        piece.setDecoMap(decoMapTablePart);

        return piece;
    }

    protected void throwDecoMapReadFailureException(InputStream ins, Exception e) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to read the deco-map file.");
        br.addItem("Advice");
        br.addElement("Make sure the map-string is correct in the file.");
        br.addElement("For example, the number of start and end braces are the same.");
        br.addItem("Decomment Map");
        br.addElement(ins);
        final String msg = br.buildExceptionMessage();
        throw new DfPropFileReadFailureException(msg, e);
    }

    // ===================================================================================
    //                                                                               Write
    //                                                                               =====
    public void writeMap(OutputStream ous, Map<String, Object> map) {
        final MapListFile mapListFile = createMapListFile();
        try {
            mapListFile.writeMap(ous, map);
        } catch (Exception e) {
            throwDecoMapWriteFailureException(ous, e);
        }
    }

    protected void throwDecoMapWriteFailureException(OutputStream ous, Exception e) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to write the deco-map file.");
        br.addItem("Decomment Map");
        br.addElement(ous);
        final String msg = br.buildExceptionMessage();
        // TODO cabos use WriteFailure by jflute (2017/08/10)
        throw new DfPropFileReadFailureException(msg, e);
    }

    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
