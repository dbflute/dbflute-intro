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
package org.dbflute.intro.app.logic.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectMeta;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.outsidesql.OutsideSqlMap;
import org.dbflute.intro.app.model.client.reps.AdditionalUserMap;
import org.dbflute.intro.app.model.client.reps.ReplaceSchemaMap;
import org.dbflute.intro.app.model.client.reps.SystemUserMap;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.jdbc.Classification;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.util.LaClassificationUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;

    // ===================================================================================
    //                                                                        Project Info
    //                                                                        ============
    public boolean existsClientProject(String project) {
        return new File(introPhysicalLogic.buildClientPath(project)).exists();
    }

    public List<String> getProjectList() {
        final List<String> projectList = new ArrayList<String>();
        final File baseDir = new File(IntroPhysicalLogic.BASE_DIR_PATH);
        if (baseDir.exists()) {
            for (File file : baseDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute_")) {
                    if (!file.getName().contains("dbflute_intro")) {
                        projectList.add(file.getName().substring(8));
                    }
                }
            }
        }
        return projectList;
    }

    // ===================================================================================
    //                                                                    Environment List
    //                                                                    ================
    // TODO jflute intro: unused? (2016/08/02)
    public List<String> getEnvList(String project) {
        final List<String> envList = new ArrayList<String>();
        final File dfpropDir = new File(introPhysicalLogic.buildDfpropDirPath(project));
        for (File file : dfpropDir.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith("schemaSyncCheck_")) {
                envList.add(file.getName().substring("schemaSyncCheck_".length()));
            }
        }
        return envList;
    }

    // ===================================================================================
    //                                                                       ReplaceSchema
    //                                                                       =============
    public boolean existsReplaceSchema(String project) {
        boolean exists = false;
        final File playsqlDir = new File(introPhysicalLogic.buildClientResourcePath(project, "playsql"));
        for (File file : playsqlDir.listFiles()) {
            if (file.isFile() && file.getName().startsWith("replace-schema") && file.getName().endsWith(".sql")) {
                try {
                    if (FileUtils.readFileToString(file, Charsets.UTF_8).trim().length() > 0) {
                        exists = true;
                    }
                } catch (IOException e) {
                    continue;
                }
            }
        }
        return exists;
    }

    // ===================================================================================
    //                                                                        Client Model
    //                                                                        ============
    public OptionalThing<ClientModel> findClient(String clientProject) {
        if (!existsClientProject(clientProject)) {
            return OptionalThing.empty();
        }
        final Map<String, Map<String, Object>> dfpropMap = dfpropInfoLogic.findDfpropMap(clientProject);
        final ClientModel clientModel = newClientModel(clientProject, dfpropMap);
        clientModel.setDocumentMap(prepareDocumentMap(dfpropMap));
        clientModel.setOutsideSqlMap(prepareOutsideSqlMap(dfpropMap));
        clientModel.setReplaceSchemaMap(prepareReplaceSchemaMap(dfpropMap));
        return OptionalThing.of(clientModel);
    }

    private ClientModel newClientModel(String clientProject, Map<String, Map<String, Object>> dfpropMap) {
        final ProjectMeta projectMeta = prepareProjectMeta(clientProject);
        final BasicInfoMap basicInfoMap = prepareBasicInfoMap(dfpropMap);
        final DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(dfpropMap);
        return new ClientModel(projectMeta, basicInfoMap, databaseInfoMap);
    }

    // -----------------------------------------------------
    //                                          Project Core
    //                                          ------------
    private ProjectMeta prepareProjectMeta(String clientProject) {
        return new ProjectMeta(clientProject, prepareJdbcDriverJarPath(clientProject), prepareDBFluteVersion(clientProject));
    }

    private String prepareJdbcDriverJarPath(String clientProject) {
        final File extlibDir = new File(introPhysicalLogic.buildClientResourcePath(clientProject, "extlib"));
        if (extlibDir.exists()) {
            File[] extlibFiles = extlibDir.listFiles();
            for (File file : extlibFiles) {
                if (file.getName().endsWith(".jar")) {
                    return file.getPath();
                }
            }
        }
        return null;
    }

    private String prepareDBFluteVersion(String clientProject) {
        final File projectFile = new File(introPhysicalLogic.buildClientResourcePath(clientProject, "_project.sh"));
        final String data;
        try {
            data = FileUtils.readFileToString(projectFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read the project file: " + projectFile, e);
        }
        final Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        final Matcher matcher = pattern.matcher(data);
        return matcher.find() ? matcher.group(2) : null;
    }

    // -----------------------------------------------------
    //                                            Basic Info
    //                                            ----------
    private BasicInfoMap prepareBasicInfoMap(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> dataMap = dfpropMap.get("basicInfoMap.dfprop");
        final CDef.TargetDatabase databaseType = toCls(CDef.TargetDatabase.class, dataMap, "database");
        final CDef.TargetLanguage languageType = toCls(CDef.TargetLanguage.class, dataMap, "targetLanguage");
        final CDef.TargetContainer containerType = toCls(CDef.TargetContainer.class, dataMap, "targetContainer");
        final String generationPackageBase = required(dataMap, "packageBase");
        return new BasicInfoMap(databaseType, languageType, containerType, generationPackageBase);
    }

    // -----------------------------------------------------
    //                                         Database Info
    //                                         -------------
    private DatabaseInfoMap prepareDatabaseInfoMap(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> dataMap = dfpropMap.get("databaseInfoMap.dfprop");
        final String jdbcDriverFqcn = required(dataMap, "driver");
        final String url = required(dataMap, "url");
        final String schema = prepareSchema(dataMap);
        final String user = required(dataMap, "user");
        final String password = (String) dataMap.get("password");
        return new DatabaseInfoMap(jdbcDriverFqcn, new DbConnectionBox(url, schema, user, password));
    }

    private String prepareSchema(Map<String, Object> databaseInfoMap) {
        String schema = (String) databaseInfoMap.get("schema");
        @SuppressWarnings("unchecked")
        Map<String, Object> variousMap = ((Map<String, Object>) databaseInfoMap.get("variousMap"));
        if (variousMap != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalSchemaMap = ((Map<String, Object>) variousMap.get("additionalSchemaMap"));
            if (additionalSchemaMap != null) {
                Set<String> keySet = additionalSchemaMap.keySet();
                for (String additionalSchema : keySet) {
                    schema += "," + additionalSchema;
                }
            }
        }
        return schema;
    }

    // -----------------------------------------------------
    //                                              Document
    //                                              --------
    private DocumentMap prepareDocumentMap(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> dataMap = dfpropMap.get("documentMap.dfprop");
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setDbCommentOnAliasBasis(Boolean.parseBoolean((String) dataMap.get("isDbCommentOnAliasBasis")));
        documentMap.setAliasDelimiterInDbComment((String) dataMap.get("aliasDelimiterInDbComment"));
        documentMap.setCheckColumnDefOrderDiff(Boolean.parseBoolean((String) dataMap.get("isCheckColumnDefOrderDiff")));
        documentMap.setCheckDbCommentDiff(Boolean.parseBoolean((String) dataMap.get("isCheckDbCommentDiff")));
        documentMap.setCheckProcedureDiff(Boolean.parseBoolean((String) dataMap.get("isCheckProcedureDiff")));
        return documentMap;
    }

    // -----------------------------------------------------
    //                                            OutsideSql
    //                                            ----------
    private OutsideSqlMap prepareOutsideSqlMap(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> dataMap = dfpropMap.get("outsideSqlMap.dfprop");
        final OutsideSqlMap outsideSqlMap = new OutsideSqlMap();
        outsideSqlMap.setGenerateProcedureParameterBean(Boolean.parseBoolean((String) dataMap.get("isGenerateProcedureParameterParam")));
        outsideSqlMap.setProcedureSynonymHandlingType((String) dataMap.get("procedureSynonymHandlingType"));
        return outsideSqlMap;
    }

    // -----------------------------------------------------
    //                                         ReplaceSchema
    //                                         -------------
    private ReplaceSchemaMap prepareReplaceSchemaMap(Map<String, Map<String, Object>> dfpropMap) {
        return new ReplaceSchemaMap(prepareAdditionalUserMap(dfpropMap));
    }

    private AdditionalUserMap prepareAdditionalUserMap(Map<String, Map<String, Object>> dfpropMap) {
        final List<DbConnectionBox> dbConnectionBoxList = Collections.emptyList(); // unused for now
        return new AdditionalUserMap(prepareSystemUserModel(dfpropMap), dbConnectionBoxList);
    }

    private SystemUserMap prepareSystemUserModel(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> replaceSchemaMap = dfpropMap.get("replaceSchemaMap.dfprop");
        if (replaceSchemaMap != null) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> additionalUserMap = (Map<String, Object>) replaceSchemaMap.get("additionalUserMap");
            if (additionalUserMap != null) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> systemUserDatabaseMap = (Map<String, Object>) additionalUserMap.get("system");
                if (systemUserDatabaseMap != null) {
                    final String url = required(systemUserDatabaseMap, "url");
                    final String schema = (String) systemUserDatabaseMap.get("schema");
                    final String user = required(systemUserDatabaseMap, "user");
                    final String password = (String) systemUserDatabaseMap.get("password");
                    final DbConnectionBox dbConnectionModel = new DbConnectionBox(url, schema, user, password);
                    return new SystemUserMap(dbConnectionModel);
                }
            }
        }
        return null;
    }

    // TODO jflute intro: schema sync check handling (2016/08/12)
    //private Map<String, DatabaseInfoMap> prepareSchemaSyncCheckMap(String project) {
    //    final Map<String, DatabaseInfoMap> databaseParamMap = new LinkedHashMap<String, DatabaseInfoMap>();
    //    final File dfpropDir = new File(introPhysicalLogic.toDfpropDirPath(project));
    //    Stream.of(dfpropDir.listFiles()).forEach(file -> {
    //        if (!file.isDirectory() || !file.getName().startsWith("schemaSyncCheck_")) {
    //            return;
    //        }
    //
    //        File documentMapFile = new File(file, "documentMap+.dfprop");
    //        if (!documentMapFile.exists() || !documentMapFile.isFile()) {
    //            documentMapFile = new File(file, "documentDefinitionMap+.dfprop");
    //            return;
    //        }
    //
    //        final DfPropFile dfpropFile = new DfPropFile();
    //        final Map<String, Object> readMap = dfpropFile.readMap(documentMapFile.getAbsolutePath(), null);
    //        @SuppressWarnings("all")
    //        final Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");
    //
    //        DatabaseInfoMap databaseModel = new DatabaseInfoMap();
    //        databaseModel.setUrl((String) schemaSyncCheckMap.get("url"));
    //        databaseModel.setSchema((String) schemaSyncCheckMap.get("schema"));
    //        databaseModel.setUser((String) schemaSyncCheckMap.get("user"));
    //        databaseModel.setPassword((String) schemaSyncCheckMap.get("password"));
    //        databaseParamMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseModel);
    //    });
    //    return databaseParamMap;
    //}

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    @SuppressWarnings("unchecked")
    private <CLS extends Classification> CLS toCls(Class<CLS> cdefType, Map<String, Object> map, String key) {
        return (CLS) LaClassificationUtil.findByCode(cdefType, required(map, key)).get();
    }

    private String required(Map<String, Object> map, String key) {
        final Object value = map.get(key);
        if (value == null) {
            throw new IllegalStateException("Not found the key in the map: key=" + key + ", map=" + map.keySet());
        }
        return (String) value;
    }
}
