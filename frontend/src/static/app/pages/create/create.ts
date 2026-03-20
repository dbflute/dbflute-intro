import i18n from '../../components/common/i18n.riot'
import { api } from '../../api/api'
import { appRoutes } from '../../app-router'
import { readFile } from '../../shared/io-utils'
import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import { DropdownItem } from '../../components/dropdown/dropdown'

/**
 * デフォルトで表示するドロップダウンの項目 (要は未選択)
 */
const defaultDropDownItem = {
  value: '',
  label: 'not selected',
  default: true,
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface State {
  /** DBFluteが利用するJDBCドライバーに関する情報、主にはjarファイル */
  jdbcDriver: { fileName: string; data: string } | undefined

  /**
   * JDBCドライバーのアップロードが必要なDBMSかどうか？サーバー側のDBMS定義より設定される
   * (例えば、MySQLだとDBFlute Engineに組み込まれているので false となる)
   */
  needsJdbcDriver: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Create extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
  /** ドロップダウンにてデフォルトで選択されるデータベースの区分値コード (EmptyAllowed: no selected) */
  defaultDatabaseCode: string

  /** デフォルトで表示されるJDBCドライバーFQCN (EmptyAllowed: no use jar) */
  defaultJdbcDriver: string

  /** デフォルトで表示されるJDBC接続URL (EmptyAllowed: no default) */
  defaultLanguageCode: string

  /** ドロップダウンにてデフォルトで選択されるプログラミング言語の区分値コード (EmptyAllowed: no selected) */
  defaultContainerCode: string

  // -----------------------------------------------------
  //                                 Initialized Reference
  //                                 ---------------------
  /** ドロップダウンにてデフォルトで選択されるDIコンテナーの区分値コード (EmptyAllowed: no selected) */
  databaseMap: Map<string, IntroClassificationsResult_DatabaseDefPart>

  /**
   * DBFluteエンジンの最新バージョン、インターネットで公開されている最新バージョン e.g. 1.2.5
   * (undefined: if onMounted()で初期化失敗の場合など)
   */
  engineVersions: DropdownItem[]

  /** DBFluteクライアントで定義する対象データベースのドロップダウン項目たち (NotEmpty) */
  targetDatabaseItems: DropdownItem[]

  /** DBFluteクライアントで定義する対象プログラミング言語のドロップダウン項目たち (NotEmpty) */
  targetLanguageItems: DropdownItem[]

  /** DBFluteクライアントで定義する対象DIコンテナーのドロップダウン項目たち (NotEmpty) */
  targetContainerItems: DropdownItem[]

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted: () => void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する。
   * @param targetDatabase - 選択されたDBMSの区分値コード
   */
  onchangeDatabase: (databaseCode: DropdownItem) => void

  /**
   * DBFluteクライアントを作成する。(作成ボタンの処理)
   */
  onclickCreate: () => void

  /**
   * JDBCドライバーのファイルが指定されたときの処理。
   * @param event - この関数を呼び出したイベントのオブジェクト
   */
  onchangeJarFile: (event: InputEvent) => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * 区分値情報をUI用のデータに整形する。
   * @param classifications - APIで取得した区分値情報
   */
  convertClassificationsForUI: (classifications: IntroClassificationsResult) => {
    /** DBMSごとの情報、区分値のコードがキー値 (NotEmpty) */
    databaseMap: Map<string, IntroClassificationsResult_DatabaseDefPart>

    /** DBFluteクライアントで定義する対象データベースのドロップダウン項目たち (NotEmpty) */
    targetContainerItems: DropdownItem[]

    /** DBFluteクライアントで定義する対象プログラミング言語のドロップダウン項目たち (NotEmpty) */
    targetLanguageItems: DropdownItem[]

    /** DBFluteクライアントで定義する対象DIコンテナーのドロップダウン項目たち (NotEmpty) */
    targetDatabaseItems: DropdownItem[]
  }

  /**
   * DBFluteクライアントを作成したことを知らせるパンじゃなくてトースト。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名。
   */
  showToast: (projectName: string) => void
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<Create>({
  components: {
    i18n,
  },
  state: {
    jdbcDriver: undefined, // JDBCドライバーの設定が必要なので一部DBMSなのでデフォルト指定なし
    needsJdbcDriver: false, // とりあえずデフォルトはDBMS未選択想定でfalse
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
  defaultDatabaseCode: '',
  defaultJdbcDriver: '',
  defaultLanguageCode: '',
  defaultContainerCode: '',

  // -----------------------------------------------------
  //                                 Initialized Reference
  //                                 ---------------------
  databaseMap: new Map(), // e.g. targetDatabase
  engineVersions: [],
  targetDatabaseItems: [],
  targetLanguageItems: [],
  targetContainerItems: [],

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    const classifications = await api.findClassifications().then((data) => this.convertClassificationsForUI(data))
    this.databaseMap = classifications.databaseMap
    this.targetDatabaseItems = classifications.targetDatabaseItems
    this.targetLanguageItems = classifications.targetLanguageItems
    this.targetContainerItems = classifications.targetContainerItems
    this.engineVersions = await api
      .engineVersions()
      .then((data) => data.map((version) => ({ label: version, value: version, default: false })))
    this.update()
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onchangeDatabase(targetDatabase: DropdownItem) {
    // Dropdownでselectedを選択した場合はデフォルト値に戻す
    if (targetDatabase.default) {
      this.inputElementBy('[ref=jdbcDriverFqcn]').value = ''
      this.inputElementBy('[ref=url]').value = ''
      this.inputElementBy('[ref=schema]').value = ''
      this.update({
        needsJdbcDriver: false,
        jdbcDriver: undefined,
      })
    } else {
      const database = this.databaseMap.get(targetDatabase.value)
      if (!database) {
        throw new Error('not found the database code: ' + targetDatabase.value)
      }
      this.inputElementBy('[ref=jdbcDriverFqcn]').value = database.driverName
      this.inputElementBy('[ref=url]').value = database.urlTemplate
      if (database.defaultSchema) {
        this.inputElementBy('[ref=schema]').value = database.defaultSchema
      }
      this.update({
        // switch showing JDBCDriver select form
        needsJdbcDriver: !database.embeddedJar,
        // initialize JDBC Driver
        jdbcDriver: undefined,
      })
    }
  },

  onclickCreate() {
    const body: ClientCreateBody = {
      client: {
        projectName: this.inputElementBy('[ref=projectName]').value,
        databaseCode: this.valueAttributeBy('[ref=databaseCode]'),
        languageCode: this.valueAttributeBy('[ref=languageCode]'),
        containerCode: this.valueAttributeBy('[ref=containerCode]'),
        packageBase: this.inputElementBy('[ref=packageBase]').value,
        jdbcDriverFqcn: this.inputElementBy('[ref=jdbcDriverFqcn]').value,
        dbfluteVersion: this.valueAttributeBy('[ref=dbfluteVersion]'),
        jdbcDriver: this.state.jdbcDriver,
        mainSchemaSettings: {
          url: this.inputElementBy('[ref=url]').value,
          schema: this.inputElementBy('[ref=schema]').value,
          user: this.inputElementBy('[ref=user]').value,
          password: this.inputElementBy('[ref=password]').value,
        },
      },
      testConnection: this.inputElementBy('[ref=testConnection]').checked,
    }

    api.createClient(body).then(() => {
      appRoutes.main.open()
      this.showToast(body.client.projectName)
    })
  },

  onchangeJarFile(event: InputEvent) {
    const eventTarget = event.target as HTMLInputElement // event.targetはイベント発生元のオブジェクト
    // TypeScriptのstrictモードを適用したのでnullチェックが必須になった
    if (!eventTarget.files) {
      throw new Error('not found any files')
    }
    const file = eventTarget.files[0]
    readFile(file).then((result) => {
      // base64にencodeする: https://developer.mozilla.org/ja/docs/Web/API/btoa
      const encoded = window.btoa(result)
      this.state.jdbcDriver = { fileName: file.name, data: encoded }
    })
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  convertClassificationsForUI(classifications: IntroClassificationsResult) {
    const dbMap = new Map(classifications.targetDatabaseList.map((obj) => [obj.databaseCode, obj]))
    const langMap = new Map(classifications.targetLanguageList.map((obj) => [obj.languageCode, obj]))
    const contMap = new Map(classifications.targetContainerList.map((obj) => [obj.containerCode, obj]))

    return {
      databaseMap: dbMap,
      targetDatabaseItems: [
        ...Object.entries(Object.fromEntries(dbMap)).map(([key, value]) => {
          return { value: key, label: value.databaseName, default: false }
        }),
        defaultDropDownItem,
      ],
      targetLanguageItems: [
        ...Object.entries(Object.fromEntries(langMap)).map(([key, value]) => {
          return { value: key, label: value.languageName, default: false }
        }),
        defaultDropDownItem,
      ],
      targetContainerItems: [
        ...Object.entries(Object.fromEntries(contMap)).map(([key, value]) => {
          return { value: key, label: value.containerName, default: false }
        }),
        defaultDropDownItem,
      ],
    }
  },

  showToast(projectName: string) {
    this.successToast({
      title: 'Create task completed',
      message: "Client for project '" + projectName + "', was successfully created!!",
    })
  },
})
