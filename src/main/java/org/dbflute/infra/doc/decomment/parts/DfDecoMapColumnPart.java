package org.dbflute.infra.doc.decomment.parts;

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
    protected List<DfDecoMapPropertyPart> propertyList;

    // ===================================================================================
    //                                                                           Converter
    //                                                                           =========
    @SuppressWarnings("unchecked")
    public static DfDecoMapColumnPart createColumnPart(Map<String, Object> columnPartMap) {
        DfDecoMapColumnPart column = new DfDecoMapColumnPart();
        column.setColumnName((String) columnPartMap.get("columnName"));
        List<DfDecoMapPropertyPart> propertyList = ((List<Map<String, Object>>) columnPartMap.get("propertyList")).stream()
            .map(DfDecoMapPropertyPart::new)
            .collect(Collectors.toList());
        column.setPropertyList(propertyList);
        return column;
    }

    // TODO done cabos convertToMap()? by jflute (2017/11/11)
    public List<Map<String, Object>> convertToMap() {
        return propertyList.stream().map(property -> property.convertToMap()).collect(Collectors.toList());
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

    public List<DfDecoMapPropertyPart> getPropertyList() {
        return this.propertyList;
    }

    public void setPropertyList(List<DfDecoMapPropertyPart> properties) {
        this.propertyList = properties;
    }
}
