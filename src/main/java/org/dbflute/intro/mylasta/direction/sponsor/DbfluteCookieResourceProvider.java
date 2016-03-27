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
package org.dbflute.intro.mylasta.direction.sponsor;

import org.dbflute.intro.mylasta.direction.DbfluteConfig;
import org.lastaflute.core.security.InvertibleCryptographer;
import org.lastaflute.web.servlet.cookie.CookieResourceProvider;

/**
 * @author p1us2er0
 */
public class DbfluteCookieResourceProvider implements CookieResourceProvider {

    protected final DbfluteConfig dbfluteConfig;
    protected final InvertibleCryptographer cookieCipher;

    public DbfluteCookieResourceProvider(DbfluteConfig dbfluteConfig, InvertibleCryptographer cookieCipher) {
        this.dbfluteConfig = dbfluteConfig;
        this.cookieCipher = cookieCipher;
    }

    public String provideDefaultPath() {
        return dbfluteConfig.getCookieDefaultPath();
    }

    public Integer provideDefaultExpire() {
        return dbfluteConfig.getCookieDefaultExpireAsInteger();
    }

    public InvertibleCryptographer provideCipher() {
        return cookieCipher;
    }
}
