package org.dbflute.intro.bizfw.tellfailure;

import org.dbflute.intro.bizfw.tellfailure.base.IntroApplicationException;
import org.dbflute.intro.mylasta.action.IntroMessages;

import java.util.function.Consumer;

/**
 * @author deco
 */
public class NetworkErrorException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public NetworkErrorException(String debugMsg) {
        super(debugMsg, prepareMessages());
    }

    private static Consumer<IntroMessages> prepareMessages() {
        return messages -> messages.addErrorsAppNetworkError(GLOBAL);
    }
}
