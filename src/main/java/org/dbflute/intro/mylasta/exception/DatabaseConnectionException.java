/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.DbfluteMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * データベース接続例外クラス。
 * @author p1us2er0
 */
public class DatabaseConnectionException extends LaApplicationException {

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public DatabaseConnectionException(String debugMsg) {
        super(debugMsg);
        saveMessage(DbfluteMessages.ERRORS_APP_DATABASE_CONNECTION, new Object[0]);
    }

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     * @param cause 原因
     */
    public DatabaseConnectionException(String debugMsg, Throwable cause) {
        super(debugMsg, cause);
        saveMessage(DbfluteMessages.ERRORS_APP_DATABASE_CONNECTION, new Object[0]);
    }
}
