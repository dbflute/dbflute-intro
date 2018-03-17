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

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.logic.document.hacomment.DocumentHacommentPhysicalLogic;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapFile;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPickup;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPiece;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author hakiba
 */
public class DocumentHacommentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private DocumentHacommentPhysicalLogic hacommentPhysicalLogic;
    @Resource
    private DocumentAuthorLogic authorLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> save(String projectName, HacommentSaveBody body) {
        validate(body, messages -> moreValidate(body));
        // TODO hakiba add client error message by hakiba (2018/02/15)
        // verifyOrClientError(buildDebugMessageColumnNameIsNull(body), existsColumnNameIfTargetTypeColumn(body));
        hacommentPhysicalLogic.savePiece(projectName, mappingToHacoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    private void moreValidate(HacommentSaveBody body) {
        // TODO hakiba add validate logic by hakiba (2018/02/15)
    }

    private HacoMapPiece mappingToHacoMapPiece(HacommentSaveBody body) {
        String author = authorLogic.getAuthor();
        LocalDateTime mappingDateTime = timeManager.currentDateTime();
        String mappingCode = buildPieceCode(body, mappingDateTime, author);
        String diffCode = HacoMapFile.generateDiffCode(body.diffDate);
        return new HacoMapPiece(diffCode, body.diffDate, body.hacomment, body.authors, mappingCode, author, mappingDateTime,
            body.previousPieces);
    }

    private String buildPieceCode(HacommentSaveBody body, LocalDateTime mappingDateTime, String author) {
        StringBuilder sb = new StringBuilder();
        sb.append(body.diffDate);
        sb.append(":").append(mappingDateTime);
        sb.append(":").append(author);
        return Integer.toHexString(sb.toString().hashCode());
    }

    @Execute(urlPattern = "{}/@word")
    public JsonResponse<HaccomentPickupResult> pickup(String projectName) {
        HacoMapPickup hacoMapPickup = hacommentPhysicalLogic.readMergedPickup(projectName);
        return asJson(new HaccomentPickupResult(hacoMapPickup));
    }
}
