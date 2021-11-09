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
package org.dbflute.intro.app.logic.engine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.exception.PublicPropertiesLoadingFailureException;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.util.ZipUtil;
import org.dbflute.util.DfStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The logic for DBFlute Engine install.
 * @author p1us2er0
 * @author jflute
 */
public class EngineInstallLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Logger logger = LoggerFactory.getLogger(EngineInstallLogic.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;

    // ===================================================================================
    //                                                                            Download
    //                                                                            ========
    public boolean isDownloaded(String dbfluteVersion) {
        final File engineDir = introPhysicalLogic.findEngineDir(dbfluteVersion);
        return engineDir.exists();
    }

    public void downloadUnzipping(String dbfluteVersion, boolean useSystemProxies) throws PublicPropertiesLoadingFailureException { // overriding if already exists
        if (DfStringUtil.is_Null_or_TrimmedEmpty(dbfluteVersion)) {
            throw new IllegalArgumentException("dbfluteVersion is null or empty: " + dbfluteVersion);
        }
        logger.debug("...Downloading DBflute Engine: {}", dbfluteVersion);
        final String downloadUrl = publicPropertiesLogic.findProperties(useSystemProxies).getDBFluteDownloadUrl(dbfluteVersion);
        final File engineDir = introPhysicalLogic.findEngineDir(dbfluteVersion);
        engineDir.getParentFile().mkdirs(); // make 'mydbflute' directory
        final Path zipFile = doDownloadToZip(downloadUrl, engineDir);
        engineDir.mkdirs(); // make 'engine' directory e.g. mydbflute/dbflute-1.1.1
        logger.debug("...Unzipping DBflute Engine: {}", zipFile.getFileName());
        ZipUtil.decrypt(zipFile.toFile().getPath(), engineDir.getAbsolutePath());
        FileUtils.deleteQuietly(zipFile.toFile());
    }

    private Path doDownloadToZip(String downloadUrl, File engineDir) {
        final Path zipFile = Paths.get(engineDir.getAbsolutePath() + ".zip");
        try (InputStream ins = new URL(downloadUrl).openStream()) {
            Files.copy(ins, zipFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy the downloaded data to zip file: " + zipFile, e);
        }
        return zipFile;
    }

    // ===================================================================================
    //                                                                       Remove Engine
    //                                                                       =============
    public void remove(String engineVersion) {
        final File engineDir = introPhysicalLogic.findEngineDir(engineVersion);
        if (engineDir.exists()) {
            try {
                FileUtils.deleteDirectory(engineDir);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to delete the engine: " + engineDir, e);
            }
        }
    }
}
