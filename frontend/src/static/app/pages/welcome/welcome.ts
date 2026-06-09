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
  // JDBCドライバーのjarファイル情報を格納するオブジェクト
  // #thinking jflute こういう構造 { fileName: null, data: null } って書きたいけどスクリプト言語のお作法に反する？ (2022/03/17)
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/ プルリクにて:
  // [雑談]
  // 多分 java の書き方に合わせただけなんじゃないですかね？
  // 別にコメントのように書いても問題ないと思います
  //
  // [雑談]
  // this.jdbcDriver という変数の使われ方的にファイルがある/ないを示したい気もするので、型をつけるとしたら Optional<{ fileName: String, data: String }> ってみたいな感じになって、案外適切な表現なのかもなって気もしました 笑
  // （TypeScript的な型をつけるなら { fileName: String, data: String } | null という感じ）
  //
  // なるほど。このへんはriot6のときに方向性を統一したいね。
  // _/_/_/_/_/_/_/_/_/_/
  //
  // コメントで書いても定義先で変わったときに追従できない。(ついつい型定義したくなってしまうな...)
  // というか、この宣言要るのか？なければないでundefinedとかで落ちちゃうのかな？？？
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/ プルリクにて:
  // 使われ方的にはnullによる初期化はなくてもいいですね
  // 話がそれますが、TypeScriptの界隈ではnullとundefinedのどちらを使うのか、使い分けるべきかみたいなところはいろいろ意見が分かれてるみたいです。
  // _/_/_/_/_/_/_/_/_/_/
  //
  /** DBFluteが利用するJDBCドライバーに関する情報、主にはjarファイル */
  jdbcDriver: { fileName: string; data: string } | undefined

  /**
   * JDBCドライバーのアップロードが必要なDBMSかどうか？サーバー側のDBMS定義より設定される
   * (例えば、MySQLだとDBFlute Engineに組み込まれているので false となる)
   */
  needsJdbcDriver: boolean

  /** O/Rマッパー関連設定の表示/非表示 */
  oRMapperOptionsFlg: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Welcome extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
  // #thinking jflute databaseMap, 自動生成typeの都合でObjectからMapに変えたが、Objectで実現できる？ (2025/11/04)
  // #thinking jflute Definition というタグコメント、もっと良い名前があったら修正したい (2025/12/16)
  // #thinking jflute default...は完全な定数なので、違う形で表現してもいいかも (2025/12/16)
  // _/_/_/_/_/_/_/_/
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
  /** ドロップダウンにてデフォルトで選択されるデータベースの区分値コード (EmptyAllowed: no selected) */
  defaultDatabaseCode: string

  /** デフォルトで表示されるJDBCドライバーFQCN (EmptyAllowed: no use jar) */
  defaultJdbcDriver: string

  /** デフォルトで表示されるJDBC接続URL (EmptyAllowed: no default) */
  defaultJdbcUrl: string

  /** ドロップダウンにてデフォルトで選択されるプログラミング言語の区分値コード (EmptyAllowed: no selected) */
  defaultLanguageCode: string

  /** ドロップダウンにてデフォルトで選択されるDIコンテナーの区分値コード (EmptyAllowed: no selected) */
  defaultContainerCode: string

  // -----------------------------------------------------
  //                                 Initialized Reference
  //                                 ---------------------
  /** DBMSごとの情報、区分値のコードがキー値 */
  databaseMap: Map<string, IntroClassificationsResult_DatabaseDefPart>

  /**
   * DBFluteエンジンの最新バージョン、インターネットで公開されている最新バージョン e.g. 1.2.5
   * (undefined: if onMounted()で初期化失敗の場合など)
   */
  latestVersion: string | undefined

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
  onMounted(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する。
   * @param targetDatabase - 選択されたDBMSの区分値コード
   */
  onchangeDatabase: (targetDatabase: DropdownItem) => void

  /**
   * O/Rマッパー設定の表示/非表示をトグルする。
   */
  onclickOrmSetting: () => void

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
    targetDatabaseItems: DropdownItem[]

    /** DBFluteクライアントで定義する対象プログラミング言語のドロップダウン項目たち (NotEmpty) */
    targetLanguageItems: DropdownItem[]

    /** DBFluteクライアントで定義する対象DIコンテナーのドロップダウン項目たち (NotEmpty) */
    targetContainerItems: DropdownItem[]
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

export default withIntroTypes<Welcome>({
  components: {
    i18n,
  },
  state: {
    jdbcDriver: undefined, // JDBCドライバーの設定が必要なので一部DBMSなのでデフォルト指定なし
    needsJdbcDriver: false, // とりあえずデフォルトはDBMS未選択想定でfalse
    oRMapperOptionsFlg: false, // Introのコンセプト的にO/Rマッパーオプションはデフォルト非表示
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  // #thiking jflute 本当はこういうのもサーバー側のロジックで決めたいかも？ (2022/03/17)
  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/ プルリクにて
  // [雑談] 個人的には、サーバサイドの区分値と、ここの文字列が常に一致していることを担保できるかどうかが気になります
  //
  // とある現場では、TypeScript用の区分値CDefクラスを自動生成して、フロントとサーバーで同期してる。
  // IntroはTypeScriptじゃないけど、実験的にそういうのやってもいいかも（＾＾
  //
  // #thiking jflute ↑もうTypeScriptなので、いずれTypeScriptのCDefを自動生成したい (2025/12/16)
  // _/_/_/_/_/_/_/_/_/_/
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
  defaultDatabaseCode: '',
  defaultJdbcDriver: '',
  defaultJdbcUrl: '',
  defaultLanguageCode: 'java',
  defaultContainerCode: 'lasta_di',

  // -----------------------------------------------------
  //                                 Initialized Reference
  //                                 ---------------------
  databaseMap: new Map(), // e.g. targetDatabase
  latestVersion: undefined,
  targetDatabaseItems: [],
  targetLanguageItems: [],
  targetContainerItems: [],

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    const classifications = await api.findClassifications().then((data) => this.convertClassificationsForUI(data))
    const latestVersion = await api.findEngineLatestVersion().then((data) => data.latestReleaseVersion)
    this.databaseMap = classifications.databaseMap
    this.targetDatabaseItems = classifications.targetDatabaseItems
    this.targetLanguageItems = classifications.targetLanguageItems
    this.targetContainerItems = classifications.targetContainerItems
    this.latestVersion = latestVersion
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
      // done jflute self.targetDatabaseItems を使えばいいんじゃないか？と思ったんだけど... (2022/03/13)
      // それはあくまでリストボックス用だから、全部入りのclassificationMapの方から取ってるのかな!?
      // それはそれでいいんだけど、サーバー側のキー値に依存するコードを散らばせたくない気はする。
      // プルリクより: データベース名だけ持った配列に変換してて用途を満たしてないからかもですね
      // そっか、こっちは embeddedJar も使うかありがとう。
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

  onclickOrmSetting() {
    this.update({
      oRMapperOptionsFlg: !this.state.oRMapperOptionsFlg,
    })
  },

  onclickCreate() {
    // TypeScriptのstrictモードを適用したのでnullチェックが必須になった
    if (!this.latestVersion) {
      throw new Error('not complete fetch latest version')
    }
    const body: WelcomeCreateBody = {
      // #thinking inputElement用意してるくらいなら、リストボックスもthis.$()の部分使わなくていいようにしてもいい？ jflute (2023/12/25)
      // (というか、そもそもHTMLElementとして取得したらダメなのか？)
      client: {
        projectName: this.inputElementBy('[ref=projectName]').value,
        databaseCode: this.valueAttributeBy('[ref=databaseCode]'),
        mainSchemaSettings: {
          user: this.inputElementBy('[ref=user]').value,
          url: this.inputElementBy('[ref=url]').value,
          schema: this.inputElementBy('[ref=schema]').value,
          password: this.inputElementBy('[ref=password]').value,
        },
        dbfluteVersion: this.latestVersion,
        packageBase: this.inputElementBy('[ref=packageBase]').value,
        containerCode: this.valueAttributeBy('[ref=containerCode]'),
        languageCode: this.valueAttributeBy('[ref=languageCode]'),
        jdbcDriver: this.state.jdbcDriver,
        jdbcDriverFqcn: this.inputElementBy('[ref=jdbcDriverFqcn]').value,
      },
      testConnection: this.inputElementBy('[ref=testConnection]').checked,
    }
    this.suLoading(true)
    api
      .createWelcomeClient(body)
      .then(() => {
        appRoutes.main.open()
        this.showToast(body.client.projectName)
      })
      .catch((error) => {
        // ApiClientのmodal表示に任せて画面固有の例外ハンドリングなし (throw終了のため空catchは必要)
      })
      .finally(() => {
        this.suLoading(false)
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

  // _/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/_/
  // done jflute シンプルな関数名にするか、showClientCreatedToast()とか具体的な名前にするか迷うね (2022/03/17)
  // いや、showToast()だと汎用的な処理なのかなとかふと思ってしまって。要はJavaだとhelper的なprivateメソッドみたいな。
  // もちろん、welcome.tagというクラスみたいな世界の中のshowToast()なのでクライアント作成時のものでしょうって解釈もできるけど。
  // 少なくともこういうところ、人によってバラけそうじゃない？って思った。
  //
  // いんとろんずうむにて、この件を議論(2022/03/24):
  // o tagはクラスみたいなものなので、シンプルな名前寄りでOK
  // o でも、toggle()とかは無し、トグルは画面で一つだけ感がない
  // o なので、多少ケースバイケースで属人的な判断ではある
  // o 一方で、業務のコードだと2個目が出てきたとき、シンプルな方が置き去りにされやすい
  // o でも、ここはOSSの世界、2個目が出てきたときは既存も直そうポリシーで
  // _/_/_/_/_/_/_/_/_/_/
  showToast(projectName: string) {
    this.successToast({
      title: 'Create task completed',
      message: "Client for project '" + projectName + "', was successfully created!!",
    })
  },
})
