/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.app.web;

import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.lastaflute.meta.SwaggerGenerator;
import org.lastaflute.meta.agent.SwaggerAgent;
import org.lastaflute.meta.swagger.web.LaActionSwaggerable;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.HtmlResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * @author cabos
 */
public class SwaggerAction extends IntroBaseAction implements LaActionSwaggerable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String _apiPrefix = "api/";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private RequestManager requestManager;
    @Resource
    private IntroConfig config;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public HtmlResponse index() {
        verifySwaggerAllowed();
        String swaggerJsonUrl = toActionUrl(SwaggerAction.class, moreUrl("json"));
        return new SwaggerAgent(requestManager).prepareSwaggerUiResponse(swaggerJsonUrl);
    }

    @Execute
    public JsonResponse<Map<String, Object>> json() {
        verifySwaggerAllowed();
        return asJson(new SwaggerGenerator().generateSwaggerMap(op -> {
            op.derivedBasePath(basePath -> basePath + _apiPrefix);
        }));
    }

    private void verifySwaggerAllowed() { // also check in ActionAdjustmentProvider
        verifyOrClientError("Swagger is not enabled.", config.isSwaggerEnabled());
    }
}
