/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.app.logic.dfprop.schemapolicy;

import java.util.Arrays;
import java.util.List;

/**
 * The logic for definition of Schema Policy. <br>
 * Subjectのリストなど、schemaPolicyMap.dfpropの定義を扱う。<br>
 * schemaPolicyMap: http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html
 * @author jflute
 * @author prprmurakami
 * @since 0.5.0 split from DfpropSchemaPolicyReadLogic (2021/12/25 Saturday at urayasu)
 */
public class DfpropSchemaPolicyDefLogic {

    // ===================================================================================
    //                                                                        Subject List
    //                                                                        ============
    /**
     * if の主語 (of table) のリストを取得する。(e.g. tableName, alias, ...) <br>
     * http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatementifsubject
     * @return The read-only list of subject object. (NotNull, NotEmpty)
     */
    public List<TableMapSubject> getStatementTableMapSubjectList() {
        // Create subject list here because contents does not change frequently
        return Arrays.asList(TableMapSubject.values());
    }

    /**
     * if の主語 (of column) のリストを取得する。(e.g. columnName, dbType, ...) <br>
     * http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatementifsubject
     * @return The read-only list of subject object. (NotNull, NotEmpty)
     */
    public List<ColumnMapSubject> getStatementColumnMapSubjectList() {
        // Create subject list here because contents does not change frequently
        return Arrays.asList(ColumnMapSubject.values());
    }

    // 以下のページの内容と一致しており、項目が増えた場合は追加する。 by prprmurakami (2021/05/27)
    // http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#tablestatement
    public enum TableMapSubject {
        TABLE_NAME("tableName"), //
        ALIAS("alias"), //
        FIRST_DATE("firstDate"), //
        PK_COLUMN_NAME("pk_columnName"), //
        PK_DB_TYPE("pk_dbType"), //
        PK_SIZE("pk_size"), //
        PK_DB_TYPE_WITH_SIZE("pk_dbType_with_size"); //

        private final String title;

        private TableMapSubject(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    // 以下のページの内容と一致しており、項目が増えた場合は追加する。 by prprmurakami (2021/05/27)
    // http://dbflute.seasar.org/ja/manual/reference/dfprop/schemapolicy/index.html#columnstatement
    public enum ColumnMapSubject {
        TABLE_NAME("tableName"), //
        ALIAS("alias"), //
        FIRST_DATE("firstDate"), //
        COLUMN("column"), //
        COLUMN_NAME("columnName"), //
        TABLE_COLUMN_NAME("tableColumnName"), //
        DB_TYPE_WITH_SIZE("dbType_with_size"), //
        SIZE("size"), //
        DB_TYPE("dbType");

        private final String title;

        private ColumnMapSubject(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
