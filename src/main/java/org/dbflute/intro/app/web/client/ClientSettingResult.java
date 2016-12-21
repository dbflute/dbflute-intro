package org.dbflute.intro.app.web.client;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

import javax.validation.Valid;

/**
 * @author hakiba
 */
public class ClientSettingResult {

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
    public ClientSettingResult() {
        this.mainSchemaSettings = new DatabaseSettingsPart();
    }
}
