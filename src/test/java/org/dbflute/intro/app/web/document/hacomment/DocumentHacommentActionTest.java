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
package org.dbflute.intro.app.web.document.hacomment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.web.document.hacomment.HacommentPickupResult.DiffPart;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author hakiba
 * @author jflute
 */
public class DocumentHacommentActionTest extends UnitIntroTestCase {

    @Resource
    private DocumentAuthorLogic documentAuthorLogic;

    // ===================================================================================
    //                                                                                Save
    //                                                                                ====
    public void test_save() throws Exception {
        // ## Arrange ##
        DocumentHacommentAction action = new DocumentHacommentAction();
        inject(action);
        File pieceDir = getTestHacommentPieceDir();
        assertFalse(pieceDir.exists());
        assertFalse(pieceDir.isDirectory());

        // ## Act ##
        HacommentSaveBody body = createSaveBody();
        action.save(TEST_CLIENT_PROJECT, body);

        // ## Assert ##
        File pieceFile = verifyPieceFile(pieceDir);
        Map<String, Object> actualMap = new DfMapFile().readMap(new FileInputStream(pieceFile));
        log("[Saved Piece]: {}", pieceFile.getName());
        actualMap.forEach((key, value) -> {
            log("  {} = {}", key, value);
        });
        assertNotNull(actualMap.get("diffCode"));
        assertEquals(body.diffDate, actualMap.get("diffDate"));
        assertEquals(body.hacomment, actualMap.get("hacomment"));
        ArrayList<String> expectedAuthorsList = newArrayList(body.authors);
        expectedAuthorsList.add(documentAuthorLogic.getAuthor());
        assertEquals(expectedAuthorsList, actualMap.get("authorList"));
        assertNotNull(actualMap.get("pieceDatetime"));
        assertEquals(documentAuthorLogic.getAuthor(), actualMap.get("pieceOwner"));
        assertEquals(body.previousPieces, actualMap.get("previousPieceList"));
    }

    private HacommentSaveBody createSaveBody() {
        HacommentSaveBody body = new HacommentSaveBody();
        body.diffDate = "2018/02/20 22:30:22";
        body.hacomment = "sample hacomment";
        body.authors = Arrays.asList("hakiba", "cabos");
        body.previousPieces = Arrays.asList("FIRST_PIECE_CODE", "SECOND_PIECE_CODE");
        return body;
    }

    private File verifyPieceFile(File pieceDir) {
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        File[] pieceFiles = pieceDir.listFiles();
        assertNotNull(pieceFiles);
        assertEquals(1, pieceFiles.length);
        File pieceFile = pieceFiles[0];
        Pattern pattern = Pattern.compile("^hacomment-piece-diffdate\\d{14}-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        assertTrue(pattern.matcher(pieceFile.getName()).find());
        return pieceFile;
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_pickup() throws Exception {
        // ## Arrange ##
        prepareTestHacommentFiles();
        DocumentHacommentAction action = new DocumentHacommentAction();
        inject(action);

        // ## Act ##
        JsonResponse<HacommentPickupResult> response = action.pickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        showJson(response);
        validateJsonData(response);
        HacommentPickupResult result = response.getJsonResult();
        // done (by jflute) hakiba add Assertions by hakiba (2018/02/21)
        // sample assertion for now (it's enough) by jflute (2020/11/02)
        log(result);
        List<DiffPart> diffList = result.diffList;
        assertHasAnyElement(diffList);
        assertTrue(diffList.stream().anyMatch(diff -> "20180110160922".equals(diff.diffCode)));
    }
}
