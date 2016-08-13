package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author deco
 * @author jflute
 */
public class DfpropFileNotFoundException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public DfpropFileNotFoundException(String debugMsg, String dfpropFile) {
        super(debugMsg, prepareMessages(dfpropFile));
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppDfpropFileNotFound(GLOBAL, failureHint);
    }
}
