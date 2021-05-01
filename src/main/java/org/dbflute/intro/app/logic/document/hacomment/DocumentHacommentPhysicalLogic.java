/*
 * Copyright 2014-2021 the original author or authors.
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
package org.dbflute.intro.app.logic.document.hacomment;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.infra.doc.hacomment.DfHacoMapFile;
import org.dbflute.infra.doc.hacomment.DfHacoMapPickup;
import org.dbflute.infra.doc.hacomment.DfHacoMapPiece;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;

/**
 * @author hakiba
 */
public class DocumentHacommentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    private final DfHacoMapFile _hacoMapFile = new DfHacoMapFile(() -> timeManager.currentDateTime());

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void savePiece(String clientName, DfHacoMapPiece hacoMapPiece) {
        _hacoMapFile.writePiece(buildClientPath(clientName), hacoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    public DfHacoMapPickup readMergedPickup(String clientName) {
        List<DfHacoMapPiece> pieces = readHacommentPiece(clientName);
        OptionalThing<DfHacoMapPickup> pickupOpt = readHacommentPickup(clientName);
        return _hacoMapFile.merge(pickupOpt, pieces);
    }

    private List<DfHacoMapPiece> readHacommentPiece(String clientName) {
        return _hacoMapFile.readPieceList(buildClientPath(clientName));
    }

    private OptionalThing<DfHacoMapPickup> readHacommentPickup(String clientName) {
        return _hacoMapFile.readPickup(buildClientPath(clientName));
    }

    private String buildClientPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName);
    }

    // ===================================================================================
    //                                                                           Diff Code
    //                                                                           =========
    public String generateDiffCode(String diffDate) {
        return _hacoMapFile.generateDiffCode(diffDate);
    }
}
