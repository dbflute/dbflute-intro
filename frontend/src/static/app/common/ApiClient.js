import i18n from 'i18next'
import { resultModal$ } from './result-view.riot'
import { FFetch } from 'ffetch'
import { Subject } from 'rxjs'
import Q from 'q'

function checkStatus(res) {
  if (!res.ok) {
    return res.json()
      .then(err => Promise.reject({
        status: res.status,
        data: err
      }))
  }
  return res
}

export class FFetchWrapper extends FFetch {

  constructor(...arg) {
    super(...arg)

    this.errorEvents = new Subject()
  }

  get(url, options) {
    return Q(super.get(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res))
  }

  post(url, options) {
    return Q(super.post(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res))
  }

  put(url, options) {
    return Q(super.put(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res))
  }

  del(url, options) {
    return Q(super.del(url, options))
      .then(res => checkStatus(res))
      .then(res => res.json())
      .catch(res => this.handleResponseError(res))
  }

  get errors() {
    return this.errorEvents
  }

  handleResponseError(res) {
    this.errorEvents.next(res)
    return Q.reject(new Error(res))
  }
}

const ffetch = new FFetchWrapper()

//===================================================================================
//                                                                     Error Handling
//                                                                     ==============
// see IntroApiFailureHook.java for failure response
ffetch.errors.subscribe(response => {
  let header = null
  let messages = null
  // #thinking improvement: does it need to reload screen when status=0, 401? (implemented until 0.2.x)
  //let reload = false;
  let validationError = false
  if (response.status === 0) {
    messages = ['Cannot access the server, retry later']
  }
  // #hope refactor: extract to method
  if (response.status === 400) {
    header = '400 Bad Request'
    // #hope improvement: formal validation error handling
    if (response.data.failureType) { // basically here (unified JSON if 400)
      header = header + ': ' + response.data.failureType
      validationError = response.data.failureType === 'VALIDATION_ERROR'
    }
    if (response.data.messages) { // basically here (unified JSON if 400)
      let messageList = new Array()
      for (let key in response.data.messages) {
        for (let i in response.data.messages[key]) {
          let message = response.data.messages[key][i]
          if (key.match(/List/)) {
            if (key.match(/[0-9]/)) {
              let newKey = ''
              let splitList = key.split(/[0-9]/)
              for (i in splitList) {
                newKey += splitList[i].replace(/[0-9]/, '')
              }
              key = newKey
            } else {
              key += '[]'
            }
          }
          if (key.lastIndexOf('.')) {
            key = key.substring(key.lastIndexOf('.') + 1)
          }
          if (key === '_global') { // don't use key if global
            messageList.push(message + '\r\n')
          } else {
            const label = i18n.t(`LABEL_${key}`)
            const symbol = (label === '') ? '' : '：'
            messageList.push(`${label}${symbol}${message}\r\n`)
          }
        }
      }
      messages = messageList
    } else {
      messages = Array.isArray(response.data) ? response.data : [response.data]
    }
  } else if (response.status === 401) {
    header = '401 Not Authorized'
  } else if (response.status === 403) {
    header = '403 Forbidden'
  } else if (response.status >= 500) {
    header = '500 Server Error'
    messages = Array.isArray(response.data) ? response.data : [response.data]
  }
  if (header != null || messages != null) {
    const modalSize = validationError ? 'small' : 'large'
    resultModal$.trigger('result', { header, messages, modalSize })
  }
})

export class ApiClient {

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
      { body: { client: client, testConnection: testConnection } , timeout: 180000 }) // Docker起動でクライアント作成時はDBFluteEngineのunzipに1分以上かかる場合があるため、タイムアウト時間に余裕を持たせる
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

  clientPropbase(projectName) {
    return ffetch.post(`api/client/propbase/${projectName}`)
  }

  createClient(client, testConnection) {
    return ffetch.post('api/client/create', {
      body: { client, testConnection },
    })
  }

  removeClient(clientBody) {
    return ffetch.post(`api/client/delete/${clientBody.project}`)
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
    return ffetch.post(`api/dfprop/schemasync/${projectName}`)
  }

  editSyncSchema(projectName, syncSchemaSettingData) {
    return ffetch.post(`api/dfprop/schemasync/edit/${projectName}/`, {
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

  getSchemapolicyStatementSubject(mapType) {
    return ffetch.post(`api/dfprop/schemapolicy/statement/subject?maptype=${mapType}`)
  }

  deleteSchemapolicyStatement(projectName, schemaPolicyData) {
    return ffetch.post(
      `api/dfprop/schemapolicy/statement/delete/${projectName}`,
      {
        body: schemaPolicyData,
      }
    )
  }

  moveSchemapolicyStatement(projectName, schemaPolicyData) {
    return ffetch.post(
      `api/dfprop/schemapolicy/statement/move/${projectName}`, { body: schemaPolicyData }
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

  // -----------------------------------------------------
  //                                              Settings
  //                                              --------
  settings(projectName) {
    return ffetch.post(`api/dfprop/settings/${projectName}`)
  }

  updateSettings(clientBody) {
    return ffetch.post(`api/dfprop/settings/edit/${clientBody.projectName}`, {
      body: { client: clientBody },
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
}

export const api = new ApiClient()

export class DbfluteTask {
  task(task, projectName, callback) { // callback: (message: string) => void
    return api.task(projectName, task).then((response) => {
      const message = response.success ? 'success' : 'failure'
      callback(message)
    })
  }
}

