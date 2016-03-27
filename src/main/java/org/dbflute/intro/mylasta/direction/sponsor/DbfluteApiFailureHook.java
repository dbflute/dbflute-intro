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
package org.dbflute.intro.mylasta.direction.sponsor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.dbflute.intro.mylasta.action.DbfluteIntroMessages;
import org.dbflute.intro.mylasta.bean.ErrorBean;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfCollectionUtil;
import org.dbflute.util.DfStringUtil;
import org.lastaflute.core.json.exception.JsonPropertyDateTimeParseFailureException;
import org.lastaflute.core.json.exception.JsonPropertyNumberParseFailureException;
import org.lastaflute.core.json.exception.JsonPropertyParseFailureException;
import org.lastaflute.core.message.MessageManager;
import org.lastaflute.core.util.ContainerUtil;
import org.lastaflute.web.api.ApiFailureHook;
import org.lastaflute.web.api.ApiFailureResource;
import org.lastaflute.web.exception.ForcedRequest403ForbiddenException;
import org.lastaflute.web.exception.ForcedRequest404NotFoundException;
import org.lastaflute.web.exception.RequestJsonParseFailureException;
import org.lastaflute.web.login.exception.LoginUnauthorizedException;
import org.lastaflute.web.response.ApiResponse;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 */
public class DbfluteApiFailureHook implements ApiFailureHook {

    private static final String GLOBAL_PROPERTY_KEY = DbfluteIntroMessages.GLOBAL_PROPERTY_KEY;

    @Override
    public ApiResponse handleValidationError(ApiFailureResource resource) {
        return createErrorResponse(resource.getPropertyMessageMap(), HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public ApiResponse handleApplicationException(ApiFailureResource resource, RuntimeException cause) {
        Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
        messages.putAll(resource.getPropertyMessageMap());
        if (cause instanceof LoginUnauthorizedException) {
            return createErrorResponse(messages, HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            return createErrorResponse(messages, HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public OptionalThing<ApiResponse> handleClientException(ApiFailureResource resource, RuntimeException cause) {
        if (cause instanceof ForcedRequest403ForbiddenException) {
            Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
            return OptionalThing.of(createErrorResponse(messages, HttpServletResponse.SC_FORBIDDEN));
        }
        if (cause instanceof ForcedRequest404NotFoundException) {
            Map<String, List<String>> messages = DfCollectionUtil.newHashMap();
            return OptionalThing.of(createErrorResponse(messages, HttpServletResponse.SC_NOT_FOUND));
        }

        DbfluteIntroMessages dbfluteIntroMessages = new DbfluteIntroMessages();
        if (cause instanceof RequestJsonParseFailureException) {
            RequestJsonParseFailureException requestJsonParseFailureException = (RequestJsonParseFailureException) cause;
            Throwable parseFailureCause = requestJsonParseFailureException.getCause();
            if (parseFailureCause instanceof JsonPropertyParseFailureException) {
                JsonPropertyParseFailureException jsonPropertyParseFailureException = (JsonPropertyParseFailureException) parseFailureCause;
                String propertyPath = jsonPropertyParseFailureException.getPropertyPath();
                if (parseFailureCause instanceof JsonPropertyNumberParseFailureException) {
                    dbfluteIntroMessages.addAppConverterNumberMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                } else if (parseFailureCause instanceof JsonPropertyDateTimeParseFailureException) {
                    Class<?> propertyType = jsonPropertyParseFailureException.getPropertyType();
                    if (LocalDate.class.isAssignableFrom(propertyType)) {
                        dbfluteIntroMessages.addAppConverterDateMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                    } else if (LocalTime.class.isAssignableFrom(propertyType)) {
                        dbfluteIntroMessages.addAppConverterTimeMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                    } else if (LocalDateTime.class.isAssignableFrom(propertyType)) {
                        dbfluteIntroMessages.addAppConverterDateTimeMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                    } else {
                        dbfluteIntroMessages.addAppConverterValidMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                    }
                } else {
                    dbfluteIntroMessages.addAppConverterValidMessage(DfStringUtil.substringFirstRear(propertyPath, "$."));
                }
            } else {
                dbfluteIntroMessages.addErrorsAppSystemError(GLOBAL_PROPERTY_KEY);
            }
        } else {
            dbfluteIntroMessages.addErrorsAppSystemError(GLOBAL_PROPERTY_KEY);
        }

        Map<String, List<String>> messages =
                geMessageManager().toPropertyMessageMap(resource.getRequestManager().getUserLocale(), dbfluteIntroMessages);
        return OptionalThing.of(createErrorResponse(messages, HttpServletResponse.SC_BAD_REQUEST));
    }

    @Override
    public OptionalThing<ApiResponse> handleServerException(ApiFailureResource resource, Throwable cause) {
        DbfluteIntroMessages dbfluteIntroMessages = new DbfluteIntroMessages();
        dbfluteIntroMessages.addErrorsAppSystemError(GLOBAL_PROPERTY_KEY);
        Map<String, List<String>> messages =
                geMessageManager().toPropertyMessageMap(resource.getRequestManager().getUserLocale(), dbfluteIntroMessages);
        return OptionalThing.of(createErrorResponse(messages, HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
    }

    protected ApiResponse createErrorResponse(Map<String, List<String>> messages, int httpStatus) {
        ErrorBean errorBean = new ErrorBean();
        errorBean.setMessages(messages);
        return new JsonResponse<ErrorBean>(errorBean).httpStatus(httpStatus);
    }

    protected MessageManager geMessageManager() {
        return ContainerUtil.getComponent(MessageManager.class);
    }
}
