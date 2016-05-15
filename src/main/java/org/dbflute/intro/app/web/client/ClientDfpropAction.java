package org.dbflute.intro.app.web.client;

import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.FileNotFoundException;
import org.dbflute.intro.mylasta.exception.FileNotReadException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deco
 */
public class ClientDfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String UTF8 = "UTF-8";

    private static final String BR = "<br>";

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // TODO DONE deco pri.A migration to ClientDfpropAction by jflute (2016/05/09)
    // TODO DONE deco pri.B don't use throws (IOException) by jflute (2016/05/09)
    @Execute
    public JsonResponse<List<ClientDfpropBean>> index(String project) {
        // find dfprop files.
        File dfpropDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        // TODO DONE deco pri.B file filtering by jflute (2016/05/09)
        // e.g. File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        // TODO DONE deco pri.B rename files to dfpropFiles by jflute (2016/05/09)
        File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) {
            // TODO DONE deco pri.C BusinessException by jflute (2016/05/09)
            throw new FileNotFoundException("Not found dfprop files. file dir=" + dfpropDir.getPath());
        }

        // create display bean.
        List<ClientDfpropBean> bean = mappingToBean(dfpropFiles);

        return asJson(bean);
    }

    private List<ClientDfpropBean> mappingToBean(File[] dfpropFiles) {
        // TODO DONE deco pri.C use List interface by jflute (2016/05/09)
        // TODO deco pri.C use stream by jflute (2016/05/09)
        // For returning an exception, I don't use stream.
        List<ClientDfpropBean> dfpropList = new ArrayList<>();
        for (File dfpropFile : dfpropFiles) {
            ClientDfpropBean bean = new ClientDfpropBean();
            bean.fileName = dfpropFile.getName();
            // TODO deco pri.C don't depend on view, with refactoring logic by jflute (2016/05/09)
            // TODO DONE deco pri.B avoid mojibake by jflute (2016/05/09)
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dfpropFile), UTF8));
                bean.content = br.lines()
                    .collect(StringBuilder::new
                        , (stringBuilder, str) -> stringBuilder.append(str).append(BR)
                        , StringBuilder::append)
                    .toString();
                dfpropList.add(bean);
            } catch (UnsupportedEncodingException | java.io.FileNotFoundException e) {
                throw new FileNotReadException(e.getMessage(), e);
            }
        }
        return dfpropList;
    }
}
