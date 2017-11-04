package org.dbflute.intro.app.model.document.decomment.parts;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.helper.HandyDate;

/**
 * @author cabos
 */
public class DfDecoMapPropertyPart {

    protected String decomment;
    protected String databaseComment;
    protected LocalDateTime pieceDatetime;
    protected long commentVersion;
    protected List<String> authorList;

    public DfDecoMapPropertyPart() {
    }

    public DfDecoMapPropertyPart(Map<String, Object> columnEntry) {
        this.decomment = (String) columnEntry.get("decomment");
        this.databaseComment = (String) columnEntry.get("databaseComment");
        this.pieceDatetime = new HandyDate((String) columnEntry.get("pieceDatetime")).getLocalDateTime();
        this.commentVersion = Long.valueOf((String) columnEntry.get("commentVersion"));
        this.authorList = ((List<?>) columnEntry.get("authorList")).stream().map(obj -> (String) obj).collect(Collectors.toList());
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

    public LocalDateTime getPieceDatetime() {
        return pieceDatetime;
    }

    public void setPieceDatetime(LocalDateTime pieceDatetime) {
        this.pieceDatetime = pieceDatetime;
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

    public Map<String, Object> convertMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("decomment", this.decomment);
        map.put("databaseComment", this.databaseComment);
        map.put("pieceDatetime", this.pieceDatetime);
        map.put("commentVersion", this.commentVersion);
        map.put("authorList", this.authorList);
        return map;
    }
}
