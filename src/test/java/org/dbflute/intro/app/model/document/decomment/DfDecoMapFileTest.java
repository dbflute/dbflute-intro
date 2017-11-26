/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.model.document.decomment;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.dbflute.infra.doc.decomment.DfDecoMapFile;
import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.optional.OptionalThing;

/**
 * @author hakiba
 * @author cabos
 */
public class DfDecoMapFileTest extends UnitIntroTestCase {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final Long LATEST_COMMENT_VERSION = 3L;

    // ===================================================================================
    //                                                                               Merge
    //                                                                               =====
    public void test_mergeAllColumnComment() throws Exception {
        // ## Arrange ##
        DfDecoMapFile decoMapFile = new DfDecoMapFile();
        OptionalThing<DfDecoMapPickup> optPickup = OptionalThing.empty(); // not exists pickup
        LocalDateTime now = currentLocalDateTime();
        DfDecoMapPiece piece1 = preparePiece("MEMBER", "MEMBER_NAME", "hakiba", LATEST_COMMENT_VERSION, now);
        DfDecoMapPiece piece2 = preparePiece("MEMBER", "MEMBER_STATUS", "cabos", LATEST_COMMENT_VERSION, now);
        DfDecoMapPiece piece3 = preparePiece("PURCHASE", "PURCHASE_PRODUCT", "cabos", LATEST_COMMENT_VERSION, now);
        List<DfDecoMapPiece> pieceList = Arrays.asList(piece1, piece2, piece3);

        // ## Act ##
        DfDecoMapPickup result = decoMapFile.merge(optPickup, pieceList);

        // ## Assert ##
        assertNotNull(result);
        log(result);
        // assert all table and column
        long columnCount = result.getTableList()
                .stream()
                .map(tablePart -> tablePart.getColumnList())
                .flatMap(columnParts -> columnParts.stream())
                .count();
        assertEquals(3, columnCount);
    }

    private DfDecoMapPiece preparePiece(String tableName, String columnName, String author, long commentVersion,
            LocalDateTime decommentDateTime) {
        DfDecoMapPiece piece = new DfDecoMapPiece();
        piece.setFormatVersion("1.0");
        piece.setTableName(tableName);
        piece.setColumnName(columnName);
        piece.setTargetType(DfDecoMapPieceTargetType.Column);
        piece.setDecomment("decomment");
        piece.setDatabaseComment("databasecomment");
        piece.setCommentVersion(commentVersion);
        piece.addAllAuthors(Collections.singletonList(author));
        piece.setPieceCode("DE000000");
        piece.setPieceDatetime(decommentDateTime);
        piece.setPieceOwner(author);
        piece.addAllPreviousPieces(Collections.emptyList());
        return piece;
    }

    // hakiba's memorable code by cabos (2017 pocky day)
    //public void test_merge_latest_comment_version() throws Exception {
    //    // ## Arrange ##
    //    DfDecoMapFile decoMapFile = new DfDecoMapFile();
    //    OptionalThing<DfDecoMapPickup> optPickup = preparePickup();
    //    LocalDateTime now = currentLocalDateTime();
    //    DfDecoMapPiece oldPiece1 = preparePiece("MEMBER", "MEMBER_NAME", "hakiba", LATEST_COMMENT_VERSION - 1, now);
    //    DfDecoMapPiece latestPiece1 = preparePiece("MEMBER", "MEMBER_NAME", "hakiba", LATEST_COMMENT_VERSION, now);
    //    DfDecoMapPiece latestPiece2 = preparePiece("MEMBER", "MEMBER_STATUS", "cabos", LATEST_COMMENT_VERSION, now);
    //    List<DfDecoMapPiece> pieceList = Arrays.asList(oldPiece1, latestPiece1, latestPiece2);
    //
    //    // ## Act ##
    //    DfDecoMapPickup result = decoMapFile.merge(optPickup, pieceList);
    //
    //    // ## Assert ##
    //    assertNotNull(result);
    //    // assert all comment version are latest
    //    result.getTableList()
    //        .stream()
    //        .map(tablePart -> tablePart.getColumnList())
    //        .flatMap(columnParts -> columnParts.stream())
    //        .map(columnPart -> columnPart.getPropertyList())
    //        .flatMap(propertyParts -> propertyParts.stream())
    //        .map(columnPropertyPart -> columnPropertyPart.getCommentVersion())
    //        .forEach(commentVersion -> assertEquals(commentVersion, LATEST_COMMENT_VERSION));
    //}
    //
    //private OptionalThing<DfDecoMapPickup> preparePickup() {
    //    DfDecoMapPropertyPart propertyPart = new DfDecoMapPropertyPart();
    //    propertyPart.setDecomment("decomment");
    //    propertyPart.setDatabaseComment("databasecomment");
    //    propertyPart.setAuthorList(Collections.singletonList("hakiba"));
    //    propertyPart.setCommentVersion(LATEST_COMMENT_VERSION);
    //
    //    DfDecoMapColumnPart columnPart = new DfDecoMapColumnPart();
    //    columnPart.setColumnName("MEMBER");
    //    columnPart.setPropertyList(Collections.singletonList(propertyPart));
    //
    //    DfDecoMapTablePart tablePart = new DfDecoMapTablePart();
    //    tablePart.setTableName("MEMBER_NAME");
    //    tablePart.setColumnList(Collections.singletonList(columnPart));
    //    tablePart.setPropertyList(Collections.singletonList(propertyPart));
    //
    //    DfDecoMapPickup pickup = new DfDecoMapPickup();
    //    pickup.setFormatVersion("1.0");
    //    pickup.setTableList(Collections.singletonList(tablePart));
    //
    //    return OptionalThing.of(pickup);
    //}
}
