import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import LatestResult from '../latest-result.riot'
import { api } from '../../../api/api'
import TaskExecuteModal from '../task-execute-modal.riot'
import { TaskExecuteStatus } from '../task-execute-modal'
import DocumentFormModal from './document-form-modal.riot'

type DocumentLatestResult = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  documentSetting?: DfpropDocumentResult
  latestResult?: DocumentLatestResult
  hasSchemaHtml: boolean
  hasHistoryHtml: boolean
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
  showDocumentSettingModal: boolean
  prepared: boolean
}

interface Document extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted: () => void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                       Open Document
  //                                       --------------
  openSchemaHTML: () => void
  openHistoryHTML: () => void
  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  showDocumentSettingModal: () => void
  onSettingSaved: () => void
  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  onclickGenerateTask: () => void
  onExecuteModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareComponents: () => void
  updateContents: (additionalState?: Partial<State>) => Promise<void>
}

export default withIntroTypes<Document>({
  components: {
    LatestResult,
    TaskExecuteModal,
    DocumentFormModal,
  },
  state: {
    documentSetting: undefined,
    latestResult: undefined,
    hasSchemaHtml: false,
    hasHistoryHtml: false,
    executeStatus: 'None',
    executeResultMessage: undefined,
    showDocumentSettingModal: false,
    prepared: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted() {
    this.prepareComponents()
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                       Open Document
  //                                       --------------
  /**
   * SchemaHTML を新しいタブで開く
   */
  openSchemaHTML() {
    window.open('/api/document/' + this.props.projectName + '/schemahtml/')
  },

  /**
   * HistoryHTML を新しいタブで開く
   */
  openHistoryHTML() {
    window.open('/api/document/' + this.props.projectName + '/historyhtml/')
  },

  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  /**
   * Document 設定モーダルを表示する
   */
  showDocumentSettingModal() {
    this.update({ showDocumentSettingModal: true })
  },

  /**
   * Document 設定を保存するとき、画面の状態を最新化する
   */
  async onSettingSaved() {
    await this.updateContents({ showDocumentSettingModal: false })
  },

  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  /**
   * Doc タスクを実行する
   * すでに実行されている状態であれば何もしない
   */
  async onclickGenerateTask() {
    if (this.state.executeStatus !== 'None') {
      return
    }
    await this.updateContents({ executeStatus: 'Executing', executeResultMessage: 'Generating...' })
    await api
      .task(this.props.projectName, 'doc')
      .then(async (data) => {
        const executeResultMessage = data.success ? 'Success' : 'Failure'
        await this.updateContents({ executeStatus: 'Completed', executeResultMessage })
      })
      .catch(async () => {
        // APIリクエストに失敗した際の情報も反映するため更新（一緒に実行モーダルは閉じる）
        await this.updateContents({ executeStatus: 'None' })
      })
  },

  /**
   * タスク実行モーダルを閉じるとき、画面の状態を最新化する
   */
  onExecuteModalHide() {
    // タスク実行モーダルが閉じられたときに executeStatus をリセット
    this.update({ executeStatus: 'None' })
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * コンポーネントの初期化を完了した状態にし、画面を最新化する
   */
  prepareComponents() {
    this.updateContents({ prepared: true })
  },

  /**
   * 引数で受け取った state を更新しつつ、画面を最新化する
   * @param additionalState 更新する state の値
   */
  async updateContents(additionalState?: Partial<State>) {
    const projectName = this.props.projectName
    const documentSetting = await api.findDocumentDfprop(projectName)
    const latestResult = await api.latestResult(projectName, 'doc').then((body) => {
      if (body) {
        return {
          success: body.fileName.includes('success'),
          content: body.content,
        }
      }
    })
    const client = await api.clientPropbase(projectName)
    this.update({
      documentSetting,
      latestResult,
      hasSchemaHtml: client.hasSchemaHtml,
      hasHistoryHtml: client.hasHistoryHtml,
      ...additionalState,
    })
  },
})
