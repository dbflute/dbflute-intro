package org.dbflute.intro.app.web.dfprop;

import org.lastaflute.web.validation.Required;

/**
 * @author deco
 */
public class DfpropDocumentEditBody {

    @Required
    public Boolean upperCaseBasic;
    public String aliasDelimiterInDbComment;
    @Required
    public Boolean dbCommentOnAliasBasis;
}
