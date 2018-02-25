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
package org.dbflute.intro.app.web.document.hacomment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPickup;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPiece;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 */
public class HaccomentPickupResult {

    @Valid
    @NotNull
    public List<HacoMap> hacoMaps;

    public static class HacoMap {
        @Required
        public String diffDate;

        @Required
        public String hacomment;

        @Valid
        @Required
        public List<String> authorList;

        @Required
        public String pieceCode;

        @Required
        public String pieceOwner;

        @Required
        public LocalDateTime pieceDatetime;

        @Valid
        @NotNull
        public List<String> previousPieceList;

        // ===================================================================================
        //                                                                         Constructor
        //                                                                         ===========
        public HacoMap(HacoMapPiece piece) {
            this.diffDate = piece.getDiffDate();
            this.hacomment = piece.getHacomment();
            this.authorList = piece.getAuthorList();
            this.pieceCode = piece.getPieceCode();
            this.pieceOwner = piece.getPieceOwner();
            this.pieceDatetime = piece.getPieceDatetime();
            this.previousPieceList = piece.getPreviousPieceList();
        }
    }

    public HaccomentPickupResult(HacoMapPickup pickup) {
        this.hacoMaps = pickup.getHacoMap().stream().map(piece -> new HacoMap(piece)).collect(Collectors.toList());
    }
}
