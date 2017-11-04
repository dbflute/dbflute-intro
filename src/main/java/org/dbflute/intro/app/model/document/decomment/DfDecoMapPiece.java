package org.dbflute.intro.app.model.document.decomment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.helper.mapstring.MapListString;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapPiece {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String DECO_MAP_KEY = "tableList";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String author;
    protected boolean merged;
    protected String formatVersion;
    protected Map<String, List<DfDecoMapTablePart>> decoMap;

    // done cabos move to before accessor by jflute (2017/08/10)
    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
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
    public Map<String, Object> convertMap() {
        // done cabos Map by jflute (2017/08/10)
        // done cabos use Linked by jflute (2017/09/07)
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("author", this.author);
        map.put("merged", this.merged);
        map.put("formatVersion", this.formatVersion);
        List<Map<String, Object>> tableList =
            this.decoMap.get(DECO_MAP_KEY).stream().map(table -> table.convertPieceMap()).collect(Collectors.toList());
        LinkedHashMap<Object, Object> decoMap = new LinkedHashMap<>();
        decoMap.put(DECO_MAP_KEY, tableList);
        map.put("decoMap", decoMap);
        return map;
    }

    // ===================================================================================
    //                                                                            Override
    //                                                                            ========
    @Override
    public String toString() {
        return new MapListString().buildMapString(this.convertMap());
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public Map<String, List<DfDecoMapTablePart>> getDecoMap() {
        return decoMap;
    }

    public void setDecoMap(Map<String, List<DfDecoMapTablePart>> decoMap) {
        this.decoMap = decoMap;
    }

    public List<DfDecoMapTablePart> getTableList() {
        return decoMap.get(DECO_MAP_KEY);
    }

    public void setTableList(List<DfDecoMapTablePart> decoMap) {
        Map<String, List<DfDecoMapTablePart>> map = new LinkedHashMap<>();
        map.put(DECO_MAP_KEY, decoMap);
        this.decoMap = map;
    }
}
