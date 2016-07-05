package org.dbflute.intro.app.logic.dbfluteclient;

import java.io.File;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author deco
 */
public class DocumentLogic {

    public File findDocumentFile(String project, String type) {
        return new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/output/doc/" + type + "-" + project + ".html");
    }
}
