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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.dbflute.infra.doc.hacomment.DfHacoMapDiffPart;
import org.dbflute.infra.doc.hacomment.DfHacoMapPickup;
import org.dbflute.infra.doc.hacomment.DfHacoMapPropertyPart;
import org.lastaflute.core.util.Lato;
import org.lastaflute.web.validation.Required;

/**
 * @author hakiba
 * @author jflute
 */
public class HacommentPickupResult {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Valid
    @NotNull
    public List<DiffPart> diffList;

    public static class DiffPart {

        @Required
        public String diffCode;

        @Required
        public String diffDate;

        @Valid
        @NotNull
        public List<PropertyPart> properties;

        // ===================================================================================
        //                                                                         Constructor
        //                                                                         ===========
        public DiffPart(DfHacoMapDiffPart diffPart) {
            this.diffCode = diffPart.getDiffCode();
            this.diffDate = diffPart.getDiffDate();
            this.properties = diffPart.getPropertyList().stream().map(property -> new PropertyPart(property)).collect(Collectors.toList());
        }
    }

    public static class PropertyPart {

        // ===================================================================================
        //                                                                           Attribute
        //                                                                           =========
        @Required
        public String hacomment;

        public String diffComment;

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
        public PropertyPart(DfHacoMapPropertyPart property) {
            this.hacomment = property.getHacomment();
            this.diffComment = property.getDiffComment();
            this.authorList = property.getAuthorList();
            this.pieceCode = property.getPieceCode();
            this.pieceOwner = property.getPieceOwner();
            this.pieceDatetime = property.getPieceDatetime();
            this.previousPieceList = property.getPreviousPieceList();
        }
    }

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HacommentPickupResult(DfHacoMapPickup pickup) {
        this.diffList = pickup.getDiffList().stream().map(diffPart -> new DiffPart(diffPart)).collect(Collectors.toList());
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return Lato.string(this);
    }
}
