/*
 * Copyright 2014-2017 the original author or authors.
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
package org.dbflute.infra.doc.decomment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dbflute.utflute.core.PlainTestCase;

/**
 * @author cabos
 */
public class DfDecoMapFileTest extends PlainTestCase {

    // ===================================================================================
    //                                                                           file name
    //                                                                           =========
    public void test_buildPieceFileName() throws Exception {
        // ## Arrange ##
        final String sampleTableName = "EBISU_GARDEN_PLACE";
        final String sampleColumnName = "PLAZA";
        final String sampleAuthor = "cabos";
        final String samplePieceCode = "FE893L1";
        final String currentDateStr = "2999/12/31";
        DfDecoMapFile decoMapFile = new DfDecoMapFile() {
            @Override
            protected String getCurrentDateStr() {
                return currentDateStr;
            }
        };

        // e.g decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap
        final String expFileName = "decomment-piece-" + sampleTableName + "-" + sampleColumnName + "-" + currentDateStr + "-" + sampleAuthor
                + "-" + samplePieceCode + ".dfmap";

        // ## Act ##
        final String fileName = decoMapFile.buildPieceFileName(sampleTableName, sampleColumnName, sampleAuthor, samplePieceCode);

        // ## Assert ##
        assertEquals(fileName, expFileName);
    }

    public void test_getCurrentDateStr() throws Exception {
        // ## Arrange ##
        final LocalDateTime currentDate = currentLocalDateTime();
        DfDecoMapFile decoMapFile = new DfDecoMapFile() {

            @Override
            protected LocalDateTime getCurrentLocalDateTime() {
                return currentDate;
            }
        };

        // e.g 20170316-123456-789
        String expDatePattern = "yyyyMMdd-HHmmss-SSS";
        final String expDateStr = DateTimeFormatter.ofPattern(expDatePattern).format(currentDate);

        // ## Act ##
        final String dateStr = decoMapFile.getCurrentDateStr();

        // ## Assert ##
        assertEquals(dateStr, expDateStr);
    }

    // -----------------------------------------------------
    //                                           create file
    //                                           -----------
    // cabos's memorable test code (2017/11/18)
    //    public void test_createPieceMapFile_init() throws Exception {
    //        // ## Arrange ##
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
    //        inject(physicalLogic);
    //
    //        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
    //        final File sampleFile =
    //            new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece", sampleFileName));
    //
    //        assertFalse(sampleFile.exists()); // before check file not exits
    //
    //        // ## Act ##
    //        logic.createPieceMapFile(sampleFile);
    //
    //        // ## Assert ##
    //        assertTrue(sampleFile.exists());
    //    }
    //
    //    public void test_createPieceMapFile_prepared() throws Exception {
    //        // ## Arrange ##
    //        super.prepareTestDecommentFiles();
    //
    //        DocumentDecommentPhysicalLogic logic = new DocumentDecommentPhysicalLogic();
    //        inject(logic);
    //        IntroPhysicalLogic physicalLogic = new IntroPhysicalLogic();
    //        inject(physicalLogic);
    //
    //        final String sampleFileName = "decomment-piece-TABLE_NAME-20170316-123456-789-authorName.dfmap";
    //        final File sampleFile =
    //            new File(physicalLogic.buildClientPath(TEST_CLIENT_PROJECT, "schema", "decomment", "piece", sampleFileName));
    //
    //        assertFalse(sampleFile.exists()); // before check file not exits
    //
    //        // ## Act ##
    //        logic.createPieceMapFile(sampleFile);
    //
    //        // ## Assert ##
    //        assertTrue(sampleFile.exists());
    //    }

}
