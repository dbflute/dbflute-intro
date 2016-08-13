package org.dbflute.intro.app.logic.document;

import java.io.File;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author deco
 */
public class DocumentPhysicalLogic {

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

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

    private File toDocumentFile(String clientProject, String type) {
        final String htmlFileName = buildHtmlFileName(clientProject, type);
        return new File(introPhysicalLogic.buildClientPath(clientProject, "output", "doc", htmlFileName));
    }

    private String buildHtmlFileName(String clientProject, String type) {
        return type + "-" + clientProject + ".html";
    }
}
