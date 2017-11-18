package org.dbflute.intro.app.logic.document.decomment;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.infra.doc.decomment.DfDecoMapFile;
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

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // TODO cabos shugyo++: move it to infra's decomment classes, file name and path handling by jflute (2017/11/11)
    DfDecoMapFile decoMapFile = new DfDecoMapFile();

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // done cabos move this above logic DI variables, framework component should be top (jflute policy...) by jflute (2017/11/11)
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;
    @Resource
    private DocumentAuthorLogic documentAuthorLogic;

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    public void saveDecommentPiece(String clientProject, DfDecoMapPiece decoMapPiece) {
        decoMapFile.writePiece(buildClientPath(clientProject), decoMapPiece);
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // done hakiba tag comment: Pickup Map by jflute (2017/08/17)
    public DfDecoMapPickup readMergedPickup(String clientProject) {
        List<DfDecoMapPiece> pieces = readDecommentPiece(clientProject);
        OptionalThing<DfDecoMapPickup> pickupOpt = readDecommentPickup(clientProject);
        return decoMapFile.merge(pickupOpt, pieces);
    }

    // done hakoba public on demand, so private now by jflute (2017/08/17)
    private List<DfDecoMapPiece> readDecommentPiece(String clientProject) {
        // done hakiba support no-existing directory by jflute (2017/09/28)
        return decoMapFile.readPiece(buildClientPath(clientProject));
    }

    private OptionalThing<DfDecoMapPickup> readDecommentPickup(String clientProject) {
        // done hakiba support no-existing directory or file by jflute (2017/09/28)
        return decoMapFile.readPickup(buildClientPath(clientProject));
    }

    // ===================================================================================
    //                                                                              Author
    //                                                                              ======
    public String getAuthor() {
        return documentAuthorLogic.getAuthor();
    }

    private String buildClientPath(String clientProject) {
        return introPhysicalLogic.buildClientPath(clientProject);
    }
}
