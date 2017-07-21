package org.dbflute.intro.app.web.document.decoment;

import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author cabos
 */
public class DocumentDecomentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> post(String projectName, DecomentPostBody body) {
        validate(body, messages -> {});
        // TODO cabos create decomap file (2017/07/20)
        return JsonResponse.asEmptyBody();
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> get(String projectName) {
        // TODO hakiba create new decoment response (2017/07/20)
        return JsonResponse.asEmptyBody();
    }
}
