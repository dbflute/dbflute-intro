/*
 * Copyright 2014-2021 the original author or authors.
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.intro.app.logic.intro.IntroPhysicalLogic;
import org.dbflute.intro.bizfw.util.ProcessUtil;
import org.dbflute.intro.dbflute.allcommon.CDef;
import org.dbflute.optional.OptionalThing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The logic for DBFlute task execution. (e.g. you can actually execute it)
 * @author p1us2er0
 * @author jflute
 * @author deco
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
    // #needs_fix jflute the env is only for SchemaSyncCheck however is it used now? can be removed? (2021/05/01)
    public String execute(String projectName, List<CDef.TaskType> taskTypeList, OptionalThing<String> env) throws TaskErrorResultException {
        logger.debug("...Executing the DBFlute task: client={}, tasks={}", projectName, taskTypeList);
        final List<ProcessBuilder> dbfluteTaskList = prepareTaskList(taskTypeList);
        final StringBuilder logSb = new StringBuilder();
        for (ProcessBuilder processBuilder : dbfluteTaskList) {
            final Map<String, String> environment = processBuilder.environment();
            environment.put("pause_at_end", "n");
            environment.put("answer", "y");
            env.ifPresent(value -> {
                environment.put("DBFLUTE_ENVIRONMENT_TYPE", "schemaSyncCheck_" + value);
            });
            final String clientPath = introPhysicalLogic.buildClientPath(projectName);
            processBuilder.directory(new File(clientPath)); // change current directory
            final String commandResult = executeCommand(processBuilder);
            logSb.append(commandResult);
        }
        return logSb.toString();
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
    private String executeCommand(ProcessBuilder processBuilder) throws TaskErrorResultException {
        processBuilder.redirectErrorStream(true);
        final Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start the process: builder=" + processBuilder, e);
        }
        int resultCode = 0;
        boolean schemaNotSynchronized = false;
        boolean schemaPolicyViolated = false;
        StringBuilder logSb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream())) // for getting console
        ) {
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                if (isErrorMessageLine(line)) {
                    logSb.append(line).append("\n"); // for error message, fixedly LF
                }

                if (isErrorLine(line)) {
                    resultCode = 1;
                }

                if (isSchemaNotSynchronized(line)) {
                    schemaNotSynchronized = true;
                }

                if (isSchemaPolicyViolated(line)) {
                    schemaPolicyViolated = true;
                }
            }
        } catch (IOException e) {
            if (isNazoErrorByJetty(e)) { // #for_now may be unneeded by quitting output stream by jflute
                logger.debug("Nazo Error! {}", processBuilder, e);
                return logSb.toString();
            } else {
                String msg = "Failed to execute the command: " + process;
                throw new IllegalStateException(msg, e);
            }
        }
        if (resultCode == 0) {
            resultCode = waitForProcessEnding(process);
        }
        throwIfSchemaNotSynchronized(schemaNotSynchronized, resultCode, logSb);
        throwIfSchemaPolicyViolated(schemaPolicyViolated, resultCode, logSb);
        handleErrorResultCode(resultCode, logSb);
        return logSb.toString();
    }

    private boolean isErrorMessageLine(String line) {
        return !line.contains("[df-doc] \tat ");
    }

    private boolean isErrorLine(String line) { // depending on Apache Ant
        return line.equals("BUILD FAILED");
    }

    private boolean isSchemaNotSynchronized(String line) {
        return line.contains("org.dbflute.exception.DfSchemaSyncCheckGhastlyTragedyException");
    }

    private boolean isSchemaPolicyViolated(String line) {
        return line.contains("org.dbflute.exception.DfSchemaPolicyCheckViolationException");
    }

    private boolean isNazoErrorByJetty(IOException e) {
        return e.getClass().getSimpleName().equals("EofException") && e.getMessage() != null && e.getMessage().contains("Closed");
    }

    private int waitForProcessEnding(Process process) {
        try {
            return process.waitFor();
        } catch (InterruptedException continued) {
            logger.warn("Failed to wait for the process: " + process, continued);
            return 0; // unknown as success for now
        }
    }

    // -----------------------------------------------------
    //                                        Error Handling
    //                                        --------------
    private void throwIfSchemaNotSynchronized(boolean schemaNotSynchronized, int resultCode, StringBuilder logSb)
            throws SchemaNotSynchronizedException {
        if (schemaNotSynchronized) {
            String msg = "Schema not synchronized";
            throw new SchemaNotSynchronizedException(msg, resultCode, logSb.toString());
        }
    }

    private void throwIfSchemaPolicyViolated(boolean schemaPolicyViolated, int resultCode, StringBuilder logSb)
            throws SchemaPolicyViolatedException {
        if (schemaPolicyViolated) {
            String msg = "Schema Policy is violated";
            throw new SchemaPolicyViolatedException(msg, resultCode, logSb.toString());
        }
    }

    private void handleErrorResultCode(int resultCode, StringBuilder logSb) throws TaskErrorResultException {
        if (resultCode != 0) {
            // #hope want to set task type as exception info by jflute
            String msg = "Failed to execute the task: resultCode=" + resultCode;
            throw new TaskErrorResultException(msg, resultCode, logSb.toString());
        }
    }

    public static class TaskErrorResultException extends Exception {

        private static final long serialVersionUID = 1L;
        private final String processLog;
        private final int resultCode;

        public TaskErrorResultException(String msg, int resultCode, String processLog) {
            super(msg);
            this.resultCode = resultCode;
            this.processLog = processLog;
        }

        public int getResultCode() {
            return resultCode;
        }

        public String getProcessLog() {
            return processLog;
        }
    }

    public static class SchemaNotSynchronizedException extends TaskErrorResultException {
        private static final long serialVersionUID = 1L;

        public SchemaNotSynchronizedException(String msg, int resultCode, String processLog) {
            super(msg, resultCode, processLog);
        }
    }

    public static class SchemaPolicyViolatedException extends TaskErrorResultException {
        private static final long serialVersionUID = 1L;

        public SchemaPolicyViolatedException(String msg, int resultCode, String processLog) {
            super(msg, resultCode, processLog);
        }
    }
}
