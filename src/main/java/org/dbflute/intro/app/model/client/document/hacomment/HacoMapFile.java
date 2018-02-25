/*
 * Copyright 2014-2018 the original author or authors.
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

package org.dbflute.intro.app.model.client.document.hacomment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.dbflute.helper.HandyDate;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.DfStringUtil;
import org.dbflute.util.DfTypeUtil;

/**
 * @author hakiba
 */
public class HacoMapFile {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // e.g. dbflute_maihamadb/scheme/decomment/
    private static final String BASE_HACOMMENT_DIR_PATH = "/schema/hacomment/";
    // e.g. dbflute_maihamadb/scheme/decomment/piece/
    private static final String BASE_PIECE_DIR_PATH = BASE_HACOMMENT_DIR_PATH + "piece/";
    // e.g. dbflute_maihamadb/scheme/decomment/pickup/decomment-pickup.dfmap
    private static final String BASE_PICKUP_FILE_PATH = BASE_HACOMMENT_DIR_PATH + "pickup/hacomment-pickup.dfmap";

    private static final Map<String, String> REPLACE_CHAR_MAP;

    static {
        // done cabos add spaces and replaceChar should be underscore? by jflute (2017/09/07)
        List<String> notAvailableCharList = Arrays.asList("/", "\\", "<", ">", "*", "?", "\"", "|", ":", ";", "\0", " ");
        String replaceChar = "_";
        REPLACE_CHAR_MAP = notAvailableCharList.stream().collect(Collectors.toMap(ch -> ch, ch -> replaceChar));
    }

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====

    public List<HacoMapPiece> readPieceList(String clientDirPath) {
        assertClientDirPath(clientDirPath);
        String pieceDirPath = buildPieceDirPath(clientDirPath);
        if (Files.notExists(Paths.get(pieceDirPath))) {
            return Collections.emptyList();
        }
        try {
            return Files.list(Paths.get(pieceDirPath))
                    .filter(path -> path.toString().endsWith(".dfmap"))
                    .filter(path -> path.toString().contains("-piece-"))
                    .map(path -> doReadPiece(path))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // TODO hakiba resolve Exception by hakiba (2018/02/22)
            return Collections.emptyList();
        }
    }

    private HacoMapPiece doReadPiece(Path path) {
        final MapListFile mapListFile = new MapListFile();
        try {
            Map<String, Object> map = mapListFile.readMap(Files.newInputStream(path));
            return mappingToDecoMapPiece(map);
        } catch (RuntimeException | IOException e) {
            // TODO hakiba resolve Exception by hakiba (2018/02/22)
            return null; // unreachable
        }
    }

    // done hakiba cast check by hakiba (2017/07/29)
    private HacoMapPiece mappingToDecoMapPiece(Map<String, Object> map) {
        String diffdate = (String) map.get("diffDate");
        String hacomment = (String) map.get("hacomment");
        @SuppressWarnings("unchecked")
        List<String> authorList = (List<String>) map.get("authorList");
        String pieceCode = (String) map.get("pieceCode");
        LocalDateTime pieceDatetime = new HandyDate((String) map.get("pieceDatetime")).getLocalDateTime();
        String pieceOwner = (String) map.get("pieceOwner");
        @SuppressWarnings("unchecked")
        List<String> previousPieceList = (List<String>) map.get("previousPieceList");
        return new HacoMapPiece(diffdate, hacomment, authorList, pieceCode, pieceOwner, pieceDatetime, previousPieceList);
    }

    public OptionalThing<HacoMapPickup> readPickup(String clientDirPath) {
        assertClientDirPath(clientDirPath);
        String filePath = buildPickupFilePath(clientDirPath);
        if (Files.notExists(Paths.get(filePath)))
            return OptionalThing.empty();
        return OptionalThing.ofNullable(doReadPickup(Paths.get(filePath)), () -> {});
    }

    private HacoMapPickup doReadPickup(Path path) {
        MapListFile mapListFile = new MapListFile();
        try {
            Map<String, Object> map = mapListFile.readMap(Files.newInputStream(path));
            return mappingToDecoMapPickup(map);
        } catch (RuntimeException | IOException e) {
            // TODO hakiba resolve Exception by hakiba (2018/02/22)
            return null; // unreachable
        }
    }

    private HacoMapPickup mappingToDecoMapPickup(Map<String, Object> map) {
        LocalDateTime pickupDatetime = DfTypeUtil.toLocalDateTime(map.get("pickupDatetime"));
        String formatVersion = (String) map.get("formatVersion");
        HacoMapPickup pickup = new HacoMapPickup(formatVersion);
        pickup.setPickupDatetime(pickupDatetime);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> hacoMapList = (List<Map<String, Object>>) map.getOrDefault("hacoMap", new ArrayList<>());
        if (hacoMapList.isEmpty()) {
            return pickup;
        }

        List<HacoMapPiece> hacoMapPieceList = hacoMapList.stream().map(hacoMap -> new HacoMapPiece(hacoMap)).collect(Collectors.toList());

        pickup.addAllHacoMaps(hacoMapPieceList);
        return pickup;
    }

    // ===================================================================================
    //                                                                               Write
    //                                                                               =====
    public void writePiece(String clientDirPath, HacoMapPiece piece) {
        assertClientDirPath(clientDirPath);

        String piecePath = buildPieceDirPath(clientDirPath) + buildPieceFileName(piece);
        doWritePiece(piecePath, piece);
    }

    private void doWritePiece(String pieceFilePath, HacoMapPiece piece) {
        File pieceMapFile = new File(pieceFilePath);
        if (pieceMapFile.exists()) { // no way, but just in case
            pieceMapFile.delete(); // simply delete old file
        }
        createPieceMapFile(pieceMapFile);

        Map<String, Object> hacoMap = piece.convertToMap();
        createMapListFile(pieceFilePath, pieceMapFile, hacoMap);
    }

    protected void createPieceMapFile(File pieceMapFile) {
        try {
            Files.createDirectories(Paths.get(pieceMapFile.getParentFile().getAbsolutePath()));
            Files.createFile(Paths.get(pieceMapFile.getAbsolutePath()));
        } catch (IOException e) {
            // TODO hakiba implement HacoMapWriteFailureException by hakiba (2018/02/15)
        }
    }

    private void createMapListFile(String mappingFilePath, File mappingMapFile, Map<String, Object> hacoMap) {
        MapListFile mapListFile = new MapListFile();
        try (OutputStream ous = new FileOutputStream(mappingMapFile)) {
            try {
                mapListFile.writeMap(ous, hacoMap);
            } catch (IOException e) {
                // TODO hakiba implements HacoMapWriteFailureException by hakiba (2018/02/15)
            }
        } catch (IOException e) {
            // TODO hakiba implements HacoMapResourceReleaseFailureException by hakiba (2018/02/15)
        }
    }

    // TODO hakiba refactor for better looks by hakiba (2018/02/15)
    public String buildPieceFileName(HacoMapPiece piece) {
        String diffDateStr = generateDiffDateStrForFileName(piece);
        String filteredOwner = DfStringUtil.replaceBy(piece.getPieceOwner(), REPLACE_CHAR_MAP);
        // e.g. hacomment-piece-diffdate20180220161718-20171015-161718-199-jflute-HF7ELSE.dfmap
        return "hacomment-piece-" + diffDateStr + "-" + getCurrentDateStr() + "-" + filteredOwner + "-" + piece.pieceCode + ".dfmap";
    }

    private String generateDiffDateStrForFileName(HacoMapPiece piece) {
        // e.g. 2018/02/21 16:17:18 -> diffdate20180220161718
        Map<String, String> replaceMap = new HashMap<>();
        replaceMap.put(" ", "");
        replaceMap.put("/", "");
        replaceMap.put(":", "");
        return "diffdate" + DfStringUtil.replaceBy(piece.getDiffDate(), replaceMap);
    }

    protected String getCurrentDateStr() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS").format(getCurrentLocalDateTime());
    }

    protected LocalDateTime getCurrentLocalDateTime() {
        // TODO hakiba use callback by jflute (2018/02/22)
        return LocalDateTime.now();
    }

    // ===================================================================================
    //                                                                               Merge
    //                                                                               =====
    public HacoMapPickup merge(OptionalThing<HacoMapPickup> pickupOpt, List<HacoMapPiece> pieces) {
        Set<String> pieceCodeSet = extractAllMergedPieceCode(pickupOpt, pieces);
        HacoMapPickup mergedPickup = doMerge(pickupOpt, pieces, pieceCodeSet);
        return mergedPickup;
    }

    private Set<String> extractAllMergedPieceCode(OptionalThing<HacoMapPickup> pickupOpt, List<HacoMapPiece> pieces) {
        Stream<String> pickupPieceCodeStream =
                pickupOpt.map(pickup -> pickup.hacoMap.stream().flatMap(piece -> piece.previousPieceList.stream())).orElse(Stream.empty());
        Stream<String> previousPieceCodeStream = pieces.stream().flatMap(piece -> piece.previousPieceList.stream());
        return Stream.concat(pickupPieceCodeStream, previousPieceCodeStream).collect(Collectors.toSet());
    }

    private HacoMapPickup doMerge(OptionalThing<HacoMapPickup> pickupOpt, List<HacoMapPiece> pieces, Set<String> mergedPieceCodeSet) {
        Stream<HacoMapPiece> pickupStream =
                pickupOpt.map(pickup -> pickup.hacoMap).map(pieceList -> pieceList.stream()).orElse(Stream.empty());
        List<HacoMapPiece> filteredHacoMap =
                Stream.concat(pickupStream, pieces.stream()).filter(piece -> !mergedPieceCodeSet.contains(piece.pieceCode)).collect(
                        Collectors.toList());
        HacoMapPickup newPickup = new HacoMapPickup();
        newPickup.addAllHacoMaps(filteredHacoMap);
        newPickup.setPickupDatetime(getCurrentLocalDateTime());

        return newPickup;
    }

    // ===================================================================================
    //                                                                           File Path
    //                                                                           =========
    protected String buildPieceDirPath(String clientDirPath) {
        return clientDirPath + BASE_PIECE_DIR_PATH;
    }

    protected String buildPickupFilePath(String clientDirPath) {
        return clientDirPath + BASE_PICKUP_FILE_PATH;
    }

    // ===================================================================================
    //                                                                       Assert Helper
    //                                                                       =============
    protected void assertClientDirPath(String clientDirPath) {
        if (clientDirPath == null || clientDirPath.trim().length() == 0) {
            String msg = "The argument 'clientDirPath' should not be null or empty: " + clientDirPath;
            throw new IllegalArgumentException(msg);
        }
    }
}
