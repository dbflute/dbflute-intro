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

import org.dbflute.jetty.JettyBoot;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroBoot {

    // TODO jflute intro: default port (2016/08/02)
    protected static final int DEFAULT_PORT = 9000;

    // TODO jflute intro: automatically set lasta.env in production (2016/11/10)
    public static void main(String[] args) { // e.g. java -Dlasta.env=production -jar dbflute-intro.war
        JettyBoot boot = new JettyBoot(getPort(), "/");
        if (isNoneEnv()) { // development
            boot.asDevelopment();
        } else { // production
            boot.browseOnDesktop();
        }
        boot.bootAwait();
    }

    public static int getPort() {
        return Integer.parseInt(System.getProperty("port", String.valueOf(DEFAULT_PORT)));
    }

    private static boolean isNoneEnv() {
        return System.getProperty("lasta.env") == null;
    }
}
