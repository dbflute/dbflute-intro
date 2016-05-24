package org.dbflute.intro.app.web.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.FileNotFoundException;
import org.dbflute.intro.mylasta.exception.FileNotReadException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

/**
 * @author deco
 */
public class ClientDfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String UTF8 = "UTF-8";

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<List<ClientDfpropBean>> index(String project) {
        // find dfprop files.
        // TODO deco to private method by jflute (2016/05/24)
        File dfpropDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/dfprop");
        File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) {
            throw new FileNotFoundException("Not found dfprop files. file dir=" + dfpropDir.getPath());
        }

        // create display bean.
        List<ClientDfpropBean> beans = mappingToBeans(dfpropFiles);

        return asJson(beans);
    }

    private List<ClientDfpropBean> mappingToBeans(File[] dfpropFiles) {
        // ↑ For returning an exception, I don't use stream.
        // TODO deco like this by jflute (2016/05/24)
        //return Stream.of(dfpropFiles).map(dfpropFile -> {
        //    String fileText;
        //    try {
        //        fileText = FileUtils.readFileToString(dfpropFile, UTF8);
        //    } catch (IOException e) {
        //        throw new LaSystemException("Cannot read the file: " + dfpropFile);
        //    }
        //    return new ClientDfpropBean(dfpropFile.getName(), fileText);
        //}).collect(Collectors.toList());

        List<ClientDfpropBean> dfpropList = new ArrayList<>();
        for (File dfpropFile : dfpropFiles) {
            ClientDfpropBean bean = new ClientDfpropBean();
            bean.fileName = dfpropFile.getName();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dfpropFile), UTF8));
                bean.content = br.lines()
                        .collect(StringBuilder::new, (stringBuilder, str) -> stringBuilder.append(str).append("\n"), StringBuilder::append)
                        .toString();
                dfpropList.add(bean);
            } catch (UnsupportedEncodingException | java.io.FileNotFoundException e) {
                throw new FileNotReadException(e.getMessage(), e);
            }
        }
        return dfpropList;
    }
}
