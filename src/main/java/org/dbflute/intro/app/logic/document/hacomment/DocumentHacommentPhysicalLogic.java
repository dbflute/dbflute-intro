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
package org.dbflute.intro.app.logic.document.hacomment;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapFile;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPickup;
import org.dbflute.intro.app.model.client.document.hacomment.HacoMapPiece;
import org.dbflute.optional.OptionalThing;

/**
 * @author hakiba
 */
public class DocumentHacommentPhysicalLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    private final HacoMapFile _hacoMapFile = new HacoMapFile();

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void savePiece(String clientProject, HacoMapPiece hacoMapPiece) {
        _hacoMapFile.writePiece(buildClientPath(clientProject), hacoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    public HacoMapPickup readMergedPickup(String clientProject) {
        List<HacoMapPiece> pieces = readHacommentPiece(clientProject);
        OptionalThing<HacoMapPickup> pickupOpt = readHacommentPickup(clientProject);
        return _hacoMapFile.merge(pickupOpt, pieces);
    }

    private List<HacoMapPiece> readHacommentPiece(String clientProject) {
        return _hacoMapFile.readPieceList(buildClientPath(clientProject));
    }

    private OptionalThing<HacoMapPickup> readHacommentPickup(String clientProject) {
        return _hacoMapFile.readPickup(buildClientPath(clientProject));
    }

    private String buildClientPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject);
    }
}
