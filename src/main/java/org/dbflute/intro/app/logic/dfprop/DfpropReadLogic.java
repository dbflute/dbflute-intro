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
package org.dbflute.intro.app.logic.dfprop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.NeighborhoodSchemaHtmlMap;
import org.dbflute.intro.app.model.client.document.SchemaDiagramMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.dbflute.intro.bizfw.tellfailure.DfpropFileNotFoundException;

/**
 * The logic for reading DBFlute property (dfprop) information.
 * @author jflute
 * @author deco
 * @author cabos
 * @author hakiba
 * @author subaru
 * @author prprmurakami
 */
public class DfpropReadLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DocumentPhysicalLogic documentPhysicalLogic;

    // ===================================================================================
    //                                                                 Find all dfprop Map
    //                                                                 ===================
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The map of dfprop contents. map:{ [file-name] = map:{ [dfprop key-values] } } (NotNull)
     */
    public Map<String, Map<String, Object>> findDfpropMap(String projectName) {
        final Map<String, Map<String, Object>> dfpropMap = new LinkedHashMap<String, Map<String, Object>>();
        // #thinking jflute dfpropPhysicalLogic.findDfpropFileAllList()を使ってもいいかも？ (2022/01/13)
        final File dfpropDir = dfpropPhysicalLogic.findDfpropDirExisting(projectName);
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) { // basically no way, what happens by returning empty?
            return dfpropMap;
        }
        Stream.of(dfpropFiles).forEach(file -> {
            if (!file.getName().endsWith("Map.dfprop")) {
                return;
            }

            final String fileNameKey;
            if (file.getName().equals("classificationDefinitionMap.dfprop")) {
                fileNameKey = file.getName();
            } else { // supporting DBFlute old naming style
                // e.g. replaceSchemaDefinitionMap.dfprop to replaceSchemaMap.dfprop
                fileNameKey = file.getName().replace("DefinitionMap.dfprop", "Map.dfprop");
            }
            dfpropMap.put(fileNameKey, readMap(file));

            final File plusFile = new File(file.getName().replace("Map.dfprop", "Map+.dfprop"));
            if (plusFile.exists()) {
                dfpropMap.get(fileNameKey).putAll(readMap(plusFile));
            }
        });
        final Map<String, Object> basicInfoMap = dfpropMap.get(BasicInfoMap.DFPROP_NAME);
        if (basicInfoMap == null) {
            final String debugMsg = "Not found the basicInfoMap.dfprop in the dfprop: existings=" + dfpropMap.keySet();
            throw new DfpropFileNotFoundException(debugMsg, BasicInfoMap.DFPROP_NAME);
        }
        final Map<String, Object> databaseInfoMap = dfpropMap.get(DatabaseInfoMap.DFPROP_NAME);
        if (databaseInfoMap == null) {
            final String debugMsg = "Not found the databaseInfoMap.dfprop in the dfprop: existings=" + dfpropMap.keySet();
            throw new DfpropFileNotFoundException(debugMsg, DatabaseInfoMap.DFPROP_NAME);
        }
        return dfpropMap;
    }

    // ===================================================================================
    //                                                                        SchemaPolicy
    //                                                                        ============
    // split it to DfpropSchemaPolicyUpdateLogic by jflute (2021/06/24)

    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    /**
     * @param projectName The project name of DBFlute client. (NotNull)
     * @return The optional for the map of schema-sync-check. (NotNull)
     */
    public Optional<SchemaSyncCheckMap> findSchemaSyncCheckMap(String projectName) {
        final File dfpropDir = dfpropPhysicalLogic.findDfpropDirExisting(projectName);
        final File[] dfpropFiles = dfpropDir.listFiles();
        if (dfpropFiles == null) { // そのディレクトリがなかったとき、Existingでチェック済なのでありえない
            return Optional.empty();
        }
        return Arrays.stream(dfpropFiles).filter(file -> StringUtils.equals(file.getName(), "documentMap.dfprop")).findAny().map(file -> {
            Map<String, Object> documentMap = readMap(file);
            @SuppressWarnings("unchecked")
            Map<String, Object> schemaSyncCheckMap = (Map<String, Object>) documentMap.get("schemaSyncCheckMap");
            return schemaSyncCheckMap; // null allowed
        }).map(schemaSyncCheckMap -> {
            final String url = convertSettingToString(schemaSyncCheckMap.get("url"));
            final String schema = convertSettingToString(schemaSyncCheckMap.get("schema"));
            final String user = convertSettingToString(schemaSyncCheckMap.get("user"));
            final String password = convertSettingToString(schemaSyncCheckMap.get("password"));
            final DbConnectionBox dbConnectionBox = new DbConnectionBox(url, schema, user, password);
            final Boolean isSuppressCraftDiff = Boolean.valueOf(convertSettingToString(schemaSyncCheckMap.get("isSuppressCraftDiff")));
            return new SchemaSyncCheckMap(dbConnectionBox, isSuppressCraftDiff);
        });
    }

    // ===================================================================================
    //                                                                    LittleAdjustment
    //                                                                    ================
    public LittleAdjustmentMap findLittleAdjustmentMap(String projectName) {
        final File littleAdjustmentMap = dfpropPhysicalLogic.findDfpropFileExisting(projectName, "littleAdjustmentMap.dfprop");
        final Map<String, Object> readMap = readMap(littleAdjustmentMap);
        final boolean isTableDispNameUpperCase = convertSettingToBoolean(readMap.get("isTableDispNameUpperCase"));
        final boolean isTableSqlNameUpperCase = convertSettingToBoolean(readMap.get("isTableSqlNameUpperCase"));
        final boolean isColumnSqlNameUpperCase = convertSettingToBoolean(readMap.get("isColumnSqlNameUpperCase"));
        return new LittleAdjustmentMap(isTableDispNameUpperCase, isTableSqlNameUpperCase, isColumnSqlNameUpperCase);
    }

    // ===================================================================================
    //                                                                            Document
    //                                                                            ========
    public DocumentMap findDocumentMap(String projectName) {
        final File documentDefinitionMap = dfpropPhysicalLogic.findDfpropFileExisting(projectName, "documentMap.dfprop");
        final Map<String, Object> readMap = readMap(documentDefinitionMap);
        return prepareDocumentMapInner(readMap);
    }

    /**
     * schema diagramのファイルを取得します
     * <li>ファイルパスはDBFluteIntroプロジェクトからの相対パスに変換します</li>
     * @param projectName The project name of DBFlute client. (NotNull)
     * @param diagramName The diagram name. (NotNull)
     * @return schema diagram file path (NotNull, EmptyAllowed: not setup schemaDiagramMap)
     */
    public Optional<File> findSchemaDiagram(String projectName, String diagramName) {
        final DocumentMap documentMap = findDocumentMap(projectName);
        return documentMap.getSchemaDiagramMap()
                .toOptional()
                .flatMap(schemaDiagrams -> schemaDiagrams.get(diagramName).map(diagram -> diagram.path))
                .map(path -> new File(documentPhysicalLogic.buildDocumentDirPath(projectName) + "/" + path));
    }

    private DocumentMap prepareDocumentMapInner(Map<String, Object> readMap) {
        final DocumentMap documentMap = new DocumentMap();
        documentMap.setDbCommentOnAliasBasis(convertSettingToBoolean(readMap.get("isDbCommentOnAliasBasis")));
        documentMap.setAliasDelimiterInDbComment(convertSettingToString(readMap.get("aliasDelimiterInDbComment")));
        documentMap.setCheckColumnDefOrderDiff(convertSettingToBoolean(readMap.get("isCheckColumnDefOrderDiff")));
        documentMap.setCheckDbCommentDiff(convertSettingToBoolean(readMap.get("isCheckDbCommentDiff")));
        documentMap.setCheckProcedureDiff(convertSettingToBoolean(readMap.get("isCheckProcedureDiff")));
        documentMap.setNeighborhoodSchemaHtmlMap(new NeighborhoodSchemaHtmlMap(convertSettingToMap(readMap.get("neighborhoodSchemaHtmlMap"))));
        documentMap.setSchemaDiagramMap(new SchemaDiagramMap(convertSettingToMap(readMap.get("schemaDiagramMap"))));
        return documentMap;
    }

    // ===================================================================================
    //                                                                        Small Helper
    //                                                                        ============
    private Map<String, Object> readMap(File targetFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return new DfMapFile().readMap(new FileInputStream(targetFile));
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop as map: " + absolutePath, e);
        }
    }

    private String convertSettingToString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    private boolean convertSettingToBoolean(Object obj) {
        return obj != null && Boolean.parseBoolean(convertSettingToString(obj));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, String>> convertSettingToMap(Object obj) {
        return obj != null ? (Map<String, Map<String, String>>) obj : new HashMap<>();
    }
}
