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
package org.dbflute.intro.app.logic.engine;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EnginePhysicalLogic {

    public static final String ENGINE_TEMPLATE_PATH = "mydbflute/dbflute-%1$s";

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    public String buildEnginePath(String engineVersion) {
        final String resolvedPath = String.format(ENGINE_TEMPLATE_PATH, engineVersion);
        return introPhysicalLogic.buildResourcePath(resolvedPath);
    }
}
