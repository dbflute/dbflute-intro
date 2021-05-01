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
 * The logic for hacomment physical operation. <br>
 * Hacomment is a function to add comment for SchemaHTML. <br>
 * You can handle e.g. hacomment-pickup.dfmap, hacomment-piece-...dfmap by this logic.
 * @author hakiba
 * @author jflute
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
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // piece file:
    //  o Intro creates when hacomment input
    //  o DBFlute Engine reads when creating pickup
    // _/_/_/_/_/_/_/_/_/_/
    public void savePiece(String projectName, DfHacoMapPiece hacoMapPiece) {
        _hacoMapFile.writePiece(buildClientPath(projectName), hacoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
    // pickup file:
    //  o DBFlute Engine creates when Doc task
    //  o Intro reads 
    // _/_/_/_/_/_/_/_/_/_/
    public DfHacoMapPickup readMergedPickup(String projectName) {
        List<DfHacoMapPiece> pieces = readHacommentPiece(projectName);
        OptionalThing<DfHacoMapPickup> pickupOpt = readHacommentPickup(projectName);
        return _hacoMapFile.merge(pickupOpt, pieces);
    }

    private List<DfHacoMapPiece> readHacommentPiece(String projectName) {
        return _hacoMapFile.readPieceList(buildClientPath(projectName));
    }

    private OptionalThing<DfHacoMapPickup> readHacommentPickup(String projectName) {
        return _hacoMapFile.readPickup(buildClientPath(projectName));
    }

    private String buildClientPath(String projectName) {
        return introPhysicalLogic.buildClientPath(projectName);
    }

    // ===================================================================================
    //                                                                           Diff Code
    //                                                                           =========
    public String generateDiffCode(String diffDate) {
        return _hacoMapFile.generateDiffCode(diffDate);
    }
}
