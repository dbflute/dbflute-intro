/*
 * Copyright 2014-2017 the original author or authors.
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
 * @author jflute
 */
public class IntroFwAssistantDirector extends CachedFwAssistantDirector {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String INTRO_CONFIG_FILE = "intro_config.properties";
    public static final String INTRO_ENV_FILE = "intro_env.properties";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroConfig config;

    // ===================================================================================
    //                                                                              Assist
    //                                                                              ======
    @Override
    protected void prepareAssistDirection(FwAssistDirection direction) {
        direction.directConfig(nameList -> nameList.add("intro_config.properties"), "intro_env.properties");
    }

    // ===================================================================================
    //                                                                                Core
    //                                                                                ====
    @Override
    protected void prepareCoreDirection(FwCoreDirection direction) {
        // this configuration is on intro_env.properties because this is true only when development
        direction.directDevelopmentHere(config.isDevelopmentHere());

        // titles of the application for logging are from configurations
        direction.directLoggingTitle(config.getDomainTitle(), config.getEnvironmentTitle());

        // this configuration is on sea_env.properties because it has no influence to production
        // even if you set true manually and forget to set false back
        direction.directFrameworkDebug(config.isFrameworkDebug()); // basically false

        // you can add your own process when your application is booting
        direction.directCurtainBefore(createCurtainBefore());

        direction.directSecurity(createSecurityResourceProvider());
        direction.directTime(createTimeResourceProvider());
    }

    protected IntroCurtainBeforeHook createCurtainBefore() {
        return new IntroCurtainBeforeHook();
    }

    protected IntroSecurityResourceProvider createSecurityResourceProvider() { // #changed basically unused
        final InvertibleCryptographer inver = InvertibleCryptographer.createBlowfishCipher("intro");
        final OneWayCryptographer oneWay = OneWayCryptographer.createSha256Cryptographer();
        return new IntroSecurityResourceProvider(inver, oneWay);
    }

    protected IntroTimeResourceProvider createTimeResourceProvider() {
        return new IntroTimeResourceProvider(config);
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
        direction.directMessage(nameList -> nameList.add("intro_message"), "intro_label");
        direction.directApiCall(createApiFailureHook());
        // see IntroBaseAction's hookBefore() for the detail
        //direction.directCors(createCorsHook());
    }

    protected IntroUserLocaleProcessProvider createUserLocaleProcessProvider() {
        return new IntroUserLocaleProcessProvider();
    }

    protected IntroUserTimeZoneProcessProvider createUserTimeZoneProcessProvider() {
        return new IntroUserTimeZoneProcessProvider();
    }

    protected IntroCookieResourceProvider createCookieResourceProvider() { // #changed basically unused
        final InvertibleCryptographer cr = InvertibleCryptographer.createBlowfishCipher("intro");
        return new IntroCookieResourceProvider(config, cr);
    }

    protected IntroActionAdjustmentProvider createActionAdjustmentProvider() {
        return new IntroActionAdjustmentProvider();
    }

    protected ApiFailureHook createApiFailureHook() {
        return new IntroApiFailureHook();
    }

    // see IntroBaseAction's hookBefore() for the detail
    //protected CorsHook createCorsHook() {
    //    return new CorsHook(new BootingInternetDomain(config).toCompleteDomain());
    //}
}
