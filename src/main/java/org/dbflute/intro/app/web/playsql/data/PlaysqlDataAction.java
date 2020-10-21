package org.dbflute.intro.app.web.playsql.data;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author prprmurakami
 */
public class PlaysqlDataAction extends IntroBaseAction{

	// ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> data(String clientName) {
    	File dataDir = new File(introPhysicalLogic.buildClientPath(clientName, "playsql", "data"));
        if (dataDir.exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(dataDir);
            } catch (IOException e) {
                throw new UncheckedIOException("fail to open alter directory of" + clientName, e);
            }
        }
        return JsonResponse.asEmptyBody();
    }
}
