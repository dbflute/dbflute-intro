package org.dbflute.intro.app.web.document;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class SchemaHtmlResult {

    @Required
    final public String content;

    public SchemaHtmlResult(String schemaHtmlSource) {
        this.content = schemaHtmlSource;
    }
}
