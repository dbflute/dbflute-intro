package org.dbflute.intro.app.web.log;

import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class LogForm {

    @Required
    public String project;
    @Required
    public String fileName;
}
