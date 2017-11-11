package org.dbflute.infra.doc.decomment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.helper.HandyDate;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapPropertyPart;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapTablePart;
import org.dbflute.optional.OptionalThing;

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
    // done yuto write e.g. (2017/11/11)
    // map:{
    //     ; formatVersion = 1.0
    //     ; merged = false
    //     ; tableName = MEMBER
    //     ; columnName = null
    //     ; targetType = TABLE
    //     ; decomment = loginable user, my name is deco
    //     ; databaseComment = loginable user
    //     ; commentVersion = 0
    //     ; authorList = list:{ deco }
    //     ; pieceCode = AL3OR1P
    //     ; pieceDatetime = 2017-12-31T12:34:56.789
    //     ; pieceOwner = deco
    //     ; previousPieceList = list:{}
    // }
    // map:{
    //     ; formatVersion = 1.0
    //     ; merged = false
    //     ; tableName = MEMBER
    //     ; columnName = MEMBER_NAME
    //     ; targetType = COLUMN
    //     ; decomment = sea mystic land oneman
    //     ; databaseComment = sea mystic
    //     ; commentVersion = 1
    //     ; authorList = list:{ cabos ; hakiba ; deco ; jflute }
    //     ; pieceCode = HF7ELSE
    //     ; pieceDatetime = 2017-10-15T16:17:18.199
    //     ; pieceOwner = jflute
    //     ; previousPieceList = list:{ FE893L1 }
    // }
    public DfDecoMapPiece readPiece(InputStream ins) {
        final MapListFile mapListFile = createMapListFile();
        try {
            Map<String, Object> map = mapListFile.readMap(ins);
            return mappingToDecoMapPiece(map);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }

    // done hakiba cast check by hakiba (2017/07/29)
    @SuppressWarnings("unchecked")
    private DfDecoMapPiece mappingToDecoMapPiece(Map<String, Object> map) throws Exception {
        Boolean merged = Boolean.valueOf((String) map.get("merged"));
        String tableName = (String) map.get("tableName");
        String columnName = (String) map.get("columnName");
        DfDecoMapPieceTargetType targetType = DfDecoMapPieceTargetType.of(map.get("targetType")).get();
        String decomment = (String) map.get("decomment");
        String databaseComment = (String) map.get("databaseComment");
        Long commentVersion = Long.valueOf(map.get("commentVersion").toString());
        List<String> authorList = (List<String>) map.get("authorList");
        String pieceCode = (String) map.get("pieceCode");
        LocalDateTime pieceDatetime = new HandyDate((String) map.get("pieceDatetime")).getLocalDateTime();
        String pieceOwner = (String) map.get("pieceOwner");
        List<String> previousPieceList = (List<String>) map.get("previousPieceList");
        String formatVersion = (String) map.get("formatVersion");

        DfDecoMapPiece piece = new DfDecoMapPiece();
        piece.setMerged(merged);
        piece.setTableName(tableName);
        piece.setColumnName(columnName);
        piece.setTargetType(targetType);
        piece.setDecomment(decomment);
        piece.setDatabaseComment(databaseComment);
        piece.setCommentVersion(commentVersion);
        piece.setAuthorList(authorList);
        piece.setPieceCode(pieceCode);
        piece.setPieceDatetime(pieceDatetime);
        piece.setPieceOwner(pieceOwner);
        piece.setPreviousPieceList(previousPieceList);
        piece.setFormatVersion(formatVersion);
        return piece;
    }

    // -----------------------------------------------------
    //                                                Pickup
    //                                                ------
    // map:{
    //     ; formatVersion = 1.0
    //     ; pickupDatetime = 2017-11-09T09:09:09.009
    //     ; decoMap = map:{
    //         ; tableList = list:{
    //             ; map:{
    //                 ; tableName = MEMBER
    //                 ; propertyList = list:{
    //                     ; map:{
    //                         ; decomment = first decomment
    //                         ; databaseComment = ...
    //                         ; commentVersion = ...
    //                         ; authorList = list:{ deco }
    //                         ; pieceCode = DECO0000
    //                         ; pieceDatetime = 2017-11-05T00:38:13.645
    //                         ; pieceOwner = cabos
    //                         ; previousPieceList = list:{}
    //                     }
    //                     ; map:{ // propertyList size is more than 2 if decomment conflicts exists
    //                         ; ...
    //                     }
    //                 }
    //                 ; columnList = list:{
    //                     ; map:{
    //                         ; columnName = MEMBER_NAME
    //                         ; propertyList = list:{
    //                             ; map:{
    //                                 ; decomment = sea mystic land oneman
    //                                 ; databaseComment = sea mystic
    //                                 ; commentVersion = 1
    //                                 ; authorList = list:{ cabos, hakiba, deco, jflute }
    //                                 ; pieceCode = HAKIBA00
    //                                 ; pieceDatetime = 2017-11-05T00:38:13.645
    //                                 ; pieceOwner = cabos
    //                                 ; previousPieceList = list:{ JFLUTE00, CABOS000 }
    //                             }
    //                         }
    //                     }
    //                     ; ... // more other columns
    //                 }
    //             }
    //             ; map:{ // Of course, other table decomment info is exists that
    //                 ; tableName = MEMBER_LOGIN
    //                 ; ...
    //             }
    //         }
    //     }
    // }
    // done hakiba sub tag comment by jflute (2017/08/17)
    public DfDecoMapPickup readPickup(InputStream ins) {
        MapListFile mapListFile = createMapListFile();
        try {
            Map<String, Object> map = mapListFile.readMap(ins);
            return mappingToDecoMapPickup(map);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }

    private DfDecoMapPickup mappingToDecoMapPickup(Map<String, Object> map) {
        String formatVersion = (String) map.get("formatVersion");
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> decoMap = (Map<String, List<Map<String, Object>>>) map.get("decoMap");
        List<DfDecoMapTablePart> tableList = decoMap.get("tableList").stream().map(tablePartMap -> {
            return DfDecoMapTablePart.createTablePart(tablePartMap);
        }).collect(Collectors.toList());
        DfDecoMapPickup pickup = new DfDecoMapPickup();
        pickup.setFormatVersion(formatVersion);
        pickup.setTableList(tableList);
        return pickup;
    }

    // -----------------------------------------------------
    //                                          Assist Logic
    //                                          ------------
    protected void throwDecoMapReadFailureException(InputStream ins, Exception cause) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to read the deco-map file.");
        br.addItem("InputStream");
        br.addElement(ins);
        final String msg = br.buildExceptionMessage();
        throw new DfDecoMapFileReadFailureException(msg, cause);
    }

    public static class DfDecoMapFileReadFailureException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DfDecoMapFileReadFailureException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    // ===================================================================================
    //                                                                               Write
    //                                                                               =====
    public void writePiece(String pieceMapPath, DfDecoMapPiece decoMapPiece) throws FileNotFoundException, IOException {
        File pieceMapFile = new File(pieceMapPath);
        if (pieceMapFile.exists()) { // no way, but just in case
            pieceMapFile.delete(); // simply delete old file
        }
        createPieceMapFile(pieceMapFile);
        try (OutputStream ous = new FileOutputStream(pieceMapFile)) {
            writeMap(ous, decoMapPiece.convertToMap());
        }
    }

    protected void createPieceMapFile(File pieceMapFile) throws IOException {
        Files.createDirectories(Paths.get(pieceMapFile.getParentFile().getAbsolutePath()));
        Files.createFile(Paths.get(pieceMapFile.getAbsolutePath()));
    }

    // done (by jflute) hakiba be more rich method, e.g. saveDecommentPieceMap()'s logic by jflute (2017/09/21)
    protected void writeMap(OutputStream ous, Map<String, Object> decoMap) {
        final MapListFile mapListFile = createMapListFile();
        try {
            mapListFile.writeMap(ous, decoMap);
        } catch (Exception e) {
            throwDecoMapWriteFailureException(ous, decoMap, e);
        }
    }

    protected void throwDecoMapWriteFailureException(OutputStream ous, Map<String, Object> decoMap, Exception cause) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to write the deco-map file.");
        br.addItem("OutputStream");
        br.addElement(ous);
        br.addItem("Written decoMap");
        br.addElement(decoMap);
        final String msg = br.buildExceptionMessage();
        throw new DfDecoMapFileWriteFailureException(msg, cause);
    }

    public static class DfDecoMapFileWriteFailureException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public DfDecoMapFileWriteFailureException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    // ===================================================================================
    //                                                                               Merge
    //                                                                               =====
    // done (by cabos) hakiba write unit test by jflute (2017/09/21)
    public DfDecoMapPickup merge(OptionalThing<DfDecoMapPickup> pickupOpt, List<DfDecoMapPiece> pieces) {
        Set<String> pieceCodeSet = pickupOpt.map(this::extractAllPieceCode).orElse(Collections.emptySet());
        List<DfDecoMapPiece> filteredPieces = filterPieces(pieces, pieceCodeSet);
        DfDecoMapPickup pickUp = pickupOpt.orElse(new DfDecoMapPickup());
        mergeInternal(filteredPieces, pickUp);
        return pickUp;
    }

    protected void mergeInternal(List<DfDecoMapPiece> filteredPieces, DfDecoMapPickup pickUp) {
        filteredPieces.forEach(piece -> {
            DfDecoMapPropertyPart property = mappingPieceToProperty(piece);

            if (piece.getTargetType() == DfDecoMapPieceTargetType.Table) { // table decomment
                List<DfDecoMapTablePart> tableList = pickUp.getTableList();

                tableList.stream().filter(table -> table.getTableName().equals(piece.getTableName())).findFirst().map(table -> {
                    // exists other table decomment
                    addTableProperty(property, table);
                    return table;
                }).orElseGet(() -> {
                    // not exists other table decoment
                    DfDecoMapTablePart table = new DfDecoMapTablePart();
                    table.setTableName(piece.getTableName());
                    table.setColumnList(Collections.emptyList());
                    table.setPropertyList(Collections.singletonList(property));
                    addTable(table, pickUp);
                    return table;
                });

            } else if (piece.getTargetType() == DfDecoMapPieceTargetType.Column) { // column decomment
                List<DfDecoMapTablePart> tableList = pickUp.getTableList();
                tableList.stream().filter(table -> table.getTableName().equals(piece.getTableName())).findFirst().map(table -> {
                    // exists table or column decoment, but we don't know that target decomment exists now...
                    table.getColumnList()
                        .stream()
                        .filter(column -> column.getColumnName().equals(piece.getColumnName()))
                        .findFirst()
                        .map(column -> {
                            // exists column comment
                            addColumnProperty(property, column);
                            return column;
                        })
                        .orElseGet(() -> {
                            // not exists column comment
                            DfDecoMapColumnPart column = new DfDecoMapColumnPart();
                            column.setColumnName(piece.getColumnName());
                            column.setPropertyList(Collections.singletonList(property));
                            addColumn(column, table);
                            return column;
                        });
                    return table;
                }).orElseGet(() -> {
                    // not exists table and column decoment
                    DfDecoMapColumnPart column = new DfDecoMapColumnPart();
                    column.setColumnName(piece.getColumnName());
                    column.setPropertyList(Collections.singletonList(property));

                    DfDecoMapTablePart table = new DfDecoMapTablePart();
                    table.setTableName(piece.getTableName());
                    table.setColumnList(Collections.singletonList(column));
                    table.setPropertyList(Collections.emptyList());
                    addTable(table, pickUp);
                    return table;
                });
            }
        });
    }

    private void addTableProperty(DfDecoMapPropertyPart property, DfDecoMapTablePart table) {
        ArrayList<DfDecoMapPropertyPart> propertyPartArrayList = new ArrayList<>(table.getPropertyList());
        propertyPartArrayList.add(property);
        table.setPropertyList(propertyPartArrayList);
    }

    private void addColumnProperty(DfDecoMapPropertyPart property, DfDecoMapColumnPart column) {
        ArrayList<DfDecoMapPropertyPart> propertyPartArrayList = new ArrayList<>(column.getPropertyList());
        propertyPartArrayList.add(property);
        column.setPropertyList(propertyPartArrayList);
    }

    private void addColumn(DfDecoMapColumnPart column, DfDecoMapTablePart table) {
        ArrayList<DfDecoMapColumnPart> columnPartArrayList = new ArrayList<>(table.getColumnList());
        columnPartArrayList.add(column);
        table.setColumnList(columnPartArrayList);
    }

    private void addTable(DfDecoMapTablePart table, DfDecoMapPickup pickUp) {
        ArrayList<DfDecoMapTablePart> tableArrayList = new ArrayList<>(pickUp.getTableList());
        tableArrayList.add(table);
        pickUp.setTableList(tableArrayList);
    }

    private DfDecoMapPropertyPart mappingPieceToProperty(DfDecoMapPiece piece) {
        DfDecoMapPropertyPart property = new DfDecoMapPropertyPart();
        property.setDecomment(piece.getDecomment());
        property.setDatabaseComment(piece.getDatabaseComment());
        property.setCommentVersion(piece.getCommentVersion());
        property.setAuthorList(piece.getAuthorList());
        property.setPieceCode(piece.getPieceCode());
        property.setPieceDatetime(piece.getPieceDatetime());
        property.setPieceOwner(piece.getPieceOwner());
        property.setPreviousPieceList(piece.getPreviousPieceList());
        return property;
    }

    private Set<String> extractAllPieceCode(DfDecoMapPickup pickup) {
        return pickup.getTableList().stream().flatMap(table -> {
            Stream<String> previousTablePieceStream =
                table.getPropertyList().stream().flatMap(property -> property.getPreviousPieceList().stream());
            Stream<String> previousColumnPieceStream = table.getColumnList()
                .stream()
                .flatMap(column -> column.getPropertyList().stream())
                .flatMap(property -> property.getPreviousPieceList().stream());
            Stream<String> tablePieceStream = table.getPropertyList().stream().map(property -> property.getPieceCode());
            Stream<String> columnPieceStream = table.getColumnList()
                .stream()
                .flatMap(column -> column.getPropertyList().stream())
                .map(property -> property.getPieceCode());
            return Stream.concat(Stream.concat(Stream.concat(previousTablePieceStream, previousColumnPieceStream), tablePieceStream),
                columnPieceStream);
        }).collect(Collectors.toSet());
    }

    private List<DfDecoMapPiece> filterPieces(List<DfDecoMapPiece> pieces, Set<String> pieceCodeSet) {
        Set<String> previousAllPieceSet =
            pieces.stream().flatMap(piece -> piece.getPreviousPieceList().stream()).collect(Collectors.toSet());
        return pieces.stream()
            .filter(piece -> !previousAllPieceSet.contains(piece.getPieceCode()) && !pieceCodeSet.contains(piece.getPieceCode()))
            .collect(Collectors.toList());
    }

    // hakiba's memorable code by jflute (2017/11/11)
    //public DfDecoMapPickup merge(OptionalThing<DfDecoMapPickup> pickupOpt, List<DfDecoMapPiece> pieces) {
    //    // Create all table part list
    //    final List<DfDecoMapTablePart> allTablePartListFiltered =
    //        generateLatestCommentVersionStream(pickupOpt, pieces).collect(Collectors.toList());
    //
    //    // Extract all table name
    //    final Set<String> allTableNameSet =
    //        allTablePartListFiltered.stream().map(tablePart -> tablePart.getTableName()).collect(Collectors.toSet());
    //    // Extract all column name
    //    final Set<String> allColumnNameSet = allTablePartListFiltered.stream()
    //        .flatMap(tablePart -> tablePart.getColumnList().stream())
    //        .map(columnPart -> columnPart.getColumnName())
    //        .collect(Collectors.toSet());
    //
    //    // Merge tables
    //    final List<DfDecoMapTablePart> mergedTableList = allTableNameSet.stream().map(tableName -> {
    //        // Merge columns
    //        final List<DfDecoMapColumnPart> mergedColumnPartList = allColumnNameSet.stream().map(columnName -> {
    //            // Merge propertyList of column (already filtering by latest comment version)
    //            final List<DfDecoMapPropertyPart> mergedProperties = allTablePartListFiltered.stream()
    //                .filter(tablePart -> tableName.equals(tablePart.getTableName()))
    //                .flatMap(tablePart -> tablePart.getColumnList().stream())
    //                .filter(columnPart -> columnName.equals(columnPart.getColumnName()))
    //                .flatMap(columnPart -> columnPart.getPropertyList().stream())
    //                .collect(Collectors.toList());
    //
    //            DfDecoMapColumnPart mergedColumn = new DfDecoMapColumnPart();
    //            mergedColumn.setColumnName(columnName);
    //            mergedColumn.setPropertyList(mergedProperties);
    //            return mergedColumn;
    //        }).filter(columnPart -> !columnPart.getPropertyList().isEmpty()).collect(Collectors.toList());
    //
    //        DfDecoMapTablePart mergedTablePart = new DfDecoMapTablePart();
    //        mergedTablePart.setTableName(tableName);
    //        mergedTablePart.setColumnList(mergedColumnPartList);
    //        return mergedTablePart;
    //    }).filter(tablePart -> !tablePart.getColumnList().isEmpty()).collect(Collectors.toList());
    //
    //    final String formatVersion = pickupOpt.map(pickup -> pickup.getFormatVersion()).orElse(null);
    //    DfDecoMapPickup mergedPickup = new DfDecoMapPickup();
    //    mergedPickup.setFormatVersion(formatVersion);
    //    mergedPickup.setTableList(mergedTableList);
    //    return mergedPickup;
    //}
    //
    //private Stream<DfDecoMapTablePart> generateLatestCommentVersionStream(OptionalThing<DfDecoMapPickup> pickupOpt,
    //    List<DfDecoMapPiece> pieces) {
    //    final Map<String, Long> latestCommentVersionMap = generateAllColumnLatestCommentVersionMap(pickupOpt, pieces);
    //
    //    // Pickup: Extract latest comment version column
    //    Stream<DfDecoMapTablePart> pickupStream =
    //        pickupOpt.map(pickup -> pickup.getTableList()).map(tableParts -> tableParts.stream()).orElse(Stream.empty()).map(tablePart -> {
    //            final List<DfDecoMapColumnPart> maxCommentVersionColumnPartList = tablePart.getColumnList()
    //                .stream()
    //                .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
    //                .collect(Collectors.toList());
    //            tablePart.setColumnList(maxCommentVersionColumnPartList);
    //            return tablePart;
    //        });
    //
    //    // filtering latest comment version column Function
    //    Function<DfDecoMapPiece, DfDecoMapPiece> filteringLatestCommentVersion = piece -> {
    //        piece.getTableList().forEach(tablePart -> {
    //            List<DfDecoMapColumnPart> columnList = tablePart.getColumnList()
    //                .stream()
    //                .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
    //                .collect(Collectors.toList());
    //            tablePart.setColumnList(columnList);
    //        });
    //        return piece;
    //    };
    //
    //    // filtering latest decomment datetime Function
    //    BinaryOperator<DfDecoMapPiece> filteringLatestDecommentDatetime = (v1, v2) -> {
    //        final Optional<LocalDateTime> optV1LocalDateTime = v1.getTableList()
    //            .stream()
    //            .flatMap(table -> table.getColumnList().stream())
    //            .flatMap(column -> column.getPropertyList().stream())
    //            .map(property -> property.getPieceDatetime())
    //            .filter(time -> time != null)  // none cabos why does here need null check?
    //            .findFirst();
    //        final Optional<LocalDateTime> optV2LocalDateTime = v2.getTableList()
    //            .stream()
    //            .flatMap(table -> table.getColumnList().stream())
    //            .flatMap(column -> column.getPropertyList().stream())
    //            .map(property -> property.getPieceDatetime())
    //            .filter(time -> time != null)
    //            .findFirst();
    //
    //        return optV1LocalDateTime.flatMap(v1LocalDateTime -> optV2LocalDateTime.map(v2LocalDateTime -> {
    //            return v1LocalDateTime.isAfter(v2LocalDateTime) ? v1 : v2;
    //        })).orElse(v1);
    //    };
    //
    //    // mapping piece to table part Function
    //    Function<Stream<DfDecoMapPiece>, Stream<DfDecoMapTablePart>> pieceToTablePartWithFiltering = pieceStream -> pieceStream
    //        // filtering latest comment version column
    //        .map(filteringLatestCommentVersion)
    //        // filtering latest decomment datetime for each column by author
    //        .collect(Collectors.toMap(piece -> piece.getAuthor(), Function.identity(), filteringLatestDecommentDatetime))
    //        .entrySet()
    //        .stream()
    //        .map(pieceEntry -> pieceEntry.getValue())
    //        .flatMap(piece -> piece.getTableList().stream());
    //
    //    //@formatter:off
    //    // extract map {key: table name value: {key: column name, value: piece}}
    //    Map<String, Map<String,List<DfDecoMapPiece>>> tableMap = pieces.stream()
    //        .collect(Collectors.groupingBy(
    //            piece -> piece.getTableList().get(0).getTableName(),
    //            Collectors.mapping(
    //                piece -> piece,
    //                Collectors.groupingBy(piece -> piece.getTableList().get(0).getColumnList().get(0).getColumnName())
    //            )
    //        )
    //    );
    //
    //     // Piece: Extract with latest comment version and latest decomment datetime for each column by author
    //     Stream<DfDecoMapTablePart> pieceStream = tableMap.entrySet()
    //         .stream()
    //         .map(tableEntry -> tableEntry.getValue())
    //         .map(columnMap -> columnMap.entrySet())
    //         .flatMap(columnEntry -> columnEntry.stream())
    //         .map(columnEntry -> columnEntry.getValue())
    //         .map(columnList -> columnList.stream())
    //         .map(pieceToTablePartWithFiltering).flatMap(tablePartStream -> tablePartStream);
    //     //@formatter:on
    //
    //    return Stream.concat(pickupStream, pieceStream);
    //}
    //
    //private Map<String, Long> generateAllColumnLatestCommentVersionMap(OptionalThing<DfDecoMapPickup> pickupOpt,
    //    List<DfDecoMapPiece> pieces) {
    //    Stream<DfDecoMapTablePart> pickupStream =
    //        pickupOpt.map(pickup -> pickup.getTableList()).map(tableList -> tableList.stream()).orElse(Stream.empty());
    //    Stream<DfDecoMapTablePart> pieceStream = pieces.stream().flatMap(dfDecoMapPiece -> dfDecoMapPiece.getTableList().stream());
    //    return Stream.concat(pickupStream, pieceStream)
    //        .flatMap(tablePart -> tablePart.getColumnList().stream())
    //        .collect(Collectors.toMap(// Convert Map
    //            column -> column.getColumnName(), // key: column name
    //            column -> column.getLatestCommentVersion(), // value: max comment version
    //            (v1, v2) -> Math.max(v1, v2)));
    //}

    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
