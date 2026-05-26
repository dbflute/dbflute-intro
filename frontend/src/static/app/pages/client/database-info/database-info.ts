import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 */
  projectName: string
}

type DatabaseInfoForm = {
  /** JDBCの接続URL */
  url: string
  /** JDBCの接続スキーマ */
  schema: string
  /** JDBCの接続ユーザー */
  user: string
  /** JDBCの接続パスワード */
  password: string
}

interface State {
  /** DBFluteクライアントの基本設定情報 (undefined: 初期化前) */
  settings?: DfpropSettingsResult

  /** 画面で編集するDB接続情報 */
  form: DatabaseInfoForm
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface DatabaseInfo extends IntroRiotComponent<Props, State> {
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
   * URL の入力値が変更されたとき、state を更新する。
   * @param e - 入力イベント
   */
  onChangeUrl(e: InputEvent): void

  /**
   * Schema の入力値が変更されたとき、state を更新する。
   * @param e - 入力イベント
   */
  onChangeSchema(e: InputEvent): void

  /**
   * User の入力値が変更されたとき、state を更新する。
   * @param e - 入力イベント
   */
  onChangeUser(e: InputEvent): void

  /**
   * Password の入力値が変更されたとき、state を更新する。
   * @param e - 入力イベント
   */
  onChangePassword(e: InputEvent): void

  /**
   * 入力されたDB接続情報を実際のDBFluteクライアントに更新する。
   */
  editClient(): Promise<void>

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * DBFluteクライアントの基本設定情報を取得して画面に反映する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名
   */
  prepareSettings(projectName: string): Promise<void>

  /**
   * APIで取得した設定情報から画面入力用のフォームデータを組み立てる。
   * @param settings - DBFluteクライアントの基本設定情報
   */
  buildForm(settings?: DfpropSettingsResult): DatabaseInfoForm

  /**
   * DB接続情報を保存する。
   * @param settingsBody - 基本的なdfprop情報の編集情報(実際に更新されるのはDB情報だけ)
   */
  saveSettings(settingsBody: DfpropSettingsEditBody): Promise<void>

  /**
   * 更新したことを知らせるトーストを表示する。
   */
  showToast(): void
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<DatabaseInfo>({
  state: {
    settings: undefined,
    form: {
      url: '',
      schema: '',
      user: '',
      password: '',
    },
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted(): Promise<void> {
    await this.prepareSettings(this.props.projectName)
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onChangeUrl(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, url: value },
    })
  },

  onChangeSchema(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, schema: value },
    })
  },

  onChangeUser(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, user: value },
    })
  },

  onChangePassword(e: InputEvent): void {
    const value = (e.target as HTMLInputElement).value
    this.update({
      form: { ...this.state.form, password: value },
    })
  },

  async editClient(): Promise<void> {
    const settings = this.state.settings
    if (!settings) {
      return
    }

    const body: DfpropSettingsEditBody = {
      client: {
        databaseCode: settings.databaseCode,
        languageCode: settings.languageCode,
        containerCode: settings.containerCode,
        packageBase: settings.packageBase,
        jdbcDriverFqcn: settings.jdbcDriverFqcn,
        dbfluteVersion: settings.dbfluteVersion,
        mainSchemaSettings: {
          url: this.state.form.url,
          schema: this.state.form.schema,
          user: this.state.form.user,
          password: this.state.form.password,
        },
      },
    }

    await this.saveSettings(body)
      .then(async () => {
        await this.prepareSettings(this.props.projectName)
        this.showToast()
      })
      .catch(() => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  async prepareSettings(projectName: string): Promise<void> {
    const coreDfprop = await api.findCoreDfprop(projectName)
    const state = {
      settings: coreDfprop,
      form: this.buildForm(coreDfprop),
    }
    this.update(state)
  },

  buildForm(settings?: DfpropSettingsResult): DatabaseInfoForm {
    const mainSchemaSettings = settings?.mainSchemaSettings
    return {
      url: mainSchemaSettings?.url || '',
      schema: mainSchemaSettings?.schema || '',
      user: mainSchemaSettings?.user || '',
      password: mainSchemaSettings?.password || '',
    }
  },

  async saveSettings(settingsBody: DfpropSettingsEditBody): Promise<void> {
    await api.editCoreDfprop(this.props.projectName, settingsBody)
  },

  showToast(): void {
    this.successToast({
      title: 'Setting Updated',
      message: undefined,
    })
  },
})
