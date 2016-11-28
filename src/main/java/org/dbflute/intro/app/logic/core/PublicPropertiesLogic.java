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
package org.dbflute.intro.app.logic.core;

import java.lang.reflect.Field;
import java.util.Properties;

import javax.annotation.Resource;

import org.dbflute.infra.dfprop.DfPublicProperties;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.util.DfReflectionUtil;

/**
 * @author p1us2er0
 * @author jflute
 */
public class PublicPropertiesLogic {

    private static DfPublicProperties publicProperties; // cached

    @Resource
    private IntroConfig introConfig;

    public DfPublicProperties findProperties() {
        if (publicProperties != null) {
            return publicProperties;
        }
        synchronized (PublicPropertiesLogic.class) {
            if (publicProperties != null) {
                return publicProperties;
            }
            publicProperties = new DfPublicProperties();
            publicProperties.load();
            return publicProperties;
        }
    }

    public Properties asPlainProperties() {
        // TODO jflute intro: quit reflection (2016/08/12)
        DfPublicProperties prop = findProperties();
        Field field = DfReflectionUtil.getWholeField(DfPublicProperties.class, "_publicProp");
        return (Properties) DfReflectionUtil.getValueForcedly(field, prop);
    }
}
