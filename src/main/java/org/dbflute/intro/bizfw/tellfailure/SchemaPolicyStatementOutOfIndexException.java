/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.bizfw.tellfailure;

import java.util.function.Consumer;

import org.dbflute.intro.bizfw.tellfailure.base.IntroApplicationException;
import org.dbflute.intro.mylasta.action.IntroMessages;

/**
 * @author hakiba
 */
public class SchemaPolicyStatementOutOfIndexException extends IntroApplicationException {

    private static final long serialVersionUID = 1L;

    public SchemaPolicyStatementOutOfIndexException(String debugMsg, String failureHint) {
        super(debugMsg, prepareMessages(failureHint));
    }

    private static Consumer<IntroMessages> prepareMessages(String failureHint) {
        return messages -> messages.addErrorsAppSchemapolicyStatementIndexError(GLOBAL, failureHint);
    }
}
