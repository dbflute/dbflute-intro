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
package org.dbflute.intro.app.model.client;

import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 */
public class ProjectMeta {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String clientProject; // not null, torque.project
    protected final String dbfluteVersion; // not null, e.g. 1.1.1 (at _project.sh)
    protected final String jdbcDriverJarPath; // path to extlib

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ProjectMeta(String clientProject, String dbfluteVersion, String jdbcDriverJarPath) {
        this.clientProject = clientProject;
        this.dbfluteVersion = dbfluteVersion;
        this.jdbcDriverJarPath = jdbcDriverJarPath;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getClientProject() {
        return clientProject;
    }

    public String getDbfluteVersion() {
        return dbfluteVersion;
    }
    
    public OptionalThing<String> getJdbcDriverJarPath() {
        return OptionalThing.ofNullable(jdbcDriverJarPath, () -> {
            throw new IllegalStateException("Not found the jdbcDriverJarPath.");
        });
    }
}
