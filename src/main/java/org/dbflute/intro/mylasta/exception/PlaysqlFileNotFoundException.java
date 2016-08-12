package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.message.exception.MessagingApplicationException;

/**
 * @author deco
 * @author jflute
 */
public class PlaysqlFileNotFoundException extends MessagingApplicationException {

    private static final long serialVersionUID = 1L;

    public PlaysqlFileNotFoundException(String debugMsg, String fileName) {
        super(debugMsg, prepareMessages(fileName));
    }

    private static IntroMessages prepareMessages(String fileName) {
        return new IntroMessages().addErrorsAppPlaysqlFileNotFound(IntroMessages.GLOBAL_PROPERTY_KEY, fileName);
    }
}
