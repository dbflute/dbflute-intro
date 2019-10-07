package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.bizfw.tellfailure.base.IntroApplicationException;
import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author cabos
 */
public class IntroFileOperaionException extends IntroApplicationException {

    public IntroFileOperaionException(String debugMsg, String failureHint) {
        super(debugMsg, prepareMessages(failureHint));
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppFileOperationError(GLOBAL, failureHint);
    }
}
