package org.dbflute.intro.app.model.document.decomment;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.exception.DfPropFileWriteFailureException;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapPropertyPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
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
    // -----------------------------------------------------
    //                                                 Piece
    //                                                 -----
    //    [e.g. piece map, table decomment edited]
    //    map:{
    //        ; author = hakiba
    //        ; merged = false
    //        ; formatVersion = 1.0
    //        ; decoMap = map:{
    //            ; tableList = list:{
    //                ; map:{
    //                    ; tableName = MEMBER
    //                    ; propertyList = list:{
    //                        ; map:{
    //                            ; decomment = piece column decomment
    //                            ; databaseComment = sea mystic land oneman
    //                            ; commentVersion = 2 // incremented
    //                            ; authorList = list:{ cabos, hakiba, deco, jflute }
    //                            ; pieceDatetime = 2017-08-16T23:26:36.690
    //                        }
    //                    }
    //                    ; columnList = list:{}  // empty
    //                }
    //            }
    //        }
    //    }
    //
    //    [e.g. piece map, column decomment edited]
    //    map:{
    //        ; author = hakiba
    //        ; merged = false
    //        ; formatVersion = 1.0
    //        ; decoMap = map:{
    //            ; tableList = list:{
    //                ; map:{
    //                    ; tableName = MEMBER
    //                    ; propertyList = list:{}  // empty
    //                    ; columnList = list:{
    //                        ; map:{
    //                            ; columnName = MEMBER_NAME
    //                            ; propertyList = list:{
    //                                ; map:{
    //                                    ; decomment = sea mystic land oneman
    //                                    ; databaseComment = sea mystic
    //                                    ; commentVersion = 2 // incremented
    //                                    ; authorList = list:{ cabos, hakiba, deco, jflute }
    //                                    ; pieceDatetime = 2017-08-16T23:26:36.690
    //                                }
    //                            }
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //    }
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
        Boolean merged = Boolean.valueOf((String) map.get("merged"));
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Map<String, Object>>> decoMap = (Map<String, Map<String, Map<String, Object>>>) map.get("decoMap");
        DfDecoMapTablePart tablePart = decoMap.entrySet().stream().map(tableEntry -> {
            return DfDecoMapTablePart.createPieceTablePart(tableEntry);
        }).findFirst().orElseThrow(() -> createDecoMapTableElementNotFoundException(fileName, map));

        DfDecoMapPiece piece = new DfDecoMapPiece();
        piece.setFormatVersion(formatVersion);
        piece.setAuthor(author);
        piece.setMerged(merged);
        piece.setTableList(Collections.singletonList(tablePart));

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
    //    map:{
    //        ; formatVersion = 1.0
    //        ; author = jflute
    //        ; decommentDatetime = 2017-10-26T12:35:39.262
    //        ; merged = false
    //        ; decoMap = map:{
    //            ; tableList = list:{
    //                ; map:{
    //                    ; tableName = MEMBER
    //                    ; propertyList = list:{
    //                        ; map:{
    //                            ; decomment = first decomment
    //                            ; databaseComment = ...
    //                            ; commentVersion = ...
    //                            ; authorList = list:{ ... }
    //                        }
    //                        ; map:{   // propertyList size is more than 2 if decomment conflicts exists
    //                            ; decomment = second decomment
    //                            ; databaseComment = ...
    //                            ; commentVersion = ...
    //                            ; authorList = list:{ ... }
    //                        }
    //                    }
    //                    ; columnList = list:{
    //                        ; map:{
    //                            ; columnName = MEMBER_NAME
    //                            ; propertyList = list:{
    //                                ; map:{
    //                                    ; decomment = sea mystic land oneman
    //                                    ; databaseComment = sea mystic
    //                                    ; commentVersion = 2 // incremented
    //                                    ; authorList = list:{ cabos, hakiba, deco, jflute }
    //                                }
    //                            }
    //                        }
    //                        ; ... // more column maps (also conflict column)
    //                    }
    //                }
    //                ; map:{ // Of course, other table decomment info is exists that
    //                    ; tableName = MEMBER_LOGIN
    //                    ; ...
    //                }
    //            }
    //        }
    //    }
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
        Map<String, Map<String, List<Map<String, Object>>>> decoMap =
            (Map<String, Map<String, List<Map<String, Object>>>>) map.get("decoMap");
        List<DfDecoMapTablePart> tablePartList = decoMap.entrySet()
            .stream()
            .map(tableEntry -> DfDecoMapTablePart.createPickupTablePart(tableEntry))
            .collect(Collectors.toList());

        DfDecoMapPickup pickup = new DfDecoMapPickup();
        pickup.setFormatVersion(formatVersion);
        pickup.setTableList(tablePartList);
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
    public DfDecoMapPickup merge(OptionalThing<DfDecoMapPickup> pickupOpt, List<DfDecoMapPiece> pieces) {
        // Create all table part list
        final List<DfDecoMapTablePart> allTablePartListFiltered =
            generateLatestCommentVersionStream(pickupOpt, pieces).collect(Collectors.toList());

        // Extract all table name
        final Set<String> allTableNameSet =
            allTablePartListFiltered.stream().map(tablePart -> tablePart.getTableName()).collect(Collectors.toSet());
        // Extract all column name
        final Set<String> allColumnNameSet = allTablePartListFiltered.stream()
            .flatMap(tablePart -> tablePart.getColumnList().stream())
            .map(columnPart -> columnPart.getColumnName())
            .collect(Collectors.toSet());

        // Merge tables
        final List<DfDecoMapTablePart> mergedTableList = allTableNameSet.stream().map(tableName -> {
            // Merge columns
            final List<DfDecoMapColumnPart> mergedColumnPartList = allColumnNameSet.stream().map(columnName -> {
                // Merge propertyList of column (already filtering by latest comment version)
                final List<DfDecoMapPropertyPart> mergedProperties = allTablePartListFiltered.stream()
                    .filter(tablePart -> tableName.equals(tablePart.getTableName()))
                    .flatMap(tablePart -> tablePart.getColumnList().stream())
                    .filter(columnPart -> columnName.equals(columnPart.getColumnName()))
                    .flatMap(columnPart -> columnPart.getPropertyList().stream())
                    .collect(Collectors.toList());

                DfDecoMapColumnPart mergedColumn = new DfDecoMapColumnPart();
                mergedColumn.setColumnName(columnName);
                mergedColumn.setPropertyList(mergedProperties);
                return mergedColumn;
            }).filter(columnPart -> !columnPart.getPropertyList().isEmpty()).collect(Collectors.toList());

            DfDecoMapTablePart mergedTablePart = new DfDecoMapTablePart();
            mergedTablePart.setTableName(tableName);
            mergedTablePart.setColumnList(mergedColumnPartList);
            return mergedTablePart;
        }).filter(tablePart -> !tablePart.getColumnList().isEmpty()).collect(Collectors.toList());

        final String formatVersion = pickupOpt.map(pickup -> pickup.getFormatVersion()).orElse(null);
        DfDecoMapPickup mergedPickup = new DfDecoMapPickup();
        mergedPickup.setFormatVersion(formatVersion);
        mergedPickup.setTableList(mergedTableList);
        return mergedPickup;
    }

    private Stream<DfDecoMapTablePart> generateLatestCommentVersionStream(OptionalThing<DfDecoMapPickup> pickupOpt,
        List<DfDecoMapPiece> pieces) {
        final Map<String, Long> latestCommentVersionMap = generateAllColumnLatestCommentVersionMap(pickupOpt, pieces);

        // Pickup: Extract latest comment version column
        Stream<DfDecoMapTablePart> pickupStream =
            pickupOpt.map(pickup -> pickup.getTableList()).map(tableParts -> tableParts.stream()).orElse(Stream.empty()).map(tablePart -> {
                final List<DfDecoMapColumnPart> maxCommentVersionColumnPartList = tablePart.getColumnList()
                    .stream()
                    .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
                    .collect(Collectors.toList());
                tablePart.setColumnList(maxCommentVersionColumnPartList);
                return tablePart;
            });

        // filtering latest comment version column Function
        Function<DfDecoMapPiece, DfDecoMapPiece> filteringLatestCommentVersion = piece -> {
            piece.getTableList().forEach(tablePart -> {
                List<DfDecoMapColumnPart> columnList = tablePart.getColumnList()
                    .stream()
                    .filter(columnPart -> columnPart.getLatestCommentVersion() == latestCommentVersionMap.get(columnPart.getColumnName()))
                    .collect(Collectors.toList());
                tablePart.setColumnList(columnList);
            });
            return piece;
        };

        // filtering latest decomment datetime Function
        BinaryOperator<DfDecoMapPiece> filteringLatestDecommentDatetime = (v1, v2) -> {
            final Optional<LocalDateTime> optV1LocalDateTime = v1.getTableList()
                .stream()
                .flatMap(table -> table.getColumnList().stream().flatMap(column -> column.getPropertyList().stream()))
                .map(property -> property.getPieceDatetime())
                .findFirst();
            final Optional<LocalDateTime> optV2LocalDateTime = v2.getTableList()
                .stream()
                .flatMap(table -> table.getColumnList().stream().flatMap(column -> column.getPropertyList().stream()))
                .map(property -> property.getPieceDatetime())
                .findFirst();

            return optV1LocalDateTime.flatMap(v1LocalDateTime -> optV2LocalDateTime.map(v2LocalDateTime -> {
                return v1LocalDateTime.isAfter(v2LocalDateTime) ? v1 : v2;
            })).orElse(v1);
        };

        // mapping piece to table part Function
        Function<Stream<DfDecoMapPiece>, Stream<DfDecoMapTablePart>> pieceToTablePartWithFiltering = pieceStream -> pieceStream
            // filtering latest comment version column
            .map(filteringLatestCommentVersion)
            // filtering latest decomment datetime for each column by author
            .collect(Collectors.toMap(piece -> piece.getAuthor(), Function.identity(), filteringLatestDecommentDatetime))
            .entrySet()
            .stream()
            .map(pieceEntry -> pieceEntry.getValue())
            .flatMap(piece -> piece.getTableList().stream());

        //@formatter:off
        // extract map {key: table name value: {key: column name, value: piece}}
        Map<String, Map<String,List<DfDecoMapPiece>>> tableMap = pieces.stream()
            .collect(Collectors.groupingBy(
                piece -> piece.getTableList().get(0).getTableName(),
                Collectors.mapping(
                    piece -> piece,
                    Collectors.groupingBy(piece -> piece.getTableList().get(0).getColumnList().get(0).getColumnName())
                )
            )
        );

         // Piece: Extract with latest comment version and latest decomment datetime for each column by author
         Stream<DfDecoMapTablePart> pieceStream = tableMap.entrySet()
             .stream()
             .map(tableEntry -> tableEntry.getValue())
             .map(columnMap -> columnMap.entrySet())
             .flatMap(columnEntry -> columnEntry.stream())
             .map(columnEntry -> columnEntry.getValue())
             .map(columnList -> columnList.stream())
             .map(pieceToTablePartWithFiltering).flatMap(tablePartStream -> tablePartStream);
         //@formatter:on

        return Stream.concat(pickupStream, pieceStream);
    }

    private Map<String, Long> generateAllColumnLatestCommentVersionMap(OptionalThing<DfDecoMapPickup> pickupOpt,
        List<DfDecoMapPiece> pieces) {
        Stream<DfDecoMapTablePart> pickupStream =
            pickupOpt.map(pickup -> pickup.getTableList()).map(tableList -> tableList.stream()).orElse(Stream.empty());
        Stream<DfDecoMapTablePart> pieceStream = pieces.stream().flatMap(dfDecoMapPiece -> dfDecoMapPiece.getTableList().stream());
        return Stream.concat(pickupStream, pieceStream)
            .flatMap(tablePart -> tablePart.getColumnList().stream())
            .collect(Collectors.toMap(// Convert Map
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
