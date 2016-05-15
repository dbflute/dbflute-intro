package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * ファイル取得例外クラス。
 * @author deco
 */
public class FileNotFoundException extends LaApplicationException {

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public FileNotFoundException(String debugMsg) {
        super(debugMsg);
        saveMessage(IntroMessages.ERRORS_FILE_NOT_FOUND, new Object[0]);
    }
}
