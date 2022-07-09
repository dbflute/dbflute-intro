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
package org.dbflute.intro.app.web.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.dbflute.intro.unit.UnitIntroTestCase;
import org.junit.Test;
import org.lastaflute.web.response.StreamResponse;
import org.lastaflute.web.servlet.request.stream.WrittenStreamOut;

/**
 * @author hakiba
 */
public class DocumentActionTest extends UnitIntroTestCase {

    @Test
    public void test_schemahtml() throws Exception {
        // ## Arrange ##
        DocumentAction action = new DocumentAction();
        inject(action);

        // ## Act ##
        StreamResponse response = action.schemahtml(TEST_CLIENT_PROJECT);
        String schemaHtml = readContent(response);

        // ## Assert ##
        // assert that neighborhoodSchemaHtml url replaced
        assertContains(schemaHtml, "<a href=\"/api/document/trohamadb/schemahtml\">to trohama</a>");
        // assert that schemaDiagram url replaced
        assertContains(schemaHtml, "src=\"/api/dfprop/document/schemadiagram/testdb/test_erd\"");
    }

    private String readContent(StreamResponse response) throws IOException {
        StringBuilder builder = new StringBuilder();
        WrittenStreamOut out = new WrittenStreamOut() {
            @Override
            public OutputStream stream() {
                return null;
            }
            @Override
            public void write(InputStream ins) throws IOException {
                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        };

        response.getStreamCall().callback(out);

        return builder.toString();
    }
}
