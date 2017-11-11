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
    protected String pieceCode;
    protected LocalDateTime pieceDatetime;
    protected String pieceOwner;
    protected List<String> previousPieceList;
    protected long commentVersion;
    protected List<String> authorList;

    public DfDecoMapPropertyPart() {
    }

    public DfDecoMapPropertyPart(Map<String, Object> propertyMap) {
        this.decomment = (String) propertyMap.get("decomment");
        this.databaseComment = (String) propertyMap.get("databaseComment");
        this.pieceCode = (String) propertyMap.get("pieceCode");
        this.pieceDatetime = new HandyDate((String) propertyMap.get("pieceDatetime")).getLocalDateTime();
        this.previousPieceList =
            ((List<?>) propertyMap.get("previousPieceList")).stream().map(obj -> (String) obj).collect(Collectors.toList());
        this.commentVersion = Long.valueOf((String) propertyMap.get("commentVersion"));
        this.authorList = ((List<?>) propertyMap.get("authorList")).stream().map(obj -> (String) obj).collect(Collectors.toList());
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

    public void setPieceCode(String pieceCode) {
        this.pieceCode = pieceCode;
    }

    public String getPieceCode() {
        return pieceCode;
    }
    public LocalDateTime getPieceDatetime() {
        return pieceDatetime;
    }

    public void setPieceDatetime(LocalDateTime pieceDatetime) {
        this.pieceDatetime = pieceDatetime;
    }

    public void setPieceOwner(String pieceOwner) {
        this.pieceOwner = pieceOwner;
    }

    public String getPieceOwner() {
        return pieceOwner;
    }
    public void setPreviousPieceList(List<String> previousPieceList) {
        this.previousPieceList = previousPieceList;
    }

    public List<String> getPreviousPieceList() {
        return previousPieceList;
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
        map.put("pieceCode", this.pieceCode);
        map.put("pieceDatetime", this.pieceDatetime);
        map.put("previousPieceList", this.previousPieceList);
        map.put("commentVersion", this.commentVersion);
        map.put("authorList", this.authorList);
        return map;
    }
}
