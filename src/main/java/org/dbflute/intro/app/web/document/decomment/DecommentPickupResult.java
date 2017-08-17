package org.dbflute.intro.app.web.document.decomment;

import java.util.List;
import java.util.stream.Collectors;

import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;

/**
 * @author hakiba
 */
public class DecommentPickupResult {

    // ===================================================================================
    //                                                                         Inner Class
    //                                                                         ===========
    // TODO hakiba move it under tables by jflute (2017/08/17)
    // TODO hakiba validator annotation (Required only) by jflute (2017/08/17)
    public static class TablePart {
        public String tableName;
        public List<ColumnPart> columns;

        public TablePart(DfDecoMapTablePart tablePart) {
            this.tableName = tablePart.getTableName();
            this.columns = tablePart.getColumns().stream().map(columnPart -> new ColumnPart(columnPart)).collect(Collectors.toList());
        }

        public static class ColumnPart {
            public String columnName;
            public List<Property> properties;

            public ColumnPart(DfDecoMapColumnPart columnPart) {
                this.columnName = columnPart.getColumnName();
                this.properties = columnPart.getProperties().stream().map(property -> new Property(property)).collect(Collectors.toList());
            }

            // TODO hakiba PropertyPart by jflute (2017/08/17)
            public static class Property {
                public String decomment;
                public String databaseComment;
                public String previousWholeComment;
                public Long commentVersion;
                public List<String> authorList;

                public Property(DfDecoMapColumnPart.ColumnProperty property) {
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
    //                                                                           Attribute
    //                                                                           =========
    public List<TablePart> tables;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public DecommentPickupResult(List<DfDecoMapTablePart> tableParts) {
        this.tables = tableParts.stream().map(tablePart -> new TablePart(tablePart)).collect(Collectors.toList());
    }
}
