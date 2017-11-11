package org.dbflute.intro.app.model.document.decomment;

import static org.dbflute.intro.mylasta.appcls.AppCDef.PieceTargetType;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dbflute.helper.mapstring.MapListString;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapPiece {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String formatVersion;
    protected boolean merged;
    protected String tableName;
    protected String columnName;
    protected PieceTargetType targetType;
    protected String decomment;
    protected String databaseComment;
    protected Long commentVersion;
    protected List<String> authorList;
    protected String pieceCode;
    protected LocalDateTime pieceDatetime;
    protected String pieceOwner;
    protected List<String> previousPieceList;

    // done cabos move to before accessor by jflute (2017/08/10)
    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    // TODO yuto write e.g. (2017/11/11)
    public Map<String, Object> convertMap() {
        // done cabos Map by jflute (2017/08/10)
        // done cabos use Linked by jflute (2017/09/07)
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("formatVersion", this.formatVersion);
        map.put("merged", this.merged);
        map.put("tableName", this.tableName);
        map.put("columnName", this.columnName);
        map.put("decomment", this.decomment);
        map.put("databaseComment", this.databaseComment);
        map.put("commentVersion", this.commentVersion);
        map.put("authorList", this.authorList);
        map.put("pieceCode", this.pieceCode);
        map.put("pieceDatetime", this.pieceDatetime);
        map.put("pieceOwner", this.pieceOwner);
        map.put("previousPieceList", this.previousPieceList);
        return map;
    }

    // ===================================================================================
    //                                                                            Override
    //                                                                            ========
    @Override
    public String toString() {
        return new MapListString().buildMapString(this.convertMap());
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setTargetType(PieceTargetType targetType) {
        this.targetType = targetType;
    }

    public void setDecomment(String decomment) {
        this.decomment = decomment;
    }

    public void setDatabaseComment(String databaseComment) {
        this.databaseComment = databaseComment;
    }

    public void setCommentVersion(Long commentVersion) {
        this.commentVersion = commentVersion;
    }

    public void setAuthorList(List<String> authorList) {
        this.authorList = authorList;
    }

    public void setPieceCode(String pieceCode) {
        this.pieceCode = pieceCode;
    }

    public void setPieceDatetime(LocalDateTime pieceDatetime) {
        this.pieceDatetime = pieceDatetime;
    }

    public void setPieceOwner(String pieceOwner) {
        this.pieceOwner = pieceOwner;
    }

    public void setPreviousPieceList(List<String> previousPieceList) {
        this.previousPieceList = previousPieceList;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public boolean isMerged() {
        return merged;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public PieceTargetType getTargetType() {
        return targetType;
    }

    public String getDecomment() {
        return decomment;
    }

    public String getDatabaseComment() {
        return databaseComment;
    }

    public Long getCommentVersion() {
        return commentVersion;
    }

    public List<String> getAuthorList() {
        return authorList;
    }

    public String getPieceCode() {
        return pieceCode;
    }

    public LocalDateTime getPieceDatetime() {
        return pieceDatetime;
    }

    public String getPieceOwner() {
        return pieceOwner;
    }

    public List<String> getPreviousPieceList() {
        return previousPieceList;
    }
}
