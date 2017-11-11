package org.dbflute.intro.app.model.document.decomment;

import static org.dbflute.intro.mylasta.appcls.AppCDef.PieceTargetType;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.exception.DfPropFileWriteFailureException;
import org.dbflute.helper.HandyDate;
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
    // TODO yuto write e.g. (2017/11/11)
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
        PieceTargetType targetType = PieceTargetType.of(map.get("targetType")).get();
        String decomment = (String) map.get("decomment");
        String databaseComment = (String) map.get("databaseComment");
        Long commentVersion = Long.valueOf("commentVersion");
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
        Set<String> pieceCodeSet = pickupOpt.map(this::extractAllPieceCode).orElse(Collections.emptySet());
        List<DfDecoMapPiece> filteredPieces = filterPieces(pieces, pieceCodeSet);
        DfDecoMapPickup pickUp = pickupOpt.orElse(new DfDecoMapPickup());

        mergeInternal(filteredPieces, pickUp);

        return pickUp;
    }

    private void mergeInternal(List<DfDecoMapPiece> filteredPieces, DfDecoMapPickup pickUp) {
        filteredPieces.forEach(piece -> {
            DfDecoMapPropertyPart property = mappingPieceToProperty(piece);

            if (piece.getTargetType() == PieceTargetType.Table) { // table decomment
                List<DfDecoMapTablePart> tableList = pickUp.getTableList();

                tableList.stream().filter(table -> table.getTableName().equals(piece.getTableName())).findFirst().map(table -> {
                    // exists other table decomment
                    table.getPropertyList().add(property);
                    return table;
                }).orElseGet(() -> {
                    // not exists other table decoment
                    DfDecoMapTablePart table = new DfDecoMapTablePart();
                    table.setTableName(piece.getTableName());
                    table.setColumnList(Collections.emptyList());
                    table.setPropertyList(Collections.singletonList(property));
                    return table;
                });

            } else if (piece.getTargetType() == PieceTargetType.Column) { // column decomment
                List<DfDecoMapTablePart> tableList = pickUp.getTableList();
                tableList.stream().filter(table -> table.getTableName().equals(piece.getTableName())).findFirst().map(table -> {
                    // exists table or column decoment, but we don't know that target decomment exists now...
                    table.getColumnList()
                        .stream()
                        .filter(column -> column.getColumnName().equals(piece.getColumnName()))
                        .findFirst()
                        .map(column -> {
                            // exists column comment
                            column.getPropertyList().add(property);
                            return column;
                        })
                        .orElseGet(() -> {
                            // not exists column comment
                            DfDecoMapColumnPart column = new DfDecoMapColumnPart();
                            column.setColumnName(piece.getColumnName());
                            column.setPropertyList(Collections.singletonList(property));
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
                    return table;
                });
            }
        });
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
        return pieces.stream().filter(piece -> pieceCodeSet.contains(piece.getPieceCode())).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
