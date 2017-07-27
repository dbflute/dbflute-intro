package org.dbflute.intro.app.web.document.decoment;

import javax.validation.Valid;

import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class DecomentPostBody {

    @Required
    @Valid
    public DecomentTablePart table;

    public static class DecomentTablePart {

        @Required
        public String tableName;
        // TODO cabos List<DecomentColumnPart> by jflute (2017/07/27)
        @Valid
        @Required
        public DecomentColumnPart column;

        public static class DecomentColumnPart {

            @Required
            public String columnName;
            @Required
            public String decoment;
            // TODO cabos not required by jflute (2017/07/27)
            @Required
            public String databaseComment;
            // TODO cabos not required by jflute (2017/07/27)
            @Required
            public String previousWholeComment;
        }
    }
}
