/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.app.logic.simple;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.mylasta.direction.DbfluteConfig;
import org.dbflute.intro.mylasta.util.ZipUtil;
import org.dbflute.util.DfStringUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteEngineLogic {

    public static final String MY_DBFLUTE_PATH = DbFluteIntroLogic.BASE_DIR_PATH + "/mydbflute/dbflute-%1$s";
    private static final String KEY_DBFLUTE_ENGINE_DOWNLOAD_URL = "dbflute.engine.download.url";
    private static final String CLIENT_TEMPLATE_PATH = "/client-template/dbflute_dfclient.zip";

    @Resource
    private DbfluteConfig dbfluteConfig;

    private Properties publicProperties;

    public Properties getPublicProperties() {

        if (publicProperties != null) {
            return publicProperties;
        }

        publicProperties = new Properties();
        try {
            URL url = new URL(dbfluteConfig.getDbflutePublicPropertiesUrl());
            publicProperties.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return publicProperties;
    }

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
        return getPublicProperties().getProperty(KEY_DBFLUTE_ENGINE_DOWNLOAD_URL).replace("$$version$$", dbfluteVersion);
    }

    public void remove(String dbfluteVersion) {
        final File mydbfluteDir = new File(String.format(MY_DBFLUTE_PATH, dbfluteVersion));
        if (mydbfluteDir.exists()) {
            try {
                FileUtils.deleteDirectory(mydbfluteDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> getExistedVersionList() {
        try {
            List<String> list = Files.list(Paths.get(DbFluteIntroLogic.BASE_DIR_PATH, "mydbflute")).filter(file -> {
                return file.toFile().isDirectory() && file.toFile().getName().startsWith("dbflute-");
            }).map(file -> file.toFile().getName().substring(8)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
