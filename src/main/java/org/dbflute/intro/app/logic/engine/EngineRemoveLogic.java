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

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EngineRemoveLogic { // TODO jflute intro: will rename? (2016/07/05)

    @Resource
    private EnginePhysicalLogic enginePhysicalLogic;

    public void remove(String engineVersion) {
        final String enginePath = enginePhysicalLogic.buildEnginePath(engineVersion);
        final File engineDir = new File(enginePath);
        if (engineDir.exists()) {
            try {
                FileUtils.deleteDirectory(engineDir);
            } catch (IOException e) {
                throw new IllegalStateException("Failed to delete the engine: " + enginePath, e);
            }
        }
    }
}
