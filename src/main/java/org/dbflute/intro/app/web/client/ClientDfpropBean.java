package org.dbflute.intro.app.web.client;

/**
 * @author deco
 */
public class ClientDfpropBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public final String fileName;
    public final String content;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ClientDfpropBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
