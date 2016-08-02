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
package org.dbflute.intro.mylasta.direction;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.mylasta.direction.sponsor.IntroActionAdjustmentProvider;
import org.dbflute.intro.mylasta.direction.sponsor.IntroApiFailureHook;
import org.dbflute.intro.mylasta.direction.sponsor.IntroCookieResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.IntroCurtainBeforeHook;
import org.dbflute.intro.mylasta.direction.sponsor.IntroSecurityResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.IntroTimeResourceProvider;
import org.dbflute.intro.mylasta.direction.sponsor.IntroUserLocaleProcessProvider;
import org.dbflute.intro.mylasta.direction.sponsor.IntroUserTimeZoneProcessProvider;
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
public class IntroFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String INTRO_CONFIG_FILE = "intro_config.properties";
    public static final String INTRO_ENV_FILE = "intro_env.properties";
    public static final String INTRO_LABEL_NAME = "intro_label";
    public static final String INTRO_MESSAGE_NAME = "intro_message";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    protected IntroConfig introConfig;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void prepareAssistDirection(FwAssistDirection direction) {
        direction.directConfig(nameList -> setupAppConfig(nameList), INTRO_CONFIG_FILE, INTRO_ENV_FILE);
    }

    protected void setupAppConfig(List<String> nameList) {

    }

    // ===================================================================================
    //                                                                                Core
    //                                                                                ====
    @Override
    protected void prepareCoreDirection(FwCoreDirection direction) {
        // this configuration is on intro_env.properties because this is true only when development
        direction.directDevelopmentHere(introConfig.isDevelopmentHere());

        // titles of the application for logging are from configurations
        direction.directLoggingTitle(introConfig.getDomainTitle(), introConfig.getEnvironmentTitle());

        // this configuration is on sea_env.properties because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(introConfig.isFrameworkDebug()); // basically false

        // you can add your own process when your application is booting
        direction.directCurtainBefore(createCurtainBefore());

        direction.directSecurity(createSecurityResourceProvider());
        direction.directTime(createTimeResourceProvider());
    }

    protected IntroCurtainBeforeHook createCurtainBefore() {
        return new IntroCurtainBeforeHook();
    }

    protected IntroSecurityResourceProvider createSecurityResourceProvider() { // #change_it
        final InvertibleCryptographer inver = InvertibleCryptographer.createAesCipher("intro");
        final OneWayCryptographer oneWay = OneWayCryptographer.createSha256Cryptographer();
        return new IntroSecurityResourceProvider(inver, oneWay);
    }

    protected IntroTimeResourceProvider createTimeResourceProvider() {
        return new IntroTimeResourceProvider(introConfig);
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
        direction.directMessage(nameList -> setupAppMessage(nameList), INTRO_LABEL_NAME, INTRO_MESSAGE_NAME);
        direction.directApiCall(createApiFailureHook());
    }

    protected IntroUserLocaleProcessProvider createUserLocaleProcessProvider() {
        return new IntroUserLocaleProcessProvider();
    }

    protected IntroUserTimeZoneProcessProvider createUserTimeZoneProcessProvider() {
        return new IntroUserTimeZoneProcessProvider();
    }

    protected IntroCookieResourceProvider createCookieResourceProvider() { // #change_it
        final InvertibleCryptographer cr = InvertibleCryptographer.createAesCipher("intro");
        return new IntroCookieResourceProvider(introConfig, cr);
    }

    protected IntroActionAdjustmentProvider createActionAdjustmentProvider() {
        return new IntroActionAdjustmentProvider();
    }

    protected ApiFailureHook createApiFailureHook() {
        return new IntroApiFailureHook();
    }

    protected void setupAppMessage(List<String> nameList) {

    };
}
