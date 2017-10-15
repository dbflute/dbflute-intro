package org.dbflute.intro.app.web.document.decomment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.document.decomment.DocumentDecommentPhysicalLogic;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPickup;
import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.document.decomment.DecommentSaveBody.DecommentTablePart;
import org.dbflute.intro.app.web.document.decomment.DecommentSaveBody.DecommentTablePart.DecommentColumnPart;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// done cabos decoment to decomment by jflute (2017/07/27)

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
        validate(body, messages -> {});
        decommentPhysicalLogic.saveDecommentPieceMap(projectName, mappingToDecoMapPiece(body));
        return JsonResponse.asEmptyBody();
    }

    // done cabos use mappingTo... by jflute (2017/08/10)
    private DfDecoMapPiece mappingToDecoMapPiece(DecommentSaveBody body) {
        String author = getAuthor();
        DfDecoMapPiece pieceMap = new DfDecoMapPiece();
        pieceMap.setFormatVersion("1.0");
        pieceMap.setAuthor(author);
        pieceMap.setDecommentDatetime(timeManager.currentDateTime());
        pieceMap.setMerged(body.merged);
        pieceMap.setDecoMap(mappingToDecoMapPiece(body.table, author));
        return pieceMap;
    }

    private String getAuthor() {
        return decommentPhysicalLogic.getAuthor();
    }

    private DfDecoMapTablePart mappingToDecoMapPiece(DecommentTablePart tablePart, String author) {
        DfDecoMapTablePart tablePartMap = new DfDecoMapTablePart();
        tablePartMap.setTableName(tablePart.tableName);
        List<DfDecoMapColumnPart> columns =
                tablePart.columns.stream().map(columnPart -> mappingPartToDecoMapPiece(columnPart, author)).collect(Collectors.toList());
        tablePartMap.setColumns(columns);
        return tablePartMap;
    }

    private DfDecoMapColumnPart mappingPartToDecoMapPiece(DecommentColumnPart columnPart, String author) {
        DfDecoMapColumnPart columnPartMap = new DfDecoMapColumnPart();
        columnPartMap.setColumnName(columnPart.columnName);
        DfDecoMapColumnPart.ColumnPropertyPart property = new DfDecoMapColumnPart.ColumnPropertyPart();
        property.setDecomment(columnPart.decomment);
        property.setDatabaseComment(columnPart.databaseComment);
        property.setPreviousWholeComment(columnPart.previousWholeComment);
        property.setCommentVersion(columnPart.commentVersion);
        // done cabos add (merge) top author by jflute (2017/08/10)
        property.setAuthorList(mergeAuthorList(columnPart.authorList, author));
        columnPartMap.setProperties(Collections.singletonList(property));
        return columnPartMap;
    }

    private List<String> mergeAuthorList(List<String> authorList, String author) {
        // done cabos use LinkedHashSet to keep order by jflute (2017/09/07)
        Set<String> authorSet = new LinkedHashSet<>(authorList);
        authorSet.add(author);
        return new ArrayList<>(authorSet);
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
        DfDecoMapPickup dfDecoMapPickup = decommentPhysicalLogic.readMergedDecommentPickupMap(projectName);
        return asJson(new DecommentPickupResult(dfDecoMapPickup.getDecoMap()));
    }
}
