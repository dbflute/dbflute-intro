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
package org.dbflute.intro.app.logic.dfprop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.lastaflute.core.exception.LaSystemException;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author jflute
 * @author deco
 */
public class DfpropInfoLogic {

    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

    public Map<String, Map<String, Object>> findDfpropMap(String clientProject) {
        final Map<String, Map<String, Object>> dfpropMap = new LinkedHashMap<String, Map<String, Object>>();
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + clientProject + "/dfprop");

        Stream.of(dfpropDir.listFiles()).forEach(file -> {
            if (!file.getName().endsWith("Map.dfprop")) {
                return;
            }

            final String fileNameKey;
            if (file.getName().equals("classificationDefinitionMap.dfprop")) {
                fileNameKey = file.getName();
            } else {
                fileNameKey = file.getName().replace("DefinitionMap.dfprop", "Map.dfprop");
            }
            final DfPropFile dfpropFile = new DfPropFile();
            dfpropMap.put(fileNameKey, readMap(file, dfpropFile));

            final File plusFile = new File(file.getName().replace("Map.dfprop", "Map+.dfprop"));
            if (plusFile.exists()) {
                dfpropMap.get(fileNameKey).putAll(readMap(plusFile, dfpropFile));
            }
        });
        final Map<String, Object> basicInfoMap = dfpropMap.get("basicInfoMap.dfprop");
        if (basicInfoMap == null) {
            throw new RuntimeException("Not found the basicInfoMap.dfprop: " + dfpropMap.keySet());
        }
        final Map<String, Object> databaseInfoMap = dfpropMap.get("databaseInfoMap.dfprop");
        if (databaseInfoMap == null) {
            throw new RuntimeException("Not found the databaseInfoMap.dfprop: " + dfpropMap.keySet());
        }
        return dfpropMap;
    }

    private Map<String, Object> readMap(File targetFile, DfPropFile dfpropFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return dfpropFile.readMap(absolutePath, null);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop as map: " + absolutePath, e);
        }
    }

    public Optional<SchemaSyncCheckMap> findSchemaSyncCheckMap(String projectName) {
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + projectName + "/dfprop");
        // TODO deco dfpropDir.listFiles() may return null so check it (or use nio) by jflute (2017/02/23)
        return Arrays.stream(dfpropDir.listFiles())
            .filter(file -> StringUtils.equals(file.getName(), "documentMap.dfprop"))
            .map(file -> {
                final DfPropFile dfpropFile = new DfPropFile();
                Map<String, Object> readMap = readMap(file, dfpropFile);
                @SuppressWarnings("unchecked")
                Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");
                return schemaSyncCheckMap;
            }).filter(Objects::nonNull)
            .map(schemaSyncCheckMap -> {
                // TODO deco for NotRequired by jflute (2017/02/23)
                DbConnectionBox dbConnectionBox = new DbConnectionBox(
                    (String) schemaSyncCheckMap.get("url"),
                    (String) schemaSyncCheckMap.get("schema"),
                    (String) schemaSyncCheckMap.get("user"),
                    (String) schemaSyncCheckMap.get("password")
                );
                return new SchemaSyncCheckMap(dbConnectionBox, Boolean.valueOf(schemaSyncCheckMap.get("isSuppressCraftDiff").toString()));
            }).findAny();
    }

    // TODO deco move to DfpropUpdateLogic by jflute (2017/02/23)
    public void replaceSchemaSyncCheckMap(String project, SchemaSyncCheckMap schemaSyncCheckMap) {
        File documentMap = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");

        try (BufferedReader br = Files.newBufferedReader(documentMap.toPath())) {
            // TODO deco way of false to true by jflute (2017/02/23)
            boolean isExampleComment = true;
            boolean inSyncSchemeSetting = false;
            // TODO deco rename to sb by jflute (2017/02/23)
            StringBuilder stringBuilder = new StringBuilder();

            // TODO deco please refactor by jflute (2017/02/23)
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    isExampleComment = false;
                }
                if (!isExampleComment && line.contains("; schemaSyncCheckMap = map:{")) {
                    inSyncSchemeSetting = true;
                }
                if (!isExampleComment && (inSyncSchemeSetting && line.contains("# - - - - - - - - - -/"))) {
                    inSyncSchemeSetting = false;
                    line = schemaSyncCheckMap.convertToDfpropStr() + "\n" + line;
                }
                if (!isExampleComment && inSyncSchemeSetting) {
                    continue;
                }
                stringBuilder.append(line).append("\n");
            }
            // TODO deco use FlutyFileLogic by jflute (2017/02/23)
            FileUtils.write(documentMap, stringBuilder.toString());
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace schema sync check map", e);
        }
    }
}
