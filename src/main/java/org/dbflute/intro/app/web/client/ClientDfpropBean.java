package org.dbflute.intro.app.web.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author deco
 */
public class ClientDfpropBean {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String BR = "<br />";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    public String fileName;
    public String content;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ClientDfpropBean(File file) throws IOException {
        this.fileName = file.getName();

        // read content
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            while (br.readLine() != null) {
                sb.append(br.readLine()).append(BR);
            }
            this.content = sb.toString();
            br.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("file not found. file = " + file);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
