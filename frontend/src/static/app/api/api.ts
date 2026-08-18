import i18n from 'i18next'
import { triggerShowResult } from '../app-events'
import axios, { AxiosError, AxiosRequestConfig } from 'axios'

// ===================================================================================
//                                                                          API Client
//                                                                          ==========
/**
 * IntroサーバーAPIとやり取りするクラス。Axiosを利用。
 *
 * ここでは汎用的なリクエスト関数を装備し、それぞれの業務ごとのリクエスト関数がこれらを呼ぶ。
 */
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
/**
 * APIエラーレスポンスから表示用のメッセージ一覧を抽出する。
 * @param data - APIエラーレスポンスのデータ。
 * @param fallbackMessage - メッセージを抽出できない場合に表示する文言。
 * @returns 表示するエラーメッセージの一覧。
 */
const extractMessages = (data: any, fallbackMessage: string): string[] => {
  if (data?.messages && typeof data.messages === 'object') {
    const values = Object.values(data.messages)
    return values.reduce<string[]>((messageList, value) => {
      return messageList.concat(
        Array.isArray(value) ? value.map(String) : [typeof value === 'object' && value !== null ? JSON.stringify(value) : String(value)],
      )
    }, [])
  }
  if (Array.isArray(data)) return data.map(String)
  if (typeof data === 'string' && data.trim()) return [data]
  if (data && typeof data === 'object') return [JSON.stringify(data)]
  return [fallbackMessage]
}

/**
 * API ClientでAPIエラーを検知した時のコールバック関数。
 * @param error - Axios のエラーオブジェクト。
 */
const handleError = (error: AxiosError) => {
  let header = undefined
  let messages = undefined
  // #thinking improvement: does it need to reload screen when status=0, 401? (implemented until 0.2.x)
  //let reload = false;
  let validationError = false
  const response: any = error.response
  if (!response) {
    triggerShowResult({
      header: 'Network Error',
      messages: ['Cannot access the server, retry later'],
      modalSize: 'large',
    })
    return Promise.reject(error)
  }
  const status = response.status
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
      messages = extractMessages(response.data, 'Bad request')
    }
  } else if (status === 401) {
    header = '401 Not Authorized'
  } else if (status === 403) {
    header = '403 Forbidden'
  } else if (status === 500) {
    header = '500 Server Error'
    messages = extractMessages(response.data, 'Server error occurred')
  } else if (status === 504) {
    header = '504 Gateway Timeout'
    messages = extractMessages(response.data, 'Cannot access the server, retry later')
  } else if (status >= 400 && status <= 499) {
    // Intro想定外のクライアントエラー
    header = 'Unknown Client Error: ' + status
    messages = extractMessages(response.data, 'Unexpected client error occurred')
  } else if (status >= 500 && status <= 599) {
    // Intro想定外のサーバーエラー
    header = 'Unknown Server Error: ' + status
    messages = extractMessages(response.data, 'Cannot access the server, retry later')
  } else {
    // さらなる想定外のエラー (API呼び出しの例外ハンドリングはすべてApiClientで完結させるため)
    header = 'Unknown Error: ' + status
    messages = extractMessages(response.data, 'Unexpected error occurred')
  }
  const modalSize = validationError ? 'small' : 'large'
  triggerShowResult({ header, messages: messages || [], modalSize })
  return Promise.reject(error) // 画面固有の処理も付け足せるように、rejectで例外を継続
}

// IntroサーバーAPIのインスタンス準備
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
  findManifest(): Promise<any> {
    // 単なるkey/valueでLasta側もただのMap
    return apiClient.post('api/intro/manifest')
  }

  /**
   * Introのサーバー区分値をすべて取得する。
   * 区分値少ないので全部持ってきてしまっているfor now。
   * @returns 区分値情報まんさい (自動生成クラス)
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
   * @param body - DBFluteクライアントを作るための入力情報 (自動生成クラス)
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
   * @returns DBFluteクライアント情報のリスト (自動生成クラス)
   */
  findClientList(): Promise<ClientListResult[]> {
    return apiClient.post('api/client/list')
  }

  /**
   * プロジェクトの基本プロパティを取得する
   * @param projectName DBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns プロジェクトの基本情報 e.g. プロジェクト名、DBMSコード (自動生成クラス)
   */
  findClientPropbase(projectName: string): Promise<ClientPropbaseResult> {
    return apiClient.post(`api/client/propbase/${projectName}`)
  }

  /**
   * DBFluteクライアントを作成する。
   * @param body - DBFluteクライアントを作るための入力情報 (自動生成クラス)
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

  // ===============================================================================
  //                                                       Client :: SchemaSyncCheck
  //                                                       =========================
  /**
   * SchemaSyncCheckのdfprop設定情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @returns 一つのSchemaSyncCheckの設定情報、主に比較相手スキーマのJDBC接続先 (自動生成クラス)
   */
  findSchemaSyncDfprop(projectName: string): Promise<DfpropSchemasyncResult> {
    return apiClient.post(`api/dfprop/schemasync/${projectName}`)
  }

  // #hope jflute 引数を DfpropSchemasyncEditBody にして、画面側でstateから詰め替えるようにしたいところ (2026/02/06)
  /**
   * SchemaSyncCheckのdfprop設定情報を編集する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param schemasyncResult - SchemaSyncCheckの設定情報 (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  editSyncSchemaDfprop(projectName: string, schemasyncResult: DfpropSchemasyncResult): Promise<void> {
    return apiClient.post(`api/dfprop/schemasync/edit/${projectName}/`, {
      url: schemasyncResult.url,
      schema: schemasyncResult.schema,
      user: schemasyncResult.user,
      password: schemasyncResult.password,
      isSuppressCraftDiff: schemasyncResult.isSuppressCraftDiff || false, // need not null
    })
  }

  // ===============================================================================
  //                                                     Client :: SchemaPolicyCheck
  //                                                     ===========================
  /**
   * スキーマポリシーの設定情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @returns スキーマポリシーの設定情報、wholeからcolumnまで、themeやstatementなどまるごと (自動生成クラス)
   */
  schemaPolicy(projectName: string): Promise<DfpropSchemapolicyResult> {
    return apiClient.post(`api/dfprop/schemapolicy/${projectName}`)
  }

  // #thinking jflute なんかthemeだけの修正だったりする？DfpropSchemaPolicyEditBody を見るとそう。 (2026/02/13)
  // statementは別途あるしね。であれば、URLも関数名もそれがわかるような名前にしたいかも。
  /**
   * スキーマポリシーの設定を編集する。(Themeのみ)
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param themeEditBody - 編集したtheme情報、wholeからcolumnまで全てのtheme (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  editSchemaPolicy(projectName: string, themeEditBody: DfpropSchemapolicyEditBody): Promise<void> {
    return apiClient.post(`api/dfprop/schemapolicy/edit/${projectName}`, {
      wholeMap: themeEditBody.wholeMap,
      tableMap: themeEditBody.tableMap,
      columnMap: themeEditBody.columnMap,
    })
  }

  /**
   * スキーマポリシーの一つのstatement設定を登録する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param statementRegisterBody - 登録予定の one statement の構成要素を保持する (自動生成クラス)
   * @returns 個々の構成要素が連結されてdfprop上での表現になった文字列 e.g. if alias is $$tableName$$ then bad
   */
  registerSchemapolicyStatement(projectName: string, statementRegisterBody: DfpropSchemapolicyStatementRegisterBody): Promise<string> {
    return apiClient.post(`api/dfprop/schemapolicy/statement/register/${projectName}`, statementRegisterBody)
  }

  // #thinking jflute 戻り値が List<String> だから自動生成クラスなし!? Array<string> で良い？ (2026/02/13)
  /**
   * スキーマポリシーのsubject候補の一覧を取得する。
   * (dfpropの文法情報なのでプロジェクト名は不要)
   * @param mapType - テーブルか？カラムか？
   * @returns 個々の構成要素が連結されてdfprop上での表現になった文字列 e.g. if alias is $$tableName$$ then bad
   */
  getSchemapolicyStatementSubject(mapType: string): Promise<Array<string>> {
    // #thiking jflute POST で queryパラメーター私は避けたい (2026/02/13)
    // DfpropSchemapolicyStatementSubjectBody が自動生成されてるので、それを使ってできない？
    return apiClient.post(`api/dfprop/schemapolicy/statement/subject?maptype=${mapType}`)
  }

  /**
   * スキーマポリシーの一つのstatementを削除する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param statementDeleteBody - 削除予定の one statement を特定する情報 (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  deleteSchemapolicyStatement(projectName: string, statementDeleteBody: DfpropSchemapolicyStatementDeleteBody): Promise<void> {
    return apiClient.post(`api/dfprop/schemapolicy/statement/delete/${projectName}`, statementDeleteBody)
  }

  /**
   * スキーマポリシーの一つのstatementの定義位置を移動する。
   * @param projectName - 現在対象としているDBFluteクライアントをプロジェクト名 e.g. maihamadb
   * @param statementMoveBody - one statement のどこからどこへ情報 (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  moveSchemapolicyStatement(projectName: string, statementMoveBody: DfpropSchemapolicyStatementMoveBody) {
    return apiClient.post(`api/dfprop/schemapolicy/statement/move/${projectName}`, statementMoveBody)
  }

  // ===============================================================================
  //                                                              Client :: Document
  //                                                              ==================
  /**
   * ドキュメントに関するdfprop情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns ドキュメントに関するdfprop情報、aliasDelimiterInDbComment など (自動生成クラス)
   */
  findDocumentDfprop(projectName: string): Promise<DfpropDocumentResult> {
    return apiClient.post(`api/dfprop/document/${projectName}`)
  }

  /**
   * ドキュメントに関するdfprop情報を編集する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @param documentResult - ドキュメント設定の編集情報 (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  editDocument(projectName: string, documentResult: DfpropDocumentResult): Promise<void> {
    // #for_now jflute DfpropDocumentResult を受け取ってるけど、DfpropDocumentEditBody で受け取りたい (2026/02/15)
    // #thinking jflute documentEditBody をそのまま第二引数にbodyとして入れるでもいいんじゃないのかな？ (2026/02/14)
    return apiClient.post(`api/dfprop/document/edit/${projectName}`, {
      upperCaseBasic: documentResult.upperCaseBasic,
      aliasDelimiterInDbComment: documentResult.aliasDelimiterInDbComment,
      dbCommentOnAliasBasis: documentResult.dbCommentOnAliasBasis,
      checkColumnDefOrderDiff: documentResult.checkColumnDefOrderDiff,
      checkDbCommentDiff: documentResult.checkDbCommentDiff,
      checkProcedureDiff: documentResult.checkProcedureDiff,
    })
  }

  // ===============================================================================
  //                                                      Client :: General Settings
  //                                                      ==========================
  /**
   * DBFluteクライアントのコアなdfprop情報を取得する。(DBMSやDB接続情報など)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns 基本的なdfprop情報 (自動生成クラス)
   */
  findCoreDfprop(projectName: string): Promise<DfpropSettingsResult> {
    return apiClient.post(`api/dfprop/settings/${projectName}`)
  }

  /**
   * DBFluteクライアントのコアなdfprop情報を更新する。(DBMSやDB接続情報など)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @param settingsEditBody - 基本的なdfprop情報の編集情報 (自動生成クラス)
   * @returns 業務的なレスポンスデータは特になし
   */
  editCoreDfprop(projectName: string, settingsEditBody: DfpropSettingsEditBody): Promise<void> {
    return apiClient.post(`api/dfprop/settings/edit/${projectName}`, settingsEditBody)
  }

  // ===============================================================================
  //                                                         Client :: ReplaceSchema
  //                                                         =======================
  /**
   * ReplaceSchema の dataディレクトリをOSのエクスプローラーで開く。(MacならFinder)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns 業務的なレスポンスデータは特になし
   */
  openDataDir(projectName: string): Promise<void> {
    return apiClient.get(`api/playsql/data/open/${projectName}`)
  }

  // #thinking jflute Javaでは、List<PlaysqlBean> で、tsでは Array<PlaysqlListResult> が名前的にうーむー (2025/10/21)
  // 自動生成でrootのBeanの名前も取って使いたいかな？
  /**
   * ReplaceSchema の playsqlディレクトリ配下のファイル情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns playsqlディレクトリのファイル情報のリスト (自動生成クラス) (EmptyAllowed)
   */
  findPlaysqlFileList(projectName: string): Promise<Array<PlaysqlListResult>> {
    return apiClient.post(`api/playsql/list/${projectName}`)
  }

  // ===============================================================================
  //                                                            Client :: AlterCheck
  //                                                            ====================
  /**
   * AlterCheckのインフラ情報(alterのファイルなど)をロードする。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns 画面の表示情報を目一杯に積んだもの (自動生成クラス)
   */
  findAlterInfra(projectName: string): Promise<PlaysqlMigrationAlterResult> {
    return apiClient.get(`api/playsql/migration/alter/${projectName}/`)
  }

  /**
   * AlterCheck の alterディレクトリをOSのエクスプローラーで開く。(MacならFinder)
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns 業務的なレスポンスデータは特になし
   */
  openAlterDir(projectName: string): Promise<void> {
    return apiClient.get(`api/playsql/migration/alter/open/${projectName}`)
  }

  /**
   * AlterCheck の alterディレクトリに、AlterDDLファイルを新規作成する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns 業務的なレスポンスデータは特になし
   */
  prepareAlterSql(projectName: string): Promise<void> {
    return apiClient.post(`api/playsql/migration/alter/prepare/${projectName}/`)
  }

  /**
   * AlterCheck の alterディレクトリに、AlterDDLファイルを新規作成する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
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
  // #thinking jflute Arrayだったり[]だったりブレてるのどうにかしたい (2026/02/15)
  /**
   * DBFluteクライアントのログファイルの一覧を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @returns ログファイル情報の一覧 (自動生成クラス)
   */
  findLogFileList(projectName: string): Promise<LogListResult[]> {
    return apiClient.post(`api/log/list/${projectName}`)
  }

  // #thinking jflute そもそもサーバー側でプロジェクト名も取り方がBodyになってて統一感がない (2026/02/15)
  // #thinking jflute 使われてないっぽい？とりあえずコメントアウトで、riot7リリースの動作確認でOKなら削除 (2026/05/08)
  ///**
  // * 特定のログファイルの情報を取得する。
  // * @param logBody - 一つのログファイルを特定するもの (自動生成クラス)
  // * @returns 一つのログファイル情報 (自動生成クラス)
  // */
  //getLog(logBody: LogBody): Promise<LogResult> {
  //  return apiClient.post('api/log', {
  //    body: logBody,
  //  })
  //}

  /**
   * 直近のDBFluteタスク実行のログファイルの情報を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @param task - DBFluteタスクを特定する名前、TaskInstruction のコード e.g. alterCheck
   * @returns 該当のログファイル情報 (自動生成クラス)
   */
  async findLatestTaskLog(projectName: string, task: string): Promise<LogLatestResult | null> {
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
   * 最新のDBFluteエンジンバージョンを取得する。
   * @returns 最新のDBFluteエンジンバージョン情報 (自動生成クラス)
   */
  findEngineLatestVersion(): Promise<EngineLatestResult> {
    return apiClient.post('api/engine/latest')
  }

  /**
   * DBFluteエンジンの一覧を取得する。
   * @returns DBFluteエンジンのバージョン番号のリスト e.g. [ "1.2.6" ]
   */
  findExistingEngineVersions(): Promise<string[]> {
    return apiClient.post('api/engine/versions')
  }

  // #for_now jflute paramsオブジェクトじゃなくて、versionを直接引数もらっても良いような？ (2026/02/15)
  /**
   * DBFluteエンジンをダウンロードする。
   * @param params バージョン情報を積んだオブジェクト e.g. "1.2.6"
   * @returns 業務的なレスポンスデータは特になし
   */
  downloadEngine(params: any): Promise<void> {
    // needs trailing slash if URL parameter contains dot
    return apiClient.post(`api/engine/download/${params.version}/`)
  }

  // #for_now jflute 同じく、paramsオブジェクトじゃなくて、versionを直接引数もらっても良いような？ (2026/02/15)
  /**
   * DBFluteエンジンを削除する。
   * @param params バージョン情報を積んだオブジェクト e.g. "1.2.6"
   * @returns 業務的なレスポンスデータは特になし
   */
  removeEngine(params: any): Promise<void> {
    // needs trailing slash if URL parameter contains dot
    return apiClient.post(`api/engine/remove/${params.version}/`)
  }

  // ===============================================================================
  //                                                                           Task
  //                                                                          ======
  /**
   * 指定されたDBFluteタスクを実行する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb
   * @param task - DBFluteタスクを特定する名前、TaskInstruction のコード e.g. alterCheck
   * @returns DBFluteタスクの実行結果 (自動生成クラス)
   */
  executeTask(projectName: string, task: string): Promise<TaskExecuteResult> {
    return apiClient.post(`api/task/execute/${projectName}/${task}`)
  }
}

// 業務ごとのサーバーAPIをコールできるオブジェクトを公開
export const api = new Api()
