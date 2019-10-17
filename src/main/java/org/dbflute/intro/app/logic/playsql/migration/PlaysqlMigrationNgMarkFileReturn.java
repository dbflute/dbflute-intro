package org.dbflute.intro.app.logic.playsql.migration;

import org.dbflute.intro.dbflute.allcommon.CDef;

/**
 * @author cabos
 */
public class PlaysqlMigrationNgMarkFileReturn {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private CDef.NgMark ngMark;
    private String content;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public PlaysqlMigrationNgMarkFileReturn(CDef.NgMark ngMark, String content) {
        this.ngMark = ngMark;
        this.content = content;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public CDef.NgMark getNgMark() {
        return ngMark;
    }

    public String getContent() {
        return content;
    }
}
