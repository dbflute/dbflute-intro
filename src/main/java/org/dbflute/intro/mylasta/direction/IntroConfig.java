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

import org.dbflute.intro.mylasta.direction.IntroEnv;
import org.lastaflute.core.direction.exception.ConfigPropertyNotFoundException;

/**
 * @author FreeGen
 */
public interface IntroConfig extends IntroEnv {

    /** The key of the configuration. e.g. DbfluteIntro */
    String DOMAIN_TITLE = "domain.title";

    /** The key of the configuration. e.g. dbfluteintro */
    String DOMAIN_NAME = "domain.name";

    /** The key of the configuration. e.g. / */
    String COOKIE_DEFAULT_PATH = "cookie.default.path";

    /** The key of the configuration. e.g. 31556926 */
    String COOKIE_DEFAULT_EXPIRE = "cookie.default.expire";

    /** The key of the configuration. e.g. 315360000 */
    String COOKIE_ETERNAL_EXPIRE = "cookie.eternal.expire";

    /** The key of the configuration. e.g. 4 */
    String PAGING_PAGE_SIZE = "paging.page.size";

    /** The key of the configuration. e.g. 3 */
    String PAGING_PAGE_RANGE_SIZE = "paging.page.range.size";

    /** The key of the configuration. e.g. true */
    String PAGING_PAGE_RANGE_FILL_LIMIT = "paging.page.range.fill.limit";

    /** The key of the configuration. e.g. 1930 */
    String MINIMUM_YEAR = "minimum.year";

    /** The key of the configuration. e.g. http://dbflute.org/meta/public.properties */
    String DBFLUTE_PUBLIC_PROPERTIES_URL = "dbflute.public.properties.url";

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
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getDomainTitle();

    /**
     * Get the value for the key 'domain.name'. <br>
     * The value is, e.g. dbfluteintro <br>
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getDomainName();

    /**
     * Get the value for the key 'cookie.default.path'. <br>
     * The value is, e.g. / <br>
     * comment: The default path of cookie (basically '/' if no context path)
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getCookieDefaultPath();

    /**
     * Get the value for the key 'cookie.default.expire'. <br>
     * The value is, e.g. 31556926 <br>
     * comment: The default expire of cookie in seconds e.g. 31556926: one year, 86400: one day
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getCookieDefaultExpire();

    /**
     * Get the value for the key 'cookie.default.expire' as {@link Integer}. <br>
     * The value is, e.g. 31556926 <br>
     * comment: The default expire of cookie in seconds e.g. 31556926: one year, 86400: one day
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getCookieDefaultExpireAsInteger();

    /**
     * Get the value for the key 'cookie.eternal.expire'. <br>
     * The value is, e.g. 315360000 <br>
     * comment: The eternal expire of cookie in seconds e.g. 315360000: ten year, 86400: one day
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getCookieEternalExpire();

    /**
     * Get the value for the key 'cookie.eternal.expire' as {@link Integer}. <br>
     * The value is, e.g. 315360000 <br>
     * comment: The eternal expire of cookie in seconds e.g. 315360000: ten year, 86400: one day
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getCookieEternalExpireAsInteger();

    /**
     * Get the value for the key 'paging.page.size'. <br>
     * The value is, e.g. 4 <br>
     * comment: The size of one page for paging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getPagingPageSize();

    /**
     * Get the value for the key 'paging.page.size' as {@link Integer}. <br>
     * The value is, e.g. 4 <br>
     * comment: The size of one page for paging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getPagingPageSizeAsInteger();

    /**
     * Get the value for the key 'paging.page.range.size'. <br>
     * The value is, e.g. 3 <br>
     * comment: The size of page range for paging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getPagingPageRangeSize();

    /**
     * Get the value for the key 'paging.page.range.size' as {@link Integer}. <br>
     * The value is, e.g. 3 <br>
     * comment: The size of page range for paging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getPagingPageRangeSizeAsInteger();

    /**
     * Get the value for the key 'paging.page.range.fill.limit'. <br>
     * The value is, e.g. true <br>
     * comment: The option 'fillLimit' of page range for paging
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getPagingPageRangeFillLimit();

    /**
     * Is the property for the key 'paging.page.range.fill.limit' true? <br>
     * The value is, e.g. true <br>
     * comment: The option 'fillLimit' of page range for paging
     * @return The determination, true or false. (if not found, exception but basically no way)
     */
    boolean isPagingPageRangeFillLimit();

    /**
     * Get the value for the key 'minimum.year'. <br>
     * The value is, e.g. 1930 <br>
     * comment: The minimum year of this project
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getMinimumYear();

    /**
     * Get the value for the key 'minimum.year' as {@link Integer}. <br>
     * The value is, e.g. 1930 <br>
     * comment: The minimum year of this project
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getMinimumYearAsInteger();

    /**
     * Get the value for the key 'dbflute.public.properties.url'. <br>
     * The value is, e.g. http://dbflute.org/meta/public.properties <br>
     * comment: Dbflute public properties url
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getDbflutePublicPropertiesUrl();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends IntroEnv.SimpleImpl implements IntroConfig {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        public String getDomainTitle() {
            return get(IntroConfig.DOMAIN_TITLE);
        }

        public String getDomainName() {
            return get(IntroConfig.DOMAIN_NAME);
        }

        public String getCookieDefaultPath() {
            return get(IntroConfig.COOKIE_DEFAULT_PATH);
        }

        public String getCookieDefaultExpire() {
            return get(IntroConfig.COOKIE_DEFAULT_EXPIRE);
        }

        public Integer getCookieDefaultExpireAsInteger() {
            return getAsInteger(IntroConfig.COOKIE_DEFAULT_EXPIRE);
        }

        public String getCookieEternalExpire() {
            return get(IntroConfig.COOKIE_ETERNAL_EXPIRE);
        }

        public Integer getCookieEternalExpireAsInteger() {
            return getAsInteger(IntroConfig.COOKIE_ETERNAL_EXPIRE);
        }

        public String getPagingPageSize() {
            return get(IntroConfig.PAGING_PAGE_SIZE);
        }

        public Integer getPagingPageSizeAsInteger() {
            return getAsInteger(IntroConfig.PAGING_PAGE_SIZE);
        }

        public String getPagingPageRangeSize() {
            return get(IntroConfig.PAGING_PAGE_RANGE_SIZE);
        }

        public Integer getPagingPageRangeSizeAsInteger() {
            return getAsInteger(IntroConfig.PAGING_PAGE_RANGE_SIZE);
        }

        public String getPagingPageRangeFillLimit() {
            return get(IntroConfig.PAGING_PAGE_RANGE_FILL_LIMIT);
        }

        public boolean isPagingPageRangeFillLimit() {
            return is(IntroConfig.PAGING_PAGE_RANGE_FILL_LIMIT);
        }

        public String getMinimumYear() {
            return get(IntroConfig.MINIMUM_YEAR);
        }

        public Integer getMinimumYearAsInteger() {
            return getAsInteger(IntroConfig.MINIMUM_YEAR);
        }

        public String getDbflutePublicPropertiesUrl() {
            return get(IntroConfig.DBFLUTE_PUBLIC_PROPERTIES_URL);
        }
    }
}
