package org.dbflute.intro.app.web.dfprop;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.DfpropFileNotFoundException;
import org.lastaflute.core.exception.LaSystemException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author deco
 */
public class DfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String UTF8 = "UTF-8";

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    @Execute(urlPattern ="{}/@word")
    public JsonResponse<List<DfpropBean>> list(String project) {
        File[] dfpropFiles = findDfpropFiles(project);
        List<DfpropBean> beans = mappingToBeans(dfpropFiles);
        return asJson(beans);
    }

    private File[] findDfpropFiles(String project) {
        File dfpropDir = new File(IntroPhysicalLogic.BASE_DIR_PATH, getProjectPath(project));
        File[] dfpropFiles = dfpropDir.listFiles((dir, name) -> name.endsWith(".dfprop"));
        if (dfpropFiles == null || dfpropFiles.length == 0) {
            throw new DfpropFileNotFoundException("Not found dfprop files. file dir: " + dfpropDir.getPath());
        }
        return dfpropFiles;
    }

    private List<DfpropBean> mappingToBeans(File[] dfpropFiles) {
        return Stream.of(dfpropFiles).map(dfpropFile -> {
            String fileText;
            try {
                fileText = FileUtils.readFileToString(dfpropFile, UTF8);
            } catch (IOException e) {
                throw new LaSystemException("Cannot read the file: " + dfpropFile);
            }
            return new DfpropBean(dfpropFile.getName(), fileText);
        }).collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String project, String fileName, DfpropUpdateForm form) {
        validate(form, messages -> {});

        File dfpropFile = findDfpropFile(project, fileName);
        writeDfpropFile(form.content, dfpropFile);

        return JsonResponse.asEmptyBody();
    }

    private File findDfpropFile(String project, String fileName) {
        File dfpropFile = new File(IntroPhysicalLogic.BASE_DIR_PATH, getProjectPath(project) + fileName);
        if (!dfpropFile.isFile()) {
            throw new DfpropFileNotFoundException("Not found dfprop file: " + dfpropFile.getPath());
        }
        return dfpropFile;
    }

    private void writeDfpropFile(String content, File dfpropFile) {
        try {
            FileUtils.write(dfpropFile, content, UTF8);
        } catch (IOException e) {
            throw new LaSystemException("Cannot write the file: " + dfpropFile);
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private String getProjectPath(String project) {
        return "dbflute_" + project + "/dfprop/";
    }
}
