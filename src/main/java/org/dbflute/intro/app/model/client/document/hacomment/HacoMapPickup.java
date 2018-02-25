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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hakiba
 */
public class HacoMapPickup {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String DEFAULT_FORMAT_VERSION = "1.0";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final String formatVersion;
    protected LocalDateTime pickupDatetime;
    protected final List<HacoMapPiece> hacoMap;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public HacoMapPickup() {
        this(DEFAULT_FORMAT_VERSION);
    }
    public HacoMapPickup(String formatVersion) {
        this.formatVersion = formatVersion;
        this.hacoMap = new ArrayList<>();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public void setPickupDatetime(LocalDateTime pickupDatetime) {
        this.pickupDatetime = pickupDatetime;
    }

    public void addAllHacoMaps(List<HacoMapPiece> hacoMap) {
        this.hacoMap.addAll(hacoMap);
    }

    public List<HacoMapPiece> getHacoMap() {
        return this.hacoMap;
    }
}
