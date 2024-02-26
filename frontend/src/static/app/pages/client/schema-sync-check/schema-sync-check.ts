import i18n from '../../components/common/i18n.riot'
import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import LatestResult from '../latest-result.riot'
import { api } from '../../../api/api'
import TaskExecuteModal from '../task-execute-modal.riot'
import { TaskExecuteStatus } from '../task-execute-modal'
import SchemaSyncCheckFormModal from './form-modal'

type SchemaSyncCheckLatestResult = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  syncSchemaSetting: DfpropSchemasyncResult
  latestResult?: SchemaSyncCheckLatestResult
  hasSchemaSyncCheckResultHtml: boolean
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
}

interface SchemaSyncCheck extends IntroRiotComponent<Props, State> {
  prepareComponents: () => Promise<void>
  canCheckSchemaSetting: () => boolean
  onclickSchemaSyncCheckTask: () => void

  // private
  updateContents: (additionalState?: Partial<State>) => Promise<void>
}

export default withIntroTypes<SchemaSyncCheck>({
  components: {
    LatestResult,
    TaskExecuteModal,
    SchemaSyncCheckFormModal,
  },
  state: {
    syncSchemaSetting: {},
    latestResult: undefined,
    hasSchemaSyncCheckResultHtml: false,
    executeStatus: 'None',
    executeResultMessage: undefined,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    await this.prepareComponents()
  },

  async prepareComponents() {
    await this.updateContents()
  },

  async updateContents(additionalState?: Partial<State>) {
    const projectName = this.props.projectName
    const syncSchemaSetting = await api.syncSchema(projectName)
    const latestResult = await api.latestResult(projectName, 'schemaSyncCheck').then((body) => {
      if (body) {
        return {
          success: body.fileName.includes('success'),
          content: body.content,
        }
      }
    })
    const client = await api.clientPropbase(projectName)
    this.update({
      syncSchemaSetting,
      latestResult,
      hasSchemaSyncCheckResultHtml: client.hasSyncCheckResultHtml,
      ...additionalState,
    })
  },

  /**
   * SchemaSyncCheck の設定が確認可能か、つまり SchemaSyncCheckの設定が存在するかを確認する
   */
  canCheckSchemaSetting() {
    const setting = this.state.syncSchemaSetting
    return setting.url != null && setting.user != null
  },

  async onclickSchemaSyncCheckTask() {
    if (this.state.executeStatus !== 'None') {
      return
    }
    await this.updateContents({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
    await api
      .task(this.props.projectName, 'schemaSyncCheck')
      .then(async (data) => {
        const executeResultMessage = data.success ? 'Success' : 'Failure'
        await this.updateContents({ executeStatus: 'Completed', executeResultMessage })
      })
      .catch(async () => {
        // APIリクエストに失敗した際の情報も反映するため更新（一緒に実行モーダルは閉じる）
        await this.updateContents({ executeStatus: 'None' })
      })
  },
})
