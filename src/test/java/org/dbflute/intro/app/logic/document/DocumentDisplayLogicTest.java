/*
 * Copyright 2014-2026 the original author or authors.
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
package org.dbflute.intro.app.logic.document;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.dbflute.intro.unit.UnitIntroTestCase;

/**
 * @author shiny
 */
public class DocumentDisplayLogicTest extends UnitIntroTestCase {

    @Override
    protected boolean isSuppressTestClient() {
        return true;
    }

    public void test_modifyHtmlForIntroOpening_schemaHtmlLinkWithFragment() throws Exception {
        // ## Arrange ##
        DocumentDisplayLogic logic = new DocumentDisplayLogic();
        inject(logic);
        File htmlFile = File.createTempFile("history-resortlinedb", ".html");
        String html = "<a href=\"./schema-resortlinedb.html\">to SchemaHTML</a>\n" // header link
                + "<a href=\"./schema-resortlinedb.html#member\">MEMBER</a>\n"; // table link
        Files.write(htmlFile.toPath(), html.getBytes(StandardCharsets.UTF_8));

        try {
            // ## Act ##
            String modifiedHtml = logic.modifyHtmlForIntroOpening("resortlinedb", htmlFile);

            // ## Assert ##
            assertContains(modifiedHtml, "href=\"/api/document/resortlinedb/schemahtml\">to SchemaHTML</a>");
            assertContains(modifiedHtml, "href=\"/api/document/resortlinedb/schemahtml#member\">MEMBER</a>");
            assertNotContains(modifiedHtml, "./schema-resortlinedb.html");
        } finally {
            Files.deleteIfExists(htmlFile.toPath());
        }
    }
}
