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
package org.dbflute.intro.app.web.client;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.dbflute.intro.app.logic.dbfluteclient.ClientParam;
import org.dbflute.intro.app.logic.dbfluteclient.DatabaseParam;
import org.dbflute.intro.app.logic.dbfluteclient.DbFluteClientLogic;
import org.dbflute.intro.app.logic.dbfluteclient.OptionParam;
import org.dbflute.intro.app.logic.simple.DbFluteIntroLogic;
import org.dbflute.intro.app.logic.simple.DbFluteTaskLogic;
import org.dbflute.intro.app.web.base.DbfluteIntroBaseAction;
import org.dbflute.intro.app.web.client.ClientCreateBody.ClientBody;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean.DatabaseBean;
import org.dbflute.intro.app.web.client.ClientDetailBean.ClientBean.OptionBean;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;
import org.lastaflute.web.response.StreamResponse;
import org.lastaflute.web.servlet.request.ResponseManager;

/**
 * @author p1us2er0
 */
public class ClientAction extends DbfluteIntroBaseAction {

    @Resource
    private DbFluteClientLogic dbFluteClientLogic;

    @Resource
    private DbFluteTaskLogic dbFluteTaskLogic;

    @Resource
    private ResponseManager responseManager;

    @Execute
    public JsonResponse<Map<String, Map<?, ?>>> classification() {
        Map<String, Map<?, ?>> classificationMap = dbFluteClientLogic.getClassificationMap();
        return asJson(classificationMap);
    }

    @Execute
    public JsonResponse<List<ClientDetailBean>> list() {
        List<String> projectList = dbFluteClientLogic.getProjectList();
        List<ClientDetailBean> clientDetailBeanList = projectList.stream().map(project -> {
            ClientParam clientParam = dbFluteClientLogic.convClientParamFromDfprop(project);
            return convert(clientParam);
        }).collect(Collectors.toList());

        return asJson(clientDetailBeanList);
    }

    @Execute
    public JsonResponse<ClientDetailBean> detail(String project) {
        ClientParam clientParam = dbFluteClientLogic.convClientParamFromDfprop(project);
        ClientDetailBean clientDetailBean = convert(clientParam);
        return asJson(clientDetailBean);
    }

    protected ClientDetailBean convert(ClientParam clientParam) {
        ClientDetailBean clientDetailBean = new ClientDetailBean();
        ClientBean clientBean = new ClientBean();
        clientBean.project = clientParam.getProject();
        clientBean.database = clientParam.getDatabase();
        clientBean.targetLanguage = clientParam.getTargetLanguage();
        clientBean.targetContainer = clientParam.getTargetContainer();
        clientBean.packageBase = clientParam.getPackageBase();
        clientBean.jdbcDriver = clientParam.getJdbcDriver();

        OptionalThing.ofNullable(clientParam.getDatabaseParam(), () -> {}).ifPresent(databaseParam -> {
            DatabaseBean databaseBean = new DatabaseBean();
            databaseBean.url = databaseParam.getUrl();
            databaseBean.schema = databaseParam.getSchema();
            databaseBean.user = databaseParam.getUser();
            databaseBean.password = databaseParam.getPassword();
            clientBean.databaseBean = databaseBean;
        });

        OptionalThing.ofNullable(clientParam.getSystemUserDatabaseParam(), () -> {}).ifPresent(databaseParam -> {
            DatabaseBean databaseBean = new DatabaseBean();
            databaseBean.url = databaseParam.getUrl();
            databaseBean.schema = databaseParam.getSchema();
            databaseBean.user = databaseParam.getUser();
            databaseBean.password = databaseParam.getPassword();
            clientBean.systemUserDatabaseBean = databaseBean;
        });

        clientBean.jdbcDriverJarPath = clientParam.getJdbcDriverJarPath();
        clientBean.dbfluteVersion = clientParam.getDbfluteVersion();

        OptionalThing.ofNullable(clientParam.getOptionParam(), () -> {}).ifPresent(optionParam -> {
            OptionBean optionBean = new OptionBean();
            optionBean.dbCommentOnAliasBasis = optionParam.isDbCommentOnAliasBasis();
            optionBean.aliasDelimiterInDbComment = optionParam.getAliasDelimiterInDbComment();
            optionBean.checkColumnDefOrderDiff = optionParam.isCheckColumnDefOrderDiff();
            optionBean.checkDbCommentDiff = optionParam.isCheckDbCommentDiff();
            optionBean.checkProcedureDiff = optionParam.isCheckProcedureDiff();
            optionBean.generateProcedureParameterBean = optionParam.isGenerateProcedureParameterBean();
            optionBean.procedureSynonymHandlingType = optionParam.getProcedureSynonymHandlingType();
            clientBean.optionBean = optionBean;
        });

        clientBean.schemaSyncCheckMap = new LinkedHashMap<String, DatabaseBean>();
        clientParam.getSchemaSyncCheckMap().entrySet().forEach(schemaSyncCheck -> {
            OptionalThing.ofNullable(schemaSyncCheck.getValue(), () -> {}).ifPresent(databaseParam -> {
                DatabaseBean databaseBean = new DatabaseBean();
                databaseBean.url = databaseParam.getUrl();
                databaseBean.schema = databaseParam.getSchema();
                databaseBean.user = databaseParam.getUser();
                databaseBean.password = databaseParam.getPassword();
                clientBean.schemaSyncCheckMap.put(schemaSyncCheck.getKey(), databaseBean);
            });
        });

        clientDetailBean.clientBean = clientBean;
        String project = clientParam.getProject();
        clientDetailBean.schemahtml = calcFile(project, "schema").exists();
        clientDetailBean.historyhtml = calcFile(project, "history").exists();
        clientDetailBean.replaceSchema = dbFluteClientLogic.existReplaceSchemaFile(project);
        return clientDetailBean;
    }

    protected ClientParam convert(ClientBody clientBody) {
        ClientParam clientParam = new ClientParam();
        clientParam.setProject(clientBody.project);
        clientParam.setDatabase(clientBody.database);
        clientParam.setTargetLanguage(clientBody.targetLanguage);
        clientParam.setTargetContainer(clientBody.targetContainer);
        clientParam.setPackageBase(clientBody.packageBase);
        clientParam.setJdbcDriver(clientBody.jdbcDriver);

        OptionalThing.ofNullable(clientBody.databaseBody, () -> {}).ifPresent(databaseBody -> {
            DatabaseParam databaseParam = new DatabaseParam();
            databaseParam.setUrl(databaseBody.url);
            databaseParam.setSchema(databaseBody.schema);
            databaseParam.setUser(databaseBody.user);
            databaseParam.setPassword(databaseBody.password);
            clientParam.setDatabaseParam(databaseParam);
        });

        OptionalThing.ofNullable(clientBody.systemUserDatabaseBody, () -> {}).ifPresent(databaseBody -> {
            DatabaseParam databaseParam = new DatabaseParam();
            databaseParam.setUrl(databaseBody.url);
            databaseParam.setSchema(databaseBody.schema);
            databaseParam.setUser(databaseBody.user);
            databaseParam.setPassword(databaseBody.password);
            clientParam.setSystemUserDatabaseParam(databaseParam);
        });

        clientParam.setJdbcDriverJarPath(clientBody.jdbcDriverJarPath);
        clientParam.setDbfluteVersion(clientBody.dbfluteVersion);

        OptionalThing.ofNullable(clientBody.optionBody, () -> {}).ifPresent(optionBody -> {
            OptionParam optionParam = new OptionParam();
            optionParam.setDbCommentOnAliasBasis(optionBody.dbCommentOnAliasBasis);
            optionParam.setAliasDelimiterInDbComment(optionBody.aliasDelimiterInDbComment);
            optionParam.setCheckColumnDefOrderDiff(optionBody.checkColumnDefOrderDiff);
            optionParam.setCheckDbCommentDiff(optionBody.checkDbCommentDiff);
            optionParam.setCheckProcedureDiff(optionBody.checkProcedureDiff);
            optionParam.setGenerateProcedureParameterBean(optionBody.generateProcedureParameterBean);
            optionParam.setProcedureSynonymHandlingType(optionBody.procedureSynonymHandlingType);
            clientParam.setOptionParam(optionParam);
        });

        clientParam.setSchemaSyncCheckMap(new LinkedHashMap<String, DatabaseParam>());
        clientBody.schemaSyncCheckMap.entrySet().forEach(schemaSyncCheck -> {
            OptionalThing.ofNullable(schemaSyncCheck.getValue(), () -> {}).ifPresent(databaseBody -> {
                DatabaseParam databaseParam = new DatabaseParam();
                databaseParam.setUrl(databaseBody.url);
                databaseParam.setSchema(databaseBody.schema);
                databaseParam.setUser(databaseBody.user);
                databaseParam.setPassword(databaseBody.password);
                clientParam.getSchemaSyncCheckMap().put(schemaSyncCheck.getKey(), databaseParam);
            });
        });

        return clientParam;
    }

    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientParam clientParam = convert(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            dbFluteClientLogic.testConnection(clientParam);
        }
        dbFluteClientLogic.createClient(clientParam, clientCreateBody.testConnection);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> update(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {});
        ClientParam clientParam = convert(clientCreateBody.clientBody);
        if (clientCreateBody.testConnection) {
            dbFluteClientLogic.testConnection(clientParam);
        }
        dbFluteClientLogic.updateClient(clientParam, clientCreateBody.testConnection);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> delete(String project) {
        dbFluteClientLogic.deleteClient(project);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public JsonResponse<Void> task(String project, String task, OptionalThing<String> env) {
        HttpServletResponse response = responseManager.getResponse();
        response.setContentType("text/plain; charset=UTF-8");
        OutputStream outputStream;
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dbFluteTaskLogic.execute(project, task, env, outputStream);
        return JsonResponse.asEmptyBody();
    }

    @Execute
    public StreamResponse schemahtml(String project) {
        return createHtmlStreamResponse(calcFile(project, "schema"));
    }

    @Execute
    public StreamResponse historyhtml(String project) {
        return createHtmlStreamResponse(calcFile(project, "history"));
    }

    protected File calcFile(String project, String type) {
        File file = new File(DbFluteIntroLogic.BASE_DIR_PATH, "dbflute_" + project + "/output/doc/" + type + "-" + project + ".html");
        return file;
    }

    protected StreamResponse createHtmlStreamResponse(File file) {
        StreamResponse streamResponse = new StreamResponse("");
        streamResponse.contentType("text/html; charset=UTF-8");
        streamResponse.stream(writtenStream -> {
            writtenStream.write(FileUtils.openInputStream(file));
        });

        return streamResponse;
    }
}
