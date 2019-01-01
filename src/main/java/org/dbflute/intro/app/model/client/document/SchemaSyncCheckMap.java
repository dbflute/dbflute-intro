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

import org.dbflute.intro.app.model.client.database.DbConnectionBox;

/**
 * @author jflute
 * @author deco
 */
public class SchemaSyncCheckMap {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public DbConnectionBox dbConnectionModel;
    public boolean isSuppressCraftDiff;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public SchemaSyncCheckMap() {
    }

    public SchemaSyncCheckMap(DbConnectionBox dbConnectionModel, boolean isSuppressCraftDiff) {
        this.dbConnectionModel = dbConnectionModel;
        this.isSuppressCraftDiff = isSuppressCraftDiff;
    }

    // ===================================================================================
    //                                                                           to Dfprop
    //                                                                           =========
    public String convertToDfpropStr() {
        final String url = dbConnectionModel.getUrl();
        final String schema = dbConnectionModel.getSchema();
        final String user = dbConnectionModel.getUser();
        final String password = dbConnectionModel.getPassword();
        return "    ; schemaSyncCheckMap = map:{" + "\n" + //
                "        ; url = " + url + "\n" + //
                "        ; schema = " + (schema != null ? schema : "") + "\n" + //
                "        ; user = " + user + "\n" + //
                "        ; password = " + (password != null ? password : "") + "\n" + //
                "        ; isSuppressCraftDiff = " + isSuppressCraftDiff + "\n" + //
                "    }";
    }
}
