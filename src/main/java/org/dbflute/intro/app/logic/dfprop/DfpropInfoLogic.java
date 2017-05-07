/*
 * Copyright 2014-2017 the original author or authors.
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

import org.apache.commons.lang3.StringUtils;
import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;

import javax.annotation.Resource;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author jflute
 * @author deco
 */
public class DfpropInfoLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

    // ===================================================================================
    //                                                                                Find
    //                                                                                ====
    // -----------------------------------------------------
    //                                                Dfprop
    //                                                ------
    public Map<String, Map<String, Object>> findDfpropMap(String clientProject) {
        final Map<String, Map<String, Object>> dfpropMap = new LinkedHashMap<String, Map<String, Object>>();
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + clientProject + "/dfprop");
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) {
            return dfpropMap;
        }
        Stream.of(dfpropFiles).forEach(file -> {
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

    // -----------------------------------------------------
    //                                       SchemaSyncCheck
    //                                       ---------------
    public Optional<SchemaSyncCheckMap> findSchemaSyncCheckMap(String projectName) {
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + projectName + "/dfprop");
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) {
            return Optional.empty();
        }
        return Arrays.stream(dfpropFiles)
            .filter(file -> StringUtils.equals(file.getName(), "documentMap.dfprop"))
            .findAny()
            .map(file -> {
                final DfPropFile dfpropFile = new DfPropFile();
                Map<String, Object> readMap = readMap(file, dfpropFile);
                @SuppressWarnings("unchecked")
                Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) readMap.get("schemaSyncCheckMap");
                return schemaSyncCheckMap;
            })
            .map(schemaSyncCheckMap -> {
                final String url = convertSettingToString(schemaSyncCheckMap.get("url"));
                final String schema = convertSettingToString(schemaSyncCheckMap.get("schema"));
                final String user = convertSettingToString(schemaSyncCheckMap.get("user"));
                final String password = convertSettingToString(schemaSyncCheckMap.get("password"));
                final DbConnectionBox dbConnectionBox = new DbConnectionBox(url, schema, user, password);
                final Boolean isSuppressCraftDiff = Boolean.valueOf(convertSettingToString(schemaSyncCheckMap.get("isSuppressCraftDiff")));
                return new SchemaSyncCheckMap(dbConnectionBox, isSuppressCraftDiff);
            });
    }

    // -----------------------------------------------------
    //                                      LittleAdjustment
    //                                      ----------------
    public LittleAdjustmentMap findLittleAdjustmentMap(String projectName) {
        final File littleAdjustmentMap = dfpropPhysicalLogic.findDfpropFile(projectName, "littleAdjustmentMap.dfprop");
        final Map<String, Object> readMap = readMap(littleAdjustmentMap, new DfPropFile());
        final boolean isTableDispNameUpperCase = convertSettingToBoolean(readMap.get("isTableDispNameUpperCase"));
        final boolean isTableSqlNameUpperCase = convertSettingToBoolean(readMap.get("isTableSqlNameUpperCase"));
        final boolean isColumnSqlNameUpperCase = convertSettingToBoolean(readMap.get("isColumnSqlNameUpperCase"));
        return new LittleAdjustmentMap(isTableDispNameUpperCase, isTableSqlNameUpperCase, isColumnSqlNameUpperCase);
    }

    // -----------------------------------------------------
    //                                              Document
    //                                              --------
    public DocumentMap findDocumentMap(String projectName) {
        final File documentDefinitionMap = dfpropPhysicalLogic.findDfpropFile(projectName, "documentMap.dfprop");
        final Map<String, Object> readMap = readMap(documentDefinitionMap, new DfPropFile());
        return prepareDocumentMapInner(readMap);
    }

    private DocumentMap prepareDocumentMapInner(Map<String, Object> readMap) {
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setDbCommentOnAliasBasis(convertSettingToBoolean(readMap.get("isDbCommentOnAliasBasis")));
        documentMap.setAliasDelimiterInDbComment(convertSettingToString(readMap.get("aliasDelimiterInDbComment")));
        documentMap.setCheckColumnDefOrderDiff(convertSettingToBoolean(readMap.get("isCheckColumnDefOrderDiff")));
        documentMap.setCheckDbCommentDiff(convertSettingToBoolean(readMap.get("isCheckDbCommentDiff")));
        documentMap.setCheckProcedureDiff(convertSettingToBoolean(readMap.get("isCheckProcedureDiff")));
        return documentMap;
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private Map<String, Object> readMap(File targetFile, DfPropFile dfpropFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return dfpropFile.readMap(absolutePath, null);
        } catch (RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop as map: " + absolutePath, e);
        }
    }

    private String convertSettingToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    private boolean convertSettingToBoolean(Object obj) {
        return obj != null && Boolean.parseBoolean(convertSettingToString(obj));
    }
}
