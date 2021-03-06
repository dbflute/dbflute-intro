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
import org.dbflute.intro.app.logic.exception.EngineDownloadErrorException;
import org.dbflute.intro.app.logic.intro.IntroInfoLogic;

/**
 * The logic for public.properties, which is DBFlute public file to provide e.g. current version. <br>
 * See the actual file: http://dbflute.org/meta/public.properties for details.
 * @author p1us2er0
 * @author jflute
 * @author deco
 */
public class PublicPropertiesLogic {

    private static DfPublicProperties cachedProps; // cached

    @Resource
    private IntroInfoLogic introInfoLogic;

    public DfPublicProperties findProperties(boolean useSystemProxies) throws EngineDownloadErrorException {
        if (cachedProps != null) {
            return cachedProps;
        }
        try {
            introInfoLogic.setProxy(useSystemProxies);
            synchronized (PublicPropertiesLogic.class) {
                if (cachedProps != null) {
                    return cachedProps;
                }
                DfPublicProperties prop = new DfPublicProperties();
                prop.load();
                cachedProps = prop; // should be set after loading for thread-safe
                return cachedProps;
            }
        } catch (RuntimeException e) {
            // #needs_fix anyone does is this class depend on engine download? should throw more general exception? by jflute (2021/04/18)
            throw new EngineDownloadErrorException("Cannot download dbflute engine", e);
        }
    }
}
