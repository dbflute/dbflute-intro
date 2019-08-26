package org.dbflute.intro.app.web.alter;

import org.lastaflute.web.validation.Required;

/**
 * @author subaru
 */
public class AlterCreateBody {
    @Required
    public String alterFileName;
}
