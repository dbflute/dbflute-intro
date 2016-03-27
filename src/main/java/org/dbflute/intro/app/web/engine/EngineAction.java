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

import org.dbflute.intro.app.logic.simple.DbFluteEngineLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author p1us2er0
 */
public class EngineAction extends DbfluteIntroBaseAction {

    @Resource
    protected DbFluteEngineLogic dbFluteEngineLogic;

    @Execute
    public JsonResponse<Properties> publicProperties() {
        return asJson(dbFluteEngineLogic.getPublicProperties());
    }

    @Execute
    public JsonResponse<List<String>> versions() {
        List<String> dbFluteVersionList = dbFluteEngineLogic.getExistedVersionList();
        return asJson(dbFluteVersionList);
    }

    @Execute
    public JsonResponse<Void> download(String version) {
        dbFluteEngineLogic.download(version);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> remove(String version) {
        dbFluteEngineLogic.remove(version);
        return JsonResponse.asEmptyBody();
    }
}
