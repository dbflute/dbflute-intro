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
package org.dbflute.intro.app.logic.context;

import javax.annotation.Resource;

import org.dbflute.hook.AccessContext;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.db.dbflute.accesscontext.AccessContextResource;

/**
 * @author jflute
 */
public class AccessContextLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                  Resource Interface
    //                                                                  ==================
    @FunctionalInterface
    public interface AppTypeSupplier {
        String supply();
    }

    // ===================================================================================
    //                                                                      Create Context
    //                                                                      ==============
    public AccessContext create(AccessContextResource resource, AppTypeSupplier appTypeSupplier) {
        final AccessContext context = new AccessContext();
        context.setAccessLocalDateTimeProvider(() -> timeManager.currentDateTime());
        context.setAccessUserProvider(() -> buildAccessUserTrace(resource, appTypeSupplier));
        return context;
    }

    private String buildAccessUserTrace(AccessContextResource resource, AppTypeSupplier appTypeSupplier) {
        // #change_it you can customize the user trace for common column
        // example default style: "M:7,HBR,ProductListAction" or "_:-1,HBR,ProductListAction"
        final StringBuilder sb = new StringBuilder();
        sb.append("_:").append(-1); // no authentication in DBFlute Intro
        //sb.append(userTypeSupplier.supply().orElse("_")).append(":");
        //sb.append(userBeanSupplier.supply().map(bean -> bean.getUserId()).orElse(-1));
        sb.append(",").append(appTypeSupplier.supply()).append(",").append(resource.getModuleName());
        final String trace = sb.toString();
        final int columnSize = 200; // is same as e.g. REGISTER_USER
        return trace.length() > columnSize ? trace.substring(0, columnSize) : trace;
    }
}
