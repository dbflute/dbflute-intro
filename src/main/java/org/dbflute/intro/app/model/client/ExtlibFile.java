package org.dbflute.intro.app.model.client;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author ryohei
 */
public class ExtlibFile {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final File file;
    protected final byte[] fileData;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ExtlibFile(String fileName, String fileDataBase64) {
        this.fileData = Base64.getDecoder().decode(fileDataBase64);
        this.file = new File(fileName);
    }

    public ExtlibFile(File file) {
        this.file = file;
        try {
            this.fileData = IOUtils.toByteArray(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy the jar file to extlib: " + file.getName(), e);
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    public File getFile() {
        return file;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public String getFileName() {
        return file.getName();
    }

    public String getCanonicalPath() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to get the canonicalPath: " + file.getName(), e);
        }
    }

}
