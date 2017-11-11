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
package org.dbflute.intro.app.web.document.decomment;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.logic.document.decomment.DocumentDecommentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// done cabos decoment to decomment by jflute (2017/07/27)

/**
 * @author cabos
 * @author hakiba
 * @author deco
 */
public class DocumentDecommentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private DocumentDecommentPhysicalLogic decommentPhysicalLogic;

    // done cabos use _taexec and use _tanest by jflute (2017/08/10)
    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                             Piece Map
    //                                             ---------
    // done cabos post to save, get to diff by jflute (2017/07/27)
    // done cabos javadoc with e.g. value by jflute (2017/09/28)
    /**
     * save decomment piece map
     *
     * @param projectName project name e.g. maihamadb (NotNull)
     * @param body decomment save body (NotNull)
     * @return void (NotNull)
     */
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> save(String projectName, DecommentSaveBody body) {
        validate(body, messages -> {});
        decommentPhysicalLogic.saveDecommentPieceMap(projectName, mappingToDecoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    // done cabos use mappingTo... by jflute (2017/08/10)
    private DfDecoMapPiece mappingToDecoMapPiece(DecommentSaveBody body) {
        String author = getAuthor();
        DfDecoMapPiece pieceMap = new DfDecoMapPiece();
        pieceMap.setMerged(body.merged);
        pieceMap.setTableName(body.tableName);
        pieceMap.setColumnName(body.columnName);
        pieceMap.setTargetType(body.targetType);
        pieceMap.setDecomment(body.decomment);
        pieceMap.setDatabaseComment(body.databaseComment);
        pieceMap.setCommentVersion(body.commentVersion);
        pieceMap.setAuthorList(mergeAuthorList(body.authors, author));
        pieceMap.setPieceCode(buildPieceCode(body));
        pieceMap.setPieceDatetime(timeManager.currentDateTime());
        pieceMap.setPieceOwner(getAuthor());
        pieceMap.setPreviousPieceList(body.previousPieces);
        return pieceMap;
    }

    private String getAuthor() {
        return decommentPhysicalLogic.getAuthor();
    }

    private List<String> mergeAuthorList(List<String> authorList, String author) {
        // done cabos use LinkedHashSet to keep order by jflute (2017/09/07)
        Set<String> authorSet = new LinkedHashSet<>(authorList);
        authorSet.add(author);
        return new ArrayList<>(authorSet);
    }

    private String buildPieceCode(DecommentSaveBody body) {
        // TODO deco use tableName, columnName, date-time, owner by jflute (2017/11/11)
        return Integer.toHexString(body.hashCode());
    }

    // -----------------------------------------------------
    //                                            Pickup Map
    //                                            ----------
    // done hakiba rename piece to pickup by jflute (2017/08/17)
    /**
     * save decomment piece map
     *
     * @param projectName project name e.g. maihamadb (NotNull)
     * @return merged pickup as json (NotNull)
     */
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DecommentPickupResult> pickup(String projectName) {
        DfDecoMapPickup dfDecoMapPickup = decommentPhysicalLogic.readMergedDecommentPickupMap(projectName);
        return asJson(new DecommentPickupResult(dfDecoMapPickup.getTableList()));
    }
}
