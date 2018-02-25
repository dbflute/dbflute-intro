/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.logic.document.decomment;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author hakiba
 * @author jflute
 * @author cabos at garden place plaza
 * @author deco
 */
public class DocumentDecommentPhysicalLogicTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    // -----------------------------------------------------
    //                                        save piece map
    //                                        --------------
    public void test_saveDecommentPieceMap_init() throws Exception {
        // ## Arrange ##
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece"));

        // ## Act ##
        logic.saveDecommentPiece(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        assertTrue(pieceMaps.length > 0);
        Arrays.asList(pieceMaps).forEach(fileName -> {
            log(fileName);
            assertTrue(expFileNamePattern.matcher(fileName).find());
        });
    }

    public void test_saveDecommentPieceMap_prepared() throws Exception {
        // ## Arrange ##
        prepareTestDecommentFiles();

        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);
        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
        inject(physicalLogic);

        final Pattern expFileNamePattern = Pattern.compile("^decomment-piece-.+-\\d{8}-\\d{6}-\\d{3}-.+\\.dfmap$");
        final File pieceDir = new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece"));

        // ## Act ##
        logic.saveDecommentPiece(TEST_CLIENT_PROJECT, createDfDecoMapPiece());

        // ## Assert ##
        assertTrue(pieceDir.exists());
        assertTrue(pieceDir.isDirectory());
        String[] pieceMaps = pieceDir.list();
        assertNotNull(pieceMaps);
        assertTrue(pieceMaps.length > 0);
        Arrays.stream(pieceMaps).forEach(fileName -> {
            log(fileName);
            assertTrue(expFileNamePattern.matcher(fileName).find());
        });
    }

    private DfDecoMapPiece createDfDecoMapPiece() {
        return new DfDecoMapPiece(DfDecoMapPiece.DEFAULT_FORMAT_VERSION, "MEMBER", "MEMBER_NAME", DfDecoMapPieceTargetType.Column, "piari",
            "sea", 1L, Collections.singletonList("cabos"), "FE893L1", currentLocalDateTime(), "cabos",
            "develop", Collections.singletonList("FE893L1"));
    }

    // ===================================================================================
    //                                                                              Pickup
    //                                                                              ======
    public void test_readMergedDecommentPickupMap_init() throws Exception {
        // ## Arrange ##
        // done (by cabos) hakiba null pointer at init state by jflute (2017/10/05)
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedPickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
    }

    public void test_readMergedDecommentPickupMap_prepared() throws Exception {
        // done hakiba put test data in test/resources by hakiba (2017/08/18)
        // ## Arrange ##
        prepareTestDecommentFiles();
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        // done hakiba use testdb by jflute (2017/08/17)
        DfDecoMapPickup pickup = logic.readMergedPickup(TEST_CLIENT_PROJECT);

        // ## Assert ##
        // Assert by visual confirmation
        log(ln() + pickup);
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public void test_getAuthor() throws Exception {
        // ## Arrange ##
        registerMock(new DocumentAuthorLogic() {
            @Override
            public String getAuthor() {
                return "ca/<bo s";
            }
        });
        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
        inject(logic);

        // ## Act ##
        String author = logic.getAuthor();

        // ## Assert ##
        log("author: {}", author);
        assertNotNull(author);
        assertEquals("ca/<bo s", author);
    }
}
