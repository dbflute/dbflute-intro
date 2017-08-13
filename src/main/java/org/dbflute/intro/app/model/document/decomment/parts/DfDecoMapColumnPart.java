package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapColumnPart {

    // ===================================================================================
    //                                                                         Inner Class
    //                                                                         ===========
    public static class ColumnProperty {
        protected String decomment;
        protected String databaseComment;
        protected String previousWholeComment;
        protected long commentVersion;
        protected List<String> authorList;

        public ColumnProperty() {
        }

        public ColumnProperty(Map columnEntry) {
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
            HashMap<String, Object> map = new HashMap<>();
            map.put("decomment", this.decomment);
            map.put("databaseComment", this.databaseComment);
            map.put("previousWholeComment", this.previousWholeComment);
            map.put("commentVersion", this.commentVersion);
            map.put("authorList", this.authorList);
            return map;
        }
    }

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String columnName;
    protected List<ColumnProperty> properties;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DfDecoMapColumnPart() {
    }

    // done hakiba check cast by hakiba (2017/07/29)
    public DfDecoMapColumnPart(Map.Entry<String, Object> columnEntry) {
        this.columnName = columnEntry.getKey();
        this.properties = ((List<?>) columnEntry.getValue()).stream()
            .filter(propertiesObject -> propertiesObject instanceof Map<?, ?>)
            .map(propertiesObject -> (Map<?, ?>) propertiesObject)
            .map(propertiesMap -> new ColumnProperty(propertiesMap))
            .collect(Collectors.toList());
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

    public List<ColumnProperty> getProperties() {
        return this.properties;
    }

    public void setProperties(List<ColumnProperty> properties) {
        this.properties = properties;
    }

    public void setProperty(ColumnProperty property) {
        this.properties = Collections.singletonList(property);
    }

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public Map<String, Object> convertMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(columnName, properties.stream().map(property -> property.convertMap()).collect(Collectors.toList()));
        return map;
    }

    public Map<String, Object> convertPieceMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(columnName, properties.stream().map(property -> property.convertMap()).findFirst().orElse(new HashMap<>()));
        return map;
    }
}
