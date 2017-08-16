package org.dbflute.intro.app.model.document.decomment;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.helper.HandyDate;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
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
            Map<String, Object> map = mapListFile.readMap(ins);
            return mappingToDecoMapPiece(fileName, map);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }

    // done hakiba cast check by hakiba (2017/07/29)
    private DfDecoMapPiece mappingToDecoMapPiece(String fileName, Map<String, Object> map) throws Exception {
        String formatVersion = (String) map.get("formatVersion");
        String author = (String) map.get("author");
        LocalDateTime decommentDatetime = new HandyDate((String) map.get("decommentDatetime")).getLocalDateTime();
        Boolean merged = Boolean.valueOf((String) map.get("merged"));
        @SuppressWarnings("unchecked")
        Map<String, Object> decoMap = (Map<String, Object>) map.get("decoMap");
        DfDecoMapTablePart tablePart = decoMap.entrySet()
            .stream()
            .map(tableEntry -> DfDecoMapTablePart.createPieceTablePart(tableEntry))
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
        piece.setDecoMap(tablePart);

        return piece;
    }

    public DfDecoMapPickup readPickup(String fileName, InputStream ins) {
        MapListFile mapListFile = createMapListFile();
        try {
            Map<String, Object> map = mapListFile.readMap(ins);
            return mappingToDecoMapPickup(fileName, map);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }
    private DfDecoMapPickup mappingToDecoMapPickup(String fileName, Map<String, Object> map) {
        String formatVersion = (String) map.get("formatVersion");
        @SuppressWarnings("unchecked")
        Map<String, Object> decoMap = (Map<String, Object>) map.get("decoMap");
        List<DfDecoMapTablePart> tablePartList = decoMap.entrySet()
            .stream()
            .map(tableEntry -> DfDecoMapTablePart.createPickupTablePart(tableEntry))
            .collect(Collectors.toList());

        DfDecoMapPickup pickup = new DfDecoMapPickup();
        pickup.setFileName(fileName);
        pickup.setFormatVersion(formatVersion);
        pickup.setDecoMap(tablePartList);
        return pickup;
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
    //                                                                               Merge
    //                                                                               =====
    public DfDecoMapPickup merge(DfDecoMapPickup pickup, List<DfDecoMapPiece> pieces) {
        final List<DfDecoMapTablePart> allTablePartList =
            Stream.concat(pickup.getDecoMap().stream(), pieces.stream().map(dfDecoMapPiece -> dfDecoMapPiece.getDecoMap()))
                .collect(Collectors.toList());
        final Set<String> allTableNameSet =
            allTablePartList.stream().map(tablePart -> tablePart.getTableName()).collect(Collectors.toSet());
        final Set<String> allColumnNameSet = allTablePartList.stream()
            .flatMap(tablePart -> tablePart.getColumns().stream())
            .map(columnPart -> columnPart.getColumnName())
            .collect(Collectors.toSet());

        final List<DfDecoMapTablePart> mergedDecoMap = allTableNameSet.stream().map(tableName -> {
            List<DfDecoMapColumnPart> mergedColumnPartList = allColumnNameSet.stream().map(columnName -> {
                List<DfDecoMapColumnPart.ColumnProperty> mergedProperties = allTablePartList.stream()
                    .filter(tablePart -> tableName.equals(tablePart.getTableName()))
                    .flatMap(tablePart -> tablePart.getColumns().stream())
                    .filter(columnPart -> columnName.equals(columnPart.getColumnName()))
                    .flatMap(columnPart -> columnPart.getProperties().stream())
                    .collect(Collectors.toList());

                DfDecoMapColumnPart mergedColumn = new DfDecoMapColumnPart();
                mergedColumn.setColumnName(columnName);
                mergedColumn.setProperties(mergedProperties);
                return mergedColumn;
            }).filter(columnPart -> !columnPart.getProperties().isEmpty()).collect(Collectors.toList());

            DfDecoMapTablePart mergedTablePart = new DfDecoMapTablePart();
            mergedTablePart.setTableName(tableName);
            mergedTablePart.setColumns(mergedColumnPartList);
            return mergedTablePart;
        }).filter(tablePart -> !tablePart.getColumns().isEmpty()).collect(Collectors.toList());

        DfDecoMapPickup mergedPickup = new DfDecoMapPickup();
        mergedPickup.setFileName(pickup.getFileName());
        mergedPickup.setFormatVersion(pickup.getFormatVersion());
        mergedPickup.setDecoMap(mergedDecoMap);
        return mergedPickup;
    }
    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
