/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.model.client.document;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.util.DfCollectionUtil;

/**
 * @author hakiba
 */
public class SchemaPolicyMap {
    public SchemaPolicyTargetSetting targetSetting;
    public SchemaPolicyWholeMap wholeMap;
    public SchemaPolicyTableMap tableMap;
    public SchemaPolicyColumnMap columnMap;
    public Map<String, Object> comments;

    public SchemaPolicyMap(SchemaPolicyTargetSetting targetSetting, SchemaPolicyWholeMap wholeMap, SchemaPolicyTableMap tableMap,
            SchemaPolicyColumnMap columnMap) {
        this(targetSetting, wholeMap, tableMap, columnMap, Collections.emptyMap());
    }
    public SchemaPolicyMap(SchemaPolicyTargetSetting targetSetting, SchemaPolicyWholeMap wholeMap, SchemaPolicyTableMap tableMap,
            SchemaPolicyColumnMap columnMap, Map<String, Object> comments) {
        this.targetSetting = targetSetting;
        this.wholeMap = wholeMap;
        this.tableMap = tableMap;
        this.columnMap = columnMap;
        this.comments = comments;
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = DfCollectionUtil.newLinkedHashMap();
        map.put("tableExceptList", targetSetting.tableExceptList);
        map.put("tableTargetList", targetSetting.tableTargetList);
        map.put("columnExceptMap", targetSetting.columnExceptMap);
        map.put("isMainSchemaOnly", targetSetting.isMainSchemaOnly ? "true" : "false");

        map.put("wholeMap", convertWholeMapToMap());
        map.put("tableMap", convertTableMapToMap());
        map.put("columnMap", convertColumnMapToMap());
        return map;
    }

    private Map<String, Object> convertWholeMapToMap() {
        LinkedHashMap<String, Object> map = DfCollectionUtil.newLinkedHashMap();
        map.put("themeList", wholeMap.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList()));
        return map;
    }

    private Map<String, Object> convertTableMapToMap() {
        LinkedHashMap<String, Object> map = DfCollectionUtil.newLinkedHashMap();
        map.put("themeList", tableMap.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList()));
        map.put("statementList", tableMap.statementList);
        return map;
    }

    private Map<String, Object> convertColumnMapToMap() {
        LinkedHashMap<String, Object> map = DfCollectionUtil.newLinkedHashMap();
        map.put("themeList", columnMap.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList()));
        map.put("statementList", columnMap.statementList);
        return map;
    }

    public static SchemaPolicyMap noSettingsInstance() {
        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), SchemaPolicyWholeMap.noSettingInstance(),
                SchemaPolicyTableMap.noSettingInstance(), SchemaPolicyColumnMap.noSettingInstance(), Collections.emptyMap());
    }
}
