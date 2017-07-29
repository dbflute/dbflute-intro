package org.dbflute.intro.app.model.document.decomment;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.dbflute.exception.DfPropFileReadFailureException;
import org.dbflute.helper.mapstring.MapListFile;
import org.dbflute.helper.message.ExceptionMessageBuilder;

// TODO done cabos DfDecoMapFile by jflute (2017/07/27)
/**
 * @author cabos
 */
public class DfDecoMapFile {

    // ===================================================================================
    //                                                                                Read
    //                                                                                ====
    public Map<String, Object> readMap(InputStream ins) {
        final MapListFile mapListFile = createMapListFile();
        try {
            return mapListFile.readMap(ins);
        } catch (Exception e) {
            throwDecoMapReadFailureException(ins, e);
            return null; // unreachable
        }
    }

    protected void throwDecoMapReadFailureException(InputStream ins, Exception e) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to read the deco-map file.");
        br.addItem("Advice");
        br.addElement("Make sure the map-string is correct in the file.");
        br.addElement("For example, the number of start and end braces are the same.");
        br.addItem("Decomment Map");
        br.addElement(ins);
        final String msg = br.buildExceptionMessage();
        throw new DfPropFileReadFailureException(msg, e);
    }

    // ===================================================================================
    //                                                                               Write
    //                                                                               =====
    public void writeMap(OutputStream ous, Map<String, Object> map) {
        final MapListFile mapListFile = createMapListFile();
        try {
            mapListFile.writeMap(ous, map);
        } catch (Exception e) {
            throwDecoMapWriteFailureException(ous, e);
        }
    }

    protected void throwDecoMapWriteFailureException(OutputStream ous, Exception e) {
        final ExceptionMessageBuilder br = new ExceptionMessageBuilder();
        br.addNotice("Failed to write the deco-map file.");
        br.addItem("Decomment Map");
        br.addElement(ous);
        final String msg = br.buildExceptionMessage();
        throw new DfPropFileReadFailureException(msg, e);
    }

    // ===================================================================================
    //                                                                        MapList File
    //                                                                        ============
    protected MapListFile createMapListFile() {
        return new MapListFile();
    }
}
