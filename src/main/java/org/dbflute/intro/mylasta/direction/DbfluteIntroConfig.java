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
package org.dbflute.intro.mylasta.direction;

import org.dbflute.intro.mylasta.direction.DbfluteIntroEnv;
import org.lastaflute.core.direction.exception.ConfigPropertyNotFoundException;

/**
 * @author FreeGen
 */
public interface DbfluteIntroConfig extends DbfluteIntroEnv {

    /** The key of the configuration. e.g. DbfluteIntro */
    String DOMAIN_TITLE = "domain.title";

    /** The key of the configuration. e.g. DbfluteIntro */
    String COOKIE_AUTO_LOGIN_DOCKSIDE_KEY = "cookie.auto.login.dockside.key";

    /**
     * Get the value of property as {@link String}.
     * @param propertyKey The key of the property. (NotNull)
     * @return The value of found property. (NotNull: if not found, exception)
     * @throws ConfigPropertyNotFoundException When the property is not found.
     */
    String get(String propertyKey);

    /**
     * Is the property true?
     * @param propertyKey The key of the property which is boolean type. (NotNull)
     * @return The determination, true or false. (if not found, exception)
     * @throws ConfigPropertyNotFoundException When the property is not found.
     */
    boolean is(String propertyKey);

    /**
     * Get the value for the key 'domain.title'. <br>
     * The value is, e.g. DbfluteIntro <br>
     * comment: @Override The title of domain the application for logging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getDomainTitle();

    /**
     * Get the value for the key 'cookie.auto.login.dockside.key'. <br>
     * The value is, e.g. DbfluteIntro <br>
     * comment: The cookie key of auto-login for Dockside
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getCookieAutoLoginDocksideKey();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends DbfluteIntroEnv.SimpleImpl implements DbfluteIntroConfig {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        @Override
        public String getDomainTitle() {
            return get(DbfluteIntroConfig.DOMAIN_TITLE);
        }

        public String getCookieAutoLoginDocksideKey() {
            return get(DbfluteIntroConfig.COOKIE_AUTO_LOGIN_DOCKSIDE_KEY);
        }
    }
}
