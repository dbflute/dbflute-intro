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
package org.dbflute.intro.app.web.base.cls;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.dbflute.intro.app.def.DatabaseInfoDef;
import org.dbflute.intro.app.logic.client.DatabaseInfoDefParam;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.intro.mylasta.webcls.WebCDef;

/**
 * @author jflute
 */
public class IntroClsAssist {

    @Resource
    private IntroConfig introConfig;

    public Map<String, Map<?, ?>> getClassificationMap() {
        Map<String, Map<?, ?>> clsMap = new LinkedHashMap<String, Map<?, ?>>();

        Map<String, String> targetLanguageMap = WebCDef.TargetLanguage.listAll()
                .stream()
                .collect(Collectors.toMap(cls -> cls.code(), cls -> cls.code(), (u, v) -> v, LinkedHashMap::new));
        clsMap.put("targetLanguageMap", targetLanguageMap);

        Map<String, String> targetContainerMap = WebCDef.TargetContainer.listAll()
                .stream()
                .collect(Collectors.toMap(cls -> cls.code(), cls -> cls.code(), (u, v) -> v, LinkedHashMap::new));
        clsMap.put("targetContainerMap", targetContainerMap);

        Map<String, DatabaseInfoDefParam> databaseInfoDefMap =
                Stream.of(DatabaseInfoDef.values()).collect(Collectors.toMap(databaseInfoDef -> databaseInfoDef.getDatabaseName(),
                        databaseInfoDef -> new DatabaseInfoDefParam(databaseInfoDef), (u, v) -> v, LinkedHashMap::new));
        clsMap.put("databaseInfoDefMap", databaseInfoDefMap);

        return clsMap;
    }
}
