package org.dbflute.intro.app.logic.dbfluteclient;

import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;

import java.io.File;

/**
 * @author deco
 */
public class DocumentLogic {

    public File findDocumentFile(String project, String type) {
        return new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/output/doc/" + type + "-" + project + ".html");
    }
}
