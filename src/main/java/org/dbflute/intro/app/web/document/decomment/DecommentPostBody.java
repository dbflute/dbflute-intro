package org.dbflute.intro.app.web.document.decomment;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class DecommentPostBody {

    @Required
    @Valid
    public DecommentTablePart table;

    public static class DecommentTablePart {

        @Required
        public String tableName;
        // TODO cabos List<DecommentColumnPart> by jflute (2017/07/27)
        @Valid
        @Required
        public DecommentColumnPart column;

        public static class DecommentColumnPart {

            @Required
            public String columnName;
            @Required
            public String decomment;
            // TODO cabos not required by jflute (2017/07/27)
            @Required
            public String databaseComment;
            // TODO cabos not required by jflute (2017/07/27)
            @Required
            public String previousWholeComment;
        }
    }
}
