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
package org.dbflute.intro.app.logic.dfprop.schemapolicy.file;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.helper.dfmap.DfMapStyle;
import org.dbflute.intro.app.logic.dfprop.schemapolicy.DfpropSchemaPolicyReadLogic;
import org.dbflute.intro.app.model.client.document.SchemaPolicyColumnMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyTableMap;
import org.dbflute.intro.app.model.client.document.SchemaPolicyWholeMap;

/**
 * The logic to replace SchemaPolicy dfprop file.
 * @author deco
 * @author subaru
 * @author hakiba
 * @author cabos
 * @author jflute
 * @since 0.5.0 split from DfpropSchemaPolicyUpdateLogic (2021/12/08 Wednesday at roppongi japanese)
 */
public class DfpropSchemaPolicyFileReplaceLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private DfpropSchemaPolicyReadLogic dfpropSchemaPolicyReadLogic;

    // ===================================================================================
    //                                                                         Replace Map
    //                                                                         ===========
    // UnitTestなどでdfpropファイルを差し替えられるように引数でFileを受け取る形式に
    // -----------------------------------------------------
    //                                              Directly
    //                                              --------
    /**
     * 指定された新しいdfpropデータを指定のdfpropファイルにそのまんま置き換える。<br>
     * @param dfpropFile SchemaPolicyのdfpropファイル (NotNull)
     * @param schemaPolicyMap まるごとの新しいdfpropデータ (NotNull)
     */
    public void replaceSchemaPolicyMapDirectly(File dfpropFile, SchemaPolicyMap schemaPolicyMap) {
        try {
            DfMapFile commentingMapFile = createCommentingMapFile(schemaPolicyMap);
            commentingMapFile.writeMap(FileUtils.openOutputStream(dfpropFile), schemaPolicyMap.convertToMap());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write dfprop file: " + dfpropFile.getAbsolutePath(), e);
        }
    }

    // -----------------------------------------------------
    //                                           Merge Input
    //                                           -----------
    /**
     * 引数で指定された差分のdfpropデータを既存のdfpropの内容とマージさせて置き換える。<br>
     * (ユーザーが入力した分を反映させる想定のメソッド)
     * @param dfpropFile SchemaPolicyのdfpropファイル (NotNull)
     * @param input 差分の新しいdfpropデータ (NotNull)
     */
    public void replaceSchemaPolicyMapWithInput(File dfpropFile, SchemaPolicyMap input) {
        SchemaPolicyMap base = dfpropSchemaPolicyReadLogic.parseSchemePolicyMap(dfpropFile);
        SchemaPolicyMap merge = mergeSchemaPolicyMap(base, input);
        replaceSchemaPolicyMapDirectly(dfpropFile, merge);
    }

    protected SchemaPolicyMap mergeSchemaPolicyMap(SchemaPolicyMap base, SchemaPolicyMap input) {
        SchemaPolicyWholeMap mergedWholeMap = mergeWholeMap(base.wholeMap, input.wholeMap);
        SchemaPolicyTableMap mergedTableMap = mergeTableMap(base.tableMap, input.tableMap);
        SchemaPolicyColumnMap mergedColumnMap = mergeColumnMap(base.columnMap, input.columnMap);

        return new SchemaPolicyMap(base.targetSetting, mergedWholeMap, mergedTableMap, mergedColumnMap, base.comments);
    }

    private SchemaPolicyWholeMap mergeWholeMap(SchemaPolicyWholeMap base, SchemaPolicyWholeMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyWholeMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyWholeMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyWholeMap(themeList);
    }

    private SchemaPolicyTableMap mergeTableMap(SchemaPolicyTableMap base, SchemaPolicyTableMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyTableMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyTableMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyTableMap(themeList, base.statementList);
    }

    private SchemaPolicyColumnMap mergeColumnMap(SchemaPolicyColumnMap base, SchemaPolicyColumnMap input) {
        List<String> inputThemeTypeCode = input.themeList.stream().map(theme -> theme.type.code).collect(Collectors.toList());
        Stream<SchemaPolicyColumnMap.Theme> filteredBaseStream =
                base.themeList.stream().filter(theme -> !inputThemeTypeCode.contains(theme.type.code));
        List<SchemaPolicyColumnMap.Theme> themeList =
                Stream.concat(filteredBaseStream, input.themeList.stream()).filter(theme -> theme.isActive).collect(Collectors.toList());
        return new SchemaPolicyColumnMap(themeList, base.statementList);
    }

    // ===================================================================================
    //                                                                  Commenting MapFile
    //                                                                  ==================
    // dfprop上のコメントを残しつつ新しい内容を反映するためのMapFileクラスを作成
    private DfMapFile createCommentingMapFile(SchemaPolicyMap schemaPolicyMap) {
        return new DfMapFile() {
            protected DfMapStyle newMapStyle() {
                return createCommentingMapStyle(schemaPolicyMap);
            }
        };
    }

    private DfMapStyle createCommentingMapStyle(SchemaPolicyMap schemaPolicyMap) {
        return new DfMapStyle() {
            private final List<String> SCOPE_LIST = Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap",
                    "isMainSchemaOnly", "wholeMap", "tableMap", "columnMap");

            private String scope = "";

            @Override
            protected boolean isIgnoreEqualAsEscapeControlMarkInList() {
                return true;
            }

            @Override
            public String toMapString(Map<String, ? extends Object> map) {
                final StringBuilder sb = new StringBuilder();
                doBuildOtherCommentString(sb, "beginningComments");
                sb.append(super.toMapString(map)).append("\n");
                doBuildOtherCommentString(sb, "endComments");
                return sb.toString();
            }

            @SuppressWarnings("unchecked")
            private void doBuildOtherCommentString(StringBuilder sb, String key) {
                final String scope = "other";
                if (schemaPolicyMap.comments.containsKey(scope)
                        && ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).containsKey(key)) {
                    sb.append(((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key));
                }
            }

            @Override
            protected void doBuildMapStringCurrentEntry(StringBuilder sb, boolean printOneLiner, String previousIndent,
                    String currentIndent, boolean withoutDisplaySideSpace, int index, String key, Object value) {
                if (SCOPE_LIST.contains(key)) {
                    scope = key;
                }
                doBuildCommentStringCurrentElement(sb, currentIndent, key);
                super.doBuildMapStringCurrentEntry(sb, printOneLiner, previousIndent, currentIndent, withoutDisplaySideSpace, index, key,
                        value);
            }

            @Override
            protected void doBuildListStringCurrentElement(StringBuilder sb, boolean printOneLiner, String previousIndent,
                    String currentIndent, boolean withoutDisplaySideSpace, int index, Object value) {
                doBuildCommentStringCurrentElement(sb, currentIndent, (String) value);
                super.doBuildListStringCurrentElement(sb, printOneLiner, previousIndent, currentIndent, withoutDisplaySideSpace, index,
                        value);
            }

            @SuppressWarnings("unchecked")
            private void doBuildCommentStringCurrentElement(StringBuilder sb, String currentIndent, String key) {
                if (schemaPolicyMap.comments.get(scope) != null
                        && ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key) != null) {
                    String commentString = (String) ((Map<String, Object>) schemaPolicyMap.comments.get(scope)).get(key);
                    String[] comments = commentString.split("\n");
                    if (comments.length == 0) { // case of containing only '\n'
                        sb.append(commentString);
                        return;
                    }
                    for (String c : comments) {
                        sb.append(c.trim().isEmpty() ? "\n" + c : "\n" + currentIndent + c);
                    }
                }
            }
        };
    }
}
