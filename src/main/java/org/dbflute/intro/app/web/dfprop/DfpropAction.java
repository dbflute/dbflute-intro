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
package org.dbflute.intro.app.web.dfprop;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dfprop.DfpropPhysicalLogic;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.mylasta.exception.DfpropFileNotFoundException;
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
public class DfpropAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private static final String UTF8 = "UTF-8";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // -----------------------------------------------------
    //                                          DI Component
    //                                          ------------
    @Resource
    private DfpropPhysicalLogic dfpropPhysicalLogic;

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
        File dfpropDir = new File(dfpropPhysicalLogic.buildDfpropDirPath(project));
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
    public JsonResponse<Void> update(String project, String fileName, DfpropUpdateBody body) {
        validate(body, messages -> {});

        File dfpropFile = findDfpropFile(project, fileName);
        writeDfpropFile(body.content, dfpropFile);

        return JsonResponse.asEmptyBody();
    }

    private File findDfpropFile(String project, String fileName) {
        File dfpropFile = new File(dfpropPhysicalLogic.buildDfpropFilePath(project, fileName));
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
}
