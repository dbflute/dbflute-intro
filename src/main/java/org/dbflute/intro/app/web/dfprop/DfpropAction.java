/*
 * Copyright 2014-2016 the original author or authors.
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

import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropInfoLogic;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author deco
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
    public JsonResponse<DfpropSchemaSyncCheckBean> syncschema(String project) {
        final SchemaSyncCheckMap schemaSyncCheckMap = dfpropInfoLogic.findSchemaSyncCheckMap(project);
        final DfpropSchemaSyncCheckBean bean = new DfpropSchemaSyncCheckBean(schemaSyncCheckMap);
        return asJson(bean);
    }

    // -----------------------------------------------------
    //                                        EditSyncSchema
    //                                        --------------
    @Execute(urlPattern = "{}/@word/@word")
    public JsonResponse<Void> syncschemaEdit(String project, DfpropEditSyncSchemaBody body) {
        validate(body, messages -> {});
        final DbConnectionBox dbConnectionBox = new DbConnectionBox(body.url, body.schema, body.user, body.password);
        final SchemaSyncCheckMap schemaSyncCheckMap = new SchemaSyncCheckMap(dbConnectionBox, body.isSuppressCraftDiff);
        dfpropInfoLogic.replaceSchemaSyncCheckMap(project, schemaSyncCheckMap);
        return JsonResponse.asEmptyBody();
    }
}
