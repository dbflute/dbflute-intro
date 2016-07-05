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
package org.dbflute.intro.app.logic.core;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import javax.annotation.Resource;

import org.dbflute.intro.mylasta.direction.IntroConfig;

/**
 * @author p1us2er0
 * @author jflute
 */
public class PublicPropertiesLogic {

    // TODO jflute intro: need to cache?
    private static Properties publicProperties;

    @Resource
    private IntroConfig introConfig;

    public Properties extractProperties() {
        // TODO jflute intro: want to use DfPublicProperties (2016/07/05)
        //DfPublicProperties prop = new DfPublicProperties();
        //prop.load();
        if (publicProperties != null) {
            return publicProperties;
        }
        synchronized (PublicPropertiesLogic.class) {
            if (publicProperties != null) {
                return publicProperties;
            }
            loadProperties();
            return publicProperties;
        }
    }

    private void loadProperties() {
        publicProperties = new Properties();
        String propertiesUrl = introConfig.getDbflutePublicPropertiesUrl();
        try {
            URL url = new URL(propertiesUrl);
            publicProperties.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load the properties: URL=" + propertiesUrl, e);
        }
    }
}
