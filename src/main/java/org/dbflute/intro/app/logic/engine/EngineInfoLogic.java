/*
 * Copyright 2014-2019 the original author or authors.
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
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author p1us2er0
 * @author jflute
 */
public class EngineInfoLogic {
    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private final static String SPLIT_DELIMITER = "\\.";

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

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

    public boolean existsNewerVersionThan(String compareVersion) {
        try {
            String[] splitCompareVersion = compareVersion.split(SPLIT_DELIMITER);
            int majorVersionOfCompare = Integer.parseInt(splitCompareVersion[0]);
            int minorVersionOfCompare = Integer.parseInt(splitCompareVersion[1]);
            int releaseVersionOfCompare = Integer.parseInt(splitCompareVersion[2]);

            boolean existsNewerMajorVersion = getExistingVersionList().stream()
                    .anyMatch(version -> Integer.parseInt(version.split(SPLIT_DELIMITER)[0]) > majorVersionOfCompare);
            if (existsNewerMajorVersion) {
                return true;
            }
            boolean existsNewerMinorVersion = getExistingVersionList().stream()
                    .map(version -> version.split(SPLIT_DELIMITER))
                    .filter(splitVersion -> Integer.parseInt(splitVersion[0]) >= majorVersionOfCompare)
                    .anyMatch(splitVersion -> Integer.parseInt(splitVersion[1]) > minorVersionOfCompare);
            if (existsNewerMinorVersion) {
                return true;
            }
            boolean existsNewerReleaseVersion = getExistingVersionList().stream()
                    .map(version -> version.split(SPLIT_DELIMITER))
                    .filter(splitVersion -> Integer.parseInt(splitVersion[0]) >= majorVersionOfCompare)
                    .filter(splitVersion -> Integer.parseInt(splitVersion[1]) >= minorVersionOfCompare)
                    .anyMatch(splitVersion -> Integer.parseInt(splitVersion[2]) >= releaseVersionOfCompare);
            return existsNewerReleaseVersion;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean existsVersion(String compareVersion) {
        try {
            String[] splitCompareVersion = compareVersion.split(SPLIT_DELIMITER);
            String majorVersionOfCompare = splitCompareVersion[0];
            String minorVersionOfCompare = splitCompareVersion[1];
            boolean notExistsReleaseVersion = splitCompareVersion.length == 2;
            if (notExistsReleaseVersion) {
                return getExistingVersionList().stream()
                        .map(version -> version.split(SPLIT_DELIMITER))
                        .filter(splitVersion -> splitVersion[0].equals(majorVersionOfCompare))
                        .anyMatch(splitVersion -> splitVersion[1].equals(minorVersionOfCompare));
            } else {
                String releaseVersionOfCompare = splitCompareVersion[2];
                return getExistingVersionList().stream()
                        .map(version -> version.split(SPLIT_DELIMITER))
                        .filter(splitVersion -> splitVersion[0].equals(majorVersionOfCompare))
                        .filter(splitVersion -> splitVersion[1].equals(minorVersionOfCompare))
                        .anyMatch(splitVersion -> splitVersion[2].equals(releaseVersionOfCompare));
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
