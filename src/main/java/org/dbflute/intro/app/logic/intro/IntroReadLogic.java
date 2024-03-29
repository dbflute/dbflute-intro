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
package org.dbflute.intro.app.logic.intro;

import java.util.Map;

import org.dbflute.intro.IntroBoot;

/**
 * The logic for DBFlute Intro information. (e.g. manifest)
 * @author p1us2er0
 * @author jflute
 */
public class IntroReadLogic {

    public Map<String, Object> getManifestMap() {
        // manifest logic is also used in IntroBoot, it should be one way reference so use it here
        // Introは組み込みjarファイルで提供され、起動時はまず IntroBoot が単体で動く。
        // なので、IntroBootからLogicを参照することはできないので、LogicがIntroBootを参照するようにしている。
        return IntroBoot.getManifestMap();
    }
}
