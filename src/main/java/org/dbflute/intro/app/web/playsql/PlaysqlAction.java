/*
 * Copyright 2014-2015 the original author or authors.
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
package org.dbflute.intro.app.web.playsql;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.core.FileHandlingLogic;
import org.dbflute.intro.app.logic.playsql.PlaysqlPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.PlaysqlFileNotFoundException;
import org.lastaflute.core.exception.LaSystemException;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
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
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private PlaysqlPhysicalLogic playsqlPhysicalLogic;
    @Resource
    private FileHandlingLogic fileHandlingLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                                  list
    //                                                  ----
    // TODO deco with directory? by jflute (2016/07/26)
    @Execute(urlPattern = "{}/@word")
    public JsonResponse<List<PlaysqlBean>> list(String project) {
        File[] playsqlFiles = findPlaysqlFiles(project);
        List<PlaysqlBean> beans = mappingToBeans(playsqlFiles);
        return asJson(beans);
    }

    private File[] findPlaysqlFiles(String project) {
        File playsqlDir = new File(playsqlPhysicalLogic.buildPlaysqlDirPath(project));
        File[] playsqlFiles = playsqlDir.listFiles((dir, name) -> name.endsWith(".sql"));
        if (playsqlFiles == null || playsqlFiles.length == 0) {
            throw new PlaysqlFileNotFoundException("Not found playsql files. file dir: " + playsqlDir.getPath());
        }
        return playsqlFiles;
    }
    
    private List<PlaysqlBean> mappingToBeans(File[] playsqlFiles) {
        return Stream.of(playsqlFiles).map(playsqlFile -> {
            return new PlaysqlBean(playsqlFile.getName(), fileHandlingLogic.readFile(playsqlFile));
        }).collect(Collectors.toList());
    }

    // -----------------------------------------------------
    //                                                update
    //                                                ------
    // TODO jflute intro: needs adjustment? (2016/07/26)
    @Execute(urlPattern = "{}/@word/{}")
    public JsonResponse<Void> update(String project, String fileName, PlaysqlUpdateBody body) {
        validate(body, messages -> {});

        File playsqlFile = findPlaysqlFile(project, fileName);
        writePlaysqlFile(body.content, playsqlFile);

        return JsonResponse.asEmptyBody();
    }

    private File findPlaysqlFile(String project, String fileName) {
        File playsqlFile = new File(playsqlPhysicalLogic.buildPlaysqlFilePath(project, fileName));
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
}
