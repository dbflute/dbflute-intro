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
        return findDocumentFile(clientProject, "schema").exists();
    }

    public boolean existsHistoryHtml(String clientProject) {
        return findDocumentFile(clientProject, "history").exists();
    }

    public File findDocumentFile(String clientProject, String type) {
        final String outputDirPath = introPhysicalLogic.buildDocumentOutputDirPath(clientProject);
        return new File(outputDirPath + "/" + type + "-" + clientProject + ".html");
    }
}
