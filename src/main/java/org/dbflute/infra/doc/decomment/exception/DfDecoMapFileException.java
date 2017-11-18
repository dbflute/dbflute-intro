package org.dbflute.infra.doc.decomment.exception;

/**
 * @author cabos (at jjug2017 fall)
 */
public class DfDecoMapFileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DfDecoMapFileException(String msg) {
        super(msg);
    }

    public DfDecoMapFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
