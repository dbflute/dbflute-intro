package org.dbflute.intro.app.web.alter;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.lastaflute.web.validation.Required;

/**
 * @author cabos
 */
public class AlterSQLResult {

    /**
     * Alter Check Result
     * e.g. alter-NG
     */
    @Required
    public CDef.NgMark ngMark;

    /** list of editing sql files in dbflute_client/playsql/migration/alter directory */
    @Valid
    public List<AlterSQLFilePart> editingFiles;

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
        public List<AlterSQLFilePart> checkedFiles;
    }

    public static class UnreleasedDirPart {

        /** list of checked sql files */
        @Valid
        public List<AlterSQLFilePart> checkedFiles;
    }

    public static class AlterSQLFilePart {

        /** sql file name e.g. alter-sql-SAMPLE.sql */
        @NotNull
        public String fileName;

        /** sql file content e.g. ALTER TABLE MEMBER ADD MAIHAMA_VISITED VARCHAR(3); */
        @NotNull
        public String content;
    }
}
