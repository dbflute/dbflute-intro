import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { TaskExecuteStatus } from '../task-execute-modal'
import { api } from '../../../api/api'
// playsql配下のSQLのハイライトのため
import Prism from 'prismjs'
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
import LatestResult from '../latest-result.riot'
import TaskExecuteModal from '../task-execute-modal.riot'

type PlaysqlDropdownItem = {
  label: string
  value?: string
}

type ReplaceSchemaLatestResultState = {
  success: boolean
  content: string
}

interface Props {
  /** プロジェクト名 */
  projectName: string
}

interface State {
  /**  */
  playsqlDropdownItems: PlaysqlDropdownItem[]
  /** タスク実行ステータス */
  executeStatus: TaskExecuteStatus
  /** タスク実行結果メッセージ */
  executeResultMessage?: string
  /** 最新のタスク実行結果 */
  latestResult?: ReplaceSchemaLatestResultState
}

interface ReplaceSchema extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMount(): void

  // ===================================================================================
  //                                                                           UI Helper
  //                                                                           =========
  replaceSchemaTask(): void
  openDataDir(): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
}

export default withIntroTypes<ReplaceSchema>({
  components: {
    TaskExecuteModal,
    LatestResult,
  },
  state: {
    playsqlDropdownItems: [],
    executeStatus: 'None',
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMount() {
    const playsqlDropdownItems = await api.playsqlBeanList(this.props.projectName).then((data) => {
      return [
        ...data.map((result) => ({
          label: result.fileName,
          value:
            `<span style="display: none;">${result.fileName}</span>` + Prism.highlight(result.content ?? '', Prism.languages.sql, 'sql'),
        })),
        { label: '-' },
      ]
    })
    this.update({
      playsqlDropdownItems,
    })
  },

  // ===================================================================================
  //                                                                           UI Helper
  //                                                                           =========
  replaceSchemaTask(): void {
    this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
      this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
      api.task(this.props.projectName, 'replaceSchema').then(async (data) => {
        const executeResultMessage = data.success ? 'Success' : 'Failure'
        const content = (await api.latestResult(this.props.projectName, 'replaceSchema').then((data) => data?.content)) ?? ''
        this.update({
          executeStatus: 'Completed',
          executeResultMessage,
          latestResult: {
            success: data.success,
            content,
          },
        })
      })
    })
  },
  async openDataDir() {
    await api.openDataDir(this.props.projectName)
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
})
