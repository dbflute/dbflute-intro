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
        Map<String, Map<String, Object>> columnNameMap =
            columns.stream().collect(Collectors.toMap(column -> column.columnName, column -> column.convertMap(), (c1, c2) -> c1));
        HashMap<String, Object> map = new HashMap<>();
        map.put(this.tableName, columnNameMap);
        return map;
    }
}
