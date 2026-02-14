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

/**
 * PlaySQLのドロップダウン項目
 */
type PlaysqlDropdownItem = {
  /** ドロップダウンに表示されるラベル */
  label: string
  /** SQLファイルの内容（シンタックスハイライト済み） (EmptyAllowed: デフォルト項目の場合) */
  value?: string
}

/**
 * ReplaceSchemaの最新実行結果の状態
 */
type ReplaceSchemaLatestResultState = {
  /** 実行が成功したかどうか */
  success: boolean
  /** 実行結果のログ内容 */
  content: string
}

/**
 * ReplaceSchemaコンポーネントのProps
 */
interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 */
  projectName: string
}

/**
 * ReplaceSchemaコンポーネントのState
 */
interface State {
  /** DBFluteの設定情報 (undefined: 初期化前) */
  settings?: DfpropSettingsResult

  /** PlaySQLのドロップダウン項目たち (NotEmpty: デフォルト項目が含まれる) */
  playsqlDropDownItems: PlaysqlDropdownItem[]

  /** ドロップダウンで選択されたSQLファイルの内容（シンタックスハイライト済み） */
  selectedSql: string

  /** タスク実行ステータス */
  executeStatus: TaskExecuteStatus

  /** タスク実行結果メッセージ (EmptyAllowed: 実行前) */
  executeResultMessage?: string

  /** ReplaceSchemaの最新実行結果 (undefined: 実行履歴なし) */
  latestResult?: ReplaceSchemaLatestResultState
}

interface ReplaceSchema extends IntroRiotComponent<Props, State> {
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
   * OSごとのファイルマネージャーでTestDataディレクトリを開く。
   */
  onclickOpenDataDir(): void

  /**
   * ReplaceSchemaタスクをAPI経由で実行する。
   * confirmを許可した場合のみ実行される。
   */
  onclickReplaceSchemaTask(): void

  /**
   * su-dropdownの変更イベントから値を取得してstateを更新する。
   * @param event - この関数を呼び出したイベントのオブジェクト
   */
  onDropdownChange(event: any): void

  /**
   * タスク実行モーダルが閉じられた時の処理。
   * 実行ステータスをリセットする。
   */
  onModalHide(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * DBFluteの設定情報を取得してstateを更新する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名
   */
  prepareSettings(projectName: string): Promise<void>

  /**
   * PlaySQLファイルの一覧を取得してドロップダウン項目を準備する。
   * SQLファイルはシンタックスハイライトされた状態でセットする。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名
   */
  preparePlaysql(projectName: string): void

  /**
   * ReplaceSchemaの最新実行結果を取得してstateを更新する。
   * @param projectName - 現在対象としているDBFluteクライアントのプロジェクト名
   */
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
