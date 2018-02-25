/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.web.base;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.context.AccessContextLogic;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.db.dbflute.accesscontext.AccessContextArranger;
import org.lastaflute.web.TypicalAction;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.login.UserBean;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.token.CsrfManager;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author p1us2er0
 * @author jflute
 */
public abstract class IntroBaseAction extends TypicalAction // has several interfaces for direct use
        implements LaValidatableApi<IntroMessages> {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for InTRo, e.g. used by access context. */
    protected static final String APP_TYPE = "ITR";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private CsrfManager csrfManager;
    @Resource
    private IntroConfig config;
    @Resource
    private AccessContextLogic accessContextLogic;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // to suppress unexpected override by sub-class
    @Override
    public final ActionResponse godHandPrologue(ActionRuntime runtime) {
        return super.godHandPrologue(runtime);
    }

    @Override
    public final ActionResponse godHandMonologue(ActionRuntime runtime) {
        return super.godHandMonologue(runtime);
    }

    @Override
    public final void godHandEpilogue(ActionRuntime runtime) {
        super.godHandEpilogue(runtime);
    }

    // #app_customize you can customize the action hook
    @Override
    public ActionResponse hookBefore(ActionRuntime runtime) { // application may override
        // same reason as prepareWebDirection()'s comment out
        //if (!introConfig.isDevelopmentHere()) {
        //    Method executeMethod = runtime.getExecuteMethod();
        //    if (runtime.isApiExecute() && !executeMethod.isAnnotationPresent(CsrfTokenVerifySkip.class)) {
        //        csrfManager.verifyToken();
        //    }
        //}
        return super.hookBefore(runtime);
    }

    @Override
    public void hookFinally(ActionRuntime runtimeMeta) {
        super.hookFinally(runtimeMeta);
    }

    // ===================================================================================
    //                                                                           User Info
    //                                                                           =========
    // -----------------------------------------------------
    //                                      Application Info
    //                                      ----------------
    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    // -----------------------------------------------------
    //                                            Login Info
    //                                            ----------
    // #app_customize return empty if login is unused
    @Override
    protected OptionalThing<? extends UserBean<?>> getUserBean() { // application may call, overriding for co-variant
        return OptionalThing.empty();
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalThing.empty();
    }

    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalThing.empty();
    }

    // ===================================================================================
    //                                                                      Access Context
    //                                                                      ==============
    @Override
    protected AccessContextArranger newAccessContextArranger() { // for framework
        return resource -> {
            return accessContextLogic.create(resource, () -> myAppType());
        };
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<IntroMessages> createValidator() { // for co-variant
        return super.createValidator();
    }

    @Override
    public IntroMessages createMessages() { // application may call
        return new IntroMessages(); // overriding to change return type to concrete-class
    }

    // ===================================================================================
    //                                                                            Document
    //                                                                            ========
    // #app_customize you should override javadoc when you add new methods for sub class at super class.
    ///**
    // * {@inheritDoc} <br>
    // * Application Native Methods:
    // * <pre>
    // * <span style="font-size: 130%; color: #553000">[xxx]</span>
    // * o xxx() <span style="color: #3F7E5E">// xxx</span>
    // * </pre>
    // */
    //@Override
    //public void document1_CallableSuperMethod() {
    //    super.document1_CallableSuperMethod();
    //}
}
