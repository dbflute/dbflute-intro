package org.dbflute.intro.app.logic.document;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author deco
 */
public class DocumentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                         Find/Exists
    //                                                                         ===========
    public boolean existsSchemaHtml(String clientProject) {
        return toDocumentFile(clientProject, "schema").exists();
    }

    public boolean existsHistoryHtml(String clientProject) {
        return toDocumentFile(clientProject, "history").exists();
    }

    public File findSchemaHtml(String clientProject) {
        return toDocumentFile(clientProject, "schema");
    }

    public File findHistoryHtml(String clientProject) {
        return toDocumentFile(clientProject, "history");
    }

    // ===================================================================================
    //                                                                                Path
    //                                                                                ====
    private File toDocumentFile(String clientProject, String type) {
        final String htmlFileName = buildHtmlFileName(clientProject, type);
        return new File(introPhysicalLogic.buildClientPath(clientProject, "output", "doc", htmlFileName));
    }

    private String buildHtmlFileName(String clientProject, String type) {
        return type + "-" + clientProject + ".html";
    }
}
