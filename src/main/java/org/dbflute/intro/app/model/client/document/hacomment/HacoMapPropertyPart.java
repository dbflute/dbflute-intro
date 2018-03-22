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
package org.dbflute.intro.app.model.client.document.hacomment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dbflute.helper.HandyDate;

/**
 * @author hakiba
 */
public class HacoMapPropertyPart {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String hacomment;
    protected final String diffComment;
    protected final List<String> authorList;
    protected final String pieceCode;
    protected final String pieceOwner;
    protected final LocalDateTime pieceDatetime;
    protected final List<String> previousPieceList;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HacoMapPropertyPart(String hacomment, String diffComment, List<String> authorList, String pieceCode, String pieceOwner,
        LocalDateTime pieceDatetime, List<String> previousPieceList) {
        this.hacomment = hacomment;
        this.diffComment = diffComment;
        this.authorList = authorList;
        this.pieceCode = pieceCode;
        this.pieceOwner = pieceOwner;
        this.pieceDatetime = pieceDatetime;
        this.previousPieceList = previousPieceList;
    }

    public HacoMapPropertyPart(Map<String, Object> propertyMap) {
        this.hacomment = (String) propertyMap.get("hacomment");
        this.diffComment = (String) propertyMap.get("diffComment");
        this.authorList = ((List<?>) propertyMap.get("authorList")).stream()
            .filter(obj -> obj instanceof String)
            .map(obj -> (String) obj)
            .collect(Collectors.toList());
        this.pieceCode = (String) propertyMap.get("pieceCode");
        this.pieceOwner = (String) propertyMap.get("pieceOwner");
        this.pieceDatetime = new HandyDate((String) propertyMap.get("pieceDatetime")).getLocalDateTime();
        this.previousPieceList =
            ((List<?>) propertyMap.get("previousPieceList")).stream().map(obj -> (String) obj).collect(Collectors.toList());

    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getHacomment() {
        return hacomment;
    }

    public String getDiffComment() {
        return diffComment;
    }

    public List<String> getAuthorList() {
        return authorList;
    }

    public String getPieceCode() {
        return pieceCode;
    }

    public String getPieceOwner() {
        return pieceOwner;
    }

    public LocalDateTime getPieceDatetime() {
        return pieceDatetime;
    }

    public List<String> getPreviousPieceList() {
        return previousPieceList;
    }
}
