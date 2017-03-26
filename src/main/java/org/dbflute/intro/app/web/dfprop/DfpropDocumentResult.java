package org.dbflute.intro.app.web.dfprop;

import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;

/**
 * @author deco
 */
public class DfpropDocumentResult {

    public final Boolean upperCaseBasic;
    public final String aliasDelimiterInDbComment;
    public final Boolean dbCommentOnAliasBasis;

    public DfpropDocumentResult(LittleAdjustmentMap tableNameUpperInfo, DocumentMap documentMap) {
        upperCaseBasic =
            tableNameUpperInfo.isTableDispNameUpperCase &&
            tableNameUpperInfo.isTableSqlNameUpperCase &&
            tableNameUpperInfo.isColumnSqlNameUpperCase;
        aliasDelimiterInDbComment = documentMap.getAliasDelimiterInDbComment().orElse(null);
        dbCommentOnAliasBasis = documentMap.isDbCommentOnAliasBasis();
    }
}
