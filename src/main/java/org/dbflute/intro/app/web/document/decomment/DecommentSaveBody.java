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
    // done cabos javadoc and add e.g. (above annotation) by jflute (2017/09/28)
    /** merged e.g. false */
    @Required(groups = ClientError.class)
    public Boolean merged;
    /** table */
    @Valid
    @Required(groups = ClientError.class)
    public DecommentTablePart table;

    public static class DecommentTablePart {

        /** table name e.g. "MEMBER" */
        @Required(groups = ClientError.class)
        public String tableName;

        // done cabos List<DecommentColumnPart> by jflute (2017/07/27)
        /** columns */
        @Valid
        @Required(groups = ClientError.class)
        public List<DecommentColumnPart> columns;

        public static class DecommentColumnPart {

            /** column name e.g. MEMBER */
            @Required(groups = ClientError.class)
            public String columnName;
            /** decomment e.g. "edited column comment" */
            @Required
            public String decomment;
            // done cabos not required by jflute (2017/07/27)
            /** database comment e.g. "database comment" */
            public String databaseComment;
            // done cabos not required by jflute (2017/07/27)
            /** previous whole comment e.g. "previous whole comment" */
            public String previousWholeComment;
            /** comment version e.g. 2 */
            @Required(groups = ClientError.class)
            public Long commentVersion;
            /** author list e.g. ["cabos", "hakiba", "deco"] */
            public List<String> authorList;
        }
    }
}
