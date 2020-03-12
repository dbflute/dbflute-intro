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
package org.dbflute.intro.app.logic.document.decomment;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.infra.doc.decomment.DfDecoMapFile;
import org.dbflute.infra.doc.decomment.DfDecoMapMapping;
import org.dbflute.infra.doc.decomment.DfDecoMapPickup;
import org.dbflute.infra.doc.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.logic.document.DocumentAuthorLogic;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;

/**
 * @author cabos
 * @author hakiba
 * @author jflute
 */
public class DocumentDecommentPhysicalLogic {

    // done cabos shugyo++: move it to infra's decomment classes, file name and path handling by jflute (2017/11/11)

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private DocumentAuthorLogic documentAuthorLogic;

    // done cabos why package scope? by jflute (2017/11/19)
    // done cabos add private modifier to _decoMapFile by jflute (2017/11/21)
    // done cabos instance variable should be under Attribute tag comment by jflute (2017/11/19)
    private final DfDecoMapFile _decoMapFile = new DfDecoMapFile(() -> timeManager.currentDateTime());

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void saveDecommentPiece(String clientName, DfDecoMapPiece decoMapPiece) {
        _decoMapFile.writePiece(buildClientPath(clientName), decoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // done hakiba tag comment: Pickup Map by jflute (2017/08/17)
    public DfDecoMapPickup readMergedPickup(String clientName) {
        List<DfDecoMapPiece> pieces = readDecommentPiece(clientName);
        OptionalThing<DfDecoMapPickup> pickupOpt = readDecommentPickup(clientName);
        List<DfDecoMapMapping> mappings = readDecommentMapping(clientName);
        return _decoMapFile.merge(pickupOpt, pieces, mappings);
    }

    // done hakoba public on demand, so private now by jflute (2017/08/17)
    private List<DfDecoMapPiece> readDecommentPiece(String clientName) {
        // done hakiba support no-existing directory by jflute (2017/09/28)
        return _decoMapFile.readPieceList(buildClientPath(clientName));
    }

    private List<DfDecoMapMapping> readDecommentMapping(String clientName) {
        return _decoMapFile.readMappingList(buildClientPath(clientName));
    }

    private OptionalThing<DfDecoMapPickup> readDecommentPickup(String clientName) {
        // done hakiba support no-existing directory or file by jflute (2017/09/28)
        return _decoMapFile.readPickup(buildClientPath(clientName));
    }

    // ===================================================================================
    //                                                                         Mapping Map
    //                                                                         ===========
    public void saveDecommentMapping(String clientName, List<DfDecoMapMapping> mappingList) {
        mappingList.forEach(mapping -> {
            _decoMapFile.writeMapping(buildClientPath(clientName), mapping);
        });
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public String getAuthor() {
        return documentAuthorLogic.getAuthor();
    }

    public OptionalThing<String> getGitBranch() {
        return documentAuthorLogic.getGitBranch();
    }

    private String buildClientPath(String clientName) {
        return introPhysicalLogic.buildClientPath(clientName);
    }
}
