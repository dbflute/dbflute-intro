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
 * @author jflute
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
    public Map<String, Object> convertMap() {
        final Map<String, Map<String, Object>> convertedDecoMap = decoMap.get("tableList").stream().collect(
                Collectors.toMap(tablePart -> tablePart.getTableName(), tablePart -> tablePart.convertPickupMap(), (c1, c2) -> c1));

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
