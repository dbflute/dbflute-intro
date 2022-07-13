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
package org.dbflute.intro.app.logic.core;

import javax.annotation.Resource;

import org.dbflute.infra.dfprop.DfPublicProperties;
import org.dbflute.intro.app.logic.exception.PublicPropertiesLoadingFailureException;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;

/**
 * The logic for public.properties, which is DBFlute public file to provide e.g. current version. <br>
 * See the actual file: http://dbflute.org/meta/public.properties for details.
 * @author p1us2er0
 * @author jflute
 * @author deco
 */
public class PublicPropertiesLogic {

    // #thinking jflute should it be no cache if server mode? cannot accept its change now (2021/11/09)
    // however when server mode, almost as decomment server so no problem for now
    private static DfPublicProperties cachedProps; // cached

    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    public DfPublicProperties findProperties(boolean useSystemProxies) throws PublicPropertiesLoadingFailureException {
        if (cachedProps != null) {
            return cachedProps;
        }
        try {
            // public.properties needs internet access
            // so you can control proxy use option of JVM by the argument
            introPhysicalLogic.setupSystemProxyUse(useSystemProxies);
            synchronized (PublicPropertiesLogic.class) {
                if (cachedProps != null) {
                    return cachedProps;
                }
                DfPublicProperties prop = new DfPublicProperties();
                prop.load(); // might throw if I/O failure
                cachedProps = prop; // should be set after loading for thread-safe
                return cachedProps;
            }
        } catch (RuntimeException e) { // e.g. I/O failure
            // done anyone does is this class depend on engine download? should throw more general exception? by jflute (2021/04/18)
            String msg = "Cannot load the public.properties: useSystemProxies=" + useSystemProxies;
            throw new PublicPropertiesLoadingFailureException(msg, e);
        }
    }
}
