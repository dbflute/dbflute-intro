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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.database.DatabaseInfoLogic;
import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.bizfw.tellfailure.ClientAlreadyExistsException;
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
    @Resource
    private DatabaseInfoLogic databaseInfoLogic;

    // ===================================================================================
    //                                                                       Create Client
    //                                                                       =============
    public void createClient(ClientModel clientModel) { // actually makes e.g. dbflute_maihamadb on filesystem
        IntroAssertUtil.assertNotNull(clientModel);
        final String projectName = clientModel.getProjectInfra().getProjectName();
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
    private void readyCreateClient(ClientModel clientModel, String projectName, File clientDir) {
        if (!clientDir.exists()) { // yes, new-create!
            clientPhysicalLogic.locateUnzippedClient(clientModel.getProjectInfra().getDbfluteVersion(), clientDir);
        } else { // no no no no, already exists
            // done anyone use application excecption by jflute (2021/04/16)
            final String debugMsg = "The DBFlute client already exists (but new-create): projectName=" + projectName;
            throw new ClientAlreadyExistsException(debugMsg, projectName);
        }
    }

    // -----------------------------------------------------
    //                                      Replace Settings
    //                                      ----------------
    private void replaceClientFilePlainly(ClientModel clientModel, String projectName) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        {
            // _project.sh, _project.bat
            final ProjectInfra projectInfra = clientModel.getProjectInfra();
            final Map<String, Object> replaceMap = projectInfra.prepareInitReplaceMap();
            fileReplaceMap.put(clientPhysicalLogic.findProjectBat(projectName), replaceMap);
            fileReplaceMap.put(clientPhysicalLogic.findProjectSh(projectName), replaceMap);
        }
        {
            // build.properties (which is Apache Torque's setting file, defines only one property)
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            replaceMap.put("torque.project = dfclient", "torque.project = " + projectName);
            fileReplaceMap.put(clientPhysicalLogic.findBuildProperties(projectName), replaceMap);
        }
        {
            // basicInfoMap.dfprop
            final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
            final BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
            // #needs_fix jflute move to BasicInfoLogic like DatabaseInfoLogic (2021/10/21)
            replaceMap.put("@database@", basicInfoMap.getDatabase().code());
            replaceMap.put("@targetLanguage@", basicInfoMap.getTargetLanguage().code());
            replaceMap.put("@targetContainer@", basicInfoMap.getTargetContainer().code());
            replaceMap.put("@packageBase@", basicInfoMap.getPackageBase());
            fileReplaceMap.put(clientPhysicalLogic.findDfpropBasicInfoMap(projectName), replaceMap);
        }
        {
            // databaseInfoMap.dfprop
            final DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
            final File databaseInfoFile = clientPhysicalLogic.findDfpropDatabaseInfoMap(projectName);
            final Map<String, Object> initReplaceMap = databaseInfoLogic.prepareInitReplaceMap(databaseInfoMap);
            fileReplaceMap.put(databaseInfoFile, initReplaceMap);
        }
        doReplaceClientFile(fileReplaceMap, /*regularExpression*/false);
    }

    private void replaceClientFileRegex(ClientModel clientModel, final String projectName) {
        final Map<File, Map<String, Object>> fileReplaceMap = new LinkedHashMap<File, Map<String, Object>>();
        final Map<String, Object> replaceMap = new LinkedHashMap<String, Object>();
        replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientModel.getProjectInfra().getDbfluteVersion());
        fileReplaceMap.put(clientPhysicalLogic.findProjectBat(projectName), replaceMap);
        fileReplaceMap.put(clientPhysicalLogic.findProjectSh(projectName), replaceMap);
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
    private void copyJarFileToExtlib(ClientModel clientModel, String projectName) {
        clientModel.getProjectInfra().getJdbcDriverExtlibFile().ifPresent(jarFile -> {
            final File extlibDir = clientPhysicalLogic.findExtlibDir(projectName);
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
        final String projectName = clientModel.getProjectInfra().getProjectName();
        final File clientDir = introPhysicalLogic.findClientDir(projectName);
        // done hakiba 2016/12/27 make readyClient for update
        readyUpdateClient(clientModel, projectName, clientDir);

        // #for_now hakiba update only minimum database items here (for settings), may increase at future (2017/01/19)
        replaceDfpropDatabaseInfoMap(clientModel.getDatabaseInfoMap(), projectName);
    }

    private void readyUpdateClient(ClientModel clientModel, String projectName, File clientDir) {
        if (!clientDir.exists()) { // no no no no, new-create
            throw new IllegalStateException("The DBFlute client has already been deleted: projectName=" + projectName);
        }
    }

    // -----------------------------------------------------
    //                                         Database Info
    //                                         -------------
    private void replaceDfpropDatabaseInfoMap(DatabaseInfoMap databaseInfoMap, String projectName) {
        databaseInfoLogic.replaceDfpropDatabaseInfoMap(databaseInfoMap, projectName);
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
