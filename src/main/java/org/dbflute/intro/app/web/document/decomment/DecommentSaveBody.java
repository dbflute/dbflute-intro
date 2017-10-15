package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

// done cabos rename DecommentSaveBody by jflute (2017/08/10)

/**
 * @author cabos
 */
public class DecommentSaveBody {

    // done cabos system parameter, use (groups = ClientError.class) _lavc... by jflute (2017/08/10)
    // done cabos javadoc and add e.g. (above annotation) by jflute (2017/09/28)
    /**
     * whether decomment merged or not
     * This field true if decomment mered.
     * e.g. false
     */
    @Required(groups = ClientError.class)
    public Boolean merged;

    /** table part */
    @Valid
    @Required(groups = ClientError.class)
    public DecommentTablePart table;

    public static class DecommentTablePart {

        /**
         * table name
         * e.g. "MEMBER"
         */
        @Required(groups = ClientError.class)
        public String tableName;

        // done cabos List<DecommentColumnPart> by jflute (2017/07/27)
        /** the list of column part */
        @Valid
        @Required(groups = ClientError.class)
        public List<DecommentColumnPart> columns;

        public static class DecommentColumnPart {

            /**
             * column name
             * e.g. "MEMBER_NAME"
             */
            @Required(groups = ClientError.class)
            public String columnName;

            /**
             * inputted column comment on the schema.html
             * e.g. "this column's value is always null ...??  DO NOT BE SILLY!!!" (NullAllowed)
             */
            @Required
            public String decomment;

            // done cabos not required by jflute (2017/07/27)
            /**
             * column comment on table definition
             * The comments on database may be blank so null allowed.
             * e.g. "this column's value allowed null?" (NullAllowed)
             */
            public String databaseComment;

            // done cabos not required by jflute (2017/07/27)
            /**
             * column comment before editing on the schema.html
             * The comments on scheme.html may be blank so null allowed.
             * e.g. "this column's value is not always null." (NullAllowed)
             */
            public String previousWholeComment;

            /**
             * column comment version
             * The comment version will update when the decomment.dfprop file is picked up.
             * e.g. 2
             */
            @Required(groups = ClientError.class)
            public Long commentVersion;

            /**
             * the list of ancestor authors
             * Current piece author is derived by server at first decomment so empty allowed.
             * e.g. ["cabos", "hakiba", "deco"]  (EmptyAllowed)
             */
            @NotNull
            public List<String> authorList;
        }
    }
}
