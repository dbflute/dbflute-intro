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
  /**
   * Introのjarファイルに同梱する MANIFEST.MF の内容を取得する。
   * 起動状態の情報として、メイン画面で表示するために。
   * @returns MANIFEST.MFの内容のMapオブジェクト (basically NotEmpty)
   */
  manifest(): Promise<any> {
    // 単なるkey/valueでLasta側もただのMap
    return apiClient.post('api/intro/manifest')
  }

  /**
   * Introのサーバー区分値をすべて取得する。
   * 区分値少ないので全部持ってきてしまっているfor now。
   * @returns 区分値情報まんさいオブジェクト
   */
  findClassifications(): Promise<IntroClassificationsResult> {
    return apiClient.post('api/intro/classifications')
  }

  // #thinking jflute 誰からも呼ばれてない。Riot3版でも使われてないコメント書いてあった。 (2026/02/06)
  // Lasta側のクラスを見ても使われない話があって、gitの履歴を見るとCORS対策？(BootingInternetDomain.java を参照)
  //configuration(): Promise<any> {
  //  return apiClient.post('api/intro/configuration')
  //}

  // ===============================================================================
  //                                                                         Welcome
  //                                                                         =======
  /**
   * Welcomeの気持ちでDBFluteクライアントを作成する。
   * @param body - DBFluteクライアントを作るための入力情報
   * @returns 業務的なレスポンスデータは特になし
   */
  createWelcomeClient(body: WelcomeCreateBody): Promise<void> {
    // Docker起動でクライアント作成時はDBFluteEngineのunzipに1分以上かかる場合があるため、タイムアウト時間に余裕を持たせる
    return apiClient.post('api/welcome/create', body, { timeout: 180000 })
  }

  // ===============================================================================
  //                                                                 Client :: Basic
  //                                                                 ===============
  /**
   * Introが起動している環境にインストールされている、DBFluteクライアントのリストを取得する
   * @returns DBFluteクライアントのリスト
   */
  clientList(): Promise<ClientListResult[]> {
    return apiClient.post('api/client/list')
  }

  /**
   * プロジェクトの基本プロパティを取得する
   * @param projectName DBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns プロジェクトの基本情報 e.g. プロジェクト名、DBMSコード
   */
  clientPropbase(projectName: string): Promise<ClientPropbaseResult> {
    return apiClient.post(`api/client/propbase/${projectName}`)
  }

  /**
   * DBFluteクライアントを作成する。
   * @param body - DBFluteクライアントを作るための入力情報
   * @returns 業務的なレスポンスデータは特になし
   */
  createClient(body: ClientCreateBody): Promise<void> {
    return apiClient.post('api/client/create', body)
  }

  // #for_now jflute DBFluteクライアントの削除は元々UI的に用意されていないので呼ばれてない。 (2026/02/06)
  // 削除はちょっと間違いが怖いから実装してないのかも。(わかる人がファイルシステム上で普通に削除すればいいだけだし)
  //removeClient(clientBody: any): Promise<void> {
  //  return apiClient.post(`api/client/delete/${clientBody.project}`)
  //}

  // ===============================================================================
  //                                                                Client :: dfprop
  //                                                                ================
  // -----------------------------------------------------
  //                                                 Basic
  //                                                 -----
  // #for_now jflute dfpropの一覧を管理/閲覧するような画面を作るまでは出番がないかも (2026/02/06)
  //dfporpBeanList(clientBody: any): Promise<DfpropListResult> {
  //  return apiClient.post(`api/dfprop/list/${clientBody.projectName}`)
  //}

  // -----------------------------------------------------
  //                                       SchemaSyncCheck
  //                                       ---------------
  /**
   * DBFluteクライアントを作成する。
   * @param projectName - DBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @returns 一つのSchemaSyncCheckの設定、主に比較相手スキーマのJDBC接続先
   */
  syncSchema(projectName: string): Promise<DfpropSchemasyncResult> {
    return apiClient.post(`api/dfprop/schemasync/${projectName}`)
  }

  // #hope jflute 引数を DfpropSchemasyncEditBody にして、画面側でstateから詰め替えるようにしたいところ (2026/02/06)
  /**
   * DBFluteクライアントを作成する。
   * @param projectName - DBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param syncSchemaSettingData - SchemaSyncCheckの設定情報オブジェクト
   * @returns 業務的なレスポンスデータは特になし
   */
  editSyncSchema(projectName: string, syncSchemaSettingData: any): Promise<void> {
    return apiClient.post(`api/dfprop/schemasync/edit/${projectName}/`, {
      url: syncSchemaSettingData.url,
      schema: syncSchemaSettingData.schema,
      user: syncSchemaSettingData.user,
      password: syncSchemaSettingData.password,
      isSuppressCraftDiff: syncSchemaSettingData.isSuppressCraftDiff || false, // need not null
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
  // -----------------------------------------------------
  //                                         ReplaceSchema
  //                                         -------------
  /**
   * ReplaceSchema の dataディレクトリをOSのエクスプローラーで開く。(MacならFinder)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns 業務的なレスポンスデータは特になし
   */
  openDataDir(projectName: string): Promise<void> {
    return apiClient.get(`api/playsql/data/open/${projectName}`)
  }

  // #thinking jflute Javaでは、List<PlaysqlBean> で、tsでは Array<PlaysqlListResult> が名前的にうーむー (2025/10/21)
  // 自動生成でrootのBeanの名前も取って使いたいかな？
  /**
   * ReplaceSchema の playsqlディレクトリ配下のファイル情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns playsqlディレクトリのファイル情報のリスト (NotNull, EmptyAllowed)
   */
  playsqlBeanList(projectName: string): Promise<Array<PlaysqlListResult>> {
    return apiClient.post(`api/playsql/list/${projectName}`)
  }

  // -----------------------------------------------------
  //                                             Migration
  //                                             ---------
  /**
   * AlterCheckの画面情報をロードする。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns 画面の表示情報を目一杯に積んだもの (NotNull)
   */
  alter(projectName: string): Promise<PlaysqlMigrationAlterResult> {
    return apiClient.get(`api/playsql/migration/alter/${projectName}/`)
  }

  /**
   * AlterCheck の alterディレクトリをOSのエクスプローラーで開く。(MacならFinder)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns 業務的なレスポンスデータは特になし
   */
  openAlterDir(projectName: string): Promise<void> {
    return apiClient.get(`api/playsql/migration/alter/open/${projectName}`)
  }

  /**
   * AlterCheck の alterディレクトリに、AlterDDLファイルを新規作成する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns 業務的なレスポンスデータは特になし
   */
  prepareAlterSql(projectName: string): Promise<void> {
    return apiClient.post(`api/playsql/migration/alter/prepare/${projectName}/`)
  }

  /**
   * AlterCheck の alterディレクトリに、AlterDDLファイルを新規作成する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   * @returns 業務的なレスポンスデータは特になし
   */
  createAlterSql(projectName: string, alterFileName: string): Promise<void> {
    return apiClient.post(`api/playsql/migration/alter/create/${projectName}/`, {
      alterFileName,
    })
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

  latestResult(projectName: string, task: string): Promise<LogLatestResult | null> {
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
  //                                                                            Task
  //                                                                            ====
  task(projectName: string, task: string): Promise<TaskExecuteResult> {
    return apiClient.post(`api/task/execute/${projectName}/${task}`)
  }
}

export const api = new Api()
