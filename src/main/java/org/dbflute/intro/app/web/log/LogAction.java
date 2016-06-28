package org.dbflute.intro.app.web.log;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
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
public class LogAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                 index
    //                                                 -----
    @Execute
    public JsonResponse<List<LogBean>> index(String project) {
        File[] logFiles = findLogFiles(project);
        if (logFiles == null) {
            return JsonResponse.asEmptyBody();
        }
        List<LogBean> beans = mappingToBeans(logFiles);
        return asJson(beans);
    }

    private File[] findLogFiles(String project) {
        File logDir = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/log/");
        return logDir.listFiles((dir, name) -> name.endsWith(".log"));
    }

    private List<LogBean> mappingToBeans(File[] logFiles) {
        return Stream.of(logFiles).map(logFile -> {
            String content = "";
            try {
                content = FileUtils.readFileToString(logFile, "UTF-8");
            } catch (IOException e) {
                throw new LaSystemException("Cannot read the file: " + logFile);
            }
            return new LogBean(logFile.getName(), content);
        }).collect(Collectors.toList());
    }
}
