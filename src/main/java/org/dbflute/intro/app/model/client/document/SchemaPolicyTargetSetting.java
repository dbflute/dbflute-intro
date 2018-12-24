package org.dbflute.intro.app.model.client.document;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author hakiba
 */
public class SchemaPolicyTargetSetting {
    public List<String> tableExceptList;
    public List<String> tableTargetList;
    public Map<String, List<String>> columnExceptMap;
    public boolean isMainSchemaOnly;

    public static SchemaPolicyTargetSetting noSettingInstance() {
        return new SchemaPolicyTargetSetting(Collections.emptyList(), Collections.emptyList(), Collections.emptyMap(), false);
    }

    public SchemaPolicyTargetSetting(List<String> tableExceptList, List<String> tableTargetList, Map<String, List<String>> columnExceptMap,
            boolean isMainSchemaOnly) {
        this.tableExceptList = tableExceptList;
        this.tableTargetList = tableTargetList;
        this.columnExceptMap = columnExceptMap;
        this.isMainSchemaOnly = isMainSchemaOnly;
    }
}
