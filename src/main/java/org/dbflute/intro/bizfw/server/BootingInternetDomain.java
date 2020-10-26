/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.bizfw.server;

import org.dbflute.intro.IntroBoot;

/**
 * @author jflute at showbase
 * @author subaru
 */
public class BootingInternetDomain {

    private final String serverDomain;
    private final int port;

    public BootingInternetDomain() {
        this.serverDomain = IntroBoot.getServerHost();
        this.port = IntroBoot.getPort();
    }

    public String toCompleteDomain() { // with trailing slash
        return buildCompleteDomain("");
    }

    public String toCompleteApiDomain() { // with trailing slash
        return buildCompleteDomain("api/");
    }

    private String buildCompleteDomain(String rearElement) {
        final String portSuffix = buildPortSuffix();
        return "http://" + serverDomain + portSuffix + "/" + rearElement;
    }

    private String buildPortSuffix() {
        return "localhost".equals(serverDomain) ? ":" + port : "";
    }
}
