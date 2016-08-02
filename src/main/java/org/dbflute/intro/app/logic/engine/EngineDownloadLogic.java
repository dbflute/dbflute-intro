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
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.intro.mylasta.util.ZipUtil;
import org.dbflute.util.DfStringUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EngineDownloadLogic {

    public static final String MY_DBFLUTE_PATH = IntroPhysicalLogic.BASE_DIR_PATH + "/mydbflute/dbflute-%1$s";
    private static final String KEY_DBFLUTE_ENGINE_DOWNLOAD_URL = "dbflute.engine.download.url";
    private static final String CLIENT_TEMPLATE_PATH = "/client-template/dbflute_dfclient.zip";

    @Resource
    private IntroConfig introConfig;
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;

    public void download(String dbfluteVersion) {
        if (DfStringUtil.is_Null_or_TrimmedEmpty(dbfluteVersion)) {
            return;
        }

        final String downloadUrl = calcDownloadUrl(dbfluteVersion);
        final File mydbfluteDir = new File(String.format(MY_DBFLUTE_PATH, dbfluteVersion));
        mydbfluteDir.getParentFile().mkdirs();

        final Path zipFile = Paths.get(mydbfluteDir.getAbsolutePath() + ".zip");
        try (InputStream inputStream = new URL(downloadUrl).openStream()){
            Files.copy(inputStream, zipFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mydbfluteDir.mkdirs();
        ZipUtil.decrypt(zipFile.toFile().getPath(), mydbfluteDir.getAbsolutePath());
        FileUtils.deleteQuietly(zipFile.toFile());

        final String templateZipFileName = mydbfluteDir.getAbsolutePath() + "/etc" + CLIENT_TEMPLATE_PATH;
        final String templateExtractDirectoryBase = mydbfluteDir.getAbsolutePath() + CLIENT_TEMPLATE_PATH;
        ZipUtil.decrypt(templateZipFileName, templateExtractDirectoryBase);
    }

    protected String calcDownloadUrl(String dbfluteVersion) {
        return publicPropertiesLogic.extractProperties().getProperty(KEY_DBFLUTE_ENGINE_DOWNLOAD_URL).replace("$$version$$", dbfluteVersion);
    }
}
