package org.dbflute.infra.doc.decomment.parts;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.infra.doc.decomment.DfDecoMapMapping;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;

/**
 * @author cabos
 */
public class DfDecoMapMappingPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String oldTableName;
    protected final String oldColumnName;
    protected final DfDecoMapPieceTargetType targetType;
    protected final List<NewName> newNameList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfDecoMapMappingPart(List<DfDecoMapMapping> mappingList) {
        DfDecoMapMapping mapping = mappingList.get(0);
        this.oldTableName = mapping.getOldTableName();
        this.oldColumnName = mapping.getOldColumnName();
        this.targetType = mapping.getTargetType();
        this.newNameList = mappingList.stream().map(NewName::new).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public Map<String, Object> convertToMap() {
        final List<Map<String, Object>> convertedNewNameList =
            this.newNameList.stream().map(NewName::convertToMap).collect(Collectors.toList());
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("oldTableName", this.oldTableName);
        map.put("oldColumnName", this.oldColumnName);
        map.put("targetType", this.targetType);
        map.put("newNameList", convertedNewNameList);
        return map;
    }

    public static class NewName {

        // ===============================================================================
        //                                                                       Attribute
        //                                                                       =========
        protected final String newTableName;
        protected final String newColumnName;
        protected final List<String> authorList;
        protected final String mappingCode;
        protected final String mappingOwner;
        protected final LocalDateTime mappingDatetime;
        protected final List<String> previousMappingList;

        // ===============================================================================
        //                                                                     Constructor
        //                                                                     ===========
        public NewName(DfDecoMapMapping mapping) {
            this.newTableName = mapping.getNewTableName();
            this.newColumnName = mapping.getNewColumnName();
            this.authorList = mapping.getAuthorList();
            this.mappingCode = mapping.getMappingCode();
            this.mappingOwner = mapping.getMappingOwner();
            this.mappingDatetime = mapping.getMappingDatetime();
            this.previousMappingList = mapping.getPreviousMappingList();
        }

        // ===============================================================================
        //                                                                       Converter
        //                                                                       =========
        public Map<String, Object> convertToMap() {
            final Map<String, Object> map = new LinkedHashMap<>();
            map.put("newTableName", this.newTableName);
            map.put("newColumnName", this.newColumnName);
            map.put("authorList", this.authorList);
            map.put("mappingCode", this.mappingCode);
            map.put("mappingOwner", this.mappingOwner);
            map.put("mappingDatetime", this.mappingDatetime);
            map.put("previousMappingList", this.previousMappingList);
            return map;
        }

        // ===============================================================================
        //                                                                        Accessor
        //                                                                        ========
        public String getNewTableName() {
            return newTableName;
        }

        public String getNewColumnName() {
            return newColumnName;
        }

        public List<String> getAuthorList() {
            return authorList;
        }

        public String getMappingCode() {
            return mappingCode;
        }

        public String getMappingOwner() {
            return mappingOwner;
        }

        public LocalDateTime getMappingDatetime() {
            return mappingDatetime;
        }

        public List<String> getPreviousMappingList() {
            return previousMappingList;
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getOldTableName() {
        return oldTableName;
    }

    public String getOldColumnName() {
        return oldColumnName;
    }

    public DfDecoMapPieceTargetType getTargetType() {
        return targetType;
    }

    public List<NewName> getNewNameList() {
        return newNameList;
    }
}
