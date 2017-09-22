package org.dbflute.intro.app.web;

import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.doc.SwaggerGenerator;
import org.lastaflute.doc.agent.SwaggerAgent;
import org.lastaflute.doc.web.LaActionSwaggerable;
import org.lastaflute.web.Execute;
import org.lastaflute.web.login.AllowAnyoneAccess;
import org.lastaflute.web.response.HtmlResponse;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.servlet.request.RequestManager;

/**
 * @author cabos
 */
@AllowAnyoneAccess
public class SwaggerAction extends IntroBaseAction implements LaActionSwaggerable {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String _apiPrefix = "api";

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
        return asJson(new SwaggerGenerator() {
            @Override
            protected OptionalThing<String> prepareApplicationVersion() {
                return OptionalThing.of(_apiPrefix);
            }
        }.generateSwaggerMap());
    }

    private void verifySwaggerAllowed() { // also check in ActionAdjustmentProvider
        verifyOrClientError("Swagger is not enabled.", config.isSwaggerEnabled());
    }
}
