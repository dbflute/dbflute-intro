/*
 * Copyright 2014-2016 the original author or authors.
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
package org.dbflute.intro.app.logic.task;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.tellfailure.TaskExecuteFailureException;
import org.dbflute.intro.bizfw.util.ProcessUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author p1us2er0
 * @author jflute
 */
public class TaskExecutionLogic {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutionLogic.class);

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // TODO jflute intro: make TaskInstruction class (2016/07/14)
    public boolean execute(String clientProject, List<CDef.TaskType> taskTypeList, OptionalThing<String> env){
        logger.debug("...Executing the DBFlute task: client={}, tasks={}", clientProject, taskTypeList);
        final List<ProcessBuilder> dbfluteTaskList = prepareTaskList(taskTypeList);
        return dbfluteTaskList.stream().allMatch(processBuilder -> {
            final Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
            env.ifPresent(value -> {
                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + value);
            });
            final String clientPath = introPhysicalLogic.buildClientPath(clientProject);
            processBuilder.directory(new File(clientPath));

            int resultCode = executeCommand(processBuilder);
            return resultCode == 0;
        });
    }

    // ===================================================================================
    //                                                                           Task List
    //                                                                           =========
    private List<ProcessBuilder> prepareTaskList(List<CDef.TaskType> taskTypeList) {
        List<List<String>> commandList =
                taskTypeList.stream().map(tp -> Arrays.asList("manage", tp.manageArg())).collect(Collectors.toList());
        return toProcessBuilderList(commandList);
    }

    private List<ProcessBuilder> toProcessBuilderList(List<List<String>> commandList) {
        return commandList.stream().filter(command -> !command.isEmpty()).map(command -> {
            List<String> partsList = new ArrayList<String>();
            if (isWindows()) {
                partsList.add("cmd");
                partsList.add("/c");
                partsList.add(command.get(0) + ".bat");
            } else {
                partsList.add("sh");
                partsList.add(command.get(0) + ".sh");
            }

            if (command.size() > 1) {
                partsList.addAll(command.subList(1, command.size()));
            }

            return new ProcessBuilder(partsList);
        }).collect(Collectors.toList());
    }

    private boolean isWindows() {
        return ProcessUtil.isWindows();
    }

    // ===================================================================================
    //                                                                             Process
    //                                                                             =======
    private int executeCommand(ProcessBuilder processBuilder) {
        processBuilder.redirectErrorStream(true);
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        int result = 0;
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream())) // for getting console
        ) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                builder.append(line).append("\n"); // for error message

                if (line.equals("BUILD FAILED")) {
                    result = 1;
                }
            }
        } catch (IOException e) {
            if (isNazoErrorByJetty(e)) {
                logger.debug("Nazo Error! {}", processBuilder, e);
                return result;
            } else {
                String msg = "Failed to execute the command: " + process;
                throw new IllegalStateException(msg, e);
            }
        }
        if (result == 0) {
            result = process.exitValue();
        }
        if (result != 0) {
            throw new TaskExecuteFailureException("Failed to execute task", builder.toString());
        }
        return result;
    }

    private boolean isNazoErrorByJetty(IOException e) {
        return e.getClass().getSimpleName().equals("EofException") && e.getMessage() != null && e.getMessage().contains("Closed");
    }
}
