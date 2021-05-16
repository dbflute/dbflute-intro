/*
 * Copyright 2014-2021 the original author or authors.
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

import java.util.LinkedHashMap;
import java.util.Map;

import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.util.ContainerUtil;

/**
 * @author jflute
 * @author hakiba
 * @author deco
 * @author cabos
 * @author subaru
 */
public class ProjectInfra {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String projectName; // not null, torque.project
    protected final String dbfluteVersion; // not null, e.g. 1.1.1 (at _project.sh)
    protected final ExtlibFile jdbcDriverExtlibFile; // extlib file

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ProjectInfra(String projectName, String dbfluteVersion) {
        this(projectName, dbfluteVersion, null);
    }

    public ProjectInfra(String projectName, String dbfluteVersion, ExtlibFile jdbcDriverFile) {
        this.projectName = projectName;
        this.dbfluteVersion = dbfluteVersion;
        this.jdbcDriverExtlibFile = jdbcDriverFile;
        ContainerUtil.injectSimply(this);
    }

    // ===================================================================================
    //                                                                         Replace Map
    //                                                                         ===========
    public Map<String, Object> prepareInitReplaceMap() {
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + projectName);
        return replaceMap;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getProjectName() {
        return projectName;
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
