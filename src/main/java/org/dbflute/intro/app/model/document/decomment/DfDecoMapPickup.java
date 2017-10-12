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
    //                                                                           Attribute
    //                                                                           =========
    protected String fileName;
    protected String formatVersion;
    protected List<DfDecoMapTablePart> decoMap;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    // map:{
    //     ; formatVersion = 1.0
    //     ; decoMap = map:{
    //         ; MEMBER = map:{
    //             ; MEMBER_NAME = list:{
    //                 ; map:{
    //                     ; decomment = piari
    //                     ; databaseComment = sea
    //                     ; previousWholeComment = seasea
    //                     ; commentVersion = 1
    //                     ; authorList = list:{ jflute ; cabos }
    //                 }
    //                 ; map:{
    //                     ; decomment = bonvo
    //                     ; databaseComment = sea
    //                     ; previousWholeComment = seasea
    //                     ; commentVersion = 1
    //                     ; authorList = list:{ jflute ; cabos }
    //                 }
    //             }
    //         }
    //     }
    // }
    public Map<String, Object> convertMap() {
        final Map<String, Map<String, Object>> convertedDecoMap = decoMap.stream().collect(
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
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public List<DfDecoMapTablePart> getDecoMap() {
        return decoMap;
    }

    public void setDecoMap(List<DfDecoMapTablePart> decoMap) {
        this.decoMap = decoMap;
    }
}
