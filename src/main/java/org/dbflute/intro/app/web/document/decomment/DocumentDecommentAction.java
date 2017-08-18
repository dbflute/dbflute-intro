package org.dbflute.intro.app.web.document.decomment;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.decomment.DocumentDecommentPhysicalLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart;
import org.dbflute.intro.app.web.document.decomment.DecommentPostBody.DecommentTablePart.DecommentColumnPart;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// TODO done cabos decoment to decomment by jflute (2017/07/27)

/**
 * @author cabos
 * @author hakiba
 */
public class DocumentDecommentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;
    @Resource
    private DocumentDecommentPhysicalLogic decommentPhysicalLogic;

    // TODO cabos use _taexec and use _tanest by jflute (2017/08/10)
    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    // done cabos post to save, get to diff by jflute (2017/07/27)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> save(String projectName, DecommentPostBody body) {
        validate(body, messages -> {});
        decommentPhysicalLogic.saveDecommentPieceMap(projectName, convertBodyToDecoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    // TODO cabos use mappingTo... by jflute (2017/08/10)
    private DfDecoMapPiece convertBodyToDecoMapPiece(DecommentPostBody body) {
        DfDecoMapPiece pieceMap = new DfDecoMapPiece();
        pieceMap.setFormatVersion("1.0");
        pieceMap.setAuthor(getAuthor());
        pieceMap.setDecommentDatetime(timeManager.currentDateTime());
        pieceMap.setMerged(body.merged);
        pieceMap.setDecoMap(convertTablePartToDecoMapPiece(body.table));
        return pieceMap;
    }

    private String getAuthor() {
        return decommentPhysicalLogic.getAuthorFromGitSystem();
    }

    private DfDecoMapTablePart convertTablePartToDecoMapPiece(DecommentTablePart tablePart) {
        DfDecoMapTablePart tablePartMap = new DfDecoMapTablePart();
        tablePartMap.setTableName(tablePart.tableName);
        tablePartMap.setColumns(tablePart.columns.stream().map(this::convertColumnPartToDecoMapPiece).collect(Collectors.toList()));
        return tablePartMap;
    }

    private DfDecoMapColumnPart convertColumnPartToDecoMapPiece(DecommentColumnPart columnPart) {
        DfDecoMapColumnPart columnPartMap = new DfDecoMapColumnPart();
        columnPartMap.setColumnName(columnPart.columnName);
        DfDecoMapColumnPart.ColumnPropertyPart property = new DfDecoMapColumnPart.ColumnPropertyPart();
        property.setDecomment(columnPart.decomment);
        property.setDatabaseComment(columnPart.databaseComment);
        property.setPreviousWholeComment(columnPart.previousWholeComment);
        property.setCommentVersion(columnPart.commentVersion);
        // TODO cabos add (merge) top author by jflute (2017/08/10)
        property.setAuthorList(columnPart.authorList);
        columnPartMap.setProperties(Collections.singletonList(property));
        return columnPartMap;
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    // TODO done hakiba rename piece to pickup by jflute (2017/08/17)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DecommentPickupResult> pickup(String projectName) {
        DfDecoMapPickup dfDecoMapPickup = decommentPhysicalLogic.readMergedDecommentPickupMap(projectName);
        return asJson(new DecommentPickupResult(dfDecoMapPickup.getDecoMap()));
    }
}
