package org.dbflute.intro.app.model.document.decomment;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.exception.DfPropFileWriteFailureException;
import org.dbflute.helper.HandyDate;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

// done cabos DfDecoMapFile by jflute (2017/07/27)

/**
 * @author cabos
 * @author hakiba
 * @author jflute
 */
public class DfDecoMapFile {

    // ===================================================================================
    //                                                                               Read
    //                                                                              ======
    /* e.g. piece map
    map:{
        ; decoMap = map:{
            ; TABLE_NAME = map:{
                ; COLUMN_NAME = map:{
                    ; databaseComment = rime
                    ; previousWholeComment = lemon
                    ; commentVersion = 2
                    ; authorList = list:{
                        ; cabos
                        ; sudachi
                    }
                    ; decomment = hakiba 2 2017-08-16T23:26:36.690
                }
            }
        }
        ; decommentDatetime = 2017-08-16T23:26:36.690
        ; author = hakiba
        ; merged = false
        ; formatVersion = 1.0
    }
    */
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
        DfDecoMapTablePart tablePart = decoMap.entrySet().stream().map(tableEntry -> {
            return DfDecoMapTablePart.createPieceTablePart(tableEntry);
        }).findFirst().orElseThrow(() -> createDecoMapTableElementNotFoundException(fileName, map));

        DfDecoMapPiece piece = new DfDecoMapPiece();
        piece.setFileName(fileName);
        piece.setFormatVersion(formatVersion);
        piece.setAuthor(author);
        piece.setDecommentDatetime(decommentDatetime);
        piece.setMerged(merged);
        piece.setDecoMap(tablePart);

        return piece;
    }

    protected RuntimeException createDecoMapTableElementNotFoundException(String fileName, Map<String, Object> pieceMap) {
        ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Not found the decoMap's table element.");
        br.addItem("File Name");
        br.addElement(fileName);
        br.addItem("Piece Map");
        br.addElement(pieceMap);
        String msg = br.buildExceptionMessage();
        return new DecoMapTableElementNotFoundException(msg);
    }

    public static class DecoMapTableElementNotFoundException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DecoMapTableElementNotFoundException(String msg) {
            super(msg);
        }
    }

    // -----------------------------------------------------
    //                                                Pickup
    //                                                ------
    /* e.g. pickup map
    map:{
        ; formatVersion = 1.0
        ; decoMap = map:{
            ; MEMBER = map:{
                ; MEMBER_NAME = list:{
                    ; map:{
                        ; decomment = piari
                        ; databaseComment = sea
                        ; previousWholeComment = seasea
                        ; commentVersion = 1
                        ; authorList = list:{ jflute ; cabos }
                    }
                    ; map:{
                        ; decomment = bonvo
                        ; databaseComment = sea
                        ; previousWholeComment = seasea
                        ; commentVersion = 1
                        ; authorList = list:{ jflute ; cabos }
                    }
                }
            }
        }
    }
     */
    // done hakiba sub tag comment by jflute (2017/08/17)
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
        List<DfDecoMapTablePart> tablePartList =
                decoMap.entrySet().stream().map(tableEntry -> DfDecoMapTablePart.createPickupTablePart(tableEntry)).collect(
                        Collectors.toList());

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
    // TODO hakiba be more rich method, e.g. saveDecommentPieceMap()'s logic by jflute (2017/09/21)
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
        // done cabos use WriteFailure by jflute (2017/08/10)
        throw new DfPropFileWriteFailureException(msg, e);
    }

    // ===================================================================================
    //                                                                               Merge
    //                                                                               =====
    // TODO hakiba write unit test by jflute (2017/09/21)
    public DfDecoMapPickup merge(DfDecoMapPickup pickup, List<DfDecoMapPiece> pieces) {
        // Create all table part list
        final List<DfDecoMapTablePart> allTablePartList = generateLatestCommentVersionStream(pickup, pieces).collect(Collectors.toList());

        // Extract all table name
        final Set<String> allTableNameSet =
                allTablePartList.stream().map(tablePart -> tablePart.getTableName()).collect(Collectors.toSet());
        // Extract all column name
        final Set<String> allColumnNameSet = allTablePartList.stream()
                .flatMap(tablePart -> tablePart.getColumns().stream())
                .map(columnPart -> columnPart.getColumnName())
                .collect(Collectors.toSet());

        // Merge tables
        final List<DfDecoMapTablePart> mergedDecoMap = allTableNameSet.stream().map(tableName -> {
            // Merge columns
            final List<DfDecoMapColumnPart> mergedColumnPartList = allColumnNameSet.stream().map(columnName -> {
                // Merge properties of column with filtering max comment version
                final List<DfDecoMapColumnPart.ColumnPropertyPart> mergedProperties = allTablePartList.stream()
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

    private Stream<DfDecoMapTablePart> generateLatestCommentVersionStream(DfDecoMapPickup pickup, List<DfDecoMapPiece> pieces) {
        final Map<String, Long> latestCommentVersionMap = generateAllColumnLatestCommentVersionMap(pickup, pieces);

        // Pickup: Extract latest comment version column
        Stream<DfDecoMapTablePart> pickupStream = pickup.getDecoMap().stream().map(tablePart -> {
            final List<DfDecoMapColumnPart> maxCommentVersionColumnPartList = tablePart.getColumns()
                    .stream()
                    .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
                    .collect(Collectors.toList());
            tablePart.setColumns(maxCommentVersionColumnPartList);
            return tablePart;
        });

        // Piece: Extract latest comment version column
        BinaryOperator<DfDecoMapPiece> filteringLatestDecommentDatetime = (v1, v2) -> {
            final LocalDateTime v1LocalDateTime = v1.getDecommentDatetime();
            final LocalDateTime v2LocalDateTime = v2.getDecommentDatetime();
            return v1LocalDateTime.isAfter(v2LocalDateTime) ? v1 : v2;
        };
        Stream<DfDecoMapTablePart> piecesStream = pieces.stream().map(piece -> {
            final List<DfDecoMapColumnPart> maxCommentVersionColumnPartList = piece.getDecoMap()
                    .getColumns()
                    .stream()
                    .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
                    .collect(Collectors.toList());
            piece.getDecoMap().setColumns(maxCommentVersionColumnPartList);
            return piece;
        })
                .collect(Collectors.toMap(piece -> piece.getAuthor(), Function.identity(), filteringLatestDecommentDatetime))
                .entrySet()
                .stream()
                .map(stringDfDecoMapPieceEntry -> stringDfDecoMapPieceEntry.getValue())
                .map(dfDecoMapPiece -> dfDecoMapPiece.getDecoMap());

        return Stream.concat(pickupStream, piecesStream);
    }

    private Map<String, Long> generateAllColumnLatestCommentVersionMap(DfDecoMapPickup pickup, List<DfDecoMapPiece> pieces) {
        Stream<DfDecoMapTablePart> pickupStream = pickup.getDecoMap().stream();
        Stream<DfDecoMapTablePart> pieceStream = pieces.stream().map(dfDecoMapPiece -> dfDecoMapPiece.getDecoMap());
        return Stream.concat(pickupStream, pieceStream).flatMap(tablePart -> tablePart.getColumns().stream()).collect(Collectors.toMap(// Convert Map
                column -> column.getColumnName(), // key: column name
                column -> column.getLatestCommentVersion(), // value: max comment version
                (v1, v2) -> Math.max(v1, v2)));
    }

    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
