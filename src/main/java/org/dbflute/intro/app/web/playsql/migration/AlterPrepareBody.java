package org.dbflute.intro.app.web.playsql.migration;

import org.lastaflute.web.validation.Required;

/**
 * @author subaru
 */
public class AlterPrepareBody {
    @Required
    public String alterFileName;
}
