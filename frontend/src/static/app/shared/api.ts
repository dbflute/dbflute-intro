import i18n from 'i18next'
import { triggerShowResult } from './app-events'
import axios, { AxiosError, AxiosRequestConfig } from 'axios';

class ApiClient {
  get(url: string, config?: AxiosRequestConfig) {
    return axios.get(url, config).then(res => res.data).catch(handleError)
  }

  post(url: string, data?: any, config?: AxiosRequestConfig) {
    return axios.post(url, data, config).then(res => res.data).catch(handleError)
  }

  put(url: string, data?: any, config?: AxiosRequestConfig) {
    return axios.put(url, data, config).then(res => res.data).catch(handleError)
  }

  del(url: string, config?: AxiosRequestConfig) {
    return axios.delete(url, config).then(res => res.data).catch(handleError)
  }
}

//===================================================================================
//                                                                     Error Handling
//                                                                     ==============
// see IntroApiFailureHook.java for failure response
const handleError = (error: AxiosError) => {
  let header = undefined
  let messages = undefined
  // #thinking improvement: does it need to reload screen when status=0, 401? (implemented until 0.2.x)
  //let reload = false;
  let validationError = false
  const response: any = error.response;
  const status = response.status;
  if (status === 0) {
    messages = ['Cannot access the server, retry later']
  }
  // #hope refactor: extract to method
  if (status === 400) {
    header = '400 Bad Request'
    // #hope improvement: formal validation error handling
    if (response.data.failureType) { // basically here (unified JSON if 400)
      header = header + ': ' + response.data.failureType
      validationError = response.data.failureType === 'VALIDATION_ERROR'
    }
    if (response.data.messages) { // basically here (unified JSON if 400)
      let messageList = []
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
  } else if (status === 401) {
    header = '401 Not Authorized'
  } else if (status === 403) {
    header = '403 Forbidden'
  } else if (status >= 500) {
    header = '500 Server Error'
    messages = Object.values(response.data.messages)
  }
  if (header != null || messages != null) {
    const modalSize = validationError ? 'small' : 'large'
    triggerShowResult({ header, messages, modalSize })
  }
  return Promise.reject(error)
}

const apiClient = new ApiClient()

class Api {

  // ===============================================================================
  //                                                                           Intro
  //                                                                           =====
  manifest() {
    return apiClient.post('api/intro/manifest')
  }

  findClassifications(): Promise<IntroClassificationsResult> {
    return apiClient.post('api/intro/classifications')
  }

  configuration() {
    return apiClient.post('api/intro/configuration')
  }

  // ===============================================================================
  //                                                                         Welcome
  //                                                                         =======
  createWelcomeClient(client: any, testConnection: boolean) {
    return apiClient.post('api/welcome/create',
      { client: client, testConnection: testConnection },
      { timeout: 180000 }
    ) // Docker起動でクライアント作成時はDBFluteEngineのunzipに1分以上かかる場合があるため、タイムアウト時間に余裕を持たせる
  }

  // ===============================================================================
  //                                                                          Client
  //                                                                          ======
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  clientList() {
    return apiClient.post('api/client/list')
  }

  clientPropbase(projectName: string) {
    return apiClient.post(`api/client/propbase/${projectName}`)
  }

  createClient(client: any, testConnection: boolean) {
    return apiClient.post('api/client/create', {
      body: { client, testConnection },
    })
  }

  removeClient(clientBody: any) {
    return apiClient.post(`api/client/delete/${clientBody.project}`)
  }

  // ===============================================================================
  //                                                                  Client::dfprop
  //                                                                  ==============
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  dfporpBeanList(clientBody: any) {
    return apiClient.post(`api/dfprop/list/${clientBody.projectName}`)
  }

  // -----------------------------------------------------
  //                                       SchemaSyncCheck
  //                                       ---------------
  syncSchema(projectName: string) {
    return apiClient.post(`api/dfprop/schemasync/${projectName}`)
  }

  editSyncSchema(projectName: string, syncSchemaSettingData: any) {
    return apiClient.post(`api/dfprop/schemasync/edit/${projectName}/`, {
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
  schemaPolicy(projectName: string) {
    return apiClient.post(`api/dfprop/schemapolicy/${projectName}`)
  }

  editSchemaPolicy(projectName: string, schemaPolicyData: any) {
    return apiClient.post(`api/dfprop/schemapolicy/edit/${projectName}`, {
      body: {
        wholeMap: schemaPolicyData.wholeMap,
        tableMap: schemaPolicyData.tableMap,
        columnMap: schemaPolicyData.columnMap,
      },
    })
  }

  registerSchemapolicyStatement(projectName: string, schemaPolicyData: any) {
    return apiClient.post(
      `api/dfprop/schemapolicy/statement/register/${projectName}`,
      {
        body: schemaPolicyData,
      }
    )
  }

  getSchemapolicyStatementSubject(mapType: string) {
    return apiClient.post(`api/dfprop/schemapolicy/statement/subject?maptype=${mapType}`)
  }

  deleteSchemapolicyStatement(projectName: string, schemaPolicyData: any) {
    return apiClient.post(
      `api/dfprop/schemapolicy/statement/delete/${projectName}`,
      {
        body: schemaPolicyData,
      }
    )
  }

  moveSchemapolicyStatement(projectName: string, schemaPolicyData: any) {
    return apiClient.post(
      `api/dfprop/schemapolicy/statement/move/${projectName}`, { body: schemaPolicyData }
    )
  }

  // -----------------------------------------------------
  //                                              Document
  //                                              --------
  document(projectName: string) {
    return apiClient.post(`api/dfprop/document/${projectName}`)
  }

  editDocument(projectName: string, documentSetting: any) {
    return apiClient.post(`api/dfprop/document/edit/${projectName}`, {
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
  settings(projectName: string) {
    return apiClient.post(`api/dfprop/settings/${projectName}`)
  }

  updateSettings(clientBody: any) {
    return apiClient.post(`api/dfprop/settings/edit/${clientBody.projectName}`, {
      body: { client: clientBody },
    })
  }

  // ===============================================================================
  //                                                               Client :: playsql
  //                                                               =================
  openAlterDir(projectName: string) {
    return apiClient.get(`api/playsql/migration/alter/open/${projectName}`)
  }

  alter(projectName: string) {
    return apiClient.get(`api/playsql/migration/alter/${projectName}/`)
  }

  prepareAlterSql(projectName: string) {
    return apiClient.post(`api/playsql/migration/alter/prepare/${projectName}/`)
  }

  createAlterSql(projectName: string, alterFileName: string) {
    return apiClient.post(`api/playsql/migration/alter/create/${projectName}/`, {
      body: {
        alterFileName,
      },
    })
  }

  openDataDir(projectName: string) {
    return apiClient.get(`api/playsql/data/open/${projectName}`)
  }

  playsqlBeanList(projectName: string) {
    return apiClient.post(`api/playsql/list/${projectName}`)
  }

  // ===============================================================================
  //                                                                   Client :: log
  //                                                                   =============
  logBeanList(projectName: string) {
    return apiClient.post(`api/log/list/${projectName}`)
  }

  getLog(projectName: string, fileName: string) {
    return apiClient.post('api/log', {
      body: {
        project: projectName,
        fileName: fileName,
      },
    })
  }

  latestResult(projectName: string, task: string) {
    return apiClient.get(`api/log/latest/${projectName}/${task}`)
  }

  // ===============================================================================
  //                                                                          Engine
  //                                                                          ======
  findEngineLatest() {
    return apiClient.post('api/engine/latest')
  }

  engineVersions() {
    return apiClient.post('api/engine/versions')
  }

  // needs trailing slash if URL parameter contains dot
  downloadEngine(params: any) {
    return apiClient.post(`api/engine/download/${params.version}/`)
  }

  removeEngine(params: any) {
    return apiClient.post(`api/engine/remove/${params.version}/`)
  }

  // ===============================================================================
  //                                                                           Task
  //                                                                          ======
  task(projectName: string, task: string) {
    return apiClient.post(`api/task/execute/${projectName}/${task}`)
  }
}

export const api = new Api()

export class DbfluteTask {
  task(task: string, projectName: string, callback: (message: string) => void) {
    return api.task(projectName, task).then((response) => {
      const message = response.status === 200 ? 'success' : 'failure'
      callback(message)
    })
  }
}

