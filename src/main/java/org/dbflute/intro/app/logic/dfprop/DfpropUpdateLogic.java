package org.dbflute.intro.app.logic.dfprop;

import org.apache.commons.lang3.StringUtils;
import org.dbflute.intro.app.logic.core.FlutyFileLogic;
import org.dbflute.intro.app.model.client.document.DocumentMap;
import org.dbflute.intro.app.model.client.document.LittleAdjustmentMap;
import org.dbflute.intro.app.model.client.document.SchemaSyncCheckMap;
import org.lastaflute.core.exception.LaSystemException;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author deco
 */
public class DfpropUpdateLogic {

    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;
    @Resource
    private FlutyFileLogic flutyFileLogic;

    public void replaceSchemaSyncCheckMap(String project, SchemaSyncCheckMap schemaSyncCheckMap) {
        File documentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");
        try (BufferedReader br = Files.newBufferedReader(documentMapFile.toPath())) {
            boolean inSetting = false;
            boolean inSyncSchemeSetting = false;
            StringBuilder sb = new StringBuilder();

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

    public void replaceDocumentMap(String project, DocumentMap documentMap) {
        final File documentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "documentMap.dfprop");
        try (BufferedReader br = Files.newBufferedReader(documentMapFile.toPath())) {
            boolean inSetting = false;
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    inSetting = true;
                }
                if (inSetting) {
                    if (line.contains("; aliasDelimiterInDbComment") && line.contains("=")) {
                        line = "    ; aliasDelimiterInDbComment = " + documentMap.getAliasDelimiterInDbComment().orElse("");
                    }
                    if (line.contains("; isDbCommentOnAliasBasis") && line.contains("=")) {
                        line = "    ; isDbCommentOnAliasBasis = " + String.valueOf(documentMap.isDbCommentOnAliasBasis());
                    }
                }
                sb.append(line).append("\n");
            }
            flutyFileLogic.writeFile(documentMapFile, sb.toString());
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace document map", e);
        }
    }

    public void replaceLittleAdjustmentMap(String project, LittleAdjustmentMap littleAdjustmentMap) {
        final File littleAdjustmentMapFile = dfpropPhysicalLogic.findDfpropFile(project, "littleAdjustmentMap.dfprop");
        try (BufferedReader br = Files.newBufferedReader(littleAdjustmentMapFile.toPath())) {
            boolean inSetting = false;
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                if (StringUtils.equals(line, "map:{")) {
                    inSetting = true;
                }
                if (inSetting) {
                    final Boolean isTableDispNameUpperCase = littleAdjustmentMap.isTableDispNameUpperCase;
                    if (isTableDispNameUpperCase != null && line.contains("; isTableDispNameUpperCase") && line.contains("=")) {
                        line = "    ; isTableDispNameUpperCase = " + String.valueOf(isTableDispNameUpperCase);
                    }
                    final Boolean isTableSqlNameUpperCase = littleAdjustmentMap.isTableSqlNameUpperCase;
                    if (isTableSqlNameUpperCase != null && line.contains("; isTableSqlNameUpperCase") && line.contains("=")) {
                        line = "    ; isTableSqlNameUpperCase = " + String.valueOf(isTableSqlNameUpperCase);
                    }
                    final Boolean isColumnSqlNameUpperCase = littleAdjustmentMap.isColumnSqlNameUpperCase;
                    if (isColumnSqlNameUpperCase != null && line.contains("; isColumnSqlNameUpperCase") && line.contains("=")) {
                        line = "    ; isColumnSqlNameUpperCase = " + String.valueOf(isColumnSqlNameUpperCase);
                    }
                }
                sb.append(line).append("\n");
            }
            flutyFileLogic.writeFile(littleAdjustmentMapFile, sb.toString());
        } catch (IOException e) {
            throw new LaSystemException("Cannot replace little adjustment map", e);
        }
    }
}
