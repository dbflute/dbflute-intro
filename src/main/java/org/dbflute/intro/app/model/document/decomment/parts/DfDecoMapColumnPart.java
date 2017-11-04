package org.dbflute.intro.app.model.document.decomment.parts;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapColumnPart {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Long MINIMUM_COMMENT_VERSION = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected String columnName;
    protected List<DfDecoMapPropertyPart> properties;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    public static DfDecoMapColumnPart createPieceColumnPart(Map.Entry<String, Map<String, Object>> columnEntry) {
        final DfDecoMapPropertyPart property = new DfDecoMapPropertyPart(columnEntry.getValue());
        DfDecoMapColumnPart column = new DfDecoMapColumnPart();
        column.setColumnName(columnEntry.getKey());
        column.setProperties(Collections.singletonList(property));
        return column;
    }

    public static DfDecoMapColumnPart createPickupColumnPart(Map.Entry<String, List<Map<String, Object>>> columnEntry) {
        final List<DfDecoMapPropertyPart> properties = columnEntry.getValue().stream().map(propertiesMap -> {
            return new DfDecoMapPropertyPart(propertiesMap);
        }).collect(Collectors.toList());
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

    public List<DfDecoMapPropertyPart> getProperties() {
        return this.properties;
    }

    public void setProperties(List<DfDecoMapPropertyPart> properties) {
        this.properties = properties;
    }

    public long getLatestCommentVersion() {
        return this.getProperties()
            .stream()
            .map(part -> part.getCommentVersion())
            .max(Comparator.naturalOrder())
            .orElse(MINIMUM_COMMENT_VERSION);
    }
}
