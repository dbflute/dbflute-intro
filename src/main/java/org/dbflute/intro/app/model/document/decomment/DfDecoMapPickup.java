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
public class DfDecoMapPickup {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String DECO_MAP_KEY = "tableList";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String formatVersion;
    protected Map<String, List<DfDecoMapTablePart>> decoMap;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
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
    public Map<String, Object> convertMap() {
        final Map<String, Map<String, Object>> convertedDecoMap = decoMap.get("tableList")
            .stream()
            .collect(Collectors.toMap(tablePart -> tablePart.getTableName(), tablePart -> tablePart.convertPickupMap(), (c1, c2) -> c1));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("formatVersion", formatVersion);
        map.put("decoMap", convertedDecoMap);
        return map;
    }

    // done hakiba move to before Accessor by jflute (2017/08/17)
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

    public List<DfDecoMapTablePart> getTableList() {
        return decoMap.get(DECO_MAP_KEY);
    }

    public void setTableList(List<DfDecoMapTablePart> tableList) {
        Map<String, List<DfDecoMapTablePart>> map = new LinkedHashMap<>();
        map.put(DECO_MAP_KEY, tableList);
        this.decoMap = map;
    }
}
