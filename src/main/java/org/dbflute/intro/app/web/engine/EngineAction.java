/*
 * Copyright 2014-2015 the original author or authors.
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
import java.util.Properties;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.engine.EngineDownloadLogic;
import org.dbflute.intro.app.logic.engine.EngineInfoLogic;
import org.dbflute.intro.app.logic.engine.EngineRemoveLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 */
public class EngineAction extends IntroBaseAction {

    @Resource
    protected PublicPropertiesLogic publicPropertiesLogic;
    @Resource
    protected EngineDownloadLogic engineDownloadLogic;
    @Resource
    protected EngineInfoLogic engineInfoLogic;
    @Resource
    protected EngineRemoveLogic engineRemoveLogic;

    // TODO jflute intro: don't want to public (2016/07/05)
    @Execute
    public JsonResponse<Properties> publicProperties() {
        return asJson(publicPropertiesLogic.extractProperties());
    }

    @Execute
    public JsonResponse<List<String>> versions() {
        List<String> dbFluteVersionList = engineInfoLogic.getExistingVersionList();
        return asJson(dbFluteVersionList);
    }

    @Execute
    public JsonResponse<Void> download(String version) {
        engineDownloadLogic.download(version);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> remove(String version) {
        engineRemoveLogic.remove(version);
        return JsonResponse.asEmptyBody();
    }
}
