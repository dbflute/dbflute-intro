package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author deco
 * @author jflute
 */
public class PlaysqlFileNotFoundException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public PlaysqlFileNotFoundException(String debugMsg, String fileName) {
        super(debugMsg, prepareMessages(fileName));
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppPlaysqlFileNotFound(GLOBAL, failureHint);
    }
}
