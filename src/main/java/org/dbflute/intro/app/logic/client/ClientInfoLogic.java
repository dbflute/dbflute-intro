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
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author p1us2er0
 * @author jflute
 */
public class ClientInfoLogic {

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                        Project List
    //                                                                        ============
    public List<String> getProjectList() {
        List<String> list = new ArrayList<String>();
        final File baseDir = new File(IntroPhysicalLogic.BASE_DIR_PATH);
        if (baseDir.exists()) {
            for (File file : baseDir.listFiles()) {
                if (file.isDirectory() && file.getName().startsWith("dbflute_")) {
                    if (!file.getName().contains("dbflute_intro")) {
                        list.add(file.getName().substring(8));
                    }
                }
            }
        }
        return list;
    }

    // ===================================================================================
    //                                                                    Environment List
    //                                                                    ================
    // TODO jflute intro: unused? (2016/08/02)
    public List<String> getEnvList(String project) {
        List<String> envList = new ArrayList<String>();
        File dfpropDir = new File(introPhysicalLogic.toDfpropDirPath(project));
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
    public boolean existsReplaceSchemaFile(String project) {
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
    //                                                                              Dfprop
    //                                                                              ======
    public ClientModel convertDfpropToClientParam(String project) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<String, Map<String, Object>>();
        File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");

        Stream.of(dfpropDir.listFiles()).forEach(file -> {
            if (!file.getName().endsWith("Map.dfprop")) {
                return;
            }

            String fileNameKey = file.getName().replace("DefinitionMap.dfprop", "Map.dfprop");
            DfPropFile dfPropFile = new DfPropFile();
            map.put(fileNameKey, dfPropFile.readMap(file.getAbsolutePath(), null));

            File plusFile = new File(file.getName().replace("Map.dfprop", "Map+.dfprop"));
            if (plusFile.exists()) {
                map.get(fileNameKey).putAll(dfPropFile.readMap(plusFile.getAbsolutePath(), null));
            }
        });

        Map<String, Object> databaseInfoMap = map.get("databaseInfoMap.dfprop");
        Map<String, Object> basicInfoMap = map.get("basicInfoMap.dfprop");
        if (databaseInfoMap == null || basicInfoMap == null) {
            throw new RuntimeException("dbflute client is invalid.");
        }
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

        ClientModel clientParam = new ClientModel();
        clientParam.setProject(project);
        clientParam.setTargetLanguage((String) basicInfoMap.get("targetLanguage"));
        clientParam.setTargetContainer((String) basicInfoMap.get("targetContainer"));
        clientParam.setPackageBase((String) basicInfoMap.get("packageBase"));
        clientParam.setDatabase((String) basicInfoMap.get("database"));
        clientParam.setJdbcDriver((String) databaseInfoMap.get("driver"));
        clientParam.getDatabaseParam().setUrl((String) databaseInfoMap.get("url"));
        clientParam.getDatabaseParam().setSchema(schema);
        clientParam.getDatabaseParam().setUser((String) databaseInfoMap.get("user"));
        clientParam.getDatabaseParam().setPassword((String) databaseInfoMap.get("password"));
        if (map.get("replaceSchemaMap.dfprop") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> additionalUserMap = (Map<String, Object>) map.get("replaceSchemaMap.dfprop").get("additionalUserMap");
            if (additionalUserMap != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> systemUserDatabaseMap = (Map<String, Object>) additionalUserMap.get("system");
                if (systemUserDatabaseMap != null) {
                    clientParam.getSystemUserDatabaseParam().setUrl((String) systemUserDatabaseMap.get("url"));
                    clientParam.getSystemUserDatabaseParam().setSchema((String) systemUserDatabaseMap.get("schema"));
                    clientParam.getSystemUserDatabaseParam().setUser((String) systemUserDatabaseMap.get("user"));
                    clientParam.getSystemUserDatabaseParam().setPassword((String) systemUserDatabaseMap.get("password"));
                }
            }
        }
        File extlibDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/extlib");
        if (extlibDir.exists()) {
            File[] extlibFiles = extlibDir.listFiles();
            for (File file : extlibFiles) {
                if (file.getName().endsWith(".jar")) {
                    clientParam.setJdbcDriverJarPath(file.getPath());
                }
            }
        }

        File projectFile = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/_project.bat");
        String data = null;
        try {
            data = FileUtils.readFileToString(projectFile);
        } catch (IOException e) {
            new RuntimeException(e);
        }

        Pattern pattern = Pattern.compile("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            clientParam.setDbfluteVersion(matcher.group(2));
        }

        Map<String, Object> documentMap = map.get("documentMap.dfprop");
        OptionParam optionParam = clientParam.getOptionParam();
        if (documentMap != null) {
            optionParam.setDbCommentOnAliasBasis(Boolean.parseBoolean((String) documentMap.get("isDbCommentOnAliasBasis")));
            optionParam.setAliasDelimiterInDbComment((String) documentMap.get("aliasDelimiterInDbComment"));
            optionParam.setCheckColumnDefOrderDiff(Boolean.parseBoolean((String) documentMap.get("isCheckColumnDefOrderDiff")));
            optionParam.setCheckDbCommentDiff(Boolean.parseBoolean((String) documentMap.get("isCheckDbCommentDiff")));
            optionParam.setCheckProcedureDiff(Boolean.parseBoolean((String) documentMap.get("isCheckProcedureDiff")));
        }

        Map<String, Object> outsideSqlMap = map.get("outsideSqlMap.dfprop");
        if (outsideSqlMap != null) {
            optionParam.setGenerateProcedureParameterBean(
                    Boolean.parseBoolean((String) outsideSqlMap.get("isGenerateProcedureParameterParam")));
        }

        Map<String, DatabaseParam> schemaSyncCheckMap = clientParam.getSchemaSyncCheckMap();
        schemaSyncCheckMap.putAll(convertDfpropToDatabaseParamMap(project));

        return clientParam;
    }

    private Map<String, DatabaseParam> convertDfpropToDatabaseParamMap(String project) {
        Map<String, DatabaseParam> databaseParamMap = new LinkedHashMap<String, DatabaseParam>();
        File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        Stream.of(dfpropDir.listFiles()).forEach(file -> {
            if (!file.isDirectory() || !file.getName().startsWith("schemaSyncCheck_")) {
                return;
            }

            File documentMapFile = new File(file, "documentMap+.dfprop");
            if (!documentMapFile.exists() || !documentMapFile.isFile()) {
                documentMapFile = new File(file, "documentDefinitionMap+.dfprop");
                return;
            }

            DfPropFile dfPropFile = new DfPropFile();
            Map<String, Object> readMap = dfPropFile.readMap(documentMapFile.getAbsolutePath(), null);
            @SuppressWarnings("all")
            Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");

            DatabaseParam databaseParam = new DatabaseParam();
            databaseParam.setUrl((String) schemaSyncCheckMap.get("url"));
            databaseParam.setSchema((String) schemaSyncCheckMap.get("schema"));
            databaseParam.setUser((String) schemaSyncCheckMap.get("user"));
            databaseParam.setPassword((String) schemaSyncCheckMap.get("password"));
            databaseParamMap.put(file.getName().replace("schemaSyncCheck_", ""), databaseParam);
        });

        return databaseParamMap;
    }
}
