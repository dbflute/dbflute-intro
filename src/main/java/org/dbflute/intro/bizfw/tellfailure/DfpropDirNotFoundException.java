package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author deco
 * @author jflute
 */
public class DfpropDirNotFoundException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public DfpropDirNotFoundException(String debugMsg, String failureHint) {
        super(debugMsg, prepareMessages(failureHint));
    }

    public DfpropDirNotFoundException(String debugMsg, String failureHint, Throwable cause) {
        super(debugMsg, prepareMessages(failureHint), cause);
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppDfpropDirNotFound(GLOBAL, failureHint);
    }
}
