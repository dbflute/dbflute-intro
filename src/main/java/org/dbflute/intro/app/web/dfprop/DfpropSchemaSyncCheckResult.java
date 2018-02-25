/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.web.dfprop;

import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;

/**
 * @author deco
 */
public class DfpropSchemaSyncCheckResult {

    public String url;
    public String schema;
    public String user;
    public String password;
    public Boolean isSuppressCraftDiff;

    public DfpropSchemaSyncCheckResult() {
    }

    public DfpropSchemaSyncCheckResult(SchemaSyncCheckMap schemaSyncCheckMap) {
        DbConnectionBox dbConnectionModel = schemaSyncCheckMap.dbConnectionModel;
        url = dbConnectionModel.getUrl();
        schema = dbConnectionModel.getSchema();
        user = dbConnectionModel.getUser();
        password = dbConnectionModel.getPassword();
        isSuppressCraftDiff = schemaSyncCheckMap.isSuppressCraftDiff;
    }
}
