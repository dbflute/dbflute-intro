import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

interface Props {
  projectName: string
  show: boolean
  documentSetting: DfpropDocumentResult
  onModalHide?: () => void
}

interface State {
  show: boolean
  documentSetting: DfpropDocumentResult
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
  modal(): SuModal

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onBeforeMount(): void
  onBeforeUpdate(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                                 Modal
  //                                                 -----
  onHide(): void
  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  onChangeAliasDelimiterInDbComment(e: InputEvent): void
  onChangeUpperCaseBasic(e: InputEvent): void
  onChangeDbCommentOnAliasBasis(e: InputEvent): void
  onChangeCheckColumnDefOrderDiff(e: InputEvent): void
  onChangeCheckDbCommentDiff(e: InputEvent): void
  onChangeCheckProcedureDiff(e: InputEvent): void
  saveSettings(): Promise<void>
}

export default withIntroTypes<DocumentFormModal>({
  state: {
    show: false,
    documentSetting: {},
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  /**
   * モーダルの定義を返す
   * @returns フォームモーダルの定義
   */
  modal(): SuModal {
    return FORM_MODAL
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * コンポーネントのマウント前に props から state を初期化する
   */
  onBeforeMount() {
    this.state.documentSetting = this.props.documentSetting || {}
  },

  /**
   * コンポーネントの更新前に props から state を同期する
   */
  onBeforeUpdate() {
    this.state.show = this.props.show
    // props.documentSetting が更新された場合のみ同期する（ユーザー入力を上書きしないため）
    if (this.props.documentSetting && !this.state.show) {
      this.state.documentSetting = this.props.documentSetting
    }
  },

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
  onHide() {
    this.update({ show: false })
    if (this.props.onModalHide) {
      this.props.onModalHide()
    }
  },

  // -----------------------------------------------------
  //                                                  Form
  //                                                  ----
  /**
   * aliasDelimiterInDbComment の入力値が変更されたとき、state を更新する
   * @param e 入力イベント（input要素からの値変更イベント）
   */
  onChangeAliasDelimiterInDbComment(e: InputEvent) {
    const value = (e.target as HTMLInputElement).value
    this.update({
      documentSetting: { ...this.state.documentSetting, aliasDelimiterInDbComment: value },
    })
  },

  /**
   * upperCaseBasic のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeUpperCaseBasic(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSetting: { ...this.state.documentSetting, upperCaseBasic: checked },
    })
  },

  /**
   * dbCommentOnAliasBasis のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeDbCommentOnAliasBasis(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSetting: { ...this.state.documentSetting, dbCommentOnAliasBasis: checked },
    })
  },

  /**
   * checkColumnDefOrderDiff のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckColumnDefOrderDiff(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSetting: { ...this.state.documentSetting, checkColumnDefOrderDiff: checked },
    })
  },

  /**
   * checkDbCommentDiff のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckDbCommentDiff(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSetting: { ...this.state.documentSetting, checkDbCommentDiff: checked },
    })
  },

  /**
   * checkProcedureDiff のチェックボックスが変更されたとき、state を更新する
   * @param e 入力イベント（checkbox要素からの値変更イベント）
   */
  onChangeCheckProcedureDiff(e: InputEvent) {
    const checked = (e.target as HTMLInputElement).checked
    this.update({
      documentSetting: { ...this.state.documentSetting, checkProcedureDiff: checked },
    })
  },

  /**
   * Document 設定を保存する
   * 保存成功時はモーダルを閉じる
   */
  async saveSettings() {
    const projectName = this.props.projectName
    const formData = this.state.documentSetting
    await api
      .editDocument(projectName, formData)
      .then(() => {
        this.onHide()
      })
      .catch((_) => {
        // API Client で modal 出す以上のハンドリングはしない
      })
  },
})
