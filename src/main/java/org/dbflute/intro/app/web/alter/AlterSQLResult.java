package org.dbflute.intro.app.web.alter;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.dbflute.allcommon.CDef;

/**
 * @author cabos
 */
public class AlterSQLResult {

    /**
     * Alter Check Result (Enable Null)
     * e.g. alter-NG
     */
    public CDef.NgMark ngMark;

    /** list of editing sql files in dbflute_client/playsql/migration/alter directory */
    @Valid
    public List<SQLFilePart> editingFiles;

    /** checked sql zip file */
    @Valid
    public CheckedZipPart checkedZip;

    /** unreleased sql dir */
    @Valid
    public UnreleasedDirPart unreleasedDir;

    public static class CheckedZipPart {

        /** zip file name e.g. 20190831_2249/checked-alter-to-20190422-2332.zip */
        @NotNull
        public String fileName;

        /** list of checked sql files */
        @Valid
        public List<SQLFilePart> checkedFiles;
    }

    public static class UnreleasedDirPart {

        /** list of checked sql files */
        @Valid
        public List<SQLFilePart> checkedFiles;
    }

    public static class SQLFilePart {

        /** file name e.g. alter-sql-SAMPLE.sql */
        @NotNull
        public String fileName;

        /** file content e.g. ALTER TABLE MEMBER ADD MAIHAMA_VISITED VARCHAR(3); */
        @NotNull
        public String content;
    }
}
