/*
 * Copyright 2014-2015 the original author or authors.
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dbflute.util.DfStringUtil;
import org.dbflute.util.DfTypeUtil;
import org.lastaflute.web.path.ActionAdjustmentProvider;
import org.lastaflute.web.ruts.config.ActionExecute;
import org.lastaflute.web.ruts.config.ActionMapping;

/**
 * @author p1us2er0
 */
public class DbfluteActionAdjustmentProvider implements ActionAdjustmentProvider {

    private static final int INDEXED_PROPERTY_SIZE_LIMIT = 200; // hard coding for now

    public int provideIndexedPropertySizeLimit() {
        return INDEXED_PROPERTY_SIZE_LIMIT;
    }

    public String decodeUrlParameterPropertyValue(Object bean, String name, String value) {
        return null;
    }

    public String filterHtmlPath(String path, ActionMapping actionMappingWrapper) {
        return null;
    }

    public List<String> prepareHtmlRetryWordList(String requestPath, List<String> wordList) {
        return null;
    }

    public boolean isForcedRoutingTarget(HttpServletRequest request, String requestPath) {
        return true;
    }

    // #migration: S2ExecuteConfig to ActionExecuteConfig
    public boolean isForcedSuppressRedirectWithSlash(HttpServletRequest request, String requestPath, ActionExecute executeConfig) {
        return true;
    }

    public String customizeActionMappingRequestPath(String requestPath) {
        return DfStringUtil.substringFirstRear(requestPath, "/api");
    }

    @Override
    public String toString() {
        return DfTypeUtil.toClassTitle(this) + ":{indexedLimit=" + INDEXED_PROPERTY_SIZE_LIMIT + "}";
    }
}
