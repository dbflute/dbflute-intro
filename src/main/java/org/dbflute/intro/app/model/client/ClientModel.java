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

import java.util.Map;

import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.model.client.outsidesql.OutsideSqlMap;
import org.dbflute.intro.app.model.client.reps.ReplaceSchemaMap;
import org.dbflute.optional.OptionalThing;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientModel {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                              Required
    //                                              --------
    protected final ProjectMeta projectMeta;
    protected final BasicInfoMap basicInfoMap;
    protected final DatabaseInfoMap databaseInfoMap; // contains additional schema

    // -----------------------------------------------------
    //                                               Options
    //                                               -------
    private DocumentMap documentMap;
    private OutsideSqlMap outsideSqlMap;
    private ReplaceSchemaMap replaceSchemaMap;
    private Map<String, SchemaSyncCheckMap> schemaSyncCheckMap; // from documentMap.dfprop

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ClientModel(ProjectMeta projectMeta, BasicInfoMap basicInfoMap, DatabaseInfoMap databaseInfoMap) {
        this.projectMeta = projectMeta;
        this.basicInfoMap = basicInfoMap;
        this.databaseInfoMap = databaseInfoMap;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return "client:{" + projectMeta + ", " + basicInfoMap + ", " + databaseInfoMap + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public ProjectMeta getProjectMeta() {
        return projectMeta;
    }

    public BasicInfoMap getBasicInfoMap() {
        return basicInfoMap;
    }

    public DatabaseInfoMap getDatabaseInfoMap() {
        return databaseInfoMap;
    }

    public OptionalThing<DocumentMap> getDocumentMap() {
        return OptionalThing.ofNullable(documentMap, () -> {
            throw new IllegalStateException("Not found the documentMap: " + toString());
        });
    }

    public void setDocumentMap(DocumentMap documentMap) {
        this.documentMap = documentMap;
    }

    public OptionalThing<OutsideSqlMap> getOutsideSqlMap() {
        return OptionalThing.ofNullable(outsideSqlMap, () -> {
            throw new IllegalStateException("Not found the outsideSqlMap: " + toString());
        });
    }

    public void setOutsideSqlMap(OutsideSqlMap outsideSqlMap) {
        this.outsideSqlMap = outsideSqlMap;
    }

    public OptionalThing<ReplaceSchemaMap> getReplaceSchemaMap() {
        return OptionalThing.ofNullable(replaceSchemaMap, () -> {
            throw new IllegalStateException("Not found the replaceSchemaMap: " + toString());
        });
    }

    public void setReplaceSchemaMap(ReplaceSchemaMap replaceSchemaModel) {
        this.replaceSchemaMap = replaceSchemaModel;
    }

    public Map<String, SchemaSyncCheckMap> getSchemaSyncCheckMap() {
        return schemaSyncCheckMap;
    }

    public void setSchemaSyncCheckMap(Map<String, SchemaSyncCheckMap> schemaSyncCheckMap) {
        this.schemaSyncCheckMap = schemaSyncCheckMap;
    }
}
