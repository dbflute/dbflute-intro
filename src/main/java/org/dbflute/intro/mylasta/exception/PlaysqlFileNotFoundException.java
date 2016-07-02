package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * @author deco
 */
public class PlaysqlFileNotFoundException extends LaApplicationException {

    private static final long serialVersionUID = 1L;

    public PlaysqlFileNotFoundException(String debugMsg) {
        super(debugMsg);
        saveMessage(IntroMessages.ERRORS_APP_PLAYSQL_FILE_NOT_FOUND);
    }
}
