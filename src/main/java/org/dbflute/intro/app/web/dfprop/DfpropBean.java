package org.dbflute.intro.app.web.dfprop;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class DfpropBean {

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
    public DfpropBean(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }
}
