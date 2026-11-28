import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  showsModal: boolean
  documentSettings: DfpropDocumentResult
  onModalHide?: () => void
}

interface State {
  showsModal: boolean
  documentSettings: DfpropDocumentResult
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
  header: 'Document Settings (documentMap.dfprop, littleAdjustmentMap.dfprop)',
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

interface DocumentFormModal extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  /**
   * モーダルの定義を返す
   * @returns フォームモーダルの定義
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
   * aliasDelimiterInDbComment の入力値が変更されたとき、state を更新する
   * @param event 入力イベント（input要素からの値変更イベント）
   */
  onChangeAliasDelimiterInDbComment(event: InputEvent): void
  /**
   * upperCaseBasic のチェックボックスが変更されたとき、state を更新する
   * @param event 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeUpperCaseBasic(event: InputEvent): void
  /**
   * dbCommentOnAliasBasis のチェックボックスが変更されたとき、state を更新する
   * @param event 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeDbCommentOnAliasBasis(event: InputEvent): void
  /**
   * checkColumnDefOrderDiff のチェックボックスが変更されたとき、state を更新する
   * @param event 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckColumnDefOrderDiff(event: InputEvent): void
  /**
   * checkDbCommentDiff のチェックボックスが変更されたとき、state を更新する
   * @param event 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckDbCommentDiff(event: InputEvent): void
  /**
   * checkProcedureDiff のチェックボックスが変更されたとき、state を更新する
   * @param event 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckProcedureDiff(event: InputEvent): void
  /**
   * Document 設定を保存する
   * 保存成功時はモーダルを閉じる
   */
  saveSettings(): Promise<void>
}

export default withIntroTypes<DocumentFormModal>({
  state: {
    showsModal: false,
    documentSettings: {},
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
    this.state.documentSettings = this.props.documentSettings || {}
  },

  onBeforeUpdate() {
    this.state.showsModal = this.props.showsModal
    // props.documentSetting が更新された場合のみ同期する（ユーザー入力を上書きしないため）
    if (this.props.documentSettings && !this.state.showsModal) {
      this.state.documentSettings = this.props.documentSettings
    }
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
  onChangeAliasDelimiterInDbComment(event: InputEvent) {
    const value = (event.target as HTMLInputElement).value
    this.update({
      documentSettings: { ...this.state.documentSettings, aliasDelimiterInDbComment: value },
    })
  },

  onChangeUpperCaseBasic(event: InputEvent) {
    const checked = (event.target as HTMLInputElement).checked
    this.update({
      documentSettings: { ...this.state.documentSettings, upperCaseBasic: checked },
    })
  },

  onChangeDbCommentOnAliasBasis(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSettings: { ...this.state.documentSettings, dbCommentOnAliasBasis: checked },
    })
  },

  onChangeCheckColumnDefOrderDiff(event: InputEvent) {
    const checked = (event.target as HTMLInputElement).checked
    this.update({
      documentSettings: { ...this.state.documentSettings, checkColumnDefOrderDiff: checked },
    })
  },

  onChangeCheckDbCommentDiff(event: InputEvent) {
    const checked = (event.target as HTMLInputElement).checked
    this.update({
      documentSettings: { ...this.state.documentSettings, checkDbCommentDiff: checked },
    })
  },

  onChangeCheckProcedureDiff(event: InputEvent) {
    const checked = (event.target as HTMLInputElement).checked
    this.update({
      documentSettings: { ...this.state.documentSettings, checkProcedureDiff: checked },
    })
  },

  async saveSettings() {
    const projectName = this.props.projectName
    const formData = this.state.documentSettings
    await api
      .editDocumentDfprop(projectName, formData)
      .then(() => {
        this.onHide()
      })
      .catch((_) => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },
})
