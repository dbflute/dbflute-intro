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
package org.dbflute.intro.app.web.base;

import org.dbflute.intro.mylasta.action.DbfluteIntroMessages;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.db.dbflute.accesscontext.AccessContextResource;
import org.lastaflute.web.login.UserBean;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author p1us2er0
 */
public abstract class DbfluteIntroBaseAction extends DbfluteBaseAction implements LaValidatableApi<DbfluteIntroMessages> {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The application type for Front, e.g. used by access context. */
    protected static final String APP_TYPE = "DbfluteIntro";

    /** The user type for Member, e.g. used by access context. */
    protected static final String USER_TYPE = "None";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========

    // ===================================================================================
    //                                                                            Callback
    //                                                                            ========
    // #app_customize you can customize the god-hand callback
    @Override
    public final void hookFinally(ActionRuntime runtime) {
        super.hookFinally(runtime);
    }

    // ===================================================================================
    //                                                                         My Resource
    //                                                                         ===========
    @Override
    protected String myAppType() { // for framework
        return APP_TYPE;
    }

    /**
     * Get the bean of login user on session. (for application)
     * @return The optional thing of found user bean. (NotNull, EmptyAllowed: when not login)
     */
    @Override
    protected OptionalThing<? extends UserBean<?>> getUserBean() { // application may call
        return OptionalThing.empty();
    }

    @Override
    protected OptionalThing<String> myUserType() { // for framework
        return OptionalThing.of(USER_TYPE); // #app_customize return empty if login is unused
    }

    // ===================================================================================
    //                                                                      Access Context
    //                                                                      ==============
    protected String buildAccessUserTrace(AccessContextResource resource) {
        final StringBuilder sb = new StringBuilder();
        sb.append(resource.getModuleName());
        final String trace = sb.toString();
        final int traceColumnSize = 200;
        if (trace.length() > traceColumnSize) {
            return trace.substring(0, traceColumnSize);
        }
        return trace;
    }

    // ===================================================================================
    //                                                                          Validation
    //                                                                          ==========
    @SuppressWarnings("unchecked")
    @Override
    public ActionValidator<DbfluteIntroMessages> createValidator() {
        return super.createValidator();
    }

    @Override
    public DbfluteIntroMessages createMessages() { // application may call
        return new DbfluteIntroMessages(); // overriding to change return type to concrete-class
    }
}
