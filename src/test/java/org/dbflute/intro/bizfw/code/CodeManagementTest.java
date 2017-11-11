package org.dbflute.intro.bizfw.code;

import java.io.File;

import org.dbflute.utflute.core.PlainTestCase;
import org.dbflute.utflute.core.filesystem.FileLineHandler;
import org.dbflute.utflute.core.policestory.javaclass.PoliceStoryJavaClassHandler;
import org.dbflute.util.Srl;

/**
 * @author jflute
 */
public class CodeManagementTest extends PlainTestCase {

    public void test_decooment_decoMapFile_dependency() {
        policeStoryOfJavaClassChase(new PoliceStoryJavaClassHandler() {
            public void handle(File srcFile, Class<?> clazz) {
                if (clazz.getName().startsWith("org.dbflute.infra")) { // e.g. DfDecoMapFile
                    log("...Checking infra class: {}", clazz.getName());
                    readLine(srcFile, "UTF-8", new FileLineHandler() {
                        public void handle(String line) {
                            if (line.startsWith("import ")) {
                                String refName = Srl.rtrim(Srl.substringFirstRear(line, "import "), ";");
                                if (refName.startsWith("org.dbflute.intro")) {
                                    String msg = "Cannot refer to intro classes: " + clazz.getName() + ", " + refName;
                                    throw new IllegalStateException(msg);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}
