/*
 * Copyright 2014-2017 the original author or authors.
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapTablePart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String tableName;
    protected List<DfDecoMapPropertyPart> propertyList;
    protected List<DfDecoMapColumnPart> columnList;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    @SuppressWarnings("unchecked")
    public static DfDecoMapTablePart createTablePart(Map<String, Object> tablePartMap) {
        DfDecoMapTablePart table = new DfDecoMapTablePart();
        table.setTableName((String) tablePartMap.get("tableName"));
        List<DfDecoMapPropertyPart> propertyList = ((List<Map<String, Object>>) tablePartMap.get("propertyList")).stream()
            .map(DfDecoMapPropertyPart::new)
            .collect(Collectors.toList());
        table.setPropertyList(propertyList);
        List<DfDecoMapColumnPart> columnList = ((List<Map<String, Object>>) tablePartMap.get("columnList")).stream()
            .map(DfDecoMapColumnPart::createColumnPart)
            .collect(Collectors.toList());
        table.setColumnList(columnList);
        return table;
    }

    public Map<String, Object> convertPickupMap() {
        Map<String, List<Map<String, Object>>> columnMap = columnList.stream()
            .collect(Collectors.toMap(column -> column.getColumnName(), column -> column.convertToMap(), (c1, c2) -> c1));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(tableName, columnMap);
        return map;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DfDecoMapPropertyPart> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<DfDecoMapPropertyPart> propertyList) {
        this.propertyList = propertyList;
    }

    public List<DfDecoMapColumnPart> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<DfDecoMapColumnPart> columns) {
        this.columnList = columns;
    }

}
