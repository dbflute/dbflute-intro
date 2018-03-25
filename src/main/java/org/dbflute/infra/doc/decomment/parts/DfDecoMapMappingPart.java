/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.infra.doc.decomment.parts;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.helper.HandyDate;
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
    /**
     * Constructor of DfDecoMapMappingPart
     * @param mappingList decomap mapping list (Not Empty)
     */
    public DfDecoMapMappingPart(List<DfDecoMapMapping> mappingList) {
        if (mappingList == null || mappingList.size() == 0) {
            throw new IllegalArgumentException("mappingList is empty, mappingList : " + mappingList);
        }
        DfDecoMapMapping mapping = mappingList.get(0);
        this.oldTableName = mapping.getOldTableName();
        this.oldColumnName = mapping.getOldColumnName();
        this.targetType = mapping.getTargetType();
        this.newNameList = mappingList.stream().map(NewName::new).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public DfDecoMapMappingPart(Map<String, Object> mappingMap) {
        this.oldTableName = (String) mappingMap.get("oldTableName");
        this.oldColumnName = (String) mappingMap.get("oldColumnName");
        this.targetType = DfDecoMapPieceTargetType.of(mappingMap.get("targetType")).get();
        this.newNameList = ((List<?>) mappingMap.get("newNameList")).stream()
            .map(obj -> new NewName((Map<String, Object>) obj))
            .collect(Collectors.toList());
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

        public NewName(Map<String, Object> mappingMap) {
            this.newTableName = (String) mappingMap.get("newTableName");
            this.newColumnName = (String) mappingMap.get("newColumnName");
            this.authorList = ((List<?>) mappingMap.get("authorList")).stream().map(obj -> (String) obj).collect(Collectors.toList());
            this.mappingCode = (String) mappingMap.get("mappingCode");
            this.mappingOwner = (String) mappingMap.get("mappingOwner");
            this.mappingDatetime = new HandyDate((String) mappingMap.get("mappingDatetime")).getLocalDateTime();
            this.previousMappingList =
                ((List<?>) mappingMap.get("previousMappingList")).stream().map(obj -> (String) obj).collect(Collectors.toList());
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
