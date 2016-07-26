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

    public File findDocumentFile(String project, String type) {
        final String outputDirPath = introPhysicalLogic.toDocumentOutputDirPath(project);
        return new File(outputDirPath + "/" + type + "-" + project + ".html");
    }
}
