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
package org.dbflute.intro.app.web.dfprop.schemapolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropUpdateLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTargetSetting;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.dfprop.DfpropSchemaPolicyResult;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author prprmurakami
 */
public class DfpropSchemapolicyAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropUpdateLogic dfpropUpdateLogic;
    @Resource
    private DfpropInfoLogic dfpropInfoLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                       GetSchemaPolicy
    //                                       ---------------
    @Execute
    public JsonResponse<DfpropSchemaPolicyResult> index(String clientName) {
        SchemaPolicyMap schemaPolicyMap = dfpropInfoLogic.findSchemaPolicyMap(clientName);
        return asJson(new DfpropSchemaPolicyResult(schemaPolicyMap));
    }

    // -----------------------------------------------------
    //                                      EditSchemaPolicy
    //                                      ----------------
    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> edit(String clientName, DfpropEditSchemaPolicyBody body) {
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
}
