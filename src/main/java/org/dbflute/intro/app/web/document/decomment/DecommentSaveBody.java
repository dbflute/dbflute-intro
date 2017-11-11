package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

// done cabos rename DecommentSaveBody by jflute (2017/08/10)

/**
 * @author cabos
 * @author deco
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

    /**
     * table name
     * e.g. "MEMBER"
     */
    @Required(groups = ClientError.class)
    public String tableName;

    /**
     * column name
     * The decomment target may be TABLE so null allowed.
     * e.g. "MEMBER_NAME"
     */
    public String columnName;

    /**
     * decomment target type
     * e.g. COLUMN
     */
    public AppCDef.PieceTargetType targetType;

    /**
     * inputted column comment on the schema.html
     * e.g. "this column's value is always null ...??  DO NOT BE SILLY!!!" (NullAllowed)
     */
    @Required
    public String decomment;

    /**
     * column comment on table definition
     * The comments on database may be blank so null allowed.
     * e.g. "this column's value allowed null?" (NullAllowed)
     */
    public String databaseComment;

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
    public List<String> authors;

    /**
     * the list of previous piece code
     * Current piece code is derived by server at first decomment so empty allowed.
     * e.g. ["FE893L1"]  (EmptyAllowed)
     */
    @NotNull
    public List<String> previousPieces;
}
