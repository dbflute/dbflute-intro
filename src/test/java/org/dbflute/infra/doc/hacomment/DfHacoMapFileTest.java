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
package org.dbflute.infra.doc.hacomment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.dbflute.optional.OptionalThing;

/**
 * @author hakiba
 */
public class DfHacoMapFileTest extends UnitIntroTestCase {

    private static final String DIFF_DATE_1 = "2018/01/10 16:09:22";
    private static final String DIFF_DATE_2 = "2018/02/10 16:09:22";

    private DfHacoMapFile hacoMapFile = new DfHacoMapFile(() -> currentLocalDateTime());

    public void test_merge() throws Exception {
        // ## Arrange ##
        DfHacoMapPiece piece1 = createPiece(DIFF_DATE_1, "comment 1", "author1");
        DfHacoMapPiece piece2AfterPiece1 = createAfterPiece("comment 2", "author2", piece1);
        List<DfHacoMapPiece> pieces = Arrays.asList(piece1, piece2AfterPiece1);

        DfHacoMapPickup pickup = createPickup();
        OptionalThing<DfHacoMapPickup> optPickup = OptionalThing.of(pickup);

        // ## Act ##
        DfHacoMapPickup mergedPickup = hacoMapFile.merge(optPickup, pieces);

        // ## Assert ##
        // all pieces and pickup are merged
        List<String> pieceCodeListFromPickup = pickup.getDiffList()
                .stream()
                .flatMap(diffPart -> diffPart.getPropertyList().stream())
                .map(propertyPart -> propertyPart.pieceCode)
                .collect(Collectors.toList());
        pieceCodeListFromPickup.add(piece2AfterPiece1.pieceCode);
        List<String> pieceCodeListFromMergedPickup = mergedPickup.getDiffList()
                .stream()
                .flatMap(diffPart -> diffPart.getPropertyList().stream())
                .map(property -> property.pieceCode)
                .collect(Collectors.toList());
        pieceCodeListFromPickup.forEach(pieceCode -> {
            assertTrue(pieceCodeListFromMergedPickup.contains(pieceCode));
        });

        // not contain previous piece
        String previousPieceCode = piece1.pieceCode;
        DfHacoMapDiffPart diffPartForDiffDate1 =
                mergedPickup.getDiffList().stream().filter(diffPart -> DIFF_DATE_1.equals(diffPart.diffDate)).findAny().get();
        boolean notContainsPreviousPiece = diffPartForDiffDate1.getPropertyList()
                .stream()
                .map(propertyPart -> propertyPart.pieceCode)
                .noneMatch(pieceCode -> pieceCode.equals(previousPieceCode));
        assertTrue(notContainsPreviousPiece);
    }

    private DfHacoMapPiece createPiece(String diffDate, String hacomment, String pieceOwner) {
        final String diffCode = hacoMapFile.generateDiffCode(diffDate);
        final String diffComment = "";
        final List<String> authorList = Collections.emptyList();
        final String pieceCode = UUID.randomUUID().toString();
        final LocalDateTime pieceDateTime = currentLocalDateTime();
        final List<String> previousPieceList = Collections.emptyList();

        return new DfHacoMapPiece(diffCode, diffDate, hacomment, diffComment, authorList, pieceCode, pieceOwner, pieceDateTime,
                previousPieceList);
    }

    private DfHacoMapPiece createAfterPiece(String hacomment, String pieceOwner, DfHacoMapPiece previousPiece) {
        final String diffDate = previousPiece.diffDate;
        final String diffCode = previousPiece.diffCode;
        final String diffComment = previousPiece.diffComment;
        final List<String> authorList = new ArrayList<>(previousPiece.authorList);
        authorList.add(pieceOwner);
        final String pieceCode = UUID.randomUUID().toString();
        final LocalDateTime pieceDateTime = currentLocalDateTime();
        final List<String> previousPieceList = new ArrayList<>(previousPiece.previousPieceList);
        previousPieceList.add(previousPiece.pieceCode);

        return new DfHacoMapPiece(diffCode, diffDate, hacomment, diffComment, authorList, pieceCode, pieceOwner, pieceDateTime,
                previousPieceList);
    }

    private DfHacoMapPickup createPickup() {
        // create single property part
        final String diffDateForSingle = DIFF_DATE_1;
        final String diffCodeForSingle = hacoMapFile.generateDiffCode(diffDateForSingle);
        final DfHacoMapPropertyPart diffPartForSingle = createDiffPart();
        DfHacoMapDiffPart singlePropertyDiffPart =
                new DfHacoMapDiffPart(diffCodeForSingle, diffDateForSingle, Collections.singletonList(diffPartForSingle));

        // create multiple property part
        final String diffDateForMulti = DIFF_DATE_2;
        final String diffCodeForMulti = hacoMapFile.generateDiffCode(diffDateForMulti);
        final DfHacoMapPropertyPart diffPart1ForMulti = createDiffPart();
        final DfHacoMapPropertyPart diffPart2ForMulti = createDiffPart();
        final DfHacoMapPropertyPart diffPart3ForMulti = createDiffPart();
        final List<DfHacoMapPropertyPart> propertyPartList = Arrays.asList(diffPart1ForMulti, diffPart2ForMulti, diffPart3ForMulti);
        DfHacoMapDiffPart multiplePropertyDiffPart = new DfHacoMapDiffPart(diffCodeForMulti, diffDateForMulti, propertyPartList);

        // create diff part list
        List<DfHacoMapDiffPart> diffPartList = Arrays.asList(singlePropertyDiffPart, multiplePropertyDiffPart);

        // create pickup file
        DfHacoMapPickup pickup = new DfHacoMapPickup();
        pickup.addAllDiffList(diffPartList);
        return pickup;
    }

    private DfHacoMapPropertyPart createDiffPart() {
        final String diffComment = "";
        final List<String> authorList = Collections.emptyList();
        final String pieceCode = UUID.randomUUID().toString();
        final LocalDateTime pieceDateTime = currentLocalDateTime();
        final List<String> previousPieceList = Collections.emptyList();

        return new DfHacoMapPropertyPart("hacomment", diffComment, authorList, pieceCode, "pieceOwner", pieceDateTime, previousPieceList);
    }
}
