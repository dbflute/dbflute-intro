import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import LatestResult from '../latest-result.riot'
import { api } from '../../../api/api'
import TaskExecuteModal from '../task-execute-modal.riot'
import { TaskExecuteStatus } from '../task-execute-modal'
import SchemaSyncCheckFormModal from './schema-sync-check-form-modal.riot'

type SchemaSyncCheckLatestResult = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  syncSchemaSetting?: DfpropSchemasyncResult
  latestResult?: SchemaSyncCheckLatestResult
  hasSchemaSyncCheckResultHtml: boolean
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
  showSyncSettingModal: boolean
  prepared: boolean
}

interface SchemaSyncCheck extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted: () => void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  // -----------------------------------------------------
  //                                           Result HTML
  //                                           -----------
  openSyncCheckResultHTML: () => void
  canCheckSchemaSetting: () => boolean
  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  showSyncSettingModal: () => void
  onSettingSaved: () => void
  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  onclickSchemaSyncCheckTask: () => void
  onExecuteModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareComponents: () => void
  updateContents: (additionalState?: Partial<State>) => Promise<void>
}

export default withIntroTypes<SchemaSyncCheck>({
  components: {
    LatestResult,
    TaskExecuteModal,
    SchemaSyncCheckFormModal,
  },
  state: {
    syncSchemaSetting: undefined,
    latestResult: undefined,
    hasSchemaSyncCheckResultHtml: false,
    executeStatus: 'None',
    executeResultMessage: undefined,
    showSyncSettingModal: false,
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
  //                                           Result HTML
  //                                           -----------
  openSyncCheckResultHTML() {
    // SchemaSyncCheck の結果HTMLを新しいタブで開く
    window.open('/api/document/' + this.props.projectName + '/synccheckresulthtml/')
  },

  /**
   * SchemaSyncCheck の設定が確認可能か、つまり SchemaSyncCheckの設定が存在するかを確認する
   */
  canCheckSchemaSetting() {
    const setting = this.state.syncSchemaSetting
    return !!setting && !!setting.url && !!setting.user
  },

  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  /**
   * SchemaSyncCheck 設定モーダルを表示する
   */
  showSyncSettingModal() {
    this.update({ showSyncSettingModal: true })
  },

  /**
   * SchemaSyncCheck 設定を保存するとき、画面の状態を最新化する
   */
  async onSettingSaved() {
    await this.updateContents({ showSyncSettingModal: false })
  },

  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  /**
   * SchemaSyncCheck タスクを実行する
   * すでに実行されている状態であれば何もしない
   */
  async onclickSchemaSyncCheckTask() {
    if (this.state.executeStatus !== 'None') {
      return
    }
    this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
    await api
      .executeTask(this.props.projectName, 'schemaSyncCheck')
      .then(async (data) => {
        const executeResultMessage = data.success ? 'Success' : 'Failure'
        await this.updateContents({ executeStatus: 'Completed', executeResultMessage })
      })
      .catch(async () => {
        this.update({ executeStatus: 'Error', executeResultMessage: 'Unexpected error occurred.' })
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
    const syncSchemaSetting = await api.findSchemaSyncDfprop(projectName)
    const latestResult = await api.findLatestTaskLog(projectName, 'schemaSyncCheck').then((body) => {
      if (body) {
        return {
          success: body.fileName.includes('success'),
          content: body.content,
        }
      }
    })
    const clientPropbase = await api.findClientPropbase(projectName)
    this.update({
      syncSchemaSetting,
      latestResult,
      hasSchemaSyncCheckResultHtml: clientPropbase.hasSyncCheckResultHtml,
      ...additionalState,
    })
  },
})
