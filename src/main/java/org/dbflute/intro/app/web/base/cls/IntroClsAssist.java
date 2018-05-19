/*
 * Copyright 2014-2018 the original author or authors.
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
package org.dbflute.intro.app.web.base.cls;

import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.intro.dbflute.allcommon.CDef.TaskType;
import org.dbflute.intro.dbflute.exbhv.ClsTargetDatabaseBhv;
import org.dbflute.intro.mylasta.appcls.AppCDef;
import org.dbflute.intro.mylasta.direction.IntroConfig;
import org.dbflute.util.DfStringUtil;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jflute
 */
public class IntroClsAssist {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroConfig introConfig;
    @Resource
    private ClsTargetDatabaseBhv databaseBhv;

    // ===================================================================================
    //                                                                          Basic Info
    //                                                                          ==========
    public Map<String, Map<?, ?>> getClassificationMap() {
        Map<String, Map<?, ?>> clsMap = new LinkedHashMap<String, Map<?, ?>>();
        clsMap.put("targetDatabaseMap", prepareTargetDatabaseMap());
        clsMap.put("targetLanguageMap", prepareTargetLanguageMap());
        clsMap.put("targetContainerMap", prepareTargetContainerMap());
        return clsMap;
    }

    private Map<String, DatabaseDefBean> prepareTargetDatabaseMap() {
        return databaseBhv.selectList(cb -> {
            cb.query().addOrderBy_DisplayOrder_Asc();
        }).stream().collect(Collectors.toMap(db -> db.getDatabaseCode(), db -> new DatabaseDefBean(db), (u, v) -> v, LinkedHashMap::new));
    }

    private Map<String, String> prepareTargetLanguageMap() {
        return CDef.TargetLanguage.listAll()
                .stream()
                .collect(Collectors.toMap(cls -> cls.code(), cls -> cls.alias(), (u, v) -> v, LinkedHashMap::new));
    }

    private Map<String, String> prepareTargetContainerMap() {
        return CDef.TargetContainer.listAll()
                .stream()
                .collect(Collectors.toMap(cls -> cls.code(), cls -> cls.alias(), (u, v) -> v, LinkedHashMap::new));
    }

    // ===================================================================================
    //                                                                    Task Instruction
    //                                                                    ================
    public List<TaskType> toTaskTypeList(AppCDef.TaskInstruction instruction) {
        String relatedTasks = instruction.relatedTasks(); // e.g. 'jdbc,doc' when 'doc'
        List<String> taskExpList = DfStringUtil.splitListTrimmed(relatedTasks, ",");
        return taskExpList.stream().map(taskExp -> checkingTaskType(taskExp, CDef.TaskType.codeOf(taskExp))).collect(Collectors.toList());
    }

    private CDef.TaskType checkingTaskType(String taskExp, CDef.TaskType taskType) {
        if (taskType == null) {
            throw new IllegalStateException("Not found the task: taskExp=" + taskExp);
        }
        return taskType;
    }

}
