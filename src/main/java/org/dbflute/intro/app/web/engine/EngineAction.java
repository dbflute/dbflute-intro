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
package org.dbflute.intro.app.web.engine;

import java.util.List;

import javax.annotation.Resource;

import org.dbflute.infra.dfprop.DfPublicProperties;
import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.engine.EngineInstallLogic;
import org.dbflute.intro.app.logic.engine.EngineReadLogic;
import org.dbflute.intro.app.logic.exception.EngineDownloadErrorException;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.annotation.NotAvailableDecommentServer;
import org.dbflute.intro.bizfw.tellfailure.NetworkErrorException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 * @author cabos
 */
public class EngineAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;
    @Resource
    private EngineInstallLogic engineInstallLogic;
    @Resource
    private EngineReadLogic engineReadLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<EngineLatestBean> latest(EngineLatestBody engineLatestBody) {
        try {
            DfPublicProperties prop = publicPropertiesLogic.findProperties(engineLatestBody.useSystemProxies);
            EngineLatestBean bean = mappingToLatestVersion(prop);
            return asJson(bean);
        } catch (EngineDownloadErrorException e) {
            throw new NetworkErrorException(e.getMessage());
        }
    }

    private EngineLatestBean mappingToLatestVersion(DfPublicProperties prop) {
        return new EngineLatestBean(prop.getDBFluteLatestReleaseVersion(), prop.getDBFluteLatestSnapshotVersion());
    }

    @Execute
    public JsonResponse<List<String>> versions() {
        List<String> dbFluteVersionList = engineReadLogic.getExistingVersionList();
        return asJson(dbFluteVersionList);
    }

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> download(String dbfluteVersion, EngineDownloadBody engineDownloadBody) {
        try {
            engineInstallLogic.downloadUnzipping(dbfluteVersion, engineDownloadBody.useSystemProxies);
            return JsonResponse.asEmptyBody();
        } catch (EngineDownloadErrorException e) {
            throw new NetworkErrorException(e.getMessage());
        }

    }

    @NotAvailableDecommentServer
    @Execute
    public JsonResponse<Void> remove(String dbfluteVersion) {
        engineInstallLogic.remove(dbfluteVersion);
        return JsonResponse.asEmptyBody();
    }
}
