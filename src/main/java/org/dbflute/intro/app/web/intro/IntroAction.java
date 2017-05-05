/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.intro.app.web.intro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroInfoLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.base.cls.IntroClsAssist;
import org.dbflute.intro.bizfw.server.BootingInternetDomain;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 * @author jflute
 * @author deco
 */
public class IntroAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroInfoLogic introInfoLogic;
    @Resource
    private IntroClsAssist introClsAssist;
    @Resource
    private IntroConfig config;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Map<String, Object>> manifest() {
        return asJson(introInfoLogic.getManifestMap());
    }

    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classifications() {
        Map<String, Map<?, ?>> classificationMap = introClsAssist.getClassificationMap();
        return asJson(classificationMap);
    }

    @Execute
    public JsonResponse<Map<String, Object>> configuration() {
        Map<String, Object> map = new LinkedHashMap<>();
        BootingInternetDomain domain = new BootingInternetDomain(config);
        String serverUrl = domain.toCompleteDomain();
        String apiServerUrl = domain.toCompleteApiDomain();
        map.put("serverUrl", serverUrl);
        map.put("apiServerUrl", apiServerUrl);
        return asJson(map);
    }
}
