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
        @Valid
        @Required
        public DecomentColumnPart column;

        public static class DecomentColumnPart {

            @Required
            public String columnName;
            @Required
            public String decoment;
            @Required
            public String databaseComment;
            @Required
            public String previousWholeComment;
        }
    }
}
