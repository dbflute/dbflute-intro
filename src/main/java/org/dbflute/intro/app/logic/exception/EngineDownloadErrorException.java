package org.dbflute.intro.app.logic.exception;

/**
 * @author deco
 */
public class EngineDownloadErrorException extends Exception {

    private static final long serialVersionUID = 1L;

    public EngineDownloadErrorException(String msg) {
        super(msg);
    }
}
