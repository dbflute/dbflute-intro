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

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.mylasta.direction.sponsor.DbfluteActionAdjustmentProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteApiFailureHook;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteCookieResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteCurtainBeforeHook;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteSecurityResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteTimeResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteUserLocaleProcessProvider;
import org.dbflute.intro.mylasta.direction.sponsor.DbfluteUserTimeZoneProcessProvider;
import org.lastaflute.core.direction.CachedFwAssistantDirector;
import org.lastaflute.core.direction.FwAssistDirection;
import org.lastaflute.core.direction.FwCoreDirection;
import org.lastaflute.core.security.InvertibleCryptographer;
import org.lastaflute.core.security.OneWayCryptographer;
import org.lastaflute.db.direction.FwDbDirection;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.direction.FwWebDirection;

/**
 * @author p1us2er0
 */
public abstract class DbfluteFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String DBFLUTE_CONFIG_FILE = "dbflute_config.properties";
    public static final String DBFLUTE_ENV_FILE = "dbflute_env.properties";
    public static final String DBFLUTE_LABEL_NAME = "dbflute_label";
    public static final String DBFLUTE_MESSAGE_NAME = "dbflute_message";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected DbfluteConfig dbfluteConfig;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void prepareAssistDirection(FwAssistDirection direction) {
        direction.directConfig(nameList -> setupAppConfig(nameList), DBFLUTE_CONFIG_FILE, DBFLUTE_ENV_FILE);
    }

    protected abstract void setupAppConfig(List<String> nameList);

    // ===================================================================================
    //                                                                                Core
    //                                                                                ====
    @Override
    protected void prepareCoreDirection(FwCoreDirection direction) {
        // this configuration is on dbflute_env.properties because this is true only when development
        direction.directDevelopmentHere(dbfluteConfig.isDevelopmentHere());

        // titles of the application for logging are from configurations
        direction.directLoggingTitle(dbfluteConfig.getDomainTitle(), dbfluteConfig.getEnvironmentTitle());

        // this configuration is on sea_env.properties because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(dbfluteConfig.isFrameworkDebug()); // basically false

        // you can add your own process when your application is booting
        direction.directCurtainBefore(createCurtainBefore());

        direction.directSecurity(createSecurityResourceProvider());
        direction.directTime(createTimeResourceProvider());
    }

    protected DbfluteCurtainBeforeHook createCurtainBefore() {
        return new DbfluteCurtainBeforeHook();
    }

    protected DbfluteSecurityResourceProvider createSecurityResourceProvider() { // #change_it
        final InvertibleCryptographer inver = InvertibleCryptographer.createAesCipher("dbflute:dbfluteintro");
        final OneWayCryptographer oneWay = OneWayCryptographer.createSha256Cryptographer();
        return new DbfluteSecurityResourceProvider(inver, oneWay);
    }

    protected DbfluteTimeResourceProvider createTimeResourceProvider() {
        return new DbfluteTimeResourceProvider(dbfluteConfig);
    }

    // ===================================================================================
    //                                                                                 DB
    //                                                                                ====

    @Override
    protected void prepareDbDirection(FwDbDirection direction) {

    }

    // ===================================================================================
    //                                                                                Web
    //                                                                               =====
    @Override
    protected void prepareWebDirection(FwWebDirection direction) {
        direction.directRequest(createUserLocaleProcessProvider(), createUserTimeZoneProcessProvider());
        direction.directCookie(createCookieResourceProvider());
        direction.directAdjustment(createActionAdjustmentProvider());
        direction.directMessage(nameList -> setupAppMessage(nameList), DBFLUTE_LABEL_NAME, DBFLUTE_MESSAGE_NAME);
        direction.directApiCall(createApiFailureHook());
    }

    protected DbfluteUserLocaleProcessProvider createUserLocaleProcessProvider() {
        return new DbfluteUserLocaleProcessProvider();
    }

    protected DbfluteUserTimeZoneProcessProvider createUserTimeZoneProcessProvider() {
        return new DbfluteUserTimeZoneProcessProvider();
    }

    protected DbfluteCookieResourceProvider createCookieResourceProvider() { // #change_it
        final InvertibleCryptographer cr = InvertibleCryptographer.createAesCipher("dbfluteintro:dbflute");
        return new DbfluteCookieResourceProvider(dbfluteConfig, cr);
    }

    protected DbfluteActionAdjustmentProvider createActionAdjustmentProvider() {
        return new DbfluteActionAdjustmentProvider();
    }

    protected ApiFailureHook createApiFailureHook() {
        return new DbfluteApiFailureHook();
    }

    protected abstract void setupAppMessage(List<String> nameList);
}
