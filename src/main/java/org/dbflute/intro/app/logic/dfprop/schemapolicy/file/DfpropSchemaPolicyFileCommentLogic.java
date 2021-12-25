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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.dbflute.helper.dfmap.DfMapFile;
import org.dbflute.util.DfCollectionUtil;
import org.dbflute.util.Srl;

/**
 * The logic for reading comments on Schema Policy (dfprop).
 * @author jflute
 * @author cabos
 * @author hakiba
 * @since 0.5.0 split from DfpropSchemaPolicyReadLogic (2021/12/25 Saturday at urayasu bay)
 */
public class DfpropSchemaPolicyFileCommentLogic {

    // ===================================================================================
    //                                                                            Comments
    //                                                                            ========
    // #needs_fix jflute can you change Map<String, Object> to Map<String, Map<String, Object>>? (2021/12/25)
    // #needs_fix jflute write detail description for returned map (2021/12/25)
    /**
     * schemaPolicyMap.dfprop上の行コメントを取得する。<br>
     * <pre>
     * e.g.
     *  # フラグのカラムでNotNull付いてなかったらビックリしません？
     *  ; if columnName is suffix:_FLG then notNull
     * </pre>
     * @param targetFile SchemaPolicyのdfpropファイル (NotNull)
     * @return コメントの入ったMap, map:{ scope = map:{ key : comment } } (NotNull, EmptyAllowed)
     */
    public Map<String, Object> readComments(File targetFile) {
        final String absolutePath = targetFile.getAbsolutePath();
        try {
            return new DfMapFile() {
                // #needs_fix jflute move this to DefLogic (2021/12/25)
                private final List<String> SCOPE_LIST = Arrays.asList("tableExceptList", "tableTargetList", "columnExceptMap",
                        "isMainSchemaOnly", "wholeMap", "tableMap", "columnMap");
                private final String OTHER_SCOPE = "other";
                private final String BEGINNING_KEY = "beginningComments";
                private final String END_KEY = "endComments";

                public Map<String, Object> readComments(InputStream ins) throws IOException {
                    assertObjectNotNull("ins", ins);
                    final Map<String, Object> keyCommentMap = DfCollectionUtil.newLinkedHashMap();
                    final String encoding = "UTF-8";
                    BufferedReader br = new BufferedReader(new InputStreamReader(ins, encoding));
                    String previousComment = "";
                    String scope = null;
                    while (true) {
                        final String line = br.readLine();
                        if (line == null) {
                            if (!previousComment.isEmpty()) {
                                addComments(keyCommentMap, OTHER_SCOPE, END_KEY, previousComment);
                            }
                            break;
                        }
                        final String ltrimmedLine = Srl.ltrim(line);
                        if (ltrimmedLine.startsWith(_lineCommentMark) || "".equals(ltrimmedLine)) {
                            previousComment += ltrimmedLine + "\n";
                            continue;
                        }
                        // key value here
                        String key = extractKey(ltrimmedLine);
                        if (SCOPE_LIST.contains(key)) {
                            scope = key;
                        }
                        if ("".equals(previousComment)) {
                            continue;
                        }

                        if (scope == null && key.startsWith("map:{")) {
                            scope = OTHER_SCOPE;
                            key = BEGINNING_KEY;
                        }
                        addComments(keyCommentMap, scope, key, previousComment);
                        previousComment = "";
                    }
                    try {
                        br.close();
                    } catch (IOException ignored) {}
                    return keyCommentMap;
                }

                private String extractKey(String line) {
                    String key;
                    if (line.contains("=>")) {
                        key = line.trim();
                    } else if (line.contains("=")) {
                        key = Srl.substringFirstFront(line, "=").trim();
                    } else {
                        key = line.trim();
                    }

                    if (key.startsWith(";")) {
                        return Srl.substringFirstRear(key, ";").trim();
                    }
                    return key;
                }

                @SuppressWarnings("unchecked")
                private void addComments(Map<String, Object> map, String scope, String key, String comments) {
                    if (map.containsKey(scope)) {
                        ((Map<String, Object>) map.get(scope)).put(key, comments);
                    } else {
                        map.put(scope, DfCollectionUtil.newLinkedHashMap(key, comments));
                    }
                }
            }.readComments(new FileInputStream(targetFile));
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Cannot read the dfprop comments: " + absolutePath, e);
        }
    }
}
