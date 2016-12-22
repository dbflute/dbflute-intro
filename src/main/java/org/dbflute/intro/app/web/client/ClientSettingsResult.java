package org.dbflute.intro.app.web.client;

import javax.validation.Valid;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 */
public class ClientSettingsResult {

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

    @Required
    @Valid
    public DatabaseSettingsPart mainSchemaSettings;

    public static class DatabaseSettingsPart {

        @Required
        public String url;
        public String schema; // contains additional schema by comma
        @Required
        public String user;
        public String password;
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ClientSettingsResult() {
    }
}
