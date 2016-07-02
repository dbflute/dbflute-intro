package org.dbflute.intro.app.web.playsql;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class PlaysqlBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public final String fileName;

    public final String content;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PlaysqlBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
