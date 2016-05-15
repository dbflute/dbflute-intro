package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * ファイル読み取り例外クラス。
 * @author deco
 */
public class FileNotReadException extends LaApplicationException {

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public FileNotReadException(String debugMsg) {
        super(debugMsg);
        saveMessage(IntroMessages.ERRORS_FILE_NOT_READ, new Object[0]);
    }

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     * @param cause    原因
     */
    public FileNotReadException(String debugMsg, Throwable cause) {
        super(debugMsg, cause);
        saveMessage(IntroMessages.ERRORS_FILE_NOT_READ, new Object[0]);
    }
}
