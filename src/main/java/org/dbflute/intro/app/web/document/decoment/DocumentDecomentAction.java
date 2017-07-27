package org.dbflute.intro.app.web.document.decoment;

import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// TODO cabos decoment to decomment by jflute (2017/07/27)
/**
 * @author cabos
 */
public class DocumentDecomentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // TODO cabos post to save, get to diff by jflute (2017/07/27)
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
