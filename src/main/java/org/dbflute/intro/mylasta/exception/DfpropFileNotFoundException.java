package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * @author deco
 * @author jflute
 */
public class DfpropFileNotFoundException extends LaApplicationException {

    private static final long serialVersionUID = 1L;

    public DfpropFileNotFoundException(String debugMsg) {
        super(debugMsg);
        saveApplicationMessage(IntroMessages.ERRORS_APP_DFPROP_FILE_NOT_FOUND);
    }
}
