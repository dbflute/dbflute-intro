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
package org.dbflute.intro;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.Manifest;

import org.dbflute.jetty.JettyBoot;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroBoot {

    private static final String LASTA_ENV_KEY = "lasta.env";
    private static final int DEFAULT_PORT = 8926;

    public static void main(String[] args) { // e.g. java -Dlasta.env=production -jar dbflute-intro.war
        automaticallySetupProduction();
        JettyBoot boot = new JettyBoot(getPort(), "/");
        if (isDevelopment()) { // development
            boot.asDevelopment();
        } else { // production
            boot.browseOnDesktop();
        }
        boot.bootAwait();
    }

    private static void automaticallySetupProduction() {
        if (hasManifest() && isDevelopment()) { // booting by war without lasta.env
            System.setProperty(LASTA_ENV_KEY, "production"); // automatically set to simple booting
        }
    }

    public static int getPort() {
        return Integer.parseInt(System.getProperty("port", String.valueOf(DEFAULT_PORT)));
    }

    private static boolean isDevelopment() {
        return System.getProperty(LASTA_ENV_KEY) == null;
    }

    // ===================================================================================
    //                                                                            Manifest
    //                                                                            ========
    private static boolean hasManifest() {
        return !getManifestMap().isEmpty();
    }

    public static Map<String, Object> getManifestMap() { // copied from IntroInfoLogic
        String manifestPath = "META-INF/MANIFEST.MF";
        Map<String, Object> manifestMap = new LinkedHashMap<String, Object>();
        InputStream inputStream = null;
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = contextClassLoader.getResources(manifestPath);
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
            throw new IllegalStateException("Failed to get manifest file: " + manifestPath, e);
        }
        return manifestMap;
    }
}
