package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.Valid;

import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

// done cabos rename DecommentSaveBody by jflute (2017/08/10)

/**
 * @author cabos
 */
public class DecommentSaveBody {

    // done cabos system parameter, use (groups = ClientError.class) _lavc... by jflute (2017/08/10)
    // TODO cabos javadoc and add e.g. (above annotation) by jflute (2017/09/28)
    @Required(groups = ClientError.class)
    public Boolean merged;
    @Required(groups = ClientError.class)
    @Valid
    public DecommentTablePart table;

    public static class DecommentTablePart {

        @Required(groups = ClientError.class)
        public String tableName;

        // done cabos List<DecommentColumnPart> by jflute (2017/07/27)
        @Valid
        @Required(groups = ClientError.class)
        public List<DecommentColumnPart> columns;

        public static class DecommentColumnPart {

            @Required(groups = ClientError.class)
            public String columnName;
            @Required
            public String decomment;
            // done cabos not required by jflute (2017/07/27)
            public String databaseComment;
            // done cabos not required by jflute (2017/07/27)
            public String previousWholeComment;
            @Required(groups = ClientError.class)
            public Long commentVersion;
            public List<String> authorList;
        }
    }
}
