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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Function;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.logic.dfprop.schemapolicy.DfpropSchemaPolicyReadLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.lastaflute.core.exception.LaSystemException;

/**
 * @author deco
 * @author subaru
 * @author jflute
 */
public class DfpropUpdateLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    // ===================================================================================
    //                                                                        SchemaPolicy
    //                                                                        ============
    // split it to DfpropSchemaPolicyUpdateLogic by jflute (2021/06/24)

    // ===================================================================================
    //                                                                     SchemaSyncCheck
    //                                                                     ===============
    public void replaceSchemaSyncCheckMap(String projectName, SchemaSyncCheckMap schemaSyncCheckMap) {
        final File documentMapFile = dfpropPhysicalLogic.findDfpropFile(projectName, "documentMap.dfprop");
        try (BufferedReader br = Files.newBufferedReader(documentMapFile.toPath())) {
            boolean inSetting = false;
            boolean inSyncSchemeSetting = false;
            final StringBuilder sb = new StringBuilder();

            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    inSetting = true;
                }
                if (inSetting) {
                    if (line.contains("; schemaSyncCheckMap = map:{")) {
                        inSyncSchemeSetting = true;
                    }
                    if (inSyncSchemeSetting) {
                        if (line.contains("# - - - - - - - - - -/")) {
                            inSyncSchemeSetting = false;
                            line = schemaSyncCheckMap.convertToDfpropStr() + "\n" + line;
                        } else {
                            continue;
                        }
                    }
                }
                sb.append(line).append("\n");
            }
            flutyFileLogic.writeFile(documentMapFile, sb.toString());
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace schema sync check map", e);
        }
    }

    // ===================================================================================
    //                                                                         DocumentMap
    //                                                                         ===========
    public void replaceDocumentMap(String project, DocumentMap documentMap) {
        final Function<String, String> lineReplacer = line -> {
            if (line.contains("; aliasDelimiterInDbComment") && line.contains("=")) {
                return "    ; aliasDelimiterInDbComment = " + documentMap.getAliasDelimiterInDbComment().orElse("");
            }
            if (line.contains("; isDbCommentOnAliasBasis") && line.contains("=")) {
                return "    ; isDbCommentOnAliasBasis = " + String.valueOf(documentMap.isDbCommentOnAliasBasis());
            }
            if (line.contains("; isCheckColumnDefOrderDiff") && line.contains("=")) {
                return "    ; isCheckColumnDefOrderDiff = " + String.valueOf(documentMap.isCheckColumnDefOrderDiff());
            }
            if (line.contains("; isCheckDbCommentDiff") && line.contains("=")) {
                return "    ; isCheckDbCommentDiff = " + String.valueOf(documentMap.isCheckDbCommentDiff());
            }
            if (line.contains("; isCheckProcedureDiff") && line.contains("=")) {
                return "    ; isCheckProcedureDiff = " + String.valueOf(documentMap.isCheckProcedureDiff());
            }
            return line;
        };
        final File documentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");
        final String fileContent = replaceDfpropFileContent(documentMapFile, lineReplacer);
        flutyFileLogic.writeFile(documentMapFile, fileContent);
    }

    // ===================================================================================
    //                                                                 LittleAdjustmentMap
    //                                                                 ===================
    public void replaceLittleAdjustmentMap(String project, LittleAdjustmentMap littleAdjustmentMap) {
        final Function<String, String> lineReplacer = line -> {
            final Boolean isTableDispNameUpperCase = littleAdjustmentMap.isTableDispNameUpperCase;
            if (isTableDispNameUpperCase != null && line.contains("; isTableDispNameUpperCase") && line.contains("=")) {
                return "    ; isTableDispNameUpperCase = " + String.valueOf(isTableDispNameUpperCase);
            }
            final Boolean isTableSqlNameUpperCase = littleAdjustmentMap.isTableSqlNameUpperCase;
            if (isTableSqlNameUpperCase != null && line.contains("; isTableSqlNameUpperCase") && line.contains("=")) {
                return "    ; isTableSqlNameUpperCase = " + String.valueOf(isTableSqlNameUpperCase);
            }
            final Boolean isColumnSqlNameUpperCase = littleAdjustmentMap.isColumnSqlNameUpperCase;
            if (isColumnSqlNameUpperCase != null && line.contains("; isColumnSqlNameUpperCase") && line.contains("=")) {
                return "    ; isColumnSqlNameUpperCase = " + String.valueOf(isColumnSqlNameUpperCase);
            }
            return line;
        };
        final File littleAdjustmentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "littleAdjustmentMap.dfprop");
        final String fileContent = replaceDfpropFileContent(littleAdjustmentMapFile, lineReplacer);
        flutyFileLogic.writeFile(littleAdjustmentMapFile, fileContent);
    }

    private String replaceDfpropFileContent(File file, Function<String, String> lineReplacer) {
        try (BufferedReader br = Files.newBufferedReader(file.toPath())) {
            boolean endComment = false;
            final StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    endComment = true;
                }
                if (endComment) {
                    line = lineReplacer.apply(line);
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace dfprop: " + file, e);
        }
    }
}
