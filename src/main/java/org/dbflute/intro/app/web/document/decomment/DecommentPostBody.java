package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

// TODO cabos rename DecommentSaveBody by jflute (2017/08/10)
/**
 * @author cabos
 */
public class DecommentPostBody {

    // TODO cabos system parameter, use (groups = ClientError.class) _lavc... by jflute (2017/08/10)
    @Required
    public Boolean merged;
    @Required
    @Valid
    public DecommentTablePart table;

    public static class DecommentTablePart {

        @Required
        public String tableName;
        // done cabos List<DecommentColumnPart> by jflute (2017/07/27)
        @Valid
        @Required
        public List<DecommentColumnPart> columns;

        public static class DecommentColumnPart {

            @Required
            public String columnName;
            @Required
            public String decomment;
            // done cabos not required by jflute (2017/07/27)
            public String databaseComment;
            // done cabos not required by jflute (2017/07/27)
            public String previousWholeComment;
            @Required
            public Long commentVersion;
            @Required
            public List<String> authorList;
        }
    }
}
