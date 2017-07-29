package org.dbflute.intro.app.web.document.decomment;

import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.model.document.decomment.DfDecoMapPiece;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapColumnPart;
import org.dbflute.intro.app.model.document.decomment.parts.DfDecoMapTablePart;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

// TODO done cabos decoment to decomment by jflute (2017/07/27)
/**
 * @author cabos
 */
public class DocumentDecommentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                           Piece Map
    //                                                                           =========
    // TODO done cabos post to save, get to diff by jflute (2017/07/27)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> save(String projectName, DecommentPostBody body) {
        validate(body, messages -> {});
        // TODO cabos create decomap file (2017/07/20)
        return JsonResponse.asEmptyBody();
    }

    private DfDecoMapPiece convertBodyToDecoMapPiece(DecommentPostBody body) {
        DfDecoMapPiece pieceMap = new DfDecoMapPiece();
        pieceMap.setFormatVersion("1.0");
        pieceMap.setAuthor(getAuthor());
        pieceMap.setDecommentDatetime(timeManager.currentDateTime());
        pieceMap.setMerged(body.merged);
        pieceMap.setDecoMap(convert(body.table));
        return null;
    }

    private String getAuthor() {
        return System.getProperty("user.home");
    }

    private DfDecoMapTablePart convert(DecommentPostBody.DecommentTablePart tablePart) {
        DfDecoMapTablePart tablePartMap = new DfDecoMapTablePart();
        tablePartMap.setTableName(tablePart.tableName);
        tablePartMap.setColumn(tablePart.columns.stream().map(column -> {
            DfDecoMapColumnPart columnPartMap = new DfDecoMapColumnPart();
            columnPartMap.setColumnName(column.columnName);
            columnPartMap.setDecomment(column.decomment);
            columnPartMap.setDatabaseComment(column.databaseComment);
            columnPartMap.setPreviousWholeComment(column.previousWholeComment);
            return columnPartMap;
        }).collect(Collectors.toList()));
        return tablePartMap;
    }

    // ===================================================================================
    //                                                                          Pickup Map
    //                                                                          ==========
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<Void> piece(String projectName) {
        // TODO hakiba create new decomment response (2017/07/20)
        return JsonResponse.asEmptyBody();
    }
}
