import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import LatestResult from '../latest-result.riot'
import { api } from '../../../api/api'
import TaskExecuteModal from '../task-execute-modal.riot'
import { TaskExecuteStatus } from '../task-execute-modal'
import SchemaSyncCheckFormModal from './schema-sync-check-form-modal.riot'
import type { LatestResult as LatestResultState } from '../latest-result'
import { isTaskLogSuccessful } from '../../../api/task-log'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  projectName: string
}

interface State {
  syncSchemaSetting?: DfpropSchemasyncResult
  latestResult?: LatestResultState
  hasSchemaSyncCheckResultHtml: boolean
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
  showsSyncSettingModal: boolean
  prepared: boolean
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface SchemaSyncCheck extends IntroRiotComponent<Props, State> {
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
  // -----------------------------------------------------
  //                                           Result HTML
  //                                           -----------
  /**
   * SchemaSyncCheck の結果HTMLを新しいタブで開く
   */
  openSyncCheckResultHTML: () => void

  /**
   * SchemaSyncCheck の設定が確認可能か、つまり SchemaSyncCheckの設定が存在するかを確認する
   * @returns 要はdfpropでSchemaSyncCheckの設定がされているならtrue
   */
  canCheckSchemaSetting: () => boolean

  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  /**
   * SchemaSyncCheck 設定モーダルを表示する
   */
  showSyncSettingModal: () => void

  /**
   * SchemaSyncCheck 設定を保存するとき、画面の状態を最新化する
   */
  onSettingSaved: () => void

  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  /**
   * SchemaSyncCheck タスクを実行する
   * すでに実行されている状態であれば何もしない
   */
  onclickSchemaSyncCheckTask: () => void

  /**
   * タスク実行モーダルを閉じるとき、画面の状態を最新化する
   */
  onExecuteModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * コンポーネントの初期化を完了した状態にし、画面を最新化する
   */
  prepareComponents: () => void

  /**
   * 引数で受け取った state を更新しつつ、画面を最新化する
   * @param additionalState 更新する state の値
   */
  updateContents: (additionalState?: Partial<State>) => Promise<void>

  /**
   * 最新のDBFluteタスク実行結果を取得する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名。
   * @returns 最新のDBFluteタスク実行結果オブジェクト
   */
  fetchLatestResult: (projectName: string) => Promise<LatestResultState | undefined>
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

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
    showsSyncSettingModal: false,
    prepared: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
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
    window.open('/api/document/' + this.props.projectName + '/synccheckresulthtml/')
  },

  canCheckSchemaSetting() {
    const setting = this.state.syncSchemaSetting
    return !!setting && !!setting.url && !!setting.user
  },

  // -----------------------------------------------------
  //                                         Setting Modal
  //                                         -------------
  showSyncSettingModal() {
    this.update({ showsSyncSettingModal: true })
  },

  async onSettingSaved() {
    await this.updateContents({ showsSyncSettingModal: false })
  },

  // -----------------------------------------------------
  //                                             Execution
  //                                             ---------
  async onclickSchemaSyncCheckTask() {
    if (this.state.executeStatus !== 'None') {
      return
    }
    await this.updateContents({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
    await api
      .executeTask(this.props.projectName, 'schemaSyncCheck')
      .then(async (data) => {
        const executeResultMessage = data.success ? 'Success' : 'Failure'
        await this.updateContents({ executeStatus: 'Completed', executeResultMessage })
      })
      .catch(async () => {
        // APIリクエストに失敗した際の情報も反映するため更新（一緒に実行モーダルは閉じる）
        await this.updateContents({ executeStatus: 'None' })
      })
  },

  onExecuteModalHide() {
    // タスク実行モーダルが閉じられたときに executeStatus をリセット
    this.update({ executeStatus: 'None' })
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareComponents() {
    this.updateContents({ prepared: true })
  },

  async updateContents(additionalState?: Partial<State>) {
    const projectName = this.props.projectName
    const syncSchemaSetting = await api.findSchemaSyncDfprop(projectName)
    console.log('Fetched syncSchemaSetting:', syncSchemaSetting)
    const latestResult = await this.fetchLatestResult(projectName)
    const clientPropbase = await api.findClientPropbase(projectName)
    this.update({
      syncSchemaSetting,
      latestResult,
      hasSchemaSyncCheckResultHtml: clientPropbase.hasSyncCheckResultHtml,
      ...additionalState,
    })
  },

  async fetchLatestResult(projectName: string) {
    const data = await api.findLatestTaskLog(projectName, 'schemaSyncCheck')
    return data ? { success: isTaskLogSuccessful(data.fileName), content: data.content } : undefined
  },
})
