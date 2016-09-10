package org.dbflute.intro.app.web.document;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class HistoryHtmlResult {

    @Required
    final public String content;

    public HistoryHtmlResult(String content) {
        this.content = content;
    }
}
