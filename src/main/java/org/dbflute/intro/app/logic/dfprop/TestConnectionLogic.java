/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.logic.dfprop;

import java.io.File;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.dbflute.intro.app.logic.engine.EnginePhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.bizfw.tellfailure.DatabaseConnectionException;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author p1us2er0
 * @author jflute
 */
public class TestConnectionLogic {

    private static final Logger logger = LoggerFactory.getLogger(TestConnectionLogic.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private EnginePhysicalLogic enginePhysicalLogic;

    // ===================================================================================
    //                                                                     Test Connection
    //                                                                     ===============
    public void testConnection(String dbfluteVersion, OptionalThing<String> jdbcDriverJarPath, DatabaseInfoMap databaseInfoMap) {
        final ProxySelector proxySelector = ProxySelector.getDefault();
        ProxySelector.setDefault(null);
        Connection connection = null;
        try {
            final Driver driver = prepareJdbcDriver(dbfluteVersion, jdbcDriverJarPath, databaseInfoMap);
            final Properties info = new Properties();
            final DbConnectionBox dbConnectionBox = databaseInfoMap.getDbConnectionBox();
            final String user = dbConnectionBox.getUser();
            if (DfStringUtil.is_NotNull_and_NotEmpty(user)) {
                info.put("user", user);
            }
            final String password = dbConnectionBox.getPassword();
            if (DfStringUtil.is_NotNull_and_NotEmpty(password)) {
                info.put("password", password);
            }
            logger.debug("...Connecting the database: " + databaseInfoMap);
            connection = driver.connect(dbConnectionBox.getUrl(), info);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | MalformedURLException | SQLException e) {
            final String failureHint = e.getClass().getName() + " :: " + e.getMessage();
            throw new DatabaseConnectionException("Failed to test the connection: " + databaseInfoMap, failureHint, e);
        } finally {
            ProxySelector.setDefault(proxySelector);
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    private Driver prepareJdbcDriver(String dbfluteVersion, OptionalThing<String> jdbcDriverJarPath, DatabaseInfoMap databaseInfoMap)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {
        final List<URL> urls = new ArrayList<URL>();
        if (jdbcDriverJarPath.isPresent()) {
            final String jarPath = jdbcDriverJarPath.get();
            final URL fileUrl = new File(jarPath).toURI().toURL();
            urls.add(fileUrl);
        } else {
            final File libDir = enginePhysicalLogic.findLibDir(dbfluteVersion);
            if (libDir.isDirectory()) {
                for (File existingJarFile : FileUtils.listFiles(libDir, FileFilterUtils.suffixFileFilter(".jar"), null)) {
                    try {
                        urls.add(existingJarFile.toURI().toURL());
                    } catch (MalformedURLException e) { // no way
                        throw new IllegalStateException("Failed to create the URL for the jar file: " + existingJarFile.getPath());
                    }
                }
            }
        }
        final URLClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[urls.size()]));
        final String jdbcDriver = databaseInfoMap.getDriver();

        @SuppressWarnings("unchecked")
        final Class<Driver> driverClass = (Class<Driver>) loader.loadClass(jdbcDriver);
        return driverClass.newInstance();
    }
}
