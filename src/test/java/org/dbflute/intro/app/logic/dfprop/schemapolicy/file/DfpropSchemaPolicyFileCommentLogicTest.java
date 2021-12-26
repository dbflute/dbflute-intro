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
import java.util.Map;
import java.util.Map.Entry;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author jflute
 * @since 0.5.0 (2021/12/25 Saturday at urayasu bay)
 */
public class DfpropSchemaPolicyFileCommentLogicTest extends UnitIntroTestCase {

    // #for_now jflute readComments()の中身を見るために、とりあえず簡易なテスト書いた (2021/12/25)
    public void test_readComments_show() {
        // ## Arrange ##
        File dfpropFile = findTestResourceFile("dfprop/javadoc_eg_schemaPolicyMap.dfprop");
        DfpropSchemaPolicyFileCommentLogic logic = new DfpropSchemaPolicyFileCommentLogic();
        inject(logic);

        // ## Act ##
        Map<String, Object> commentMap = logic.readComments(dfpropFile);

        // ## Assert ##
        assertFalse(commentMap.isEmpty());
        for (Entry<String, Object> entry : commentMap.entrySet()) {
            String key = entry.getKey();
            log(key);
            @SuppressWarnings("unchecked")
            Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
            valueMap.forEach((first, second) -> {
                log("  " + first + " :: " + filterComment(second)); // ここは見るだけ
            });
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> otherMap = (Map<String, Object>) commentMap.get("other");
        assertEquals("# へっだーこめんとよん", filterComment(otherMap.get("beginningComments")));
        assertEquals("# ふったーこめんとよん", filterComment(otherMap.get("endComments")));
        @SuppressWarnings("unchecked")
        Map<String, Object> wholeMap = (Map<String, Object>) commentMap.get("wholeMap");
        assertEquals("# ほーるまっぷよん", filterComment(wholeMap.get("wholeMap")));
        assertEquals("# ゆにーくよん", filterComment(wholeMap.get("uniqueTableAlias")));
        @SuppressWarnings("unchecked")
        Map<String, Object> tableMap = (Map<String, Object>) commentMap.get("tableMap");
        assertEquals("# すてーとめんとよん", filterComment(tableMap.get("if tableName is $$ALL$$ then fkName is prefix:FK_$$table$$")));
        assertEquals("# ほそくてきなこめんとよん", filterComment(tableMap.get("}"))); // これは本来tableMapではない!?
    }

    private String filterComment(Object obj) {
        // 改行が前後に入ったりしているが、ここではそれは無視してトリム (更新時に改行はどう使われているだろうか？)
        return obj != null ? obj.toString().trim() : null;
    }
}
