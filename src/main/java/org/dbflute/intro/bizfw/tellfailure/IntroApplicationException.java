package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.message.exception.MessagingApplicationException;

/**
 * @author jflute
 */
public abstract class IntroApplicationException extends MessagingApplicationException {

    private static final long serialVersionUID = 1L;
    protected static final String GLOBAL = IntroMessages.GLOBAL_PROPERTY_KEY;

    public IntroApplicationException(String debugMsg, Consumer<IntroMessages> messagesLambda) {
        super(debugMsg, createMessages(messagesLambda));
    }

    public IntroApplicationException(String debugMsg, Consumer<IntroMessages> messagesLambda, Throwable cause) {
        super(debugMsg, createMessages(messagesLambda), cause);
    }

    private static IntroMessages createMessages(Consumer<IntroMessages> messagesLambda) {
        final IntroMessages messages = new IntroMessages();
        messagesLambda.accept(messages);
        return messages;
    }
}
