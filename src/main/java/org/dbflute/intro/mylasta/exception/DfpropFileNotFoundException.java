package org.dbflute.intro.mylasta.exception;

import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.exception.LaApplicationException;

/**
 * ファイル取得例外クラス。
 * @author deco
 */
public class DfpropFileNotFoundException extends LaApplicationException {

    /**
     * 例外クラスを生成します。
     * @param debugMsg デバッグメッセージ
     */
    public DfpropFileNotFoundException(String debugMsg) {
        super(debugMsg);
        saveMessage(IntroMessages.ERRORS_DFPROP_FILE_NOT_FOUND);
    }
}
