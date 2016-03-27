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
package org.dbflute.intro.app.logic.simple;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.dbflute.optional.OptionalThing;

/**
 * @author p1us2er0
 * @author jflute
 */
public class DbFluteTaskLogic {

    public boolean execute(String project, String task, OptionalThing<String> env, OutputStream outputStream) {
        List<ProcessBuilder> dbfluteTaskList;
        if ("doc".equals(task)) {
            dbfluteTaskList = getDocCommandList();
        } else if ("loadDataReverse".equals(task)) {
            dbfluteTaskList = getLoadDataReverseCommandList();
        } else if ("schemaSyncCheck".equals(task)) {
            dbfluteTaskList = getSchemaSyncCheckCommandList();
        } else if ("replaceSchema".equals(task)) {
            dbfluteTaskList = getReplaceSchemaCommandList();
        } else {
            throw new RuntimeException("タスク不正");
        }

        boolean result = dbfluteTaskList.stream().allMatch(processBuilder -> {
            Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
            env.ifPresent(value -> {
                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + value);
            });
            processBuilder.directory(new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project));

            int resultCode = executeCommand(processBuilder, outputStream);
            return resultCode == 0;
        });

        return result;
    }

    public List<ProcessBuilder> getDocCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "doc"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getLoadDataReverseCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "jdbc"));
        commandList.add(Arrays.asList("manage", "load-data-reverse"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getSchemaSyncCheckCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "schema-sync-check"));

        return getCommandList(commandList);
    }

    public List<ProcessBuilder> getReplaceSchemaCommandList() {

        List<List<String>> commandList = new ArrayList<List<String>>();
        commandList.add(Arrays.asList("manage", "replace-schema"));

        return getCommandList(commandList);
    }

    protected List<ProcessBuilder> getCommandList(List<List<String>> commandList) {
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

    public int executeCommand(ProcessBuilder processBuilder, OutputStream outputStream) {
        processBuilder.redirectErrorStream(true);
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int result = 0;
        try (InputStream inputStream = process.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){

            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }

                IOUtils.write(line, outputStream);
                IOUtils.write(System.getProperty("line.separator"), outputStream);
                outputStream.flush();

                if (line.equals("BUILD FAILED")) {
                    result = 1;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (result == 0) {
            result = process.exitValue();
        }

        return result;
    }
}
