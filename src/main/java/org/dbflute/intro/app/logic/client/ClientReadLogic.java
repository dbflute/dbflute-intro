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
package org.dbflute.intro.app.logic.client;

import java.io.File;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.projectinfra.ClientProjectInfraReadLogic;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropReadLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
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
import org.dbflute.intro.bizfw.util.IntroAssertUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.allcommon.CDef.TargetContainer;
import org.dbflute.intro.dbflute.allcommon.CDef.TargetDatabase;
import org.dbflute.intro.dbflute.allcommon.CDef.TargetLanguage;
import org.dbflute.jdbc.Classification;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.util.LaClassificationUtil;

/**
 * The logic for DBFlute Client information. (e.g. version, dfprop)
 * @author p1us2er0
 * @author jflute
 * @author hakiba
 * @author cabos
 * @author deco
 * @author subaru
 */
public class ClientReadLogic {

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
    private DfpropReadLogic dfpropReadLogic;
    @Resource
    private ClientProjectInfraReadLogic projectInfraReadLogic;

    // ===================================================================================
    //                                                                        Project Info
    //                                                                        ============
    public boolean existsClientProject(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return new File(introPhysicalLogic.buildClientPath(projectName)).exists();
    }

    // done anyone should be getProjectNameList()? by jflute (2021/04/29)
    public List<String> getProjectNameList() { // e.g. [maihamadb, trohamadb], not null
        final List<String> projectList = new ArrayList<String>();
        final File baseDir = introPhysicalLogic.findIntroDir();
        if (baseDir.exists()) {
            for (File file : baseDir.listFiles()) {
                final String clientPrefix = "dbflute_";
                if (file.isDirectory() && file.getName().startsWith(clientPrefix)) {
                    if (!isFrameworkTestClient(file)) {
                        projectList.add(file.getName().substring(clientPrefix.length()));
                    }
                }
            }
        }
        return projectList;
    }

    private boolean isFrameworkTestClient(File file) {
        return file.getName().contains("dbflute_intro"); // e.g. dbflute_introdb in DBFlute Intro project
    }

    // ===================================================================================
    //                                                                       ReplaceSchema
    //                                                                       =============
    public boolean existsReplaceSchema(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        boolean exists = false;
        final File playsqlDir = clientPhysicalLogic.findPlaysqlDir(projectName);
        if (playsqlDir == null) { // playsql directory not found, not impossible
            return false;
        }
        for (File file : playsqlDir.listFiles()) {
            if (isReplaceSchemaDDLFile(file)) {
                try {
                    // empty replace-schema DDL file is treated as no-existence by DBFlute
                    if (existsContentInFile(file)) {
                        exists = true;
                    }
                } catch (UncheckedIOException e) {
                    continue; // #thinking jflute can you throw? (2021/04/16)
                }
            }
        }
        return exists;
    }

    private boolean isReplaceSchemaDDLFile(File file) {
        return file.isFile() && file.getName().startsWith("replace-schema") && file.getName().endsWith(".sql");
    }

    private boolean existsContentInFile(File file) {
        return flutyFileLogic.readFile(file).trim().length() > 0;
    }

    // ===================================================================================
    //                                                                        Client Model
    //                                                                        ============
    public OptionalThing<ClientModel> findClient(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        try {
            return doFindClient(projectName);
        } catch (RuntimeException e) {
            throw new ClientReadFailureException("Failed to find DBFlute client: " + projectName, e);
        }
    }

    private OptionalThing<ClientModel> doFindClient(String projectName) {
        if (!existsClientProject(projectName)) {
            return OptionalThing.ofNullable(null, () -> {
                throw new IllegalStateException("Not found the client project: " + projectName);
            });
        }
        // // map:{ [file-name] = map:{ [dfprop key-values] } }
        final Map<String, Map<String, Object>> dfpropMap = dfpropReadLogic.findDfpropMap(projectName);
        final ClientModel clientModel = newClientModel(projectName, dfpropMap);
        clientModel.setDocumentMap(prepareDocumentMap(dfpropMap));
        clientModel.setOutsideSqlMap(prepareOutsideSqlMap(dfpropMap));
        clientModel.setReplaceSchemaMap(prepareReplaceSchemaMap(dfpropMap));
        return OptionalThing.of(clientModel);
    }

    private ClientModel newClientModel(String projectName, Map<String, Map<String, Object>> dfpropMap) {
        final ProjectInfra projectInfra = prepareProjectInfra(projectName);
        final BasicInfoMap basicInfoMap = prepareBasicInfoMap(dfpropMap);
        final DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(dfpropMap);
        return new ClientModel(projectInfra, basicInfoMap, databaseInfoMap);
    }

    // -----------------------------------------------------
    //                                         Project Infra
    //                                         -------------
    private ProjectInfra prepareProjectInfra(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        return projectInfraReadLogic.prepareProjectInfra(projectName);
    }

    // -----------------------------------------------------
    //                                            Basic Info
    //                                            ----------
    private BasicInfoMap prepareBasicInfoMap(Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> dataMap = dfpropMap.get(BasicInfoMap.DFPROP_NAME);

        final CDef.TargetDatabase databaseType = toClsTargetDatabase(dataMap);
        final CDef.TargetLanguage languageType = toClsTargetLanguage(dataMap);
        final CDef.TargetContainer containerType = toClsTargetContainer(dataMap);

        final String generationPackageBase = required(dataMap, "packageBase");
        return new BasicInfoMap(databaseType, languageType, containerType, generationPackageBase);
    }

    // support implicit default value of DBFlute here by jflute (2019/10/24)
    // (e.g. targetLanguage and targetContainer may be omitted in non-generate client)

    private TargetDatabase toClsTargetDatabase(Map<String, Object> dataMap) {
        return toClsOrDefault(CDef.TargetDatabase.class, dataMap, "database", null); // required so no default
    }

    private TargetLanguage toClsTargetLanguage(Map<String, Object> dataMap) {
        return toClsOrDefault(CDef.TargetLanguage.class, dataMap, "targetLanguage", CDef.TargetLanguage.Java);
    }

    private TargetContainer toClsTargetContainer(Map<String, Object> dataMap) {
        return toClsOrDefault(CDef.TargetContainer.class, dataMap, "targetContainer", CDef.TargetContainer.LastaDi);
    }

    // -----------------------------------------------------
    //                                         Database Info
    //                                         -------------
    private DatabaseInfoMap prepareDatabaseInfoMap(Map<String, Map<String, Object>> dfpropMap) {
        // according to DBFlute specification
        // http://dbflute.seasar.org/ja/manual/reference/dfprop/databaseinfo/index.html
        final Map<String, Object> dataMap = dfpropMap.get(DatabaseInfoMap.DFPROP_NAME);
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
                    // #for_now see the class code
                    schemaBoxMap.put(additionalSchema, new AdditionalSchemaBox(additionalSchema));
                }
            }
        }
        return new AdditionalSchemaMap(schemaBoxMap);
    }

    // -----------------------------------------------------
    //                                              Document
    //                                              --------
    private DocumentMap prepareDocumentMap(Map<String, Map<String, Object>> dfpropMap) { // null allowed
        // according to DBFlute specification
        // http://dbflute.seasar.org/ja/manual/reference/dfprop/documentdefinition/index.html
        final Map<String, Object> dataMap = dfpropMap.get("documentMap.dfprop"); // already resolved about "Definition" suffix here
        // If documentMap.dfprop does not exist, it returns null, because it is not required.
        if (dataMap == null) {
            return null;
        }

        // only used-in-intro properties for now
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
        // according to DBFlute specification
        // http://dbflute.seasar.org/ja/manual/reference/dfprop/outsidesqldefinition/index.html
        final Map<String, Object> dataMap = dfpropMap.get("outsideSqlMap.dfprop"); // already resolved about "Definition" suffix here
        // If outsideSqlMap.dfprop does not exist, it returns null, because it is not required.
        if (dataMap == null) {
            return null;
        }

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
        // according to DBFlute specification
        // http://dbflute.seasar.org/ja/manual/reference/dfprop/replaceschemadefinition/index.html
        final Map<String, Object> replaceSchemaMap = dfpropMap.get("replaceSchemaMap.dfprop"); // already resolved about "Definition" suffix here
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
    // defaultCls is null allowed: exception if not found
    @SuppressWarnings("unchecked")
    private <CLS extends Classification> CLS toClsOrDefault(Class<CLS> cdefType, Map<String, Object> map, String key, CLS defaultCls) {
        final String requiredCode = orDefault(map, key, defaultCls != null ? defaultCls.code() : null); // default may be null (then completely required)
        return (CLS) LaClassificationUtil.findByCode(cdefType, requiredCode).get();
    }

    // following methods are for dfprop values
    private String required(Map<String, Object> map, String key) {
        return orDefault(map, key, null);
    }

    private String orDefault(Map<String, Object> map, String key, Object defaultValue) { // default may be null (then completely required)
        final Object value = map.getOrDefault(key, defaultValue);
        if (value == null) {
            throw new ClientDfPropKeyRequiredException("Not found the key in the map: key=" + key + ", map=" + map.keySet());
        }
        return (String) value;
    }

    // ===================================================================================
    //                                                                           Exception
    //                                                                           =========
    private static class ClientReadFailureException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ClientReadFailureException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    private static class ClientDfPropKeyRequiredException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public ClientDfPropKeyRequiredException(String msg) {
            super(msg);
        }
    }
}
