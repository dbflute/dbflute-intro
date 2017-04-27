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
import java.nio.file.Files;
import java.nio.file.Paths;
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
    //                                                                       Create Client
    //                                                                       =============
    public void createClient(ClientModel clientModel) {
        final String clientProject = clientModel.getProjectInfra().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        readyCreateClient(clientModel, clientProject, clientDir);
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

    private void readyCreateClient(ClientModel clientModel, String clientProject, File clientDir) {
        if (!clientDir.exists()) { // yes, new-create!
            clientPhysicalLogic.locateUnzippedClient(clientModel.getProjectInfra().getDbfluteVersion(), clientDir);
        } else { // no no no no, already exists
            // #hope to be application excecption by jflute
            throw new IllegalStateException("The DBFlute client already exists (but new-create): clientProject=" + clientProject);
        }
    }

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
        replaceMap.put("((?:set|export) DBFLUTE_HOME=[^-]*-)(.*)", "$1" + clientModel.getProjectInfra().getDbfluteVersion());
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

    private void copyJarFileToExtlib(ClientModel clientModel, String clientProject) {
        clientModel.getProjectInfra().getJdbcDriverExtlibFile().ifPresent(jarFile -> {
            final File extlibDir = clientPhysicalLogic.findExtlibDir(clientProject);
            final String fileName = jarFile.getFile().getName();
            final File previousJar = new File(extlibDir, fileName);
            try {
                if (!previousJar.exists()) {
                    // all jar files (cannot identity old version file)
                    FileUtils.listFiles(extlibDir, new String[] { ".jar" }, false).forEach(File::delete);
                    Files.write(Paths.get(extlibDir.getCanonicalPath(), fileName), jarFile.getFileData());
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to copy the jar file to extlib: " + jarFile, e);
            }
        });
    }

    private void recoveryFailureClient(File dbfluteClientDir) {
        try {
            FileUtils.deleteDirectory(dbfluteClientDir);
        } catch (IOException ignored) {}
    }

    // ===================================================================================
    //                                                                       Update Client
    //                                                                       =============
    public void updateClient(ClientModel clientModel) {
        doUpdateClient(clientModel);
    }

    private void doUpdateClient(ClientModel clientModel) {
        final String clientProject = clientModel.getProjectInfra().getClientProject();
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        // done hakiba 2016/12/27 make readyClient for update
        readyUpdateClient(clientModel, clientProject, clientDir);

        // #for_now hakiba update only minimum database items here (for settings), may increase at future (2017/01/19)
        replaceDfpropDatabaseInfoMap(clientModel, clientProject);
    }

    private void readyUpdateClient(ClientModel clientModel, String clientProject, File clientDir) {
        if (!clientDir.exists()) { // no no no no, new-create
            throw new IllegalStateException("The DBFlute client has already been deleted: clientProject=" + clientProject);
        }
    }

    private void replaceDfpropDatabaseInfoMap(ClientModel clientModel, String clientProject) {
        final File dfpropDatabaseInfoMap = clientPhysicalLogic.findDfpropDatabaseInfoMap(clientProject);
        final String databaseInfoMapPath = dfpropDatabaseInfoMap.toString();
        final DbConnectionBox box = clientModel.getDatabaseInfoMap().getDbConnectionBox();

        new FileTextIO().encodeAsUTF8().rewriteFilteringLine(databaseInfoMapPath, line -> {
            String trimmedLine = line.trim();
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
    public void deleteClient(String clientProject) {
        final File clientDir = introPhysicalLogic.findClientDir(clientProject);
        try {
            FileUtils.deleteDirectory(clientDir);
        } catch (IOException e) {
            // #for_now should be application exception by jflute (2017/01/19)
            throw new IllegalStateException("Failed to delete the DBFlute client: " + clientDir);
        }
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private String escapeControlMark(Object value) {
        return new MapListString().escapeControlMark(value);
    }
}
