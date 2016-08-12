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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.ClientModel;
import org.dbflute.intro.app.model.DatabaseModel;
import org.dbflute.intro.app.model.OptionModel;
import org.dbflute.optional.OptionalThing;

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
        return new File(introPhysicalLogic.toDBFluteClientPath(project)).exists();
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
        final File dfpropDir = new File(introPhysicalLogic.toDfpropDirPath(project));
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
        final File playsqlDir = new File(introPhysicalLogic.toDBFluteClientResourcePath(project, "playsql"));
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
    public OptionalThing<ClientModel> findClient(String project) {
        if (!existsClientProject(project)) {
            return OptionalThing.empty();
        }
        final Map<String, Map<String, Object>> dfpropMap = dfpropInfoLogic.prepareDfpropMap(project);
        final Map<String, Object> basicInfoMap = dfpropMap.get("basicInfoMap.dfprop");
        if (basicInfoMap == null) {
            throw new RuntimeException("Not found the basicInfoMap.dfprop: " + dfpropMap.keySet());
        }
        final Map<String, Object> databaseInfoMap = dfpropMap.get("databaseInfoMap.dfprop");
        if (databaseInfoMap == null) {
            throw new RuntimeException("Not found the databaseInfoMap.dfprop: " + dfpropMap.keySet());
        }
        final ClientModel clientModel = new ClientModel();
        clientModel.setClientProject(project);
        clientModel.setDatabase((String) basicInfoMap.get("database"));
        clientModel.setTargetLanguage((String) basicInfoMap.get("targetLanguage"));
        clientModel.setTargetContainer((String) basicInfoMap.get("targetContainer"));
        clientModel.setPackageBase((String) basicInfoMap.get("packageBase"));
        clientModel.setJdbcDriverFqcn((String) databaseInfoMap.get("driver"));
        clientModel.setJdbcDriverJarPath(prepareJdbcDriverJarPath(project));
        clientModel.setDbfluteVersion(prepareDBFluteVersion(project));
        clientModel.setDatabaseModel(prepareDatabaseModel(databaseInfoMap));
        clientModel.setSystemUserDatabaseModel(prepareSystemUserDatabaseModel(dfpropMap));
        clientModel.setOptionModel(prepareOptionMap(dfpropMap));
        clientModel.setSchemaSyncCheckMap(prepareSchemaSyncCheckMap(project));
        return OptionalThing.of(clientModel);
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

    private String prepareJdbcDriverJarPath(String project) {
        final File extlibDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/extlib");
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

    private String prepareDBFluteVersion(String project) {
        final File projectFile = new File(introPhysicalLogic.toDBFluteClientResourcePath(project, "_project.sh"));
        final String data;
        try {
            data = FileUtils.readFileToString(projectFile);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read the project file: " + projectFile, e);
        }
        final Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        final Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    private DatabaseModel prepareDatabaseModel(Map<String, Object> databaseInfoMap) {
        final DatabaseModel databaseModel = new DatabaseModel();
        databaseModel.setUrl((String) databaseInfoMap.get("url"));
        databaseModel.setSchema(prepareSchema(databaseInfoMap));
        databaseModel.setUser((String) databaseInfoMap.get("user"));
        databaseModel.setPassword((String) databaseInfoMap.get("password"));
        return databaseModel;
    }

    private DatabaseModel prepareSystemUserDatabaseModel(final Map<String, Map<String, Object>> dfpropMap) {
        final DatabaseModel systemUserDatabaseModel = new DatabaseModel();
        final Map<String, Object> replaceSchemaMap = dfpropMap.get("replaceSchemaMap.dfprop");
        if (replaceSchemaMap != null) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> additionalUserMap = (Map<String, Object>) replaceSchemaMap.get("additionalUserMap");
            if (additionalUserMap != null) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> systemUserDatabaseMap = (Map<String, Object>) additionalUserMap.get("system");
                if (systemUserDatabaseMap != null) {
                    systemUserDatabaseModel.setUrl((String) systemUserDatabaseMap.get("url"));
                    systemUserDatabaseModel.setSchema((String) systemUserDatabaseMap.get("schema"));
                    systemUserDatabaseModel.setUser((String) systemUserDatabaseMap.get("user"));
                    systemUserDatabaseModel.setPassword((String) systemUserDatabaseMap.get("password"));
                }
            }
        }
        return systemUserDatabaseModel;
    }

    private OptionModel prepareOptionMap(final Map<String, Map<String, Object>> dfpropMap) {
        final Map<String, Object> documentMap = dfpropMap.get("documentMap.dfprop");
        OptionModel optionModel = new OptionModel();
        if (documentMap != null) {
            optionModel.setDbCommentOnAliasBasis(Boolean.parseBoolean((String) documentMap.get("isDbCommentOnAliasBasis")));
            optionModel.setAliasDelimiterInDbComment((String) documentMap.get("aliasDelimiterInDbComment"));
            optionModel.setCheckColumnDefOrderDiff(Boolean.parseBoolean((String) documentMap.get("isCheckColumnDefOrderDiff")));
            optionModel.setCheckDbCommentDiff(Boolean.parseBoolean((String) documentMap.get("isCheckDbCommentDiff")));
            optionModel.setCheckProcedureDiff(Boolean.parseBoolean((String) documentMap.get("isCheckProcedureDiff")));
        }
        final Map<String, Object> outsideSqlMap = dfpropMap.get("outsideSqlMap.dfprop");
        if (outsideSqlMap != null) {
            optionModel.setGenerateProcedureParameterBean(
                    Boolean.parseBoolean((String) outsideSqlMap.get("isGenerateProcedureParameterParam")));
        }
        return optionModel;
    }

    private Map<String, DatabaseModel> prepareSchemaSyncCheckMap(String project) {
        final Map<String, DatabaseModel> databaseParamMap = new LinkedHashMap<String, DatabaseModel>();
        final File dfpropDir = new File(introPhysicalLogic.toDfpropDirPath(project));
        Stream.of(dfpropDir.listFiles()).forEach(file -> {
            if (!file.isDirectory() || !file.getName().startsWith("schemaSyncCheck_")) {
                return;
            }

            File documentMapFile = new File(file, "documentMap+.dfprop");
            if (!documentMapFile.exists() || !documentMapFile.isFile()) {
                documentMapFile = new File(file, "documentDefinitionMap+.dfprop");
                return;
            }

            final DfPropFile dfpropFile = new DfPropFile();
            final Map<String, Object> readMap = dfpropFile.readMap(documentMapFile.getAbsolutePath(), null);
            @SuppressWarnings("all")
            final Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");

            DatabaseModel databaseModel = new DatabaseModel();
            databaseModel.setUrl((String) schemaSyncCheckMap.get("url"));
            databaseModel.setSchema((String) schemaSyncCheckMap.get("schema"));
            databaseModel.setUser((String) schemaSyncCheckMap.get("user"));
            databaseModel.setPassword((String) schemaSyncCheckMap.get("password"));
            databaseParamMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseModel);
        });
        return databaseParamMap;
    }
}
