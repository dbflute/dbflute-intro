/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.model.client.document.hacomment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hakiba
 */
public class HacoMapDiffPart {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    protected final String diffCode;
    protected final String diffDate;
    protected final Map<String, HacoMapPropertyPart> propertyMap = new LinkedHashMap<>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HacoMapDiffPart(String diffCode, String diffDate, HacoMapPropertyPart propertyPart) {
        this.diffCode = diffCode;
        this.diffDate = diffDate;
        addProperty(propertyPart);
    }

    public HacoMapDiffPart(String diffCode, String diffDate, List<HacoMapPropertyPart> propertyPartList) {
        this.diffCode = diffCode;
        this.diffDate = diffDate;
        propertyPartList.forEach(propertyPart -> addProperty(propertyPart));
    }

    @SuppressWarnings("unchecked")
    public HacoMapDiffPart(Map<String, Object> historyPartMap) {
        this.diffCode = (String) historyPartMap.get("diffCode");
        this.diffDate = (String) historyPartMap.get("diffDate");
        final List<Map<String, Object>> propertyMapList = (List<Map<String, Object>>) historyPartMap.get("propertyList");
        propertyMapList.stream().map(propertyMap -> new HacoMapPropertyPart(propertyMap)).forEach(property -> {
            propertyMap.put(property.getPieceCode(), property);
        });
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public String getDiffCode() {
        return diffCode;
    }

    public String getDiffDate() {
        return diffDate;
    }

    public List<HacoMapPropertyPart> getPropertyList() {
        return Collections.unmodifiableList(new ArrayList<>(propertyMap.values()));
    }

    public void addProperty(HacoMapPropertyPart property) {
        this.propertyMap.put(property.getPieceCode(), property);
    }
}
