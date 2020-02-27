/*
 * Copyright 2014-2019 the original author or authors.
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
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.infra.doc.decomment.DfDecoMapMapping;
import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.infra.doc.decomment.DfDecoMapPieceTargetType;
import org.dbflute.intro.app.logic.document.decomment.DocumentDecommentPhysicalLogic;
import org.dbflute.intro.app.logic.intro.IntroSystemLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.action.IntroMessages;
import org.dbflute.optional.OptionalThing;
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
    //                                                                          Definition
    //                                                                          ==========
    private static final String STRING_OF_NULL = "null";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private DocumentDecommentPhysicalLogic decommentPhysicalLogic;
    @Resource
    private IntroSystemLogic introSystemLogic;

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
     * @param clientName project name e.g. maihamadb (NotNull)
     * @param body decomment save body (NotNull)
     * @return void (NotNull)
     */
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> save(String clientName, DecommentSaveBody body) {
        // done cabos validate columnName exists if target type is COLUMN in more validation by jflute (2017/11/11)
        // this is as client error so you can use verifyOrClientError(debugMsg, expectedBool);
        validate(body, messages -> moreValidate(body, messages));
        verifyOrClientError(buildDebugMessageColumnNameIsNull(body), hasColumnNameWhenTargetTypeIsColumn(body));
        verifyOrClientError(buildDebugMessageInputAuthorIsNull(body), hasInputAuthorWhenIntroServer(body));
        decommentPhysicalLogic.saveDecommentPiece(clientName, mappingToDecoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    private void moreValidate(DecommentSaveBody body, IntroMessages messages) {
        if (STRING_OF_NULL.equals(body.decomment)) {
            messages.addErrorsStringOfNullNotAccepted("decomment");
        }
    }

    // done cabos unneeded public here, change to private by jflute (2017/11/12)
    private String buildDebugMessageColumnNameIsNull(DecommentSaveBody body) {
        StringBuilder sb = new StringBuilder();
        sb.append("If targetType is COLUMN, columnName must exists.").append("\n");
        sb.append("   targetType : ").append(body.targetType.code()).append("\n");
        sb.append("   columnName : ").append(body.columnName).append("\n");
        return sb.toString();
    }

    private String buildDebugMessageInputAuthorIsNull(DecommentSaveBody body) {
        StringBuilder sb = new StringBuilder();
        sb.append("If run with intro server mode, input author must exists.").append("\n");
        sb.append("   inputAuthor : ").append(body.inputAuthor).append("\n");
        return sb.toString();
    }

    // done cabos change exist to exists for boolean expression by jflute (2017/11/12)
    private boolean hasColumnNameWhenTargetTypeIsColumn(DecommentSaveBody body) {
        return DfDecoMapPieceTargetType.Column != body.targetType || LaStringUtil.isNotEmpty(body.columnName);
    }

    private boolean hasInputAuthorWhenIntroServer(DecommentSaveBody body) {
        return !introSystemLogic.isDecommentServer() || LaStringUtil.isNotEmpty(body.inputAuthor);
    }

    // done cabos use mappingTo... by jflute (2017/08/10)
    private DfDecoMapPiece mappingToDecoMapPiece(DecommentSaveBody body) {
        // done cabos rename pieceMap to piece (can be simple here) by jflute (2017/11/11)
        String author = introSystemLogic.isDecommentServer() ? body.inputAuthor : getAuthor();
        String gitBranch = getGitBranch().orElse(null);
        LocalDateTime pieceDatetime = timeManager.currentDateTime();
        DfDecoMapPiece piece =
                new DfDecoMapPiece(DfDecoMapPiece.DEFAULT_FORMAT_VERSION, body.tableName, body.columnName, body.targetType, body.decomment,
                        body.databaseComment, body.commentVersion, body.authors, buildPieceCode(body, pieceDatetime, author), pieceDatetime,
                        author, gitBranch, body.previousPieces);
        return piece;
    }

    private OptionalThing<String> getGitBranch() {
        return decommentPhysicalLogic.getGitBranch();
    }

    private String buildPieceCode(DecommentSaveBody body, LocalDateTime pieceDateTime, String author) {
        // done (by cabos) deco use tableName, columnName, date-time, owner by jflute (2017/11/11)
        StringBuilder sb = new StringBuilder();
        sb.append(body.tableName);
        sb.append(":").append(body.columnName != null ? body.columnName : "");
        sb.append(":").append(body.targetType);
        sb.append(":").append(pieceDateTime);
        sb.append(":").append(author);
        return Integer.toHexString(sb.toString().hashCode());
    }

    // -----------------------------------------------------
    //                                            Pickup Map
    //                                            ----------
    // done hakiba rename piece to pickup by jflute (2017/08/17)
    /**
     * save decomment piece map
     *
     * @param clientName project name e.g. maihamadb (NotNull)
     * @return merged pickup as json (NotNull)
     */
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DecommentPickupResult> pickup(String clientName) {
        DfDecoMapPickup pickup = decommentPhysicalLogic.readMergedPickup(clientName);
        return asJson(new DecommentPickupResult(pickup.getTableList()));
    }

    // -----------------------------------------------------
    //                                               Mapping
    //                                               -------
    /**
     * save decomment mapping map
     *
     * @param clientName project name e.g. maihamadb (NotNull)
     * @param body decomment mapping save body (NotNull)
     * @return void (NotNull)
     */
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> mapping(String clientName, DecommentMappingSaveBody body) {
        validate(body, messages -> {});
        verifyOrClientError(buildDebugMessageColumnNameIsNull(body), hasColumnNameWhenTargetTypeIsColumn(body));
        decommentPhysicalLogic.saveDecommentMapping(clientName, mappingToDecoMapMapping(body));
        return JsonResponse.asEmptyBody();
    }

    private String buildDebugMessageColumnNameIsNull(DecommentMappingSaveBody body) {
        // done cabos be client error by jflute (2018/02/22)
        StringBuilder sb = new StringBuilder();
        sb.append("If targetType is COLUMN, columnName must exists.").append("\n");
        body.mappings.forEach(mapping -> {
            sb.append("\n");
            if (!hasColumnNameWhenTargetTypeIsColumn(mapping)) {
                sb.append("   targetType(?) : ").append(mapping.targetType.code()).append("\n");
            } else {
                sb.append("   targetType    : ").append(mapping.targetType.code()).append("\n");
            }
            {
                sb.append("   oldColumnName : ").append(mapping.oldColumnName).append("\n");
                sb.append("   newColumnName : ").append(mapping.newColumnName).append("\n");
            }
        });
        sb.append("\n");
        return sb.toString();
    }

    // done cabos consider non TargetTypeColumn by jflute (2018/04/12)
    private boolean hasColumnNameWhenTargetTypeIsColumn(DecommentMappingSaveBody body) {
        return body.mappings.stream().allMatch(this::hasColumnNameWhenTargetTypeIsColumn);
    }

    private boolean hasColumnNameWhenTargetTypeIsColumn(DecommentMappingSaveBody.MappingPart mapping) {
        return DfDecoMapPieceTargetType.Column != mapping.targetType
                || LaStringUtil.isNotEmpty(mapping.oldColumnName) && LaStringUtil.isNotEmpty(mapping.newColumnName);
    }

    // done cabos move under existsColumn...() by jflute (2018/04/12)
    private List<DfDecoMapMapping> mappingToDecoMapMapping(DecommentMappingSaveBody body) {
        // done cabos move getAuthor() to class rear as Assist Logic tag comment by jflute (2018/04/12)
        LocalDateTime mappingDateTime = timeManager.currentDateTime();

        return body.mappings.stream().map(mapping -> {
            String author = getMappingAuthor(mapping);
            String mappingCode = buildMappingCode(mapping, mappingDateTime, author);
            return new DfDecoMapMapping(DfDecoMapMapping.DEFAULT_FORMAT_VERSION, mapping.oldTableName, mapping.oldColumnName,
                    mapping.newTableName, mapping.newColumnName, mapping.targetType, mapping.authors, mappingCode, author, mappingDateTime,
                    mapping.previousMappings);
        }).collect(Collectors.toList());
    }

    private String getMappingAuthor(DecommentMappingSaveBody.MappingPart mapping) {
        if (introSystemLogic.isDecommentServer()) {
            return mapping.author;
        } else {
            return getAuthor();
        }
    }

    private String buildMappingCode(DecommentMappingSaveBody.MappingPart mapping, LocalDateTime mappingDateTime, String author) {
        // done cabos write example comment by jflute (2018/04/12)
        // ex1. OLD_TABLE_NAME::NEW_TABLE_NAME::TABLE:2018/04/01T00:00:00:cabos
        // ex2. OLD_TABLE_NAME:OLD_COLUMN_NAME:NEW_TABLE_NAME:NEW_COLUMN_NAME:COLUMN:2018/04/01T00:00:00:cabos
        StringBuilder sb = new StringBuilder();
        sb.append(mapping.oldTableName);
        // done cabos change oldTableName to oldColumnName by jflute (2018/04/12)
        sb.append(":").append(mapping.oldColumnName != null ? mapping.oldColumnName : "");
        sb.append(mapping.newTableName);
        sb.append(":").append(mapping.newColumnName != null ? mapping.newColumnName : "");
        sb.append(":").append(mapping.targetType);
        sb.append(":").append(mappingDateTime);
        sb.append(":").append(author);
        return Integer.toHexString(sb.toString().hashCode());
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private String getAuthor() {
        return decommentPhysicalLogic.getAuthor();
    }
}
