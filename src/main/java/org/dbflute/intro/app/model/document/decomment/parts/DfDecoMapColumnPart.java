package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.List;
import java.util.Map;

/**
 * @author hakiba
 */
public class DfDecoMapColumnPart {

    protected String columnName;
    protected String decomment;
    protected String databaseComment;
    protected String previousWholeComment;
    protected long commentVersion;
    protected List<String> authorList;

    public DfDecoMapColumnPart() {
    }

    @SuppressWarnings("unchecked")
    // TODO hakiba check cast by hakiba (2017/07/29)
    public DfDecoMapColumnPart(Map.Entry<String, Object> columnEntry) {
        this.columnName = columnEntry.getKey();
        final Map<String, Object> columnMap = (Map<String, Object>) columnEntry.getValue();
        this.columnName = (String) columnMap.get("columnName");
        this.decomment = (String) columnMap.get("decomment");
        this.databaseComment = (String) columnMap.get("databaseComment");
        this.commentVersion = Long.valueOf((String) columnMap.get("commentVersion"));
        this.authorList = (List<String>) columnMap.get("authorList");
    }

    public String getDecomment() {
        return decomment;
    }
    public void setDecomment(String decomment) {
        this.decomment = decomment;
    }
    public String getDatabaseComment() {
        return databaseComment;
    }
    public void setDatabaseComment(String databaseComment) {
        this.databaseComment = databaseComment;
    }
    public String getPreviousWholeComment() {
        return previousWholeComment;
    }
    public void setPreviousWholeComment(String previousWholeComment) {
        this.previousWholeComment = previousWholeComment;
    }
    public String getColumnName() {

        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
