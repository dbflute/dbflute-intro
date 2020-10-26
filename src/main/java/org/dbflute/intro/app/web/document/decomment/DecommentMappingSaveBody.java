/*
 * Copyright 2014-2020 the original author or authors.
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

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.intro.IntroSystemLogic;
import org.lastaflute.web.validation.ClientError;
import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class DecommentMappingSaveBody {

    // done cabos also (groups = ClientError.class) to NotNull by jflute (2018/04/12)
    /**
     * mapping list
     */
    @Valid
    @NotNull(groups = ClientError.class)
    public List<MappingPart> mappings;

    public static class MappingPart {

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
         * the current author
         * Only used when run as decomment server {@link IntroSystemLogic#isDecommentServer()}
         * e.g. "cabos"
         */
        public String author;

        /**
         * the list of ancestor authors
         * Current mapping author is derived by server at first decomment so empty allowed.
         * e.g. ["hakiba"]
         */
        @NotNull(groups = ClientError.class)
        public List<String> authors;

        /**
         * the list of previous piece code
         * Current mapping code is derived by server at first decomment so empty allowed.
         * e.g. ["FE893L1"]  (EmptyAllowed)
         */
        @NotNull(groups = ClientError.class)
        public List<String> previousMappings;
    }
}
