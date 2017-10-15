package org.dbflute.intro.app.web.document.decomment;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 * @author cabos
 */
public class DecommentPickupResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** tables */
    @Valid
    @Required
    public List<TablePart> tables;

    // done hakiba move it under tables by jflute (2017/08/17)
    // done hakiba validator annotation (Required only) by jflute (2017/08/17)
    public static class TablePart {

        /** table name e.g. "MEMBER" */
        @Required
        public String tableName;

        /** list of column part, contains saved comments */
        @Valid
        @Required
        public List<ColumnPart> columns;

        public TablePart(DfDecoMapTablePart tablePart) {
            this.tableName = tablePart.getTableName();
            this.columns = tablePart.getColumns().stream().map(columnPart -> new ColumnPart(columnPart)).collect(Collectors.toList());
        }

        public static class ColumnPart {

            /** column name e.g. "MEMBER_NAME" */
            @Required
            public String columnName;

            /** properties */
            @Valid
            @Required
            public List<PropertyPart> properties;

            public ColumnPart(DfDecoMapColumnPart columnPart) {
                this.columnName = columnPart.getColumnName();
                this.properties =
                        columnPart.getProperties().stream().map(property -> new PropertyPart(property)).collect(Collectors.toList());
            }

            public static class PropertyPart {

                /** decomment e.g. "edited column comment" */
                @Required
                public String decomment;

                /** database comment e.g. "let's cabos" (NullAllowed) */
                public String databaseComment;

                /** previous whole comment e.g. "my name is hakiba" (NullAllowed) */
                public String previousWholeComment;

                /** comment version e.g. 3 */
                @Required
                public Long commentVersion;

                /** author list e.g. ["cabos", "hakiba", "deco"] */
                @Valid
                @Required
                public List<String> authorList;

                public PropertyPart(DfDecoMapColumnPart.ColumnPropertyPart property) {
                    this.decomment = property.getDecomment();
                    this.databaseComment = property.getDatabaseComment();
                    this.previousWholeComment = property.getPreviousWholeComment();
                    this.commentVersion = property.getCommentVersion();
                    this.authorList = property.getAuthorList();
                }
            }
        }
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DecommentPickupResult(List<DfDecoMapTablePart> tableParts) {
        this.tables = tableParts.stream().map(tablePart -> new TablePart(tablePart)).collect(Collectors.toList());
    }
}
