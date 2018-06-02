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
package org.dbflute.intro.app.web.dfprop;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author cabos
 */
public class DfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<DfpropBean>> list(String project) {
        List<File> dfpropFileList = dfpropPhysicalLogic.findDfpropFileAllList(project);
        List<DfpropBean> beans = dfpropFileList.stream()
                .map(dfpropFile -> new DfpropBean(dfpropFile.getName(), flutyFileLogic.readFile(dfpropFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String project, String fileName, DfpropUpdateBody body) {
        validate(body, messages -> {});

        File dfpropFile = dfpropPhysicalLogic.findDfpropFile(project, fileName);
        flutyFileLogic.writeFile(dfpropFile, body.content);

        return JsonResponse.asEmptyBody();
    }

    // -----------------------------------------------------
    //                                         GetSyncSchema
    //                                         -------------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DfpropSchemaSyncCheckResult> syncschema(String project) {
        final Optional<SchemaSyncCheckMap> schemaSyncCheckMap = dfpropInfoLogic.findSchemaSyncCheckMap(project);
        final DfpropSchemaSyncCheckResult bean =
                schemaSyncCheckMap.map(DfpropSchemaSyncCheckResult::new).orElseGet(() -> new DfpropSchemaSyncCheckResult());
        return asJson(bean);
    }

    // -----------------------------------------------------
    //                                        EditSyncSchema
    //                                        --------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> syncschemaEdit(String project, DfpropEditSyncSchemaBody body) {
        validate(body, messages -> {});
        final DbConnectionBox dbConnectionBox = new DbConnectionBox(body.url, body.schema, body.user, body.password);
        final SchemaSyncCheckMap schemaSyncCheckMap = new SchemaSyncCheckMap(dbConnectionBox, body.isSuppressCraftDiff);
        dfpropUpdateLogic.replaceSchemaSyncCheckMap(project, schemaSyncCheckMap);
        return JsonResponse.asEmptyBody();
    }

    // -----------------------------------------------------
    //                                           GetDocument
    //                                           -----------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DfpropDocumentResult> document(String project) {
        final LittleAdjustmentMap littleAdjustmentMap = dfpropInfoLogic.findLittleAdjustmentMap(project);
        final DocumentMap documentMap = dfpropInfoLogic.findDocumentMap(project);
        return asJson(new DfpropDocumentResult(littleAdjustmentMap, documentMap));
    }

    // -----------------------------------------------------
    //                                          EditDocument
    //                                          ------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> documentEdit(String project, DfpropDocumentEditBody body) {
        validate(body, messages -> {});
        final LittleAdjustmentMap littleAdjustmentMap = LittleAdjustmentMap.createAsTableNameUpperCase(body.upperCaseBasic);
        dfpropUpdateLogic.replaceLittleAdjustmentMap(project, littleAdjustmentMap);
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setAliasDelimiterInDbComment(body.aliasDelimiterInDbComment);
        documentMap.setDbCommentOnAliasBasis(body.dbCommentOnAliasBasis);
        dfpropUpdateLogic.replaceDocumentMap(project, documentMap);
        return JsonResponse.asEmptyBody();
    }
}
