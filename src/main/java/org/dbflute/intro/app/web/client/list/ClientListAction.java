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
package org.dbflute.intro.app.web.client.list;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.client.ClientReadLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectInfra;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * DBFluteクライアント一覧のためのAction。
 * @author jflute (split from large action) (at roppongi japanese)
 */
public class ClientListAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientReadLogic clientReadLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<ClientRowResult>> index() {
        List<String> projectList = clientReadLogic.getProjectNameList();
        List<ClientRowResult> beans = projectList.stream().map(project -> {
            ClientModel clientModel = clientReadLogic.findClient(project).get();
            return mappingToRowBean(clientModel);
        }).collect(Collectors.toList());
        return asJson(beans);
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private ClientRowResult mappingToRowBean(ClientModel clientModel) {
        ClientRowResult rowBean = new ClientRowResult();
        prepareBasic(rowBean, clientModel);
        return rowBean;
    }

    // -----------------------------------------------------
    //                                                 Basic
    //                                                 -----
    private void prepareBasic(ClientRowResult client, ClientModel clientModel) {
        ProjectInfra projectInfra = clientModel.getProjectInfra();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        client.projectName = projectInfra.getProjectName();
        client.databaseCode = basicInfoMap.getDatabase();
        client.languageCode = basicInfoMap.getTargetLanguage();
        client.containerCode = basicInfoMap.getTargetContainer();
    }
}
