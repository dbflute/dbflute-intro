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
package org.dbflute.intro.mylasta.direction.sponsor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.Srl;
import org.lastaflute.core.message.MessageManager;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.api.BusinessFailureMapping;
import org.lastaflute.web.login.exception.LoginFailureException;
import org.lastaflute.web.login.exception.LoginRequiredException;
import org.lastaflute.web.login.exception.LoginUnauthorizedException;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.RequestManager;
import org.lastaflute.web.validation.Required;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroApiFailureHook implements ApiFailureHook {

    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // [Front-side Implementation Image]
    //
    // if (HTTP Status: 200) { // success
    //     XxxJsonBean bean = parseJsonAsSuccess(response);
    //     ...(do process per action)
    // } else if (HTTP Status: 400) { // e.g. validation error, application exception, client exception
    //     FailureBean bean = parseJsonAsFailure(response);
    //     ...(show bean.messageList or do process per bean.failureType)
    // } else if (HTTP Status: 404) { // e.g. real not found, invalid parameter
    //     showNotFoundError();
    // } else { // basically 500, server exception
    //     showSystemError();
    // }
    // _/_/_/_/_/_/_/_/_/_/

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected static final int BUSINESS_FAILURE_STATUS = HttpServletResponse.SC_BAD_REQUEST;
    protected static final BusinessFailureMapping<ApiFailureType> failureTypeMapping; // for application exception

    static { // you can add mapping of failure type with exception
        failureTypeMapping = new BusinessFailureMapping<ApiFailureType>(failureMap -> {
            failureMap.put(LoginFailureException.class, ApiFailureType.LOGIN_FAILURE);
            failureMap.put(LoginRequiredException.class, ApiFailureType.LOGIN_REQUIRED);
        });
    }

    // ===================================================================================
    //                                                                    Validation Error
    //                                                                    ================
    @Override
    public ApiResponse handleValidationError(ApiFailureResource resource) {
        final ApiFailureBean bean = createFailureBean(ApiFailureType.VALIDATION_ERROR, resource);
        return asJson(bean).httpStatus(BUSINESS_FAILURE_STATUS);
    }

    // ===================================================================================
    //                                                               Application Exception
    //                                                               =====================
    @Override
    public ApiResponse handleApplicationException(ApiFailureResource resource, RuntimeException cause) {
        final ApiFailureType failureType = failureTypeMapping.findAssignable(cause).orElseGet(() -> {
            return ApiFailureType.APPLICATION_EXCEPTION;
        });
        final ApiFailureBean bean = createFailureBean(failureType, resource);
        return asJson(bean).httpStatus(prepareApplicationExceptionHttpStatus(cause));
    }

    private int prepareApplicationExceptionHttpStatus(RuntimeException cause) {
        if (cause instanceof LoginUnauthorizedException) {
            return HttpServletResponse.SC_UNAUTHORIZED;
        } else {
            return BUSINESS_FAILURE_STATUS;
        }
    }

    // ===================================================================================
    //                                                                    Client Exception
    //                                                                    ================
    @Override
    public OptionalThing<ApiResponse> handleClientException(ApiFailureResource resource, RuntimeException cause) {
        // HTTP status will be automatically sent as client error for the cause
        return OptionalThing.of(asJson(createFailureBean(ApiFailureType.CLIENT_EXCEPTION, resource)));
    }

    // ===================================================================================
    //                                                                    Server Exception
    //                                                                    ================
    @Override
    public OptionalThing<ApiResponse> handleServerException(ApiFailureResource resource, Throwable cause) {
        // HTTP status will be automatically sent as server error
        final Map<String, List<String>> messageMap = prepareServerExceptionMessageMap(resource, cause);
        return OptionalThing.of(asJson(createFailureBean(ApiFailureType.SERVER_EXCEPTION, messageMap)));
    }

    private Map<String, List<String>> prepareServerExceptionMessageMap(ApiFailureResource resource, Throwable cause) {
        final IntroMessages messages = new IntroMessages();
        final String errorMessage = buildDisplayErrorMessage(cause);
        messages.addErrorsAppIntroError(IntroMessages.GLOBAL_PROPERTY_KEY, errorMessage);
        final RequestManager requestManager = resource.getRequestManager();
        final MessageManager messageManager = requestManager.getMessageManager();
        return messageManager.toPropertyMessageMap(requestManager.getUserLocale(), messages);
    }

    private String buildDisplayErrorMessage(Throwable cause) {
        final String causeName = cause.getClass().getName();
        final String causeMessage = cause.getMessage();
        final String stackTraceExp = buildOneLinerStackTraceExp(cause);
        return causeName + ": " + causeMessage + " - " + stackTraceExp; // one liner for JSON view
    }

    private String buildOneLinerStackTraceExp(Throwable cause) {
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (StackTraceElement element : cause.getStackTrace()) {
            if (index >= 5) { // recent elements only
                break;
            }
            final String simpleName = Srl.substringLastRear(element.getClassName(), ".");
            final String methodName = element.getMethodName();
            final String fileName = element.getFileName();
            final int lineNumber = element.getLineNumber();
            sb.append(" at ").append(simpleName).append(".").append(methodName);
            sb.append("(").append(fileName).append(":").append(lineNumber).append(")");
            ++index;
        }
        return sb.toString();
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    protected JsonResponse<ApiFailureBean> asJson(ApiFailureBean bean) {
        return new JsonResponse<ApiFailureBean>(bean);
    }

    protected ApiFailureBean createFailureBean(ApiFailureType failureType, ApiFailureResource resource) {
        return new ApiFailureBean(failureType, resource.getPropertyMessageMap());
    }

    protected ApiFailureBean createFailureBean(ApiFailureType failureType, Map<String, List<String>> messageMap) {
        return new ApiFailureBean(failureType, messageMap);
    }

    public static class ApiFailureBean {

        @Required
        public final ApiFailureType failureType;
        @NotNull
        public final Map<String, List<String>> messages;

        public ApiFailureBean(ApiFailureType failureType, Map<String, List<String>> messages) {
            this.failureType = failureType;
            this.messages = messages;
        }
    }

    public static enum ApiFailureType {
        VALIDATION_ERROR // special type
        , LOGIN_FAILURE, LOGIN_REQUIRED // specific type of application exception
        , APPLICATION_EXCEPTION // default type of application exception
        , CLIENT_EXCEPTION, SERVER_EXCEPTION
    }
}
