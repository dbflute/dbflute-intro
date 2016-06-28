package org.dbflute.intro.app.web.log;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class LogBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String fileName;
    @Required
    public final String content;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public LogBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
