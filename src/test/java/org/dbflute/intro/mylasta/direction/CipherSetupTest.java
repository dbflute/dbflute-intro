/*
 * Copyright 2014-2020 the original author or authors.
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
package org.dbflute.intro.mylasta.direction;

import javax.annotation.Resource;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.lastaflute.core.security.PrimaryCipher;
import org.lastaflute.web.servlet.cookie.CookieCipher;

/**
 * @author jflute at rinshi-no-mori
 */
public class CipherSetupTest extends UnitIntroTestCase {

    @Resource
    private PrimaryCipher primaryCipher;
    @Resource
    private CookieCipher cookieCipher;

    public void test_primary() throws Exception {
        String encrypted = primaryCipher.encrypt("sea");
        log("encrypted: {}", encrypted);
        String decrypted = primaryCipher.decrypt(encrypted);
        log("decrypted: {}", decrypted);
        assertEquals("sea", decrypted);
        log(primaryCipher.oneway("land")); // expects no exception
    }

    public void test_cookie() throws Exception {
        String encrypted = cookieCipher.encrypt("sea");
        log("encrypted: {}", encrypted);
        String decrypted = cookieCipher.decrypt(encrypted);
        log("decrypted: {}", decrypted);
        assertEquals("sea", decrypted);
    }
}
