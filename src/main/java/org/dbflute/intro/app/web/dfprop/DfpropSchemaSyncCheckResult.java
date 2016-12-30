package org.dbflute.intro.app.web.dfprop;

import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;

/**
 * @author deco
 */
public class DfpropSchemaSyncCheckResult {

    public String url;
    public String schema;
    public String user;
    public String password;
    public Boolean isSuppressCraftDiff;

    public DfpropSchemaSyncCheckResult(SchemaSyncCheckMap schemaSyncCheckMap) {
        DbConnectionBox dbConnectionModel = schemaSyncCheckMap.dbConnectionModel;
        url = dbConnectionModel.getUrl();
        schema = dbConnectionModel.getSchema();
        user = dbConnectionModel.getUser();
        password = dbConnectionModel.getPassword();
        isSuppressCraftDiff = schemaSyncCheckMap.isSuppressCraftDiff;
    }
}
