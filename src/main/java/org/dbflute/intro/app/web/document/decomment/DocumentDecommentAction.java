package org.dbflute.intro.app.web.document.decomment;

import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// TODO done cabos decoment to decomment by jflute (2017/07/27)
/**
 * @author cabos
 */
public class DocumentDecommentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // TODO cabos post to save, get to diff by jflute (2017/07/27)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> post(String projectName, DecommentPostBody body) {
        validate(body, messages -> {});
        // TODO cabos create decomap file (2017/07/20)
        return JsonResponse.asEmptyBody();
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> get(String projectName) {
        // TODO hakiba create new decomment response (2017/07/20)
        return JsonResponse.asEmptyBody();
    }
}
