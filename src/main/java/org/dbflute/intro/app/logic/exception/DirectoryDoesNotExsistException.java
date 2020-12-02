package org.dbflute.intro.app.logic.exception;

public class DirectoryDoesNotExsistException extends Exception {

    private static final long serialVersionUID = 1L;

    public DirectoryDoesNotExsistException(String msg) {
        super(msg);
    }
}
