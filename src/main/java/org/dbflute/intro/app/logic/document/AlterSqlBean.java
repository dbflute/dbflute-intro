package org.dbflute.intro.app.logic.document;

import org.lastaflute.web.validation.Required;

/**
 * @author subaru
 */
public class AlterSqlBean {
    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String fileName;
    public final String content; // may be empty file

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public AlterSqlBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
