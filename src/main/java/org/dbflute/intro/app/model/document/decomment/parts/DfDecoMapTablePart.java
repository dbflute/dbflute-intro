package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    //                                                                            Accessor
    //                                                                            ========
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public List<DfDecoMapColumnPart> getColumn() {
        return columns;
    }
    public void setColumn(List<DfDecoMapColumnPart> columns) {
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
