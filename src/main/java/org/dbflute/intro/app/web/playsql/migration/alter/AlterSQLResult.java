/*
 * Copyright 2014-2025 the original author or authors.
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
package org.dbflute.intro.app.web.playsql.migration.alter;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 * @author jflute
 */
public class AlterSQLResult {

    /** Alter Check Result e.g. alter-NG (NullAllowed) */
    @Valid
    public AlterSQLResult.NgMarkFilePart ngMarkFile;

    /** list of editing sql files in dbflute_client/playsql/migration/alter directory (NotNull, EmptyAllowed) */
    @NotNull
    @Valid
    public List<SQLFilePart> editingFiles;

    /** checked sql zip file (NullAllowed) */
    @Valid
    public CheckedZipPart checkedZip;

    /** unreleased sql dir (NullAllowed) */
    @Valid
    public UnreleasedDirPart unreleasedDir;

    public static class NgMarkFilePart {

        @NotNull
        public CDef.NgMark ngMark;

        @NotNull
        public String content;
    }

    public static class SQLFilePart {

        /** file name e.g. alter-sql-SAMPLE.sql (NotNull) */
        @Required
        public String fileName;

        /** file content e.g. ALTER TABLE MEMBER ADD MAIHAMA_VISITED VARCHAR(3); (NotNull, EmptyAllowed) */
        @NotNull
        public String content;
    }

    public static class CheckedZipPart {

        /** zip file name e.g. 20190831_2249/checked-alter-to-20190422-2332.zip (NotNull) */
        @Required
        public String fileName;

        /** list of checked sql files (NotNull, EmptyAllowed) */
        @NotNull
        @Valid
        public List<SQLFilePart> checkedFiles;
    }

    public static class UnreleasedDirPart {

        /** list of checked sql files (NotNull, EmptyAllowed) */
        @NotNull
        @Valid
        public List<SQLFilePart> checkedFiles;
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
