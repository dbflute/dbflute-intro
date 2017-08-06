package org.dbflute.intro.app.web.document.decomment;

import static org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart;
import static org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart.DecommentColumnPart;

import java.util.Arrays;
import java.util.Collections;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;

/**
 * @author cabos
 */
public class DocumentDecommentActionTest extends UnitIntroTestCase {

    @Test
    public void test_save_createHakiMap() throws Exception {
        // ## Arrange ##
        DocumentDecommentAction action = new DocumentDecommentAction();
        inject(action);
        DecommentPostBody body = createSampleBody();

        // ## Act ##
        action.save("introdb", body);

        // ## Assert ##
        // Assert by visual confirmation
    }

    private DecommentPostBody createSampleBody() {
        DecommentPostBody body = new DecommentPostBody();
        body.merged = false;
        body.table = createSampleTablePart();
        return body;
    }

    private DecommentTablePart createSampleTablePart() {
        DecommentTablePart tablePart = new DecommentTablePart();
        tablePart.tableName = "TABLE_NAME";
        tablePart.columns = Collections.singletonList(createSampleColumnPart());
        return tablePart;
    }

    private DecommentColumnPart createSampleColumnPart() {
        DecommentColumnPart columnPart = new DecommentColumnPart();
        columnPart.authorList = Arrays.asList("cabos", "sudachi");
        columnPart.columnName = "COLUMN_NAME";
        columnPart.commentVersion = 1L;
        columnPart.decomment = "orange";
        columnPart.databaseComment = "rime";
        columnPart.previousWholeComment = "lemon";
        return columnPart;
    }
}
