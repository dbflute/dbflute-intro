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
package org.dbflute.intro.app.model.client;

import org.dbflute.optional.OptionalThing;

/**
 * @author jflute
 * @author hakiba
 */
public class ProjectInfra {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String clientProject; // not null, torque.project
    protected final String dbfluteVersion; // not null, e.g. 1.1.1 (at _project.sh)
    protected final ExtlibFile jdbcDriverExtlibFile; // extlib file

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ProjectInfra(String clientProject, String dbfluteVersion) {
        this.clientProject = clientProject;
        this.dbfluteVersion = dbfluteVersion;
        this.jdbcDriverExtlibFile = null;
    }

    public ProjectInfra(String clientProject, String dbfluteVersion, String jdbcDriverFileName, String jdbcDriverFileDataBase64) {
        this.clientProject = clientProject;
        this.dbfluteVersion = dbfluteVersion;
        this.jdbcDriverExtlibFile = new ExtlibFile(jdbcDriverFileName, jdbcDriverFileDataBase64);
    }

    public ProjectInfra(String clientProject, String dbfluteVersion, ExtlibFile jdbcDriverFile) {
        this.clientProject = clientProject;
        this.dbfluteVersion = dbfluteVersion;
        this.jdbcDriverExtlibFile = jdbcDriverFile;
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

    public OptionalThing<ExtlibFile> getJdbcDriverExtlibFile() {
        return OptionalThing.ofNullable(jdbcDriverExtlibFile, () -> {
            throw new IllegalStateException("Not found the jdbcDriverExtlibFile.");
        });
    }
}
