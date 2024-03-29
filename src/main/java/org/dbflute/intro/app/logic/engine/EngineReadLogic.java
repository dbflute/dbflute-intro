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
package org.dbflute.intro.app.logic.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * The logic for reading DBFlute Engine information. (e.g. version)
 * @author p1us2er0
 * @author jflute
 */
public class EngineReadLogic {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private final static String SPLIT_DELIMITER = "\\.";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                            Existing
    //                                                                            ========
    public List<String> getExistingVersionList() {
        String mydbflutePath = introPhysicalLogic.buildMydbflutePath();
        try {
            Path mydbfluteDir = Paths.get(mydbflutePath);
            if (!mydbfluteDir.toFile().exists()) {
                return Collections.emptyList();
            }
            String enginePrefix = "dbflute-";
            List<String> versionList = Files.list(mydbfluteDir)
                    .filter(file -> file.toFile().isDirectory() && file.toFile().getName().startsWith(enginePrefix))
                    .map(file -> file.toFile().getName().substring(enginePrefix.length()))
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());
            return versionList;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get engine versions: " + mydbflutePath, e);
        }
    }

    // ===================================================================================
    //                                                                               Newer
    //                                                                               =====
    public boolean existsNewerVersionThan(String compareVersion) {
        try {
            String[] splitCompareVersion = compareVersion.split(SPLIT_DELIMITER);
            int majorVersionOfCompare = Integer.parseInt(splitCompareVersion[0]);
            int minorVersionOfCompare = Integer.parseInt(splitCompareVersion[1]);
            int releaseVersionOfCompare = Integer.parseInt(splitCompareVersion[2]);

            String majorVersionKey = "Major";
            String minorVersionKey = "Minor";
            String releaseVersionKey = "Release";
            List<Map<String, Integer>> existingVersionMapList =
                    getExistingVersionList().stream().map(version -> version.split(SPLIT_DELIMITER)).map(splitVersion -> {
                        Map<String, Integer> versionMap = new HashMap<>();
                        versionMap.put(majorVersionKey, Integer.parseInt(splitVersion[0]));
                        versionMap.put(minorVersionKey, Integer.parseInt(splitVersion[1]));
                        versionMap.put(releaseVersionKey, Integer.parseInt(splitVersion[2]));
                        return versionMap;
                    }).collect(Collectors.toList());

            boolean existsNewerMajorVersion =
                    existingVersionMapList.stream().anyMatch(verMap -> verMap.get(majorVersionKey) > majorVersionOfCompare);
            if (existsNewerMajorVersion) {
                return true;
            }
            boolean existsNewerMinorVersion = existingVersionMapList.stream()
                    .filter(verMap -> verMap.get(majorVersionKey) >= majorVersionOfCompare)
                    .anyMatch(verMap -> verMap.get(minorVersionKey) > minorVersionOfCompare);
            if (existsNewerMinorVersion) {
                return true;
            }
            boolean existsNewerReleaseVersion = existingVersionMapList.stream()
                    .filter(verMap -> verMap.get(majorVersionKey) >= majorVersionOfCompare)
                    .filter(verMap -> verMap.get(minorVersionKey) >= minorVersionOfCompare)
                    .anyMatch(verMap -> verMap.get(releaseVersionKey) >= releaseVersionOfCompare);
            return existsNewerReleaseVersion;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
