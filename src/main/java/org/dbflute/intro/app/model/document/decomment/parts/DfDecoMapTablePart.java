package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.List;

/**
 * @author hakiba
 */
public class DfDecoMapTablePart {

    protected String tableName;
    protected List<DfDecoMapColumnPart> columns;

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
