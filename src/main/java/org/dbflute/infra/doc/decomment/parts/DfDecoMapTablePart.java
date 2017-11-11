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
            .collect(Collectors.toMap(column -> column.getColumnName(), column -> column.convertPickupMap(), (c1, c2) -> c1));

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
