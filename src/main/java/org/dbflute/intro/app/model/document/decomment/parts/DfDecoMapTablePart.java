package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hakiba
 */
public class DfDecoMapTablePart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String tableName;
    protected List<DfDecoMapColumnPart> columns;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfDecoMapTablePart() {
    }

    // TODO hakiba check cast by hakiba (2017/07/29)
    @SuppressWarnings("unchecked")
    public DfDecoMapTablePart(Map.Entry<String, Object> tablePartEntry) {
        this.tableName = tablePartEntry.getKey();
        final Set<Map.Entry<String, Object>> columnPartSet = ((Map<String, Object>) tablePartEntry.getValue()).entrySet();
        this.columns = columnPartSet.stream().map(columnEntry -> new DfDecoMapColumnPart(columnEntry)).collect(Collectors.toList());
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
    public List<DfDecoMapColumnPart> getColumns() {
        return columns;
    }
    public void setColumns(List<DfDecoMapColumnPart> columns) {
        this.columns = columns;
    }

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(tableName, columns.stream().map(column -> column.convertMap()).collect(Collectors.toList()));
        return map;
    }

    public Map<String, Object> convertPieceMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(tableName, columns.stream().map(column -> column.convertPieceMap()).collect(Collectors.toList()));
        return map;
    }
}
