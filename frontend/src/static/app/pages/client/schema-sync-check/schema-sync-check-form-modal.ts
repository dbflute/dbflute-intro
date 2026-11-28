import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  projectName: string
  showsModal: boolean
  syncSchemaSetting: DfpropSchemasyncResult
  onModalHide?: () => void
}

interface State {
  showsModal: boolean
  syncSchemaSetting: DfpropSchemasyncResult
}

type SuModalButton = {
  text: string
  action: string
  default?: boolean
  closable?: boolean
}

type SuModal = {
  header: string
  closable: boolean
  buttons: SuModalButton[]
}

const FORM_MODAL: SuModal = {
  header: 'Schema Sync Check Settings',
  closable: true,
  buttons: [
    {
      text: 'OK',
      action: 'save',
      default: true,
      closable: false,
    },
  ],
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface SchemaSyncCheckFormModal extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  /**
   * モーダルの定義を返す
   * @returns su-modal対応のモーダル定義オブジェクト
   */
  modal(): SuModal

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * コンポーネントのマウント前に props から state を初期化する
   */
  onBeforeMount(): void

  /**
   * コンポーネントの更新前に props から state を同期する
   */
  onBeforeUpdate(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                                 Modal
  //                                                 -----
  /**
   * モーダルを閉じる
   * 親コンポーネントに onModalHide イベントを通知する
   */
  onHide(): void

  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  /**
   * URL の入力値が変更されたとき、state を更新する
   * @param e 入力イベント（input要素からの値変更イベント）
   */
  onChangeUrl(e: InputEvent): void

  /**
   * Schema の入力値が変更されたとき、state を更新する
   * @param e 入力イベント（input要素からの値変更イベント）
   */
  onChangeSchema(e: InputEvent): void

  /**
   * User の入力値が変更されたとき、state を更新する
   * @param e 入力イベント（input要素からの値変更イベント）
   */
  onChangeUser(e: InputEvent): void

  /**
   * Password の入力値が変更されたとき、state を更新する
   * @param e 入力イベント（input要素からの値変更イベント）
   */
  onChangePassword(e: InputEvent): void

  /**
   * IsSuppressCraftDiff のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeIsSuppressCraftDiff(e: InputEvent): void

  /**
   * SchemaSyncCheck 設定を保存する
   * 保存成功時はモーダルを閉じる
   */
  saveSettings(): Promise<void>
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<SchemaSyncCheckFormModal>({
  state: {
    showsModal: false,
    syncSchemaSetting: {},
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  modal(): SuModal {
    return FORM_MODAL
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onBeforeMount() {
    this.state.syncSchemaSetting = this.props.syncSchemaSetting
  },

  onBeforeUpdate() {
    this.state.showsModal = this.props.showsModal
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                                 Modal
  //                                                 -----
  onHide() {
    this.update({ showsModal: false })
    if (this.props.onModalHide) {
      this.props.onModalHide()
    }
  },

  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  onChangeUrl(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeUrl called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, url: value },
    })
  },

  onChangeSchema(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeSchema called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, schema: value },
    })
  },

  onChangeUser(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangeUser called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, user: value },
    })
  },

  onChangePassword(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    console.log('onChangePassword called:', value)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, password: value },
    })
  },

  onChangeIsSuppressCraftDiff(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    console.log('onChangeIsSuppressCraftDiff called:', checked)
    this.update({
      syncSchemaSetting: { ...this.state.syncSchemaSetting, isSuppressCraftDiff: checked },
    })
  },

  async saveSettings() {
    const projectName = this.props.projectName
    const formData = this.state.syncSchemaSetting
    await api
      .editSyncSchemaDfprop(projectName, formData)
      .then(() => {
        this.onHide()
      })
      .catch((_) => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },
})
