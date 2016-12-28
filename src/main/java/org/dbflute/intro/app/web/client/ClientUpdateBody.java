package org.dbflute.intro.app.web.client;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

import javax.validation.Valid;

/**
 * @author hakiba
 */
public class ClientUpdateBody {

    @Required
    @Valid
    public ClientPart client;

    public static class ClientPart {

        @Required
        public CDef.TargetDatabase databaseCode;
        @Required
        public CDef.TargetLanguage languageCode;
        @Required
        public CDef.TargetContainer containerCode;
        @Required
        public String packageBase;
        @Required
        public String jdbcDriverFqcn;

        @Required
        @Valid
        public ClientPart.DatabaseSettingsPart mainSchemaSettings;

        public static class DatabaseSettingsPart {

            @Required
            public String url;
            public String schema; // contains additional schema by comma
            @Required
            public String user;
            public String password;
        }

        @Required
        public String dbfluteVersion;

        public String jdbcDriverJarPath;

    }
}
