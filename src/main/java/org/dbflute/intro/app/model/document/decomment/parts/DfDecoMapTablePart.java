package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hakiba
 */
public class DfDecoMapTablePart {

    protected String tableName;
    protected List<DfDecoMapColumnPart> columns;

    public DfDecoMapTablePart() {}

    // TODO hakiba check cast by hakiba (2017/07/29)
    @SuppressWarnings("unchecked")
    public DfDecoMapTablePart(Map.Entry<String, Object> tablePartEntry) {
        this.tableName = tablePartEntry.getKey();
        final Set<Map.Entry<String, Object>> columnPartSet = ((Map<String, Object>) tablePartEntry.getValue()).entrySet();
        this.columns = columnPartSet.stream().map(columnEntry -> new DfDecoMapColumnPart(columnEntry)).collect(Collectors.toList());
    }

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
}
