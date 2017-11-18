package org.dbflute.infra.doc.decomment.exception;

/**
 * @author cabos (at jjug2017 fall)
 */
public class DfDecoMapFileWriteFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DfDecoMapFileWriteFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
