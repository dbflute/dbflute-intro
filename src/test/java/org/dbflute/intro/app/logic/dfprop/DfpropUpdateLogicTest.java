package org.dbflute.intro.app.logic.dfprop;

import javax.annotation.Resource;

import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author jflute
 */
public class DfpropUpdateLogicTest extends UnitIntroTestCase {

    @Resource
    private DfpropInfoLogic dfpropInfoLogic;

    // ===================================================================================
    //                                                                replaceDocumentMap()
    //                                                                ====================
    public void test_replaceDocumentMap_basic() {
        // ## Arrange ##
        String testClientProject = getTestClientProject();
        DfpropUpdateLogic logic = new DfpropUpdateLogic();
        inject(logic);
        DocumentMap documentMap = new DocumentMap();
        documentMap.setAliasDelimiterInDbComment("@@@");
        documentMap.setDbCommentOnAliasBasis(false);

        {
            // ## Act ##
            logic.replaceDocumentMap(testClientProject, documentMap);

            // ## Assert ##
            DocumentMap actual = dfpropInfoLogic.findDocumentMap(testClientProject);
            log("actual: {}", actual);
            assertEquals(documentMap.getAliasDelimiterInDbComment(), actual.getAliasDelimiterInDbComment());
            assertEquals(documentMap.isDbCommentOnAliasBasis(), actual.isDbCommentOnAliasBasis());
        }
        {
            // re-arrange
            documentMap.setAliasDelimiterInDbComment("+++");
            documentMap.setDbCommentOnAliasBasis(true);

            // ## Act ##
            logic.replaceDocumentMap(testClientProject, documentMap);

            // ## Assert ##
            DocumentMap actual = dfpropInfoLogic.findDocumentMap(testClientProject);
            log("actual: {}", actual);
            assertEquals(documentMap.getAliasDelimiterInDbComment(), actual.getAliasDelimiterInDbComment());
            assertEquals(documentMap.isDbCommentOnAliasBasis(), actual.isDbCommentOnAliasBasis());
        }
    }
}
