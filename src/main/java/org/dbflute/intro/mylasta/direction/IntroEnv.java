/*
 * Copyright 2014-2019 the original author or authors.
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

import org.lastaflute.core.direction.ObjectiveConfig;
import org.lastaflute.core.direction.exception.ConfigPropertyNotFoundException;

/**
 * @author FreeGen
 */
public interface IntroEnv {

    /** The key of the configuration. e.g. hot */
    String lasta_di_SMART_DEPLOY_MODE = "lasta_di.smart.deploy.mode";

    /** The key of the configuration. e.g. true */
    String DEVELOPMENT_HERE = "development.here";

    /** The key of the configuration. e.g. Local Development */
    String ENVIRONMENT_TITLE = "environment.title";

    /** The key of the configuration. e.g. false */
    String FRAMEWORK_DEBUG = "framework.debug";

    /** The key of the configuration. e.g. 0 */
    String TIME_ADJUST_TIME_MILLIS = "time.adjust.time.millis";

    /** The key of the configuration. e.g. debug */
    String LOG_LEVEL = "log.level";

    /** The key of the configuration. e.g. debug */
    String LOG_CONSOLE_LEVEL = "log.console.level";

    /** The key of the configuration. e.g. /tmp/lastaflute/intro */
    String LOG_FILE_BASEDIR = "log.file.basedir";

    /** The key of the configuration. e.g. org.h2.Driver */
    String JDBC_DRIVER = "jdbc.driver";

    /** The key of the configuration. e.g. jdbc:h2:mem:introdb;DB_CLOSE_DELAY=-1 */
    String JDBC_URL = "jdbc.url";

    /** The key of the configuration. e.g. introdb */
    String JDBC_USER = "jdbc.user";

    /** The key of the configuration. e.g. introdb */
    String JDBC_PASSWORD = "jdbc.password";

    /** The key of the configuration. e.g. 10 */
    String JDBC_CONNECTION_POOLING_SIZE = "jdbc.connection.pooling.size";

    /** The key of the configuration. e.g. true */
    String SWAGGER_ENABLED = "swagger.enabled";

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
     * Get the value for the key 'lasta_di.smart.deploy.mode'. <br>
     * The value is, e.g. hot <br>
     * comment: The mode of Lasta Di's smart-deploy, should be cool in production (e.g. hot, cool, warm)
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getLastaDiSmartDeployMode();

    /**
     * Get the value for the key 'development.here'. <br>
     * The value is, e.g. true <br>
     * comment: Is development environment here? (used for various purpose, you should set false if unknown)
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getDevelopmentHere();

    /**
     * Is the property for the key 'development.here' true? <br>
     * The value is, e.g. true <br>
     * comment: Is development environment here? (used for various purpose, you should set false if unknown)
     * @return The determination, true or false. (if not found, exception but basically no way)
     */
    boolean isDevelopmentHere();

    /**
     * Get the value for the key 'environment.title'. <br>
     * The value is, e.g. Local Development <br>
     * comment: The title of environment (e.g. local or integartion or production)
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getEnvironmentTitle();

    /**
     * Get the value for the key 'framework.debug'. <br>
     * The value is, e.g. false <br>
     * comment: Does it enable the Framework internal debug? (true only when emergency)
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getFrameworkDebug();

    /**
     * Is the property for the key 'framework.debug' true? <br>
     * The value is, e.g. false <br>
     * comment: Does it enable the Framework internal debug? (true only when emergency)
     * @return The determination, true or false. (if not found, exception but basically no way)
     */
    boolean isFrameworkDebug();

    /**
     * Get the value for the key 'time.adjust.time.millis'. <br>
     * The value is, e.g. 0 <br>
     * comment: The milliseconds for (relative or absolute) adjust time (set only when test) @LongType *dynamic in development
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getTimeAdjustTimeMillis();

    /**
     * Get the value for the key 'time.adjust.time.millis' as {@link Long}. <br>
     * The value is, e.g. 0 <br>
     * comment: The milliseconds for (relative or absolute) adjust time (set only when test) @LongType *dynamic in development
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not long.
     */
    Long getTimeAdjustTimeMillisAsLong();

    /**
     * Get the value for the key 'log.level'. <br>
     * The value is, e.g. debug <br>
     * comment: The log level for logback
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getLogLevel();

    /**
     * Get the value for the key 'log.console.level'. <br>
     * The value is, e.g. debug <br>
     * comment: The log console level for logback
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getLogConsoleLevel();

    /**
     * Get the value for the key 'log.file.basedir'. <br>
     * The value is, e.g. /tmp/lastaflute/intro <br>
     * comment: The log file basedir
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getLogFileBasedir();

    /**
     * Get the value for the key 'jdbc.driver'. <br>
     * The value is, e.g. org.h2.Driver <br>
     * comment: The driver FQCN to connect database for JDBC
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getJdbcDriver();

    /**
     * Get the value for the key 'jdbc.url'. <br>
     * The value is, e.g. jdbc:h2:mem:introdb;DB_CLOSE_DELAY=-1 <br>
     * comment: The URL of database connection for JDBC
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getJdbcUrl();

    /**
     * Get the value for the key 'jdbc.user'. <br>
     * The value is, e.g. introdb <br>
     * comment: The user of database connection for JDBC
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getJdbcUser();

    /**
     * Get the value for the key 'jdbc.password'. <br>
     * The value is, e.g. introdb <br>
     * comment: @Secure The password of database connection for JDBC
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getJdbcPassword();

    /**
     * Get the value for the key 'jdbc.connection.pooling.size'. <br>
     * The value is, e.g. 10 <br>
     * comment: The (max) pooling size of Seasar's connection pool
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getJdbcConnectionPoolingSize();

    /**
     * Get the value for the key 'jdbc.connection.pooling.size' as {@link Integer}. <br>
     * The value is, e.g. 10 <br>
     * comment: The (max) pooling size of Seasar's connection pool
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     * @throws NumberFormatException When the property is not integer.
     */
    Integer getJdbcConnectionPoolingSizeAsInteger();

    /**
     * Get the value for the key 'swagger.enabled'. <br>
     * The value is, e.g. true <br>
     * comment: is it use swagger in this environment?
     * @return The value of found property. (NotNull: if not found, exception but basically no way)
     */
    String getSwaggerEnabled();

    /**
     * Is the property for the key 'swagger.enabled' true? <br>
     * The value is, e.g. true <br>
     * comment: is it use swagger in this environment?
     * @return The determination, true or false. (if not found, exception but basically no way)
     */
    boolean isSwaggerEnabled();

    /**
     * The simple implementation for configuration.
     * @author FreeGen
     */
    public static class SimpleImpl extends ObjectiveConfig implements IntroEnv {

        /** The serial version UID for object serialization. (Default) */
        private static final long serialVersionUID = 1L;

        public String getLastaDiSmartDeployMode() {
            return get(IntroEnv.lasta_di_SMART_DEPLOY_MODE);
        }

        public String getDevelopmentHere() {
            return get(IntroEnv.DEVELOPMENT_HERE);
        }

        public boolean isDevelopmentHere() {
            return is(IntroEnv.DEVELOPMENT_HERE);
        }

        public String getEnvironmentTitle() {
            return get(IntroEnv.ENVIRONMENT_TITLE);
        }

        public String getFrameworkDebug() {
            return get(IntroEnv.FRAMEWORK_DEBUG);
        }

        public boolean isFrameworkDebug() {
            return is(IntroEnv.FRAMEWORK_DEBUG);
        }

        public String getTimeAdjustTimeMillis() {
            return get(IntroEnv.TIME_ADJUST_TIME_MILLIS);
        }

        public Long getTimeAdjustTimeMillisAsLong() {
            return getAsLong(IntroEnv.TIME_ADJUST_TIME_MILLIS);
        }

        public String getLogLevel() {
            return get(IntroEnv.LOG_LEVEL);
        }

        public String getLogConsoleLevel() {
            return get(IntroEnv.LOG_CONSOLE_LEVEL);
        }

        public String getLogFileBasedir() {
            return get(IntroEnv.LOG_FILE_BASEDIR);
        }

        public String getJdbcDriver() {
            return get(IntroEnv.JDBC_DRIVER);
        }

        public String getJdbcUrl() {
            return get(IntroEnv.JDBC_URL);
        }

        public String getJdbcUser() {
            return get(IntroEnv.JDBC_USER);
        }

        public String getJdbcPassword() {
            return get(IntroEnv.JDBC_PASSWORD);
        }

        public String getJdbcConnectionPoolingSize() {
            return get(IntroEnv.JDBC_CONNECTION_POOLING_SIZE);
        }

        public Integer getJdbcConnectionPoolingSizeAsInteger() {
            return getAsInteger(IntroEnv.JDBC_CONNECTION_POOLING_SIZE);
        }

        public String getSwaggerEnabled() {
            return get(IntroEnv.SWAGGER_ENABLED);
        }

        public boolean isSwaggerEnabled() {
            return is(IntroEnv.SWAGGER_ENABLED);
        }
    }
}
