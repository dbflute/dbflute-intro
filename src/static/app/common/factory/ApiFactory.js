export default class ApiFactory {
  // ===============================================================================
  //                                                                           Intro
  //                                                                           =====
  manifest() {
    return ffetch.post('api/intro/manifest')
  }
  classifications() {
    return ffetch.post('api/intro/classifications')
  }
  configuration() {
    return ffetch.post('api/intro/configuration')
  }

  // ===============================================================================
  //                                                                         Welcome
  //                                                                         =======

  createWelcomeClient(client, testConnection) {
    return ffetch.post('api/welcome/create',
      { body: { client: client, testConnection: testConnection } , timeout: 180000 }); // Docker起動でクライアント作成時はDBFluteEngineのunzipに1分以上かかる場合があるため、タイムアウト時間に余裕を持たせる
  }

  // ===============================================================================
  //                                                                          Client
  //                                                                          ======
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  clientList() {
    return ffetch.post('api/client/list')
  }
  clientOperation(projectName) {
    return ffetch.post(`api/client/operation/${projectName}`)
  }
  createClient(client, testConnection) {
    return ffetch.post('api/client/create', {
      body: { client, testConnection },
    })
  }
  updateClient(clientBody, testConnection) {
    return ffetch.post(`api/client/edit/${clientBody.projectName}`, {
      body: { clientBody: clientBody, testConnection: testConnection },
    })
  }
  removeClient(clientBody) {
    return ffetch.post(`api/client/delete/${clientBody.project}`)
  }

  // -----------------------------------------------------
  //                                              Settings
  //                                              --------
  settings(projectName) {
    return ffetch.post(`api/settings/${projectName}`)
  }
  updateSettings(clientBody) {
    return ffetch.post(`api/settings/edit/${clientBody.projectName}`, {
      body: { client: clientBody },
    })
  }

  // ===============================================================================
  //                                                                  Client::dfprop
  //                                                                  ==============
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  dfporpBeanList(clientBody) {
    return ffetch.post(`api/dfprop/list/${clientBody.projectName}`)
  }

  // -----------------------------------------------------
  //                                       SchemaSyncCheck
  //                                       ---------------
  syncSchema(projectName) {
    return ffetch.post(`api/dfprop/syncschema/${projectName}`)
  }
  editSyncSchema(projectName, syncSchemaSettingData) {
    return ffetch.post(`api/dfprop/syncschema/edit/${projectName}/`, {
      body: {
        url: syncSchemaSettingData.url,
        schema: syncSchemaSettingData.schema,
        user: syncSchemaSettingData.user,
        password: syncSchemaSettingData.password,
        isSuppressCraftDiff: syncSchemaSettingData.isSuppressCraftDiff || false, // need not null
      },
    })
  }

  // -----------------------------------------------------
  //                                     SchemaPolicyCheck
  //                                     -----------------
  schemaPolicy(projectName) {
    return ffetch.post(`api/dfprop/schemapolicy/${projectName}`)
  }
  editSchemaPolicy(projectName, schemaPolicyData) {
    return ffetch.post(`api/dfprop/schemapolicy/edit/${projectName}`, {
      body: {
        wholeMap: schemaPolicyData.wholeMap,
        tableMap: schemaPolicyData.tableMap,
        columnMap: schemaPolicyData.columnMap,
      },
    })
  }
  registerSchemapolicyStatement(projectName, schemaPolicyData) {
    return ffetch.post(
      `api/dfprop/schemapolicy/statement/register/${projectName}`,
      {
        body: schemaPolicyData,
      }
    )
  }
  getSchemapolicyStatementSubject() {
    return ffetch.post('api/dfprop/schemapolicy/statement/subject')
  }
  deleteSchemapolicyStatement(projectName, schemaPolicyData) {
    return ffetch.post(
      `api/dfprop/schemapolicy/statement/delete/${projectName}`,
      {
        body: schemaPolicyData,
      }
    )
  }

  // -----------------------------------------------------
  //                                              Document
  //                                              --------
  document(projectName) {
    return ffetch.post(`api/dfprop/document/${projectName}`)
  }
  editDocument(projectName, documentSetting) {
    return ffetch.post(`api/dfprop/document/edit/${projectName}`, {
      body: {
        upperCaseBasic: documentSetting.upperCaseBasic,
        aliasDelimiterInDbComment: documentSetting.aliasDelimiterInDbComment,
        dbCommentOnAliasBasis: documentSetting.dbCommentOnAliasBasis,
        checkColumnDefOrderDiff: documentSetting.checkColumnDefOrderDiff,
        checkDbCommentDiff: documentSetting.checkDbCommentDiff,
        checkProcedureDiff: documentSetting.checkProcedureDiff,
      },
    })
  }

  // ===============================================================================
  //                                                               Client :: playsql
  //                                                               =================
  openAlterDir(projectName) {
    return ffetch.get(`api/playsql/migration/alter/open/${projectName}`)
  }
  alter(projectName) {
    return ffetch.get(`api/playsql/migration/alter/${projectName}/`)
  }
  prepareAlterSql(projectName) {
    return ffetch.post(`api/playsql/migration/alter/prepare/${projectName}/`)
  }
  createAlterSql(projectName, alterFileName) {
    return ffetch.post(`api/playsql/migration/alter/create/${projectName}/`, {
      body: {
        alterFileName,
      },
    })
  }
  openDataDir(projectName) {
    return ffetch.get(`api/playsql/data/open/${projectName}`)
  }
  playsqlBeanList(projectName) {
    return ffetch.post(`api/playsql/list/${projectName}`)
  }

  // ===============================================================================
  //                                                                   Client :: log
  //                                                                   =============
  logBeanList(projectName) {
    return ffetch.post(`api/log/list/${projectName}`)
  }

  getLog(projectName, fileName) {
    return ffetch.post('api/log', {
      body: {
        project: projectName,
        fileName: fileName,
      },
    })
  }

  latestResult(projectName, task) {
    return ffetch.get(`api/log/latest/${projectName}/${task}`)
  }

  // ===============================================================================
  //                                                                          Engine
  //                                                                          ======
  engineLatest() {
    return ffetch.post('api/engine/latest')
  }
  engineVersions() {
    return ffetch.post('api/engine/versions')
  }
  // needs trailing slash if URL parameter contains dot
  downloadEngine(params) {
    return ffetch.post(`api/engine/download/${params.version}/`)
  }
  removeEngine(params) {
    return ffetch.post(`api/engine/remove/${params.version}/`)
  }

  // ===============================================================================
  //                                                                           Task
  //                                                                          ======
  task(projectName, task) {
    return ffetch.post(`api/task/execute/${projectName}/${task}`)
  }

  // ===============================================================================
  //                                                                           Retry
  //                                                                           =====
  retry(method, url, data, useSystemProxies) {
    data = data || {}
    data['useSystemProxies'] = useSystemProxies
    return http({
      method: method,
      url: url,
      data: data,
    })
  }
}
