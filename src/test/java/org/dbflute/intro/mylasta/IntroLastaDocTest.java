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
package org.dbflute.intro.mylasta;

import org.dbflute.intro.IntroBoot;
import org.dbflute.intro.app.web.SwaggerAction;
import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author t-awane
 * @author cabos
 */
public class IntroLastaDocTest extends UnitIntroTestCase {

    @Override
    protected String prepareMockContextPath() {
        return IntroBoot.CONTEXT; // basically for swagger
    }

    public void test_document() {
        saveLastaDocMeta();
    }

    public void test_swaggerJson() throws Exception {
        saveSwaggerMeta(new SwaggerAction());
    }
}
