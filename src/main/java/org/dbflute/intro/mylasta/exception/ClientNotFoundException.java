package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * @author jflute
 */
public class ClientNotFoundException extends LaApplicationException {

    private static final long serialVersionUID = 1L;

    public ClientNotFoundException(String debugMsg, String clientProject) {
        super(debugMsg);
        saveApplicationMessage(IntroMessages.ERRORS_APP_CLIENT_NOT_FOUND, clientProject);
    }
}
