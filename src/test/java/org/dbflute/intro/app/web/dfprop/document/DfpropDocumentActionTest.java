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
package org.dbflute.intro.app.web.dfprop.document;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;
import org.lastaflute.web.response.StreamResponse;

/**
 * @author hakiba
 */
public class DfpropDocumentActionTest extends UnitIntroTestCase {

    @Test
    public void test_schemadiagram() throws Exception {
        // ## Arrange ##
        DfpropDocumentAction action = new DfpropDocumentAction();
        inject(action);

        // ## Act ##
        StreamResponse response = action.schemadiagram(TEST_CLIENT_PROJECT, "maihama_erd");

        // ## Assert ##
        Base64.Encoder encoder = Base64.getEncoder();
        File expected = new File(getProjectDir(), "etc/trohamadb/erd/maihamadb.png");
        // compare by base64 encoded
        assertEquals(encoder.encodeToString(response.getByteData()), encoder.encodeToString(Files.readAllBytes(expected.toPath())));
    }
}
