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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EngineInfoLogic {

    public static final String MY_DBFLUTE_PATH = IntroPhysicalLogic.BASE_DIR_PATH + "/mydbflute/dbflute-%1$s";

    public List<String> getExistingVersionList() {
        try {
            List<String> list = Files.list(Paths.get(IntroPhysicalLogic.BASE_DIR_PATH, "mydbflute")).filter(file -> {
                return file.toFile().isDirectory() && file.toFile().getName().startsWith("dbflute-");
            }).map(file -> file.toFile().getName().substring(8)).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
