package org.dbflute.intro.app.web.document.decomment;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class DecommentMappingSaveBody {

    /**
     * old table name
     * e.g. OLD_TABLE_NAME
     */
    @Required(groups = ClientError.class)
    public String oldTableName;

    /**
     * old column name
     * this field can be null if mapping is for table
     * e.g. OLD_COLUMN_NAME
     */
    public String oldColumnName;

    /**
     * new table name
     * e.g. NEW_TABLE_NAME
     */
    @Required(groups = ClientError.class)
    public String newTableName;

    /**
     * new column name
     * this field can be null if mapping is for table
     * e.g. NEW_COLUMN_NAME
     */
    public String newColumnName;

    /**
     * mapping target type
     * e.g. COLUMN
     */
    @Required(groups = ClientError.class)
    public DfDecoMapPieceTargetType targetType;

    /**
     * the list of ancestor authors
     * Current mapping author is derived by server at first decomment so empty allowed.
     * e.g. ["hakiba"]
     */
    @NotNull
    public List<String> authors;

    /**
     * the list of previous piece code
     * Current mapping code is derived by server at first decomment so empty allowed.
     * e.g. ["FE893L1"]  (EmptyAllowed)
     */
    @NotNull
    public List<String> previousMappingList;
}
