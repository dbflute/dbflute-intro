package org.dbflute.intro.app.web.dfprop.schemapolicy.statement;

import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.lastaflute.web.validation.Required;

/**
 * @author prprmurakami
 */
public class DfpropSchemapolicyStatementSubjectForm {

    @Required
    public AppCDef.SubjectableMapType mapType;
}
