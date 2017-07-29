package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.List;

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
