package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.bizfw.tellfailure.base.IntroApplicationException;
import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author cabos
 */
public class PhysicalDecoMapFileException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public PhysicalDecoMapFileException(String debugMsg, String failureHint) {
        super(debugMsg, prepareMessages(failureHint));
    }

    public PhysicalDecoMapFileException(String debugMsg, String failureHint, Throwable cause) {
        super(debugMsg, prepareMessages(failureHint), cause);
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        // done cabos use new message, add message to properties and go freegen and use it by jflute (2017/09/07)
        return messages -> messages.addErrorsAppDecoMapPhysicalError(GLOBAL, failureHint);
    }
}
