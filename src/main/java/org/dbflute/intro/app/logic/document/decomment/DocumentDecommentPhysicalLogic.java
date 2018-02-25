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
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private DocumentAuthorLogic documentAuthorLogic;

    // done cabos why package scope? by jflute (2017/11/19)
    // done cabos add private modifier to _decoMapFile by jflute (2017/11/21)
    // done cabos instance variable should be under Attribute tag comment by jflute (2017/11/19)
    private final DfDecoMapFile _decoMapFile = new DfDecoMapFile();

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void saveDecommentPiece(String clientProject, DfDecoMapPiece decoMapPiece) {
        _decoMapFile.writePiece(buildClientPath(clientProject), decoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // done hakiba tag comment: Pickup Map by jflute (2017/08/17)
    public DfDecoMapPickup readMergedPickup(String clientProject) {
        List<DfDecoMapPiece> pieces = readDecommentPiece(clientProject);
        OptionalThing<DfDecoMapPickup> pickupOpt = readDecommentPickup(clientProject);
        List<DfDecoMapMapping> mappings = readDecommentMapping(clientProject);
        return _decoMapFile.merge(pickupOpt, pieces, mappings);
    }

    // done hakoba public on demand, so private now by jflute (2017/08/17)
    private List<DfDecoMapPiece> readDecommentPiece(String clientProject) {
        // done hakiba support no-existing directory by jflute (2017/09/28)
        return _decoMapFile.readPieceList(buildClientPath(clientProject));
    }

    private List<DfDecoMapMapping> readDecommentMapping(String clientProject) {
        return _decoMapFile.readMappingList(buildClientPath(clientProject));
    }

    private OptionalThing<DfDecoMapPickup> readDecommentPickup(String clientProject) {
        // done hakiba support no-existing directory or file by jflute (2017/09/28)
        return _decoMapFile.readPickup(buildClientPath(clientProject));
    }

    // ===================================================================================
    //                                                                         Mapping Map
    //                                                                         ===========
    public void saveDecommentMapping(String projectName, DfDecoMapMapping mapping) {
        _decoMapFile.writeMapping(buildClientPath(projectName), mapping);
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public String getAuthor() {
        return documentAuthorLogic.getAuthor();
    }

    public String getGitBranchName() {
        return documentAuthorLogic.getGitBranchName();
    }

    private String buildClientPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject);
    }
}
