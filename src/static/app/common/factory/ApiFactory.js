export default class ApiFactory {
  // ===============================================================================
  //                                                                           Intro
  //                                                                           =====
  manifest() {
    return ffetch.post('api/intro/manifest');
  }
  classifications() {
    return ffetch.post('api/intro/classifications');
  }
  configuration() {
    return ffetch.post('api/intro/configuration');
  }

  // ===============================================================================
  //                                                                         Welcome
  //                                                                         =======

  createWelcomeClient(client, testConnection) {
    return ffetch.post('api/welcome/create',
      { body: { client: client, testConnection: testConnection } });
  }

  // ===============================================================================
  //                                                                          Client
  //                                                                          ======
  clientList() {
    return ffetch.post('api/client/list');
  }
  clientOperation(projectName) {
    return ffetch.post(`api/client/operation/${projectName}`);
  }
  createClient(client, testConnection) {
    return ffetch.post(
      `api/client/create`,
      { body: { client, testConnection } }
    )
  }
  updateClient(clientBody, testConnection) {
    return ffetch.post(`api/client/edit/${clientBody.projectName}`,
      { body: { clientBody: clientBody, testConnection: testConnection } });
  }
  removeClient(clientBody) {
    return ffetch.post(`api/client/delete/${clientBody.project}`);
  }
  settings(projectName) {
    return ffetch.post(`api/settings/${projectName}`)
  }
  updateSettings(clientBody) {
    return ffetch.post(`api/settings/edit/${clientBody.projectName}`,
      { body: { client: clientBody } });
  }
  dfporpBeanList(clientBody) {
    return ffetch.post(`api/dfprop/${clientBody.projectName}/list`);
  }
  syncSchema(projectName) {
    return ffetch.post(`api/dfprop/${projectName}/syncschema`);
  }
  editSyncSchema(projectName, syncSchemaSettingData) {
    return ffetch.post(`api/dfprop/${projectName}/syncschema/edit`,
      {
        body: {
          url: syncSchemaSettingData.url,
          schema: syncSchemaSettingData.schema,
          user: syncSchemaSettingData.user,
          password: syncSchemaSettingData.password,
          isSuppressCraftDiff: syncSchemaSettingData.isSuppressCraftDiff || false // need not null
        }
      });
  }
  schemaPolicy(projectName) {
    return ffetch.post(`api/dfprop/${projectName}/schemapolicy`);
  }
  editSchemaPolicy(projectName, schemaPolicyData) {
    return ffetch.post(`api/dfprop/${projectName}/schemapolicy/edit`,
      {
        body: {
          wholeMap: schemaPolicyData.wholeMap,
          tableMap: schemaPolicyData.tableMap,
          columnMap: schemaPolicyData.columnMap
        }
      });
  }
  document(projectName) {
    return ffetch.post(`api/dfprop/${projectName}/document`);
  }
  editDocument(projectName, documentSetting) {
    return ffetch.post(`api/dfprop/${projectName}/document/edit`,
      {
        body: {
          upperCaseBasic: documentSetting.upperCaseBasic,
          aliasDelimiterInDbComment: documentSetting.aliasDelimiterInDbComment,
          dbCommentOnAliasBasis: documentSetting.dbCommentOnAliasBasis,
          checkColumnDefOrderDiff: documentSetting.checkColumnDefOrderDiff,
          checkDbCommentDiff: documentSetting.checkDbCommentDiff,
          checkProcedureDiff: documentSetting.checkProcedureDiff
        }
      });
  }
  playsqlBeanList(projectName) {
    return ffetch.post(`api/playsql/${projectName}/list`);
  }
  logBeanList(projectName) {
    return ffetch.post(`api/log/${projectName}/list`);
  }

  // ===============================================================================
  //                                                                          Engine
  //                                                                          ======
  engineLatest() {
    return ffetch.post('api/engine/latest');
  }
  engineVersions() {
    return ffetch.post('api/engine/versions');
  }
  // needs trailing slash if URL parameter contains dot
  downloadEngine(params) {
    return ffetch.post(`api/engine/download/${params.version}/`);
  }
  removeEngine(params) {
    return ffetch.post(`api/engine/remove/${params.version}/`);
  }

  // ===============================================================================
  //                                                                            Task
  //                                                                            ====
  task(projectName, task) {
    return ffetch.post(`api/task/execute/${projectName}/${task}`);
  }
  // ===============================================================================
  //                                                                           Retry
  //                                                                           =====
  retry(method, url, data, useSystemProxies) {
    data = data || {};
    data['useSystemProxies'] = useSystemProxies;
    return http({
      method: method,
      url: url,
      data: data
    });
  }
}
