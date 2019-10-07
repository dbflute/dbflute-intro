package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.bizfw.tellfailure.base.IntroApplicationException;
import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author cabos
 */
public class IntroFileOperationException extends IntroApplicationException {

    public IntroFileOperationException(String debugMsg, String failureHint) {
        super(debugMsg, prepareMessages(failureHint));
    }

    public IntroFileOperationException(String debugMsg, String failureHint, Throwable e) {
        super(debugMsg, prepareMessages(failureHint), e);
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppFileOperationError(GLOBAL, failureHint);
    }
}
