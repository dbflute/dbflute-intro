package org.dbflute.intro.app.logic.exception;

/**
 * @author akifumi.tominaga
 */
public class GitBranchGetFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GitBranchGetFailureException(String msg, Exception e) {
        super(msg, e);
    }
}
