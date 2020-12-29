/*
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.bizfw.tellfailure.base;

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
