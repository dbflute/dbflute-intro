import i18n from 'i18next'
import { triggerShowResult } from '../app-events'
import axios, { AxiosError, AxiosRequestConfig } from 'axios'

// ===================================================================================
//                                                                          API Client
//                                                                          ==========
class ApiClient {
  get(url: string, config?: AxiosRequestConfig) {
    return axios
      .get(url, config)
      .then((res) => res.data)
      .catch(handleError)
  }

  post(url: string, data?: any, config?: AxiosRequestConfig) {
    return axios
      .post(url, data, config)
      .then((res) => res.data)
      .catch(handleError)
  }

  put(url: string, data?: any, config?: AxiosRequestConfig) {
    return axios
      .put(url, data, config)
      .then((res) => res.data)
      .catch(handleError)
  }

  del(url: string, config?: AxiosRequestConfig) {
    return axios
      .delete(url, config)
      .then((res) => res.data)
      .catch(handleError)
  }
}

// ===================================================================================
//                                                                      Error Handling
//                                                                      ==============
// see IntroApiFailureHook.java for failure response
const handleError = (error: AxiosError) => {
  let header = undefined
  let messages = undefined
  // #thinking improvement: does it need to reload screen when status=0, 401? (implemented until 0.2.x)
  //let reload = false;
  let validationError = false
  const response: any = error.response
  const status = response.status
  if (status === 0) {
    messages = ['Cannot access the server, retry later']
  }
  // #hope refactor: extract to method
  if (status === 400) {
    header = '400 Bad Request'
    // #hope improvement: formal validation error handling
    if (response.data.failureType) {
      // basically here (unified JSON if 400)
      header = header + ': ' + response.data.failureType
      validationError = response.data.failureType === 'VALIDATION_ERROR'
    }
    if (response.data.messages) {
      // basically here (unified JSON if 400)
      const messageList = []
      for (let key in response.data.messages) {
        for (let i in response.data.messages[key]) {
          const message = response.data.messages[key][i]
          if (key.match(/List/)) {
            if (key.match(/[0-9]/)) {
              let newKey = ''
              const splitList = key.split(/[0-9]/)
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
          if (key === '_global') {
            // don't use key if global
            messageList.push(message + '\r\n')
          } else {
            const label = i18n.t(`LABEL_${key}`)
            const symbol = label === '' ? '' : '：'
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
  /**
   * Welcomeの気持ちでDBFluteクライアントを作成する。
   * @param body - DBFluteクライアントを作るための入力情報 (NotNull)
   * @returns 業務的なレスポンスデータは特になし (NotNull)
   */
  createWelcomeClient(body: WelcomeCreateBody): Promise<void> {
    // Docker起動でクライアント作成時はDBFluteEngineのunzipに1分以上かかる場合があるため、タイムアウト時間に余裕を持たせる
    return apiClient.post('api/welcome/create', body, { timeout: 180000 })
  }

  // ===============================================================================
  //                                                                          Client
  //                                                                          ======
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  /**
   * Introが起動している環境にインストールされている、DBFluteクライアントのリストを取得する
   * @returns DBFluteクライアントのリスト (NotNull)
   */
  clientList(): Promise<ClientListResult[]> {
    return apiClient.post('api/client/list')
  }

  clientPropbase(projectName: string) {
    return apiClient.post(`api/client/propbase/${projectName}`)
  }

  /**
   * DBFluteクライアントを作成する。
   * @param {ClientCreateBody} body - DBFluteクライアントを作るための入力情報 (NotNull)
   * @returns {Promise<void>} レスポンスは特になし (NotNull)
   */
  createClient(body: ClientCreateBody) {
    return apiClient.post('api/client/create', body)
  }

  removeClient(clientBody: any) {
    return apiClient.post(`api/client/delete/${clientBody.project}`)
  }

  // ===============================================================================
  //                                                                Client :: dfprop
  //                                                                ================
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  dfporpBeanList(clientBody: any) {
    return apiClient.post(`api/dfprop/list/${clientBody.projectName}`)
  }

  // -----------------------------------------------------
  //                                       SchemaSyncCheck
  //                                       ---------------
  syncSchema(projectName: string): Promise<DfpropSchemasyncResult> {
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
    return apiClient.post(`api/dfprop/schemapolicy/statement/register/${projectName}`, {
      body: schemaPolicyData,
    })
  }

  getSchemapolicyStatementSubject(mapType: string) {
    return apiClient.post(`api/dfprop/schemapolicy/statement/subject?maptype=${mapType}`)
  }

  deleteSchemapolicyStatement(projectName: string, schemaPolicyData: any) {
    return apiClient.post(`api/dfprop/schemapolicy/statement/delete/${projectName}`, {
      body: schemaPolicyData,
    })
  }

  moveSchemapolicyStatement(projectName: string, schemaPolicyData: any) {
    return apiClient.post(`api/dfprop/schemapolicy/statement/move/${projectName}`, { body: schemaPolicyData })
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

  alter(projectName: string): Promise<AlterSQLResult> {
    return apiClient.get(`api/playsql/migration/alter/${projectName}/`)
  }

  prepareAlterSql(projectName: string) {
    return apiClient.post(`api/playsql/migration/alter/prepare/${projectName}/`)
  }

  createAlterSql(projectName: string, alterFileName: string): Promise<void> {
    return apiClient.post(`api/playsql/migration/alter/create/${projectName}/`, {
      alterFileName,
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
  logBeanList(projectName: string): Promise<LogListResult[]> {
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

  latestResult(projectName: string, task: string): Promise<LogBean | null> {
    return apiClient.get(`api/log/latest/${projectName}/${task}`).then((body) => {
      // TODO cabos レスポンスの形式が変わる実装になっているので、変わらないように修正する (2023-01-07 at Roppongi)
      // https://github.com/dbflute/dbflute-intro/issues/493
      if (Object.keys(body).length === 0) return null
      return body
    })
  }

  // ===============================================================================
  //                                                                          Engine
  //                                                                          ======
  /**
   * 最新のDBFluteエンジンバージョンを取得する
   * @returns 最新のDBFluteエンジンバージョン (NotNull)
   */
  findEngineLatest(): Promise<EngineLatestResult> {
    return apiClient.post('api/engine/latest')
  }

  /**
   * DBFluteエンジンの一覧を取得する
   * @returns {Promise<string[]>} DBFluteエンジンのバージョン番号のリスト e.g. [ "1.2.6" ] (NotNull)
   */
  engineVersions(): Promise<string[]> {
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
  task(projectName: string, task: string): Promise<TaskExecutionResult> {
    return apiClient.post(`api/task/execute/${projectName}/${task}`)
  }
}

export const api = new Api()
