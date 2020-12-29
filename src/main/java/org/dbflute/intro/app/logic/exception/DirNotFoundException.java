package org.dbflute.intro.app.logic.exception;

/**
 * @author prprmurakami
 */
public class DirNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private String dirPath;

    public DirNotFoundException(String msg, String dirPath) {
        super(msg);
        this.dirPath = dirPath;
    }

    public String getDirPath() {
        return dirPath;
    }
}
