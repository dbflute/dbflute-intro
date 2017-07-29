package org.dbflute.intro.app.model.document.decomment;

import java.time.LocalDateTime;
import java.util.List;

import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

/**
 * @author hakiba
 */
public class DfDecoMapPiece {

    protected String fileName;
    protected long formatVersion;
    protected String author;
    protected LocalDateTime decommentDatetime;
    protected boolean merged;
    protected List<DfDecoMapTablePart> decoMap;

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public long getFormatVersion() {
        return formatVersion;
    }
    public void setFormatVersion(long formatVersion) {
        this.formatVersion = formatVersion;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public LocalDateTime getDecommentDatetime() {
        return decommentDatetime;
    }
    public void setDecommentDatetime(LocalDateTime decommentDatetime) {
        this.decommentDatetime = decommentDatetime;
    }
    public boolean isMerged() {
        return merged;
    }
    public void setMerged(boolean merged) {
        this.merged = merged;
    }
    public List<DfDecoMapTablePart> getDecoMap() {
        return decoMap;
    }
    public void setDecoMap(List<DfDecoMapTablePart> decoMap) {
        this.decoMap = decoMap;
    }
}
