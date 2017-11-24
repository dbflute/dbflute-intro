package org.dbflute.infra.doc.decomment.exception;

/**
 * @author cabos (at jjug2017 fall)
 */
public class DfDecoMapFileReadFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DfDecoMapFileReadFailureException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
