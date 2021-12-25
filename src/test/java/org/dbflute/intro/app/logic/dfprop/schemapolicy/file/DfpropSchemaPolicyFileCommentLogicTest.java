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

    // #for_now jflute readComments()の中身を見るために、とりあえずログだけ出すテスト書いた (2021/12/25)
    // もう少し挙動を整理できたら、簡単でもいいのでアサートを書きたいところ
    public void test_readComments_show() {
        // ## Arrange ##
        File dfpropFile = findTestClientFile("dfprop/schemaPolicyMap.dfprop");
        DfpropSchemaPolicyFileCommentLogic logic = new DfpropSchemaPolicyFileCommentLogic();
        inject(logic);

        // ## Act ##
        Map<String, Object> commentMap = logic.readComments(dfpropFile);

        // ## Assert ##
        assertFalse(commentMap.isEmpty());
        for (Entry<String, Object> entry : commentMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            log(key + " :: " + value.getClass());
        }
    }
}
