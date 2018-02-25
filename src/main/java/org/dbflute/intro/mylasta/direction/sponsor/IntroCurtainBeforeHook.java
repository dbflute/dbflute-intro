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
package org.dbflute.intro.mylasta.direction.sponsor;

import java.util.TimeZone;

import org.dbflute.intro.mylasta.direction.sponsor.introdb.IntroDBInitializer;
import org.dbflute.system.DBFluteSystem;
import org.dbflute.system.provider.DfFinalTimeZoneProvider;
import org.dbflute.util.DfTypeUtil;
import org.lastaflute.core.direction.CurtainBeforeHook;
import org.lastaflute.core.direction.FwAssistantDirector;

/**
 * @author p1us2er0
 * @author jflute
 */
public class IntroCurtainBeforeHook implements CurtainBeforeHook {

    // ===================================================================================
    //                                                                               Hook
    //                                                                              ======
    public void hook(FwAssistantDirector assistantDirector) {
        processDBFluteSystem();
        initializeIntroDB();
    }

    // ===================================================================================
    //                                                                      DBFlute System
    //                                                                      ==============
    protected void processDBFluteSystem() {
        DBFluteSystem.unlock();
        DBFluteSystem.setFinalTimeZoneProvider(createFinalTimeZoneProvider());
        DBFluteSystem.lock();
    }

    protected DfFinalTimeZoneProvider createFinalTimeZoneProvider() {
        return new DfFinalTimeZoneProvider() {
            protected final TimeZone provided = IntroUserTimeZoneProcessProvider.centralTimeZone;

            public TimeZone provide() {
                return provided;
            }

            @Override
            public String toString() {
                return DfTypeUtil.toClassTitle(this) + ":{" + provided.getID() + "}";
            }
        };
    }

    // ===================================================================================
    //                                                                             IntroDB
    //                                                                             =======
    protected void initializeIntroDB() {
        new IntroDBInitializer().initializeIntroDB();
    }
}
