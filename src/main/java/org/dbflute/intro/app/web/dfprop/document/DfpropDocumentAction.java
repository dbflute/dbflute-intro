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
package org.dbflute.intro.app.web.dfprop.document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropReadLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;
import org.dbflute.intro.bizfw.tellfailure.OpenDirNotFoundException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.response.StreamResponse;

/**
 * @author prprmurakami
 * @author jflute
 * @author hakiba
 */
public class DfpropDocumentAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropReadLogic dfpropReadLogic;
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<DfpropDocumentResult> index(String projectName) {
        final LittleAdjustmentMap littleAdjustmentMap = dfpropReadLogic.findLittleAdjustmentMap(projectName);
        final DocumentMap documentMap = dfpropReadLogic.findDocumentMap(projectName);
        return asJson(new DfpropDocumentResult(littleAdjustmentMap, documentMap));
    }

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String projectName, DfpropDocumentEditBody body) {
        validate(body, messages -> {});
        final LittleAdjustmentMap littleAdjustmentMap = LittleAdjustmentMap.createAsTableNameUpperCase(body.upperCaseBasic);
        dfpropUpdateLogic.replaceLittleAdjustmentMap(projectName, littleAdjustmentMap);
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setAliasDelimiterInDbComment(body.aliasDelimiterInDbComment);
        documentMap.setDbCommentOnAliasBasis(body.dbCommentOnAliasBasis);
        documentMap.setCheckColumnDefOrderDiff(body.checkColumnDefOrderDiff);
        documentMap.setCheckDbCommentDiff(body.checkDbCommentDiff);
        documentMap.setCheckProcedureDiff(body.checkProcedureDiff);
        dfpropUpdateLogic.replaceDocumentMap(projectName, documentMap);
        return JsonResponse.asEmptyBody();
    }

    /**
     * schemaDiagramMapに設定された画像ファイルを取得します
     * @param projectName The project name of DBFlute client. (NotNull) e.g. trohamadb
     * @param diagramName The diagramName name of schemaDiagramMap. e.g. maihama_erd
     * @return Image file stream (NotNull, ThrowException: not found schemaDiagramMap or schemaDiagram image file)
     */
    @Execute()
    public StreamResponse schemadiagram(String projectName, String diagramName) {
        final File diagram = dfpropReadLogic.findSchemaDiagram(projectName, diagramName).orElse(null);
        if (diagram == null) {
            final String debugMsg = String.format("Not found schemaDiagram %s in the documentMap.dfprop", diagramName);
            throw new DfpropFileNotFoundException(debugMsg, DocumentMap.DFPROP_NAME);
        }
        try {
            return asStream(diagramName)
                    .headerContentDispositionInline()
                    .contentTypeJpeg()
                    .data(Files.readAllBytes(diagram.toPath()));
        }  catch (NoSuchFileException e) {
            throw new OpenDirNotFoundException("Not found schemaDiagram file:" + e.getFile(), e.getFile());
        } catch (IOException e) {
            return StreamResponse.asEmptyBody();
        }
    }
}
