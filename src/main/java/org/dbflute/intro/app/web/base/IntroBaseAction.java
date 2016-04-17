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

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.TimeZone;

import javax.annotation.Resource;

import org.dbflute.helper.HandyDate;
import org.dbflute.hook.AccessContext;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.db.dbflute.accesscontext.AccessContextArranger;
import org.lastaflute.db.dbflute.accesscontext.AccessContextResource;
import org.lastaflute.web.TypicalAction;
import org.lastaflute.web.login.LoginManager;
import org.lastaflute.web.login.UserBean;
import org.lastaflute.web.response.ActionResponse;
import org.lastaflute.web.ruts.process.ActionRuntime;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.token.CsrfManager;
import org.lastaflute.web.validation.ActionValidator;
import org.lastaflute.web.validation.LaValidatableApi;

/**
 * @author p1us2er0
 */
public abstract class IntroBaseAction extends TypicalAction implements LaValidatableApi<IntroMessages> {

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
    /** The manager of request. (NotNull) */
    @Resource
    private RequestManager requestManager;

    @Resource
    private CsrfManager csrfManager;

    @Resource
    private IntroConfig introConfig;

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    // to suppress unexpected override by sub-class
    // you should remove the 'final' if you need to override this
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

    // #app_customize you can customize the god-hand callback
    @Override
    public ActionResponse hookBefore(ActionRuntime runtime) { // application may override
        if (!introConfig.isDevelopmentHere()) {
            Method executeMethod = runtime.getExecuteMethod();
            if (runtime.isApiExecute() && !executeMethod.isAnnotationPresent(CsrfTokenVerifySkip.class)) {
                csrfManager.verifyToken();
            }
        }
        return super.hookBefore(runtime);
    }

    @Override
    public void hookFinally(ActionRuntime runtimeMeta) {
        super.hookFinally(runtimeMeta);
    }

    // ===================================================================================
    //                                                                         My Resource
    //                                                                         ===========
    @Override
    protected OptionalThing<LoginManager> myLoginManager() { // for framework
        return OptionalThing.empty(); // #app_customize if empty, login is unused
    }

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
    @Override
    protected AccessContextArranger newAccessContextArranger() { // for framework
        return resource -> {
            final AccessContext context = new AccessContext();
            context.setAccessLocalDateTimeProvider(() -> currentDateTime());
            context.setAccessUserProvider(() -> buildAccessUserTrace(resource));
            context.setAccessProcessProvider(() -> buildAccessProcessTrace(resource));
            return context;
        };
    }

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

    // #app_customize you can customize the user trace style
    private String buildAccessProcessTrace(AccessContextResource resource) {
        final StringBuilder sb = new StringBuilder();
        sb.append(myUserType()).append(":");
        getUserBean().ifPresent(usrBean -> {
            sb.append(usrBean.getUserId());
        }).orElse(() -> {
            sb.append(-1);
        });

        sb.append(",").append(myAppType()).append(",").append(resource.getModuleName());
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
    public ActionValidator<IntroMessages> createValidator() {
        return super.createValidator();
    }

    @Override
    public IntroMessages createMessages() { // application may call
        return new IntroMessages(); // overriding to change return type to concrete-class
    }

    // ===================================================================================
    //                                                                              Paging
    //                                                                              ======
    // #app_customize you can customize the paging navigation logic
    /**
     * Get page size (record count of one page) for paging.
     * @return The integer as page size. (NotZero, NotMinus)
     */
    protected int getPagingPageSize() { // application may call
        return introConfig.getPagingPageSizeAsInteger();
    }

    // ===================================================================================
    //                                                                   Conversion Helper
    //                                                                   =================
    // -----------------------------------------------------
    //                                         to Local Date
    //                                         -------------
    protected LocalDate toLocalDate(String dateExp) { // application may call
        if (dateExp == null || dateExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateExp, getConversionTimeZone()).getLocalDate();
    }

    protected LocalDateTime toLocalDateTime(String dateTimeExp) { // application may call
        if (dateTimeExp == null || dateTimeExp.isEmpty()) {
            return null;
        }
        return new HandyDate(dateTimeExp, getConversionTimeZone()).getLocalDateTime();
    }

    // -----------------------------------------------------
    //                                       to Display Date
    //                                       ---------------
    protected String toDispDate(LocalDate localDate) { // application may call
        return localDate != null ? doConvertToDispDate(localDate) : null;
    }

    protected String toDispDate(LocalDateTime localDateTime) { // application may call
        return localDateTime != null ? doConvertToDispDate(localDateTime) : null;
    }

    private String doConvertToDispDate(LocalDate localDate) {
        return new HandyDate(localDate, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    private String doConvertToDispDate(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDatePattern());
    }

    protected String toDispDateTime(LocalDateTime localDateTime) { // application may call
        return localDateTime != null ? doConvertToDispDateTime(localDateTime) : null;
    }

    private String doConvertToDispDateTime(LocalDateTime localDateTime) {
        return new HandyDate(localDateTime, getConversionTimeZone()).toDisp(getDispDateTimePattern());
    }

    // -----------------------------------------------------
    //                                   Conversion Resource
    //                                   -------------------
    // #app_customize you can customize the date style
    protected String getDispDatePattern() {
        return "yyyy/MM/dd";
    }

    protected String getDispDateTimePattern() {
        return "yyyy/MM/dd HH:mm:ss";
    }

    protected TimeZone getConversionTimeZone() {
        return requestManager.getUserTimeZone();
    }

    protected LocalDateTime toLocalDateTime(LocalDate localDate) {
        return localDate == null ? null : new HandyDate(localDate).getLocalDateTime();
    }
}
