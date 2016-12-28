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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.helper.mapstring.MapListString;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.eclipse.jetty.util.StringUtil;

/**
 * @author p1us2er0
 * @author jflute
 * @author hakiba
 */
public class ClientUpdateLogic {

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
    private EnginePhysicalLogic enginePhysicalLogic;

    // ===================================================================================
    //                                                                Create/Update Client
    //                                                                ====================
    public void createClient(ClientModel clientParam) {
        doCreateClient(clientParam, false);
    }

    private void doCreateClient(ClientModel clientModel, boolean update) {
        final String clientProject = clientModel.getProjectMeta().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        readyClient(clientModel, update, clientProject, clientDir);
        try {
            replaceClientFilePlainly(clientModel, clientProject);
            replaceClientFileRegex(clientModel, clientProject);
            copyJarFileToExtlib(clientModel, clientProject);

            // #pending: SchemaSyncCheck, Document Options, SystemUser, AdditionalSchema by jflute
        } catch (RuntimeException e) {
            recoveryFailureClient(clientDir);
            throw e;
        }
    }

    public void updateClient(ClientModel clientParam) {
        doUpdateClient(clientParam);
    }

    private void doUpdateClient(ClientModel clientModel) {
        final String clientProject = clientModel.getProjectMeta().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        // TODO: 2016/12/27 make readyClient for update by hakiba
        readyClient(clientModel, true, clientProject, clientDir);

        replaceDfpropDatabaseInfoMap(clientModel, clientProject);
    }

    // -----------------------------------------------------
    //                                          Ready Client
    //                                          ------------
    private void readyClient(ClientModel clientModel, boolean update, String clientProject, File clientDir) {
        if (!clientDir.exists()) { // new-create
            if (update) {
                throw new IllegalStateException("The DBFlute client has already been deleted: clientProject=" + clientProject);
            }
            clientPhysicalLogic.locateUnzippedClient(clientModel.getProjectMeta().getDbfluteVersion(), clientDir);
        } else { // already exists so update
            if (!update) {
                throw new IllegalStateException("The DBFlute client already exists (but new-create): clientProject=" + clientProject);
            }
        }
    }

    // -----------------------------------------------------
    //                                   Replace Client File
    //                                   -------------------
    private void replaceClientFilePlainly(ClientModel clientModel, String clientProject) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        {
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("MY_PROJECT_NAME=dfclient", "MY_PROJECT_NAME=" + clientProject);
            fileReplaceMap.put(clientPhysicalLogic.findProjectBat(clientProject), replaceMap);
            fileReplaceMap.put(clientPhysicalLogic.findProjectSh(clientProject), replaceMap);
        }
        {
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + clientProject);
            fileReplaceMap.put(clientPhysicalLogic.findBuildProperties(clientProject), replaceMap);
        }
        {
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            final BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
            replaceMap.put("@database@", basicInfoMap.getDatabase().code());
            replaceMap.put("@targetLanguage@", basicInfoMap.getTargetLanguage().code());
            replaceMap.put("@targetContainer@", basicInfoMap.getTargetContainer().code());
            replaceMap.put("@packageBase@", basicInfoMap.getPackageBase());
            fileReplaceMap.put(clientPhysicalLogic.findDfpropBasicInfoMap(clientProject), replaceMap);
        }
        {
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            final DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
            final DbConnectionBox connectionBox = databaseInfoMap.getDbConnectionBox();
            replaceMap.put("@driver@", escapeControlMark(databaseInfoMap.getDriver()));
            replaceMap.put("@url@", escapeControlMark(connectionBox.getUrl()));
            replaceMap.put("@schema@", escapeControlMark(connectionBox.getSchema()));
            replaceMap.put("@user@", escapeControlMark(connectionBox.getUser()));
            replaceMap.put("@password@", escapeControlMark(connectionBox.getPassword()));
            fileReplaceMap.put(clientPhysicalLogic.findDfpropDatabaseInfoMap(clientProject), replaceMap);
        }
        doReplaceClientFile(fileReplaceMap, false);
    }

    private void replaceClientFileRegex(ClientModel clientModel, final String clientProject) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientModel.getProjectMeta().getDbfluteVersion());
        fileReplaceMap.put(clientPhysicalLogic.findProjectBat(clientProject), replaceMap);
        fileReplaceMap.put(clientPhysicalLogic.findProjectSh(clientProject), replaceMap);
        doReplaceClientFile(fileReplaceMap, true);
    }

    private void doReplaceClientFile(Map<File, Map<String, Object>> fileMap, boolean regularExpression) {
        for (Entry<File, Map<String, Object>> entry : fileMap.entrySet()) {
            final File clientFile = entry.getKey();
            if (!clientFile.exists()) { // possible when e.g. update, #hope lazy make from client templates
                throw new IllegalStateException("Not found the client file: " + clientFile);
            }
            String replacedText = flutyFileLogic.readFile(clientFile);
            for (Entry<String, Object> replaceEntry : entry.getValue().entrySet()) {
                final String key = replaceEntry.getKey();
                final String value = String.valueOf(replaceEntry.getValue() == null ? "" : replaceEntry.getValue());
                replacedText = regularExpression ? replacedText.replaceAll(key, value) : replacedText.replace(key, value);
            }
            flutyFileLogic.writeFile(clientFile, replacedText);
        }
    }

    private void replaceDfpropDatabaseInfoMap(ClientModel clientModel, String clientProject) {
        doReplaceDfpropDatabaseInfoMap(clientModel, clientProject);
    }

    private void doReplaceDfpropDatabaseInfoMap(ClientModel clientModel, String clientProject) {
        final File dfpropDatabaseInfoMap = clientPhysicalLogic.findDfpropDatabaseInfoMap(clientProject);
        final String databaseInfoMapPath = dfpropDatabaseInfoMap.toString();
        final DbConnectionBox dbConnectionBox = clientModel.getDatabaseInfoMap().getDbConnectionBox();

        new FileTextIO().encodeAsUTF8().rewriteFilteringLine(databaseInfoMapPath, line -> {
            if (line.trim().startsWith("; url      =")) {
                return "    ; url      = " + StringUtils.defaultString(dbConnectionBox.getUrl());
            }
            if (line.trim().startsWith("; schema   =")) {
                return "    ; schema   = " + StringUtils.defaultString(dbConnectionBox.getSchema());
            }
            if (line.trim().startsWith("; user     =")) {
                return "    ; user     = " + StringUtils.defaultString(dbConnectionBox.getUser());
            }
            if (line.trim().startsWith("; password =")) {
                return "    ; password = " + StringUtils.defaultString(dbConnectionBox.getPassword());
            }
            return line;
        });
    }

    // -----------------------------------------------------
    //                                              Jar File
    //                                              --------
    private void copyJarFileToExtlib(ClientModel clientModel, String clientProject) {
        clientModel.getProjectMeta().getJdbcDriverJarPath().ifPresent(jarPath -> {
            final File extlibDir = clientPhysicalLogic.findExtlibDir(clientProject);
            final File previousJar = new File(extlibDir, new File(jarPath).getName());
            try {
                final boolean alreadyExists =
                        previousJar.exists() && new File(jarPath).getCanonicalPath().equals(previousJar.getCanonicalPath());
                if (!alreadyExists) {
                    for (File existingJarFile : FileUtils.listFiles(extlibDir, new String[] { ".jar" }, false)) {
                        existingJarFile.delete(); // all jar files (cannot identity old version file)
                    }
                    FileUtils.copyFileToDirectory(new File(jarPath), extlibDir);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to copy the jar file to extlib: " + jarPath, e);
            }
        });
    }

    // -----------------------------------------------------
    //                                          Assist Logic
    //                                          ------------
    private String escapeControlMark(Object value) {
        return new MapListString().escapeControlMark(value);
    }

    private void recoveryFailureClient(File dbfluteClientDir) {
        try {
            FileUtils.deleteDirectory(dbfluteClientDir);
        } catch (IOException ignored) {}
    }

    // #pending SchemaSyncCheck by jflute
    //    private void createSchemaSyncCheck(ClientModel clientParam) {
    //        final File dbfluteClientDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + clientParam.getClientProject());
    //        URL schemaSyncCheckURL = ClassLoader.getSystemResource("dfprop/documentMap+schemaSyncCheck.dfprop");
    //
    //        for (Entry<String, DatabaseInfoMap> entry : clientParam.getSchemaSyncCheckMap().entrySet()) {
    //            final File dfpropEnvDir = new File(dbfluteClientDir, "dfprop/schemaSyncCheck_" + entry.getKey());
    //            dfpropEnvDir.mkdir();
    //
    //            File documentMapFile = new File(dfpropEnvDir, "documentMap+.dfprop");
    //            try {
    //                FileUtils.copyURLToFile(schemaSyncCheckURL, documentMapFile);
    //            } catch (IOException e) {
    //                throw new RuntimeException(e);
    //            }
    //
    //            Map<File, Map<String, Object>> fileMap = new LinkedHashMap<File, Map<String, Object>>();
    //
    //            Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
    //            replaceMap.put("@url@", escapeControlMark(entry.getValue().getUrl()));
    //            replaceMap.put("@schema@", escapeControlMark(entry.getValue().getSchema()));
    //            replaceMap.put("@user@", escapeControlMark(entry.getValue().getUser()));
    //            replaceMap.put("@password@", escapeControlMark(entry.getValue().getPassword()));
    //            replaceMap.put("@env@", escapeControlMark(entry.getKey()));
    //            fileMap.put(documentMapFile, replaceMap);
    //
    //            replaceFile(fileMap, false);
    //        }
    //    }

    // #pending option and system user by jflute
    //            OptionModel optionParam = clientModel.getOptionModel();
    //
    //            replaceMap = new LinkedHashMap<String, Object>();
    //            replaceMap.put("@isDbCommentOnAliasBasis@", optionParam.isDbCommentOnAliasBasis());
    //            replaceMap.put("@aliasDelimiterInDbComment@", optionParam.getAliasDelimiterInDbComment());
    //            replaceMap.put("@isCheckColumnDefOrderDiff@", optionParam.isCheckColumnDefOrderDiff());
    //            replaceMap.put("@isCheckDbCommentDiff@", optionParam.isCheckDbCommentDiff());
    //            replaceMap.put("@isCheckProcedureDiff@", optionParam.isCheckProcedureDiff());
    //            fileMap.put(new File(dbfluteClientDir, "/dfprop/documentMap+.dfprop"), replaceMap);
    //
    //            replaceMap = new LinkedHashMap<String, Object>();
    //            replaceMap.put("@isGenerateProcedureParameterParam@", optionParam.isGenerateProcedureParameterBean());
    //            replaceMap.put("@procedureSynonymHandlingType@", optionParam.getProcedureSynonymHandlingType());
    //            fileMap.put(new File(dbfluteClientDir, "/dfprop/outsideSqlMap+.dfprop"), replaceMap);
    //
    //            replaceMap = new LinkedHashMap<String, Object>();
    //            replaceMap.put("@driver@", escapeControlMark(clientModel.getDriver()));
    //            replaceMap.put("@url@", escapeControlMark(clientModel.getSystemUserDatabaseModel().getUrl()));
    //            replaceMap.put("@schema@", escapeControlMark(clientModel.getSystemUserDatabaseModel().getSchema()));
    //            replaceMap.put("@user@", escapeControlMark(clientModel.getSystemUserDatabaseModel().getUser()));
    //            replaceMap.put("@password@", escapeControlMark(clientModel.getSystemUserDatabaseModel().getPassword()));
    //            fileMap.put(new File(dbfluteClientDir, "/dfprop/replaceSchemaMap+.dfprop"), replaceMap);
    //
    //            createSchemaSyncCheck(clientModel);

    // ===================================================================================
    //                                                                       Delete Client
    //                                                                       =============
    public void deleteClient(String clientProject) {
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        try {
            FileUtils.deleteDirectory(clientDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to delete the DBFlute client: " + clientDir);
        }
    }
}
