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

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.dbflute.infra.dfprop.DfPropFile;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * @author jflute
 */
public class DfpropInfoLogic {

    public Map<String, Map<String, Object>> prepareDfpropMap(String project) {
        final Map<String, Map<String, Object>> dfpropMap = new LinkedHashMap<String, Map<String, Object>>();
        final File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");

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
            dfpropMap.put(fileNameKey, dfpropFile.readMap(file.getAbsolutePath(), null));

            final File plusFile = new File(file.getName().replace("Map.dfprop", "Map+.dfprop"));
            if (plusFile.exists()) {
                dfpropMap.get(fileNameKey).putAll(dfpropFile.readMap(plusFile.getAbsolutePath(), null));
            }
        });
        return dfpropMap;
    }
}
