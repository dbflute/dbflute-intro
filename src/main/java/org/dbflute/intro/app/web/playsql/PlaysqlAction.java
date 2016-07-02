package org.dbflute.intro.app.web.playsql;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.PlaysqlFileNotFoundException;
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
public class PlaysqlAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<PlaysqlBean>> list(String project) {
        File[] playsqlFiles = findPlaysqlFiles(project);
        List<PlaysqlBean> beans = mappingToBeans(playsqlFiles);
        return asJson(beans);
    }

    private File[] findPlaysqlFiles(String project) {
        File playsqlDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, getProjectPath(project));
        File[] playsqlFiles = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"));
        if (playsqlFiles == null || playsqlFiles.length == 0) {
            throw new PlaysqlFileNotFoundException("Not found playsql files. file dir: " + playsqlDir.getPath());
        }
        return playsqlFiles;
    }

    private List<PlaysqlBean> mappingToBeans(File[] playsqlFiles) {
        return Stream.of(playsqlFiles).map(playsqlFile -> {
            String content;
            try {
                content = FileUtils.readFileToString(playsqlFile, "UTF-8");
            } catch (IOException e) {
                throw new LaSystemException("Cannot read the file: " + playsqlFile);
            }
            return new PlaysqlBean(playsqlFile.getName(), content);
        }).collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String project, String fileName, PlaysqlUpdateForm form) {
        validate(form, messages -> {});

        File playsqlFile = findPlaysqlFile(project, fileName);
        writePlaysqlFile(form.content, playsqlFile);

        return JsonResponse.asEmptyBody();
    }

    private File findPlaysqlFile(String project, String fileName) {
        File playsqlFile = new File(DbFluteIntroLogic.BASE_DIR_PATH, getProjectPath(project) + fileName);
        if (!playsqlFile.isFile()) {
            throw new PlaysqlFileNotFoundException("Not found playsql file: " + playsqlFile.getPath());
        }
        return playsqlFile;
    }

    private void writePlaysqlFile(String content, File playsqlFile) {
        try {
            FileUtils.write(playsqlFile, content, "UTF-8");
        } catch (IOException e) {
            throw new LaSystemException("Cannot write the file: " + playsqlFile);
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private String getProjectPath(String project) {
        return "dbflute_" + project + "/playsql/";
    }
}
