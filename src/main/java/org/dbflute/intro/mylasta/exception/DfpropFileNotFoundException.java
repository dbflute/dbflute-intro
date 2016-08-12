package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.message.exception.MessagingApplicationException;

/**
 * @author deco
 * @author jflute
 */
public class DfpropFileNotFoundException extends MessagingApplicationException {

    private static final long serialVersionUID = 1L;

    public DfpropFileNotFoundException(String debugMsg, String dfpropFile) {
        super(debugMsg, prepareMessages(dfpropFile));
    }

    private static IntroMessages prepareMessages(String dfpropFile) {
        return new IntroMessages().addErrorsAppDfpropFileNotFound(IntroMessages.GLOBAL_PROPERTY_KEY, dfpropFile);
    }
}
