/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.model.client.database;

/**
 * @author jflute
 */
public class DbConnectionBox {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String url;
    protected final String schema;
    protected final String user;
    protected final String password;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DbConnectionBox(String url, String schema, String user, String password) {
        this.url = url;
        this.schema = schema;
        this.user = user;
        this.password = password;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return "box:{" + url + ", " + schema + ", " + user + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getUrl() {
        return url;
    }

    public String getSchema() {
        return schema;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
