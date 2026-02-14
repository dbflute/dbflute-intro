import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { TaskExecuteStatus } from '../task-execute-modal'
import Raw from '../../../components/common/raw.riot'

import Prism from 'prismjs'
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
import LatestResult from '../latest-result.riot'
import TaskExecuteModal from '../task-execute-modal.riot'
import { api } from '../../../api/api'
import ReplaceSchema from './replace-schema'

type PlaysqlDropdownItem = {
  label: string
  value?: string // value =string | undifined と同じ。stringは任意という意味
}

type ReplaceSchemaLatestResultState = {
  success: boolean
  content: string
}

interface Props {
  projectName: string
}

interface State {
  settings?: DfpropSettingsResult
  playsqlDropDownItems: PlaysqlDropdownItem[]
  selectedSql: string
  executeStatus: TaskExecuteStatus
  executeResultMessage?: string
  latestResult?: ReplaceSchemaLatestResultState
}

interface ReplaceSchema extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onMounted(): void

  onclickOpenDataDir(): void

  onclickReplaceSchemaTask(): void

  onDropdownChange(event: any): void

  onModalHide(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareSettings(projectName: string): Promise<void>

  preparePlaysql(projectName: string): void

  prepareComponents(projectName: string): void
}

export default withIntroTypes<ReplaceSchema>({
  components: {
    LatestResult,
    TaskExecuteModal,
    Raw,
  },

  state: {
    settings: undefined,
    playsqlDropDownItems: [{ label: '-', value: undefined }],
    selectedSql: '',
    executeStatus: 'None',
  },
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void {
    this.prepareSettings(this.props.projectName)
    this.preparePlaysql(this.props.projectName)
    this.prepareComponents(this.props.projectName)
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickOpenDataDir(): void {
    api.openDataDir(this.props.projectName)
  },

  onclickReplaceSchemaTask(): void {
    this.suConfirm('Are you sure to execute Replace Schema task?').then(async () => {
      this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
      try {
        const data = await api.task(this.props.projectName, 'replaceSchema')
        const message = data.success ? 'Success' : 'Failure'
        this.update({ executeStatus: 'Completed', executeResultMessage: message })
      } catch (e) {
        this.update({ executeStatus: 'None' })
      }
    })
  },

  /**
   * su-dropdownの変更イベントから値を取得してstateを更新
   */
  onDropdownChange(event: any): void {
    this.update({
      selectedSql: event?.value || '',
    })
  },

  onModalHide(): void {
    this.update({ executeStatus: 'None' })
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  async prepareSettings(projectName: string): Promise<void> {
    const settings = await api.settings(projectName)
    this.update({
      settings: settings,
    })
  },

  async preparePlaysql(projectName: string): Promise<void> {
    const playsqlListResults = await api.playsqlBeanList(projectName)
    const playsqlDropDownItems = playsqlListResults.map((obj) => ({
      label: obj.fileName,
      value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content || '', Prism.languages.sql, 'sql'),
    }))
    this.update({
      playsqlDropDownItems: this.state.playsqlDropDownItems.concat(playsqlDropDownItems),
    })
  },

  async prepareComponents(projectName: string): Promise<void> {
    const body = await api.latestResult(projectName, 'replaceSchema')
    if (!body) {
      return
    }
    const latestResult = {
      success: body.fileName.includes('success'),
      content: body.content,
    }
    this.update({ latestResult: latestResult })
  },
})
