package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapColumnPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String columnName;
    protected List<ColumnPropertyPart> properties;

    // ===================================================================================
    //                                                                         Inner Class
    //                                                                         ===========
    public static class ColumnPropertyPart {
        protected String decomment;
        protected String databaseComment;
        protected String previousWholeComment;
        protected long commentVersion;
        protected List<String> authorList;

        public ColumnPropertyPart() {
        }

        public ColumnPropertyPart(Map<String, Object> columnEntry) {
            this.decomment = (String) columnEntry.get("decomment");
            this.databaseComment = (String) columnEntry.get("databaseComment");
            this.previousWholeComment = (String) columnEntry.get("previousWholeComment");
            this.commentVersion = Long.valueOf((String) columnEntry.get("commentVersion"));
            this.authorList = ((List<?>) columnEntry.get("authorList")).stream()
                .filter(authorObject -> authorObject instanceof String)
                .map(authorObject -> (String) authorObject)
                .collect(Collectors.toList());
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

        public Map<String, Object> convertMap() {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("decomment", this.decomment);
            map.put("databaseComment", this.databaseComment);
            map.put("previousWholeComment", this.previousWholeComment);
            map.put("commentVersion", this.commentVersion);
            map.put("authorList", this.authorList);
            return map;
        }
    }

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public static DfDecoMapColumnPart createPieceColumnPart(Map.Entry<String, Object> columnEntry) {
        //noinspection unchecked
        final ColumnPropertyPart property = new ColumnPropertyPart((Map<String, Object>) columnEntry.getValue());

        DfDecoMapColumnPart column = new DfDecoMapColumnPart();
        column.setColumnName(columnEntry.getKey());
        column.setProperties(Collections.singletonList(property));
        return column;
    }

    public static DfDecoMapColumnPart createPickupColumnPart(Map.Entry<String, Object> columnEntry) {
        //noinspection unchecked
        final List<ColumnPropertyPart> properties = ((List<?>) columnEntry.getValue()).stream()
            .map(propertiesObject -> (Map<String, Object>) propertiesObject)
            .map(propertiesMap -> new ColumnPropertyPart(propertiesMap))
            .collect(Collectors.toList());

        DfDecoMapColumnPart column = new DfDecoMapColumnPart();
        column.setColumnName(columnEntry.getKey());
        column.setProperties(properties);
        return column;
    }

    public Map<String, Object> convertPieceMap() {
        return properties.stream().map(property -> property.convertMap()).findFirst().orElse(null);
    }

    public List<Map<String, Object>> convertPickupMap() {
        return properties.stream().map(property -> property.convertMap()).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getColumnName() {
        return this.columnName;
    }
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<ColumnPropertyPart> getProperties() {
        return this.properties;
    }

    public void setProperties(List<ColumnPropertyPart> properties) {
        this.properties = properties;
    }
}
