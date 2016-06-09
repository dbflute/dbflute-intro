package org.dbflute.intro.app.web.client;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class ClientDfpropBean {

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
    public ClientDfpropBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
