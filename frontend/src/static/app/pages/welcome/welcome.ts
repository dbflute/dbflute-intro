import i18n from '../../components/common/i18n.riot'
import { api } from '../../api/api'
import { appRoutes } from '../../app-router'
import { readFile } from '../../shared/io-utils'
import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import { DropdownItem } from '../../components/dropdown/dropdown'

const defaultDropDownItem = {
  value: '',
  label: 'not selected',
  default: true,
}

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
  jdbcDriver: { fileName: string; data: string }
  // JDBCドライバーのアップロードが必要なDBMSかどうか？サーバー側のDBMS定義より設定される
  // (例えば、MySQLだとDBFlute Engineに組み込まれているので false となる)
  needsJdbcDriver: boolean
  // O/Rマッパー関連設定の表示/非表示
  oRMapperOptionsFlg: boolean
}

interface Welcome extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultDatabaseCode: string
  defaultJdbcDriver: string
  defaultJdbcUrl: string
  defaultLanguageCode: string
  defaultContainerCode: string
  databaseMap: { [key: string]: DatabaseDefBean }
  latestVersion: string | undefined
  targetDatabaseItems: DropdownItem[]
  targetLanguageItems: DropdownItem[]
  targetContainerItems: DropdownItem[]

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onchangeDatabase: (databaseCode: DropdownItem) => void
  onclickOrmSetting: () => void
  onclickCreate: () => void
  onchangeJarFile: (event: InputEvent) => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  convertClassificationsForUI: (classifications: IntroClassificationsResult) => {
    databaseMap: { [key: string]: DatabaseDefBean }
    targetContainerItems: DropdownItem[]
    targetLanguageItems: DropdownItem[]
    targetDatabaseItems: DropdownItem[]
  }
  showToast: (projectName: string) => void
}

export default withIntroTypes<Welcome>({
  components: {
    i18n,
  },
  state: {
    jdbcDriver: undefined,
    needsJdbcDriver: false,
    oRMapperOptionsFlg: false,
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
  // _/_/_/_/_/_/_/_/_/_/
  defaultDatabaseCode: '',
  defaultJdbcDriver: '',
  defaultJdbcUrl: '',
  defaultLanguageCode: 'java',
  defaultContainerCode: 'lasta_di',
  databaseMap: {}, // e.g. targetDatabase
  // DBFluteエンジンの最新バージョン e.g. 1.2.5
  latestVersion: undefined,
  targetDatabaseItems: [],
  targetLanguageItems: [],
  targetContainerItems: [],

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    const classifications = await api.findClassifications().then((data) => this.convertClassificationsForUI(data))
    const latestVersion = await api.findEngineLatest().then((data) => data.latestReleaseVersion)
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
  /**
   * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する。
   * @param {DropdownItem} targetDatabase - 選択されたDBMS (NotNull)
   */
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
      const database = this.databaseMap[targetDatabase.value]
      this.inputElementBy('[ref=jdbcDriverFqcn]').value = database.driverName
      this.inputElementBy('[ref=url]').value = database.urlTemplate
      this.inputElementBy('[ref=schema]').value = database.defaultSchema
      this.update({
        // switch showing JDBCDriver select form
        needsJdbcDriver: !database.embeddedJar,
        // initialize JDBC Driver
        jdbcDriver: undefined,
      })
    }
  },

  /**
   * O/Rマッパー設定の表示/非表示をトグルする。
   */
  onclickOrmSetting() {
    this.update({
      oRMapperOptionsFlg: !this.state.oRMapperOptionsFlg,
    })
  },

  /**
   * DBFluteクライアントを作成する。(作成ボタンの処理)
   */
  onclickCreate() {
    const body: WelcomeCreateBody = {
      client: {
        projectName: this.inputElementBy('[ref=projectName]').value,
        databaseCode: this.$('[ref=databaseCode]').getAttribute('value'),
        mainSchemaSettings: {
          user: this.inputElementBy('[ref=user]').value,
          url: this.inputElementBy('[ref=url]').value,
          schema: this.inputElementBy('[ref=schema]').value,
          password: this.inputElementBy('[ref=password]').value,
        },
        dbfluteVersion: this.latestVersion,
        packageBase: this.inputElementBy('[ref=packageBase]').value,
        containerCode: this.$('[ref=containerCode]').getAttribute('value'),
        languageCode: this.$('[ref=languageCode]').getAttribute('value'),
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
      .finally(() => {
        this.suLoading(false)
      })
  },

  /**
   * JDBCドライバーのファイルが指定されたときの処理。
   * @param {Event} event - この関数を呼び出したイベントのオブジェクト (NotNull)
   */
  onchangeJarFile(event: InputEvent) {
    const eventTarget = event.target as HTMLInputElement // event.targetはイベント発生元のオブジェクト
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
  /**
   * 区分値情報をUI用のデータに整形する。
   * @param {IntroClassificationsResult} classifications - APIで取得した区分値情報 (NotNull)
   */
  convertClassificationsForUI(classifications: IntroClassificationsResult) {
    return {
      databaseMap: classifications.targetDatabaseMap,
      targetDatabaseItems: [
        ...Object.entries(classifications.targetDatabaseMap).map(([key, value]) => {
          return { value: key, label: value.databaseName, default: false }
        }),
        defaultDropDownItem,
      ],
      targetLanguageItems: [
        ...Object.entries(classifications.targetLanguageMap).map(([key, value]) => {
          return { value: key, label: value, default: false }
        }),
        defaultDropDownItem,
      ],
      targetContainerItems: [
        ...Object.entries(classifications.targetContainerMap).map(([key, value]) => {
          return { value: key, label: value, default: false }
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
  /**
   * DBFluteクライアントを作成したことを知らせるパンじゃなくてトースト。
   * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
   */
  showToast(projectName: string) {
    this.successToast({
      title: 'Create task completed',
      message: "Client for project '" + projectName + "', was successfully created!!",
    })
  },
})
