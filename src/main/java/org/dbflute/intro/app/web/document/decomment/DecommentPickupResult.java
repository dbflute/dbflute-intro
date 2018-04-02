/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.intro.app.web.document.decomment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.infra.doc.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapMappingPart;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapPropertyPart;
import org.dbflute.infra.doc.decomment.parts.DfDecoMapTablePart;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * The pickup result of decomment as virtual pickup.
 * @author hakiba
 * @author jflute
 * @author cabos
 */
public class DecommentPickupResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** list of table part */
    @Valid @NotNull // can be empty first
    public List<TablePart> tables;

    // done hakiba move it under tables by jflute (2017/08/17)
    // done hakiba validator annotation (Required only) by jflute (2017/08/17)
    public static class TablePart {

        /**
         * table name
         * e.g. "MEMBER"
         */
        @Required
        public String tableName;

        @Valid @NotNull // can be empty not conflict mapping
        public List<MappingPart> mappings;

        // done cabos move it above columns by jflute (2017/11/11)
        /** list of decomment properties associated table  */
        @Valid
        @NotNull
        public List<PropertyPart> properties;

        /** list of column part, contains saved comments */
        @Valid
        public List<ColumnPart> columns;

        public static class ColumnPart {

            /**
             * column name
             * e.g. "MEMBER_NAME"
             */
            @Required
            public String columnName;

            @Valid @NotNull // can be empty not conflict mapping
            public List<MappingPart> mappings;

            /** list of decomment properties associated column  */
            @Valid
            @Required
            public List<PropertyPart> properties;

            public ColumnPart(DfDecoMapColumnPart columnPart) {
                this.columnName = columnPart.getColumnName();
                this.mappings =
                    columnPart.getMappingList().stream().map(mappingPart -> new MappingPart(mappingPart)).collect(Collectors.toList());
                this.properties =
                    columnPart.getPropertyList().stream().map(property -> new PropertyPart(property)).collect(Collectors.toList());
            }
        }

        public TablePart(DfDecoMapTablePart tablePart) {
            this.tableName = tablePart.getTableName();
            this.mappings =
                tablePart.getMappingList().stream().map(mappingPart -> new MappingPart(mappingPart)).collect(Collectors.toList());
            this.columns = tablePart.getColumnList().stream().map(columnPart -> new ColumnPart(columnPart)).collect(Collectors.toList());
            this.properties = tablePart.getPropertyList().stream().map(property -> new PropertyPart(property)).collect(Collectors.toList());
        }
    }

    public static class PropertyPart {

        /**
         * decomment saved as decomment piece map
         * e.g. "decomment means 'deco' + 'database comment'"
         */
        @Required
        public String decomment;

        /**
         * table or column comment on table definition
         * The comments on database may be blank so null allowed.
         * e.g. "let's cabos" (NullAllowed)
         */
        public String databaseComment;

        /**
         * table or column comment version
         * The comment version will update when the decomment.dfprop file is picked up.
         * e.g. 3
         */
        @Required
        public Long commentVersion;

        /**
         * the list of ancestor authors
         * e.g. ["cabos", "hakiba", "deco"]
         */
        @Valid
        @Required
        public List<String> authors;

        /**
         * piece code generated when decomment edited
         * e.g. "EF89371"
         */
        @Required
        public String pieceCode;

        /**
         * time of edit decomment
         * e.g. "2017-11-11T18:32:22.235"
         */
        @Required
        public LocalDateTime pieceDatetime;

        /**
         * author of this decomment piece
         * e.g. "deco"
         */
        @Required
        public String pieceOwner;

        /**
         * Branch name of this decomment piece
         * e.g. "develop"
         */
        public String pieceGitBranch;

        /**
         * list of merged piece code
         * e.g. ["HF7ELSE"]
         */
        @Valid
        @NotNull
        public List<String> previousPieces;

        // =======================================================================
        //                                                             Constructor
        //                                                             ===========
        public PropertyPart(DfDecoMapPropertyPart property) {
            this.decomment = property.getDecomment();
            this.databaseComment = property.getDatabaseComment();
            this.commentVersion = property.getCommentVersion();
            this.authors = property.getAuthorList();
            this.pieceCode = property.getPieceCode();
            this.pieceDatetime = property.getPieceDatetime();
            this.pieceOwner = property.getPieceOwner();
            this.pieceGitBranch = property.getPieceGitBranch();
            this.previousPieces = property.getPreviousPieceList();
        }
    }

    public static class MappingPart {

        // TODO cabos comment (2018/03/25)
        @Required
        public String newTableName;
        public String newColumnName;
        @NotNull
        public List<String> authorList;
        @Required
        public String mappingCode;
        @Required
        public String mappingOwner;
        @Required
        public LocalDateTime mappingDatetime;
        @NotNull
        public List<String> previousMappingList;

        public MappingPart(DfDecoMapMappingPart mappingPart) {
            this.newTableName = mappingPart.getNewTableName();
            this.newColumnName = mappingPart.getNewColumnName();
            this.authorList = mappingPart.getAuthorList();
            this.mappingCode = mappingPart.getMappingCode();
            this.mappingOwner = mappingPart.getMappingOwner();
            this.mappingDatetime = mappingPart.getMappingDatetime();
            this.previousMappingList = mappingPart.getPreviousMappingList();
        }
    }

    public DecommentPickupResult(List<DfDecoMapTablePart> tableParts) {
        this.tables = tableParts.stream().map(tablePart -> new TablePart(tablePart)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return Lato.string(this);
    }
}
