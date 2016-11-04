package org.dbflute.intro.app.web.client;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author deco at rinshi-no-mori
 */
public class ClientOperationResult {

    // ===================================================================================
    //                                                                         Client Info
    //                                                                         ===========
    @Required
    public String projectName;
    @Required
    public CDef.TargetDatabase databaseCode;
    @Required
    public CDef.TargetLanguage languageCode;
    @Required
    public CDef.TargetContainer containerCode;

    // ===================================================================================
    //                                                                        Client State
    //                                                                        ============
    @Required
    public Boolean hasSchemahtml;
    @Required
    public Boolean hasHistoryhtml;
}
