package org.dbflute.intro.app.web.client;

import org.lastaflute.web.validation.Required;

/**
 * @author akifumi.tominaga
 */
public class ClientDfpropUpdateForm {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Required
    public String fileName;
    @Required
    public String content;
}
