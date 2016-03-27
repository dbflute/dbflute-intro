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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.Manifest;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteIntroLogic {

    /**
     * <pre>
     * e.g. "."
     *  dbflute-intro
     *   |-dbflute_exampledb // DBFlute client
     *   |-mydbflute         // DBFlute module
     *   |-dbflute-intro.jar
     * </pre>
     */
    public static final String BASE_DIR_PATH = ".";

    public static final String INI_FILE_PATH = BASE_DIR_PATH + "/dbflute-intro.ini";

    public boolean upgrade() {

        File jarPathFile = new File("./dbflute-intro.jar");

        Class<?> clazz = this.getClass();
        URL location = clazz.getResource("/" + clazz.getName().replaceAll("\\.", "/") + ".class");
        String path = location.getPath();

        if (path.lastIndexOf("!") != -1) {
            try {
                jarPathFile = new File(URLDecoder.decode(path.substring("file:/".length(), path.lastIndexOf("!")),
                        Charsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            FileUtils.copyURLToFile(new URL("http://p1us2er0.github.io/dbflute-intro/download/" + jarPathFile.getName()),
                    jarPathFile);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Properties getProperties() {

        File file = new File(DbFluteIntroLogic.INI_FILE_PATH);
        Properties properties = new Properties();
        if (file.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                properties.load(fileInputStream);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(fileInputStream);
            }
        }

        return properties;
    }

    public void loadProxy() {

        System.clearProperty("proxySet");
        System.clearProperty("proxyHost");
        System.clearProperty("proxyPort");

        Properties properties = getProperties();
        String proxyHost = properties.getProperty("proxyHost");
        String proxyPort = properties.getProperty("proxyPort");
        boolean useSystemProxies = Boolean.parseBoolean(properties.getProperty("java.net.useSystemProxies"));

        if (useSystemProxies) {
            System.setProperty("java.net.useSystemProxies", String.valueOf(useSystemProxies));
        } else {
            if (proxyHost != null && !proxyHost.equals("")) {
                System.setProperty("proxySet", "true");
                System.setProperty("proxyHost", proxyHost);
            }

            if (proxyPort != null && !proxyPort.equals("")) {
                System.setProperty("proxyPort", proxyPort);
            }
        }
    }

    public Map<String, Object> getManifestMap() {

        Map<String, Object> manifestMap = new LinkedHashMap<String, Object>();
        InputStream inputStream = null;
        try {

            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = contextClassLoader.getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                inputStream = resources.nextElement().openStream();
                Manifest manifest = new Manifest(inputStream);
                if (!"dbflute-intro".equals(manifest.getMainAttributes().getValue("Implementation-Title"))) {
                    continue;
                }

                for (Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
                    manifestMap.put(String.valueOf(entry.getKey()), entry.getValue());
                }

                break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return manifestMap;
    }

    public String getVersion() {
        return String.valueOf(getManifestMap().get("Implementation-Version"));
    }
}
