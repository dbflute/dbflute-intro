/*
 * Copyright 2014-2020 the original author or authors.
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.filesystem.FileTextIO;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.bizfw.util.IntroAssertUtil;

/**
 * The logic to update DBFlute Client. (e.g. create, update, delete)
 * @author p1us2er0
 * @author jflute
 * @author hakiba
 * @author deco
 * @author cabos
 * @author subaru
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
    //                                                                       Create Client
    //                                                                       =============
    public void createClient(ClientModel clientModel) { // actually makes e.g. dbflute_maihamadb on filesystem
        IntroAssertUtil.assertNotNull(clientModel);
        final String projectName = clientModel.getProjectInfra().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(projectName);
        readyCreateClient(clientModel, projectName, clientDir);
        try {
            replaceClientFilePlainly(clientModel, projectName);
            replaceClientFileRegex(clientModel, projectName);
            copyJarFileToExtlib(clientModel, projectName);

            // #pending: SchemaSyncCheck, Document Options, SystemUser, AdditionalSchema by jflute
        } catch (RuntimeException e) {
            recoveryFailureClient(clientDir);
            throw e;
        }
    }

    // -----------------------------------------------------
    //                                   Ready from Template
    //                                   -------------------
    private void readyCreateClient(ClientModel clientModel, String clientName, File clientDir) {
        if (!clientDir.exists()) { // yes, new-create!
            clientPhysicalLogic.locateUnzippedClient(clientModel.getProjectInfra().getDbfluteVersion(), clientDir);
        } else { // no no no no, already exists
            // #needs_fix anyone use application excecption by jflute (2021/04/16)
            throw new IllegalStateException("The DBFlute client already exists (but new-create): clientName=" + clientName);
        }
    }

    // -----------------------------------------------------
    //                                      Replace Settings
    //                                      ----------------
    private void replaceClientFilePlainly(ClientModel clientModel, String clientName) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        {
            // _project.sh, _project.bat
            final ProjectInfra projectInfra = clientModel.getProjectInfra();
            final Map<String, Object> replaceMap = projectInfra.prepareInitReplaceMap();
            fileReplaceMap.put(clientPhysicalLogic.findProjectBat(clientName), replaceMap);
            fileReplaceMap.put(clientPhysicalLogic.findProjectSh(clientName), replaceMap);
        }
        {
            // build.properties (which is Apache Torque's setting file, defines only one property)
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + clientName);
            fileReplaceMap.put(clientPhysicalLogic.findBuildProperties(clientName), replaceMap);
        }
        {
            // basicInfoMap.dfprop
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            final BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
            replaceMap.put("@database@", basicInfoMap.getDatabase().code());
            replaceMap.put("@targetLanguage@", basicInfoMap.getTargetLanguage().code());
            replaceMap.put("@targetContainer@", basicInfoMap.getTargetContainer().code());
            replaceMap.put("@packageBase@", basicInfoMap.getPackageBase());
            fileReplaceMap.put(clientPhysicalLogic.findDfpropBasicInfoMap(clientName), replaceMap);
        }
        {
            // databaseInfoMap.dfprop
            final DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
            fileReplaceMap.put(databaseInfoMap.findDfpropFile(clientName), databaseInfoMap.prepareInitReplaceMap());
        }
        doReplaceClientFile(fileReplaceMap, /*regularExpression*/false);
    }

    private void replaceClientFileRegex(ClientModel clientModel, final String clientName) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientModel.getProjectInfra().getDbfluteVersion());
        fileReplaceMap.put(clientPhysicalLogic.findProjectBat(clientName), replaceMap);
        fileReplaceMap.put(clientPhysicalLogic.findProjectSh(clientName), replaceMap);
        doReplaceClientFile(fileReplaceMap, /*regularExpression*/true);
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

    // -----------------------------------------------------
    //                                       Extlib Handling
    //                                       ---------------
    private void copyJarFileToExtlib(ClientModel clientModel, String clientName) {
        clientModel.getProjectInfra().getJdbcDriverExtlibFile().ifPresent(jarFile -> {
            final File extlibDir = clientPhysicalLogic.findExtlibDir(clientName);
            final String fileName = jarFile.getFile().getName();
            final File previousJar = new File(extlibDir, fileName);
            try {
                if (!previousJar.exists()) {
                    // all jar files (cannot identity old version file)
                    FileUtils.listFiles(extlibDir, new String[] { ".jar" }, /*recursive*/false).forEach(File::delete);
                    Files.write(Paths.get(extlibDir.getCanonicalPath(), fileName), jarFile.getFileData());
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to copy the jar file to extlib: " + jarFile, e);
            }
        });
    }

    // -----------------------------------------------------
    //                                               Failure
    //                                               -------
    private void recoveryFailureClient(File dbfluteClientDir) {
        try {
            FileUtils.deleteDirectory(dbfluteClientDir);
        } catch (IOException ignored) {}
    }

    // ===================================================================================
    //                                                                       Update Client
    //                                                                       =============
    public void updateClient(ClientModel clientModel) { // updated items are limited, add if it needs
        IntroAssertUtil.assertNotNull(clientModel);
        doUpdateClient(clientModel);
    }

    private void doUpdateClient(ClientModel clientModel) {
        final String projectName = clientModel.getProjectInfra().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(projectName);
        // done hakiba 2016/12/27 make readyClient for update
        readyUpdateClient(clientModel, projectName, clientDir);

        // #for_now hakiba update only minimum database items here (for settings), may increase at future (2017/01/19)
        replaceDfpropDatabaseInfoMap(clientModel, projectName);
    }

    private void readyUpdateClient(ClientModel clientModel, String projectName, File clientDir) {
        if (!clientDir.exists()) { // no no no no, new-create
            throw new IllegalStateException("The DBFlute client has already been deleted: clientName=" + projectName);
        }
    }

    private void replaceDfpropDatabaseInfoMap(ClientModel clientModel, String projectName) {
        final File dfpropDatabaseInfoMap = clientPhysicalLogic.findDfpropDatabaseInfoMap(projectName);

        // #needs_fix anyone switch toString() to getPath() by jflute (2021/04/16)
        // File objects that are made in DBFlute intro uses slack as file separator
        // so you can getPath() here (no problem) however be careful with Windows headache
        // (while, FileTextIO should have rewrite methods that can accept File...?) 
        final String databaseInfoMapPath = dfpropDatabaseInfoMap.toString();
        final DbConnectionBox box = clientModel.getDatabaseInfoMap().getDbConnectionBox();

        // depends on the format of the dbflute_dfclient template (basically no change so almost no problem)
        new FileTextIO().encodeAsUTF8().rewriteFilteringLine(databaseInfoMapPath, line -> {
            String trimmedLine = line.trim(); // for determination
            if (trimmedLine.startsWith("; url") && line.contains("=")) {
                return "    ; url      = " + StringUtils.defaultString(box.getUrl());
            }
            if (trimmedLine.startsWith("; schema") && line.contains("=")) {
                return "    ; schema   = " + StringUtils.defaultString(box.getSchema());
            }
            if (trimmedLine.startsWith("; user") && line.contains("=")) {
                return "    ; user     = " + StringUtils.defaultString(box.getUser());
            }
            if (trimmedLine.startsWith("; password") && line.contains("=")) {
                return "    ; password = " + StringUtils.defaultString(box.getPassword());
            }
            return line;
        });
    }

    // ===================================================================================
    //                                                                       Delete Client
    //                                                                       =============
    public void deleteClient(String projectName) {
        IntroAssertUtil.assertNotEmpty(projectName);
        final File clientDir = introPhysicalLogic.findClientDir(projectName);
        try {
            FileUtils.deleteDirectory(clientDir);
        } catch (IOException e) {
            // #needs_fix anyone should be application exception by jflute (2017/01/19)
            throw new IllegalStateException("Failed to delete the DBFlute client: " + clientDir);
        }
    }
}
