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
package org.dbflute.intro.app.logic.client;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ExtlibFile;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap.AdditionalSchemaBox;
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
 * @author hakiba
 */
public class ClientInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private FlutyFileLogic flutyFileLogic;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private ClientPhysicalLogic clientPhysicalLogic;
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
    //                                                                       ReplaceSchema
    //                                                                       =============
    public boolean existsReplaceSchema(String clientProject) {
        boolean exists = false;
        final File playsqlDir = clientPhysicalLogic.findPlaysqlDir(clientProject);
        for (File file : playsqlDir.listFiles()) {
            if (file.isFile() && file.getName().startsWith("replace-schema") && file.getName().endsWith(".sql")) {
                try {
                    if (flutyFileLogic.readFile(file).trim().length() > 0) {
                        exists = true;
                    }
                } catch (UncheckedIOException e) {
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
            return OptionalThing.ofNullable(null, () -> {
                throw new IllegalStateException("Not found the client project: " + clientProject);
            });
        }
        final Map<String, Map<String, Object>> dfpropMap = dfpropInfoLogic.findDfpropMap(clientProject);
        final ClientModel clientModel = newClientModel(clientProject, dfpropMap);
        clientModel.setDocumentMap(prepareDocumentMap(dfpropMap));
        clientModel.setOutsideSqlMap(prepareOutsideSqlMap(dfpropMap));
        clientModel.setReplaceSchemaMap(prepareReplaceSchemaMap(dfpropMap));
        return OptionalThing.of(clientModel);
    }

    private ClientModel newClientModel(String clientProject, Map<String, Map<String, Object>> dfpropMap) {
        final ProjectInfra projectInfra = prepareProjectMeta(clientProject);
        final BasicInfoMap basicInfoMap = prepareBasicInfoMap(dfpropMap);
        final DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(dfpropMap);
        return new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
    }

    // -----------------------------------------------------
    //                                          Project Core
    //                                          ------------
    private ProjectInfra prepareProjectMeta(String clientProject) {
        return new ProjectInfra(clientProject, prepareDBFluteVersion(clientProject), prepareJdbcDriverExtlibFile(clientProject));
    }

    private String prepareDBFluteVersion(String clientProject) {
        final File projectFile = new File(introPhysicalLogic.buildClientPath(clientProject, "_project.sh"));
        final String data = flutyFileLogic.readFile(projectFile);
        final Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        final Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(2);
        } else {
            throw new IllegalStateException("Not found the DBFlute version in _project.sh: " + projectFile);
        }
    }

    // TODO hakiba confirm allow findFirst by hakiba (2018/04/11)
    private ExtlibFile prepareJdbcDriverExtlibFile(String clientProject) {
        final File extlibDir = clientPhysicalLogic.findExtlibDir(clientProject);
        if (!extlibDir.exists()) {
            return null;
        }
        return FileUtils.listFiles(extlibDir, new String[] { ".jar" }, false)
                .stream()
                .filter(file -> file.getName().endsWith(".jar"))
                .findFirst()
                .map(ExtlibFile::new)
                .orElse(null);
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
        final String schema = (String) dataMap.get("schema");
        final String user = required(dataMap, "user");
        final String password = (String) dataMap.get("password");
        final AdditionalSchemaMap additionalSchemaMap = prepareAdditionalSchemaMap(dataMap);
        return new DatabaseInfoMap(jdbcDriverFqcn, new DbConnectionBox(url, schema, user, password), additionalSchemaMap);
    }

    private AdditionalSchemaMap prepareAdditionalSchemaMap(Map<String, Object> dataMap) {
        final Map<String, AdditionalSchemaBox> schemaBoxMap = new LinkedHashMap<>();
        @SuppressWarnings("unchecked")
        Map<String, Object> variousMap = ((Map<String, Object>) dataMap.get("variousMap"));
        if (variousMap != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalSchemaMap = ((Map<String, Object>) variousMap.get("additionalSchemaMap"));
            if (additionalSchemaMap != null) {
                for (String additionalSchema : additionalSchemaMap.keySet()) {
                    // #pending see the class code
                    schemaBoxMap.put(additionalSchema, new AdditionalSchemaBox(additionalSchema));
                }
            }
        }
        return new AdditionalSchemaMap(schemaBoxMap);
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
                    final String url = (String) systemUserDatabaseMap.get("url");
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
