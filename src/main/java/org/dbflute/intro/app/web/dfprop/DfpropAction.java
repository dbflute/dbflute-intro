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
package org.dbflute.intro.app.web.dfprop;

import java.io.File;
import java.util.Collections;
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
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyStatement;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 * @author cabos
 * @author subaru
 * @author prprmurakami
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
    public JsonResponse<List<DfpropBean>> list(String clientName) {
        List<File> dfpropFileList = dfpropPhysicalLogic.findDfpropFileAllList(clientName);
        List<DfpropBean> beans = dfpropFileList.stream()
                .map(dfpropFile -> new DfpropBean(dfpropFile.getName(), flutyFileLogic.readFile(dfpropFile)))
                .collect(Collectors.toList());
        return asJson(beans);
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String clientName, String fileName, DfpropUpdateBody body) {
        validate(body, messages -> {});

        File dfpropFile = dfpropPhysicalLogic.findDfpropFile(clientName, fileName);
        flutyFileLogic.writeFile(dfpropFile, body.content);

        return JsonResponse.asEmptyBody();
    }

    // -----------------------------------------------------
    //                                         GetSyncSchema
    //                                         -------------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DfpropSchemaSyncCheckResult> syncschema(String clientName) {
        final Optional<SchemaSyncCheckMap> schemaSyncCheckMap = dfpropInfoLogic.findSchemaSyncCheckMap(clientName);
        final DfpropSchemaSyncCheckResult bean =
                schemaSyncCheckMap.map(DfpropSchemaSyncCheckResult::new).orElseGet(() -> new DfpropSchemaSyncCheckResult());
        return asJson(bean);
    }

    // -----------------------------------------------------
    //                                        EditSyncSchema
    //                                        --------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> syncschemaEdit(String clientName, DfpropEditSyncSchemaBody body) {
        validate(body, messages -> {});
        final DbConnectionBox dbConnectionBox = new DbConnectionBox(body.url, body.schema, body.user, body.password);
        final SchemaSyncCheckMap schemaSyncCheckMap = new SchemaSyncCheckMap(dbConnectionBox, body.isSuppressCraftDiff);
        dfpropUpdateLogic.replaceSchemaSyncCheckMap(clientName, schemaSyncCheckMap);
        return JsonResponse.asEmptyBody();
    }

    // -----------------------------------------------------
    //                                       GetSchemaPolicy
    //                                       ---------------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DfpropSchemaPolicyResult> schemapolicy(String clientName) {
        SchemaPolicyMap schemaPolicyMap = dfpropInfoLogic.findSchemaPolicyMap(clientName);
        return asJson(new DfpropSchemaPolicyResult(schemaPolicyMap));
    }

    // -----------------------------------------------------
    //                                      EditSchemaPolicy
    //                                      ----------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> schemapolicyEdit(String clientName, DfpropEditSchemaPolicyBody body) {
        validate(body, messages -> {});
        SchemaPolicyMap schemaPolicyMap = mappingToSchemaPolicyMap(body);
        dfpropUpdateLogic.replaceSchemaPolicyMap(clientName, schemaPolicyMap);
        return JsonResponse.asEmptyBody();
    }

    private SchemaPolicyMap mappingToSchemaPolicyMap(DfpropEditSchemaPolicyBody body) {
        List<SchemaPolicyWholeMap.Theme> wholeMapThemeList = body.wholeMap.themeList.stream()
                .map(theme -> new SchemaPolicyWholeMap.Theme(SchemaPolicyWholeMap.ThemeType.valueByCode(theme.typeCode), theme.isActive))
                .collect(Collectors.toList());
        SchemaPolicyWholeMap wholeMap = new SchemaPolicyWholeMap(wholeMapThemeList);
        List<SchemaPolicyTableMap.Theme> tableMapThemeList = body.tableMap.themeList.stream()
                .map(theme -> new SchemaPolicyTableMap.Theme(SchemaPolicyTableMap.ThemeType.valueByCode(theme.typeCode), theme.isActive))
                .collect(Collectors.toList());
        SchemaPolicyTableMap tableMap = new SchemaPolicyTableMap(tableMapThemeList, Collections.emptyList());
        List<SchemaPolicyColumnMap.Theme> columnMapThemeList = body.columnMap.themeList.stream()
                .map(theme -> new SchemaPolicyColumnMap.Theme(SchemaPolicyColumnMap.ThemeType.valueByCode(theme.typeCode), theme.isActive))
                .collect(Collectors.toList());
        SchemaPolicyColumnMap columnMap = new SchemaPolicyColumnMap(columnMapThemeList, Collections.emptyList());

        return new SchemaPolicyMap(SchemaPolicyTargetSetting.noSettingInstance(), wholeMap, tableMap, columnMap);
    }

    // -----------------------------------------------------
    //                         AddSchemaPolicyCheckStatement
    //                         -----------------------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word/@word")
    public JsonResponse<String> schemapolicyStatementRegister(String clientName, DfpropRegisterSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        SchemaPolicyStatement statement = mappingToStatement(body);
        String builtStatement = dfpropUpdateLogic.registerSchemaPolicyStatement(clientName, statement);
        return asJson(builtStatement);
    }

    private SchemaPolicyStatement mappingToStatement(DfpropRegisterSchemaPolicyStatementBody body) {
        SchemaPolicyStatement.Condition condition = new SchemaPolicyStatement.Condition(body.condition.operator, body.condition.conditions);
        SchemaPolicyStatement.Expected expected = new SchemaPolicyStatement.Expected(body.expected.operator, body.expected.expected);
        return new SchemaPolicyStatement(body.type, body.subject, condition, expected, body.comment);
    }

    // -----------------------------------------------------
    //                       GetschemapolicyStatementSubject
    //                       -------------------------------
    @Execute(urlPattern = "@word/@word/@word")
    public JsonResponse<List<String>> schemapolicyStatementSubject() {
        return asJson(dfpropInfoLogic.getStatementSubjectList());
    }

    // -----------------------------------------------------
    //                      DeleteSchemaPolicyCheckStatement
    //                      --------------------------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word/@word")
    public JsonResponse<Void> schemapolicyStatementDelete(String clientName, DfpropDeleteSchemaPolicyStatementBody body) {
        validate(body, messages -> {});
        dfpropUpdateLogic.deleteSchemaPolicyStatement(clientName, body.mapType, body.statement);
        return JsonResponse.asEmptyBody();
    }

    // -----------------------------------------------------
    //                                           GetDocument
    //                                           -----------
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<DfpropDocumentResult> document(String clientName) {
        final LittleAdjustmentMap littleAdjustmentMap = dfpropInfoLogic.findLittleAdjustmentMap(clientName);
        final DocumentMap documentMap = dfpropInfoLogic.findDocumentMap(clientName);
        return asJson(new DfpropDocumentResult(littleAdjustmentMap, documentMap));
    }

    // -----------------------------------------------------
    //                                          EditDocument
    //                                          ------------
    @NotAvailableDecommentServer
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> documentEdit(String clientName, DfpropDocumentEditBody body) {
        validate(body, messages -> {});
        final LittleAdjustmentMap littleAdjustmentMap = LittleAdjustmentMap.createAsTableNameUpperCase(body.upperCaseBasic);
        dfpropUpdateLogic.replaceLittleAdjustmentMap(clientName, littleAdjustmentMap);
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setAliasDelimiterInDbComment(body.aliasDelimiterInDbComment);
        documentMap.setDbCommentOnAliasBasis(body.dbCommentOnAliasBasis);
        documentMap.setCheckColumnDefOrderDiff(body.checkColumnDefOrderDiff);
        documentMap.setCheckDbCommentDiff(body.checkDbCommentDiff);
        documentMap.setCheckProcedureDiff(body.checkProcedureDiff);
        dfpropUpdateLogic.replaceDocumentMap(clientName, documentMap);
        return JsonResponse.asEmptyBody();
    }
}
