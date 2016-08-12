package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * @author deco
 * @author jflute
 */
public class DfpropDirNotFoundException extends LaApplicationException {

    private static final long serialVersionUID = 1L;

    public DfpropDirNotFoundException(String debugMsg, String dfpropDir) {
        super(debugMsg);
        saveApplicationMessage(IntroMessages.ERRORS_APP_DFPROP_DIR_NOT_FOUND, dfpropDir);
    }
}
