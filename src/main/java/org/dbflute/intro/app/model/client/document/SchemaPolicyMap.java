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

/**
 * @author hakiba
 */
public class SchemaPolicyMap {
    public SchemaPolicyTargetSetting targetSetting;
    public SchemaPolicyWholeMap wholeMap;
    public SchemaPolicyTableMap tableMap;
    public SchemaPolicyColumnMap columnMap;

    public SchemaPolicyMap(SchemaPolicyTargetSetting targetSetting, SchemaPolicyWholeMap wholeMap, SchemaPolicyTableMap tableMap,
            SchemaPolicyColumnMap columnMap) {
        this.targetSetting = targetSetting;
        this.wholeMap = wholeMap;
        this.tableMap = tableMap;
        this.columnMap = columnMap;
    }

    public static SchemaPolicyMap noSettingsInstance() {
        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), SchemaPolicyWholeMap.noSettingInstance(),
                SchemaPolicyTableMap.noSettingInstance(), SchemaPolicyColumnMap.noSettingInstance());
    }
}
