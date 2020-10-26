/*
 * Copyright 2014-2020 the original author or authors.
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
