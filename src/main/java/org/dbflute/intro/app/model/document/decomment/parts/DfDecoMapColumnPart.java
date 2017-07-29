package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapColumnPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String columnName;
    protected String decomment;
    protected String databaseComment;
    protected String previousWholeComment;
    protected long commentVersion;
    protected List<String> authorList;

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getColumnName() {
        return columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
    public long getCommentVersion() {
        return commentVersion;
    }
    public void setCommentVersion(long commentVersion) {
        this.commentVersion = commentVersion;
    }
    public List<String> getAuthorList() {
        return authorList;
    }
    public void setAuthorList(List<String> authorList) {
        this.authorList = authorList;
    }

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public Map<String, Object> convertMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("decomment", this.decomment);
        map.put("databaseComment", this.databaseComment);
        map.put("previousWholeComment", this.previousWholeComment);
        map.put("commentVersion", this.commentVersion);
        map.put("authorList", this.authorList);
        return map;
    }
}
