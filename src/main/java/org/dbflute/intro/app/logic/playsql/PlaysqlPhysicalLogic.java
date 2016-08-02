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
package org.dbflute.intro.app.logic.playsql;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

import javax.annotation.Resource;

/**
 * @author deco
 */
public class PlaysqlPhysicalLogic {

    private static final String PLAYSQL_DIR_PATH = "playsql";

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    public String buildPlaysqlDirPath(String project) {
        return introPhysicalLogic.toDBFluteClientResourcePath(project, PLAYSQL_DIR_PATH);
    }

    public String buildPlaysqlFilePath(String project, String fileName) {
        String dfpropDirPath = buildPlaysqlDirPath(project);
        return dfpropDirPath + "/" + fileName;
    }
}
