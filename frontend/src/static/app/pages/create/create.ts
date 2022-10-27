import i18n from '../../components/common/i18n.riot'
import { api } from '../../api/api'
import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import { DropdownItem } from '../../components/dropdown/dropdown'
import { appRoutes } from '../../app-router'
import { readFile } from '../../shared/io-utils'

interface State {
  jdbcDriver: { fileName: string; data: string }
  needsJdbcDriver: boolean
  oRMapperOptionsFlg: boolean
}

interface Create extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultDatabaseCode: string
  defaultJdbcDriver: string
  defaultJdbcUrl: string
  defaultLanguageCode: string
  defaultContainerCode: string
  databaseMap: { [key: string]: DatabaseDefBean }
  engineVersions: DropdownItem[]
  targetDatabaseItems: DropdownItem[]
  targetLanguageItems: DropdownItem[]
  targetContainerItems: DropdownItem[]

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onchangeDatabase: (databaseCode: DropdownItem) => void
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

export default withIntroTypes<Create>({
  components: {
    i18n,
  },
  state: {
    jdbcDriver: undefined,
    needsJdbcDriver: false,
    oRMapperOptionsFlg: true,
  },
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  defaultDatabaseCode: '',
  defaultJdbcDriver: 'com.mysql.jdbc.Driver',
  defaultJdbcUrl: 'jdbc:mysql://localhost:3306/xxx',
  defaultLanguageCode: 'java',
  defaultContainerCode: 'lasta_di',
  databaseMap: {},
  engineVersions: [],
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
  /**
   * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する
   * @param {DropdownItem} targetDatabase - 選択されたDBMS (NotNull)
   */
  onchangeDatabase(targetDatabase: DropdownItem) {
    const database = this.databaseMap[targetDatabase.value]
    this.inputElementBy('[ref=jdbcDriverFqcn]').value = database.driverName
    this.inputElementBy('[ref=url]').value = database.urlTemplate
    this.inputElementBy('[ref=schema]').value = database.defaultSchema
    this.update({
      // switch showing JDBCDriver select form
      needsJdbcDriver: true,
      jdbcDriver: undefined,
    })
  },

  /**
   * DBFluteクライアントを作成する。(作成ボタンの処理)
   */
  onclickCreate() {
    let client = {
      projectName: this.inputElementBy('[ref=projectName]').value,
      databaseCode: this.$('[ref=databaseCode]').getAttribute('value'),
      create: true,
      mainSchemaSettings: {
        user: this.inputElementBy('[ref=user]').value,
        url: this.inputElementBy('[ref=url]').value,
        schema: this.inputElementBy('[ref=schema]').value,
        password: this.inputElementBy('[ref=password]').value,
      },
      schemaSyncCheckMap: {},
      dbfluteVersion: this.inputElementBy('[ref=dbfluteVersion]').value,
      packageBase: this.inputElementBy('[ref=packageBase]').value,
      containerCode: this.inputElementBy('[ref=containerCode]').value,
      languageCode: this.inputElementBy('[ref=languageCode]').value,
      jdbcDriver: this.state.jdbcDriver,
      jdbcDriverFqcn: this.inputElementBy('[ref=jdbcDriverFqcn]').value,
    }
    const testConnection = this.inputElementBy('[ref=testConnection]').checked
    api.createWelcomeClient(client, testConnection).then(() => {
      appRoutes.main.open()
      this.showToast(client.projectName)
    })
  },

  /**
   * JDBCファイルを読み込み、このタグのプロパティとして保持する
   * フォームでJDBCドライバーのファイルを指定したときに呼び出される
   * @param {Event} event - この関数を呼び出したイベントのオブジェクト (NotNull)
   */
  onchangeJarFile(event: InputEvent) {
    const eventTarget = event.target as HTMLInputElement
    const file = eventTarget.files[0]
    readFile(file).then((result) => {
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
      targetDatabaseItems: Object.entries(classifications.targetDatabaseMap).map(([key, value]) => {
        return { value: key, label: value.databaseName, default: false }
      }),
      targetLanguageItems: Object.entries(classifications.targetLanguageMap).map(([key, value]) => {
        return { value: key, label: value, default: false }
      }),
      targetContainerItems: Object.entries(classifications.targetContainerMap).map(([key, value]) => {
        return { value: key, label: value, default: false }
      }),
    }
  },

  /**
   * DBFluteクライアントが作成できたことを知らせるトーストを表示する
   * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
   */
  showToast(projectName: string) {
    this.successToast({
      title: 'Create task completed',
      message: "Client for project '" + projectName + "', was successfully created!!",
    })
  },
})
