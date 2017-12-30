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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.document.decomment.DocumentDecommentPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.core.util.LaStringUtil;
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
        // done cabos validate columnName exists if target type is COLUMN in more validation by jflute (2017/11/11)
        // this is as client error so you can use verifyOrClientError(debugMsg, expectedBool);
        validate(body, messages -> moreValidate(body, messages));
        verifyOrClientError(buildDebugMessage(body), existsColumnNameIfTargetTypeColumn(body));
        decommentPhysicalLogic.saveDecommentPiece(projectName, mappingToDecoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    private void moreValidate(DecommentSaveBody body, IntroMessages messages) {
        if (DfDecoMapPieceTargetType.Column == body.targetType && StringUtils.isEmpty(body.columnName)) {
            messages.addConstraintsNotEmptyMessage("columnName");
        }
    }

    // done cabos unneeded public here, change to private by jflute (2017/11/12)
    private String buildDebugMessage(DecommentSaveBody body) {
        StringBuilder sb = new StringBuilder();
        sb.append("If targetType is COLUMN, columnName must exists.").append("\n");
        sb.append("   targetType : ").append(body.targetType.code()).append("\n");
        sb.append("   columnName : ").append(body.columnName).append("\n");
        return sb.toString();
    }

    // done cabos change exist to exists for boolean expression by jflute (2017/11/12)
    private boolean existsColumnNameIfTargetTypeColumn(DecommentSaveBody body) {
        return !(DfDecoMapPieceTargetType.Column == body.targetType && LaStringUtil.isEmpty(body.columnName));
    }

    // done cabos use mappingTo... by jflute (2017/08/10)
    private DfDecoMapPiece mappingToDecoMapPiece(DecommentSaveBody body) {
        // done cabos rename pieceMap to piece (can be simple here) by jflute (2017/11/11)
        String author = getAuthor();
        List<String> authorList = new ArrayList<>(body.authors);
        authorList.add(author);
        LocalDateTime pieceDatetime = timeManager.currentDateTime();
        DfDecoMapPiece piece = new DfDecoMapPiece(DfDecoMapPiece.DEFAULT_FORMAT_VERSION, body.tableName, body.columnName, body.targetType,
                body.decomment, body.databaseComment, body.commentVersion, authorList, buildPieceCode(body, pieceDatetime, author),
                pieceDatetime, author, body.previousPieces);
        return piece;
    }

    private String getAuthor() {
        return decommentPhysicalLogic.getAuthor();
    }

    private String buildPieceCode(DecommentSaveBody body, LocalDateTime pieceDateTime, String author) {
        // done (by cabos) deco use tableName, columnName, date-time, owner by jflute (2017/11/11)
        String hashString = body.tableName + body.columnName + pieceDateTime.toString() + author;
        return Integer.toHexString(hashString.hashCode());
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
        DfDecoMapPickup dfDecoMapPickup = decommentPhysicalLogic.readMergedPickup(projectName);
        return asJson(new DecommentPickupResult(dfDecoMapPickup.getTableList()));
    }
}
