/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.web.settings;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

import javax.validation.Valid;

/**
 * @author hakiba
 * @author jflute
 */
public class SettingsResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public String projectName;
    @Required
    public CDef.TargetDatabase databaseCode;
    @Required
    public CDef.TargetLanguage languageCode;
    @Required
    public CDef.TargetContainer containerCode;
    @Required
    public String packageBase;
    @Required
    public String jdbcDriverFqcn;

    @Required
    @Valid
    public DatabaseSettingsPart mainSchemaSettings;

    public static class DatabaseSettingsPart {

        @Required
        public String url;
        public String schema;
        @Required
        public String user;
        public String password;
    }

    @Required
    public String dbfluteVersion;

    public String jdbcDriverJarPath;

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
