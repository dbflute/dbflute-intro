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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.optional.OptionalThing;
import org.dbflute.util.Srl;

/**
 * @author p1us2er0
 * @author jflute
 */
public class TaskExecutionLogic {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private IntroPhysicalLogic introPhysicalLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // TODO jflute intro: make TaskInstruction class (2016/07/14)
    public boolean execute(String project, String taskExp, OptionalThing<String> env, CommandOutputStreamProvider streamProvider) {
        final List<ProcessBuilder> dbfluteTaskList = prepareTaskList(taskExp);
        return dbfluteTaskList.stream().allMatch(processBuilder -> {
            final Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
            env.ifPresent(value -> {
                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + value);
            });
            final String clientPath = introPhysicalLogic.toDBFluteClientPath(project);
            processBuilder.directory(new File(clientPath));

            int resultCode = executeCommand(processBuilder, streamProvider);
            return resultCode == 0;
        });
    }

    @FunctionalInterface
    public static interface CommandOutputStreamProvider {
        OutputStream provide() throws IOException;
    }

    // ===================================================================================
    //                                                                           Task List
    //                                                                           =========
    private List<ProcessBuilder> prepareTaskList(String taskExp) {
        List<String> taskList = Srl.splitListTrimmed(taskExp, ",");
        List<List<String>> commandList = taskList.stream().map(task -> {
            return Arrays.asList("manage", task);
        }).collect(Collectors.toList());
        return toProcessBuilderList(commandList);
    }

    protected List<ProcessBuilder> toProcessBuilderList(List<List<String>> commandList) {
        List<ProcessBuilder> processBuilderList = commandList.stream().filter(command -> !command.isEmpty()).map(command -> {
            String onName = System.getProperty("os.name").toLowerCase();
            List<String> list = new ArrayList<String>();
            if (onName.startsWith("windows")) {
                list.add("cmd");
                list.add("/c");
                list.add(command.get(0) + ".bat");
            } else {
                list.add("sh");
                list.add(command.get(0) + ".sh");
            }

            if (command.size() > 1) {
                list.addAll(command.subList(1, command.size()));
            }

            return new ProcessBuilder(list);
        }).collect(Collectors.toList());

        return processBuilderList;
    }

    // ===================================================================================
    //                                                                             Process
    //                                                                             =======
    private int executeCommand(ProcessBuilder processBuilder, CommandOutputStreamProvider streamProvider) {
        processBuilder.redirectErrorStream(true);
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        int result = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream())) // for getting console
                ; OutputStream ous = streamProvider.provide() // for e.g. GUI screen
        ) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                IOUtils.write(line, ous);
                IOUtils.write("\n", ous); // LF fixedly
                ous.flush();

                if (line.equals("BUILD FAILED")) {
                    result = 1;
                }
            }
        } catch (IOException e) {
            String msg = "Failed to execute the command: " + process;
            throw new IllegalStateException(msg, e);
        }
        if (result == 0) {
            result = process.exitValue();
        }
        return result;
    }
}
