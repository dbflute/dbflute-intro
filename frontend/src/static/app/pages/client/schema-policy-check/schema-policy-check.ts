import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'
import LatestResult from '../latest-result.riot'
import TaskExecuteModal from '../task-execute-modal.riot'
import { TaskExecuteStatus } from '../task-execute-modal'
import SchemaPolicyCheckStatementList from './schema-policy-check-statement-list.riot'
import SchemaPolicyCheckStatementFormWrapper from './schema-policy-check-statement-form-wrapper.riot'

/**
 * SchemaPolicyの3つのマップ種別。
 * dfpropのwholeMap/tableMap/columnMapに対応する。
 */
type MapType = 'wholeMap' | 'tableMap' | 'columnMap'

/**
 * SchemaPolicyCheck (= doc task) の最新実行結果。
 */
type SchemaPolicyLatestResult = {
  /** 実行が成功したかどうか (= violation がない) */
  success: boolean
  /** 実行結果のログ内容 */
  content: string
}

interface Props {
  /** 現在対象としているDBFluteクライアントのプロジェクト名 e.g. maihamadb */
  projectName: string
}

interface State {
  /** SchemaPolicyの設定情報 (undefined: 初期化前) */
  schemaPolicy?: DfpropSchemapolicyResult

  /** SchemaPolicyCheckの最新実行結果 (undefined: 実行履歴なし) */
  latestResult?: SchemaPolicyLatestResult

  /** SchemaPolicy違反があるかどうか (clientPropbase.violatesSchemaPolicy) */
  violatesSchemaPolicy: boolean

  /** タスク実行ステータス */
  executeStatus: TaskExecuteStatus

  /** タスク実行結果メッセージ (EmptyAllowed: 実行前) */
  executeResultMessage?: string
}

interface SchemaPolicyCheck extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted(): Promise<void>

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * SchemaPolicyCheck (doc task) を実行する。
   */
  onclickSchemaPolicyCheckTask(): Promise<void>

  /**
   * 実行モーダルが閉じられた時の処理。実行ステータスをリセットする。
   */
  onExecuteModalHide(): void

  /**
   * 違反時の結果リンクで SchemaPolicyCheck の結果HTMLを開く。
   */
  openSchemaPolicyHTML(): void

  /**
   * テーマのチェックボックスが切り替わった時の処理。
   * 該当テーマの isActive をトグルしてサーバーに送信し、stateも更新する。
   * @param mapType 対象マップ種別
   * @param typeCode テーマを一意に特定するコード e.g. identityIfPureIDPK
   */
  onToggleTheme(mapType: MapType, typeCode: string): Promise<void>

  /**
   * 違反時に表示するリンクのタイトルを返す。
   * 違反がなければ undefined。
   */
  failureLinkTitle(): string | undefined

  /**
   * 違反時に表示するリンクのクリックハンドラを返す。
   * 違反がなければ undefined。
   */
  failureOnClickLink(): (() => void) | undefined

  /**
   * SchemaPolicyの設定情報と最新実行結果を取得して画面に反映する。
   * 子コンポーネント (statement-list 等) からの onChanged コールバックでも呼ばれる。
   */
  loadSchemaPolicy(): Promise<void>
}

export default withIntroTypes<SchemaPolicyCheck>({
  components: {
    LatestResult,
    TaskExecuteModal,
    SchemaPolicyCheckStatementList,
    SchemaPolicyCheckStatementFormWrapper,
  },

  state: {
    schemaPolicy: undefined,
    latestResult: undefined,
    violatesSchemaPolicy: false,
    executeStatus: 'None',
    executeResultMessage: undefined,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    await this.loadSchemaPolicy()
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  async onclickSchemaPolicyCheckTask() {
    if (this.state.executeStatus !== 'None') {
      return
    }
    this.update({ executeStatus: 'Executing', executeResultMessage: 'Checking...' })
    try {
      const data = await api.executeTask(this.props.projectName, 'doc')
      // doc task 自体の成否はモーダルメッセージに反映するが、SchemaPolicy違反の最終判定は
      // この後の loadSchemaPolicy() で取得する clientPropbase.violatesSchemaPolicy で行う
      const message = data.success ? 'Success!!' : 'Failure: You need to check violation.'
      this.update({ executeStatus: 'Completed', executeResultMessage: message })
    } catch (e) {
      console.error('Failed SchemaPolicyCheck:', e)
      this.update({ executeStatus: 'None' })
    }
    await this.loadSchemaPolicy()
  },

  onExecuteModalHide() {
    this.update({ executeStatus: 'None' })
  },

  openSchemaPolicyHTML() {
    window.open(`/api/document/${this.props.projectName}/schemahtml/`)
  },

  async onToggleTheme(mapType, typeCode) {
    const schemaPolicy = this.state.schemaPolicy
    if (!schemaPolicy) {
      return
    }
    const targetTheme = schemaPolicy[mapType].themeList.find((theme) => theme.typeCode === typeCode)
    if (!targetTheme) {
      return
    }
    const toggledIsActive = !targetTheme.isActive
    const editBody: DfpropSchemapolicyEditBody = {
      wholeMap: { themeList: [] },
      tableMap: { themeList: [] },
      columnMap: { themeList: [] },
    }
    editBody[mapType] = { themeList: [{ typeCode, isActive: toggledIsActive }] }

    await api.editSchemaPolicyDfprop(this.props.projectName, editBody)
    // state.schemaPolicy を mutate せず immutable に新オブジェクトで置き換える
    const updatedMap = {
      ...schemaPolicy[mapType],
      themeList: schemaPolicy[mapType].themeList.map((theme) =>
        theme.typeCode === typeCode ? { ...theme, isActive: toggledIsActive } : theme,
      ),
    }
    this.update({
      schemaPolicy: { ...schemaPolicy, [mapType]: updatedMap },
    })
  },

  failureLinkTitle() {
    return this.state.violatesSchemaPolicy ? 'Open your SchemaPolicyCheck result (HTML)' : undefined
  },

  failureOnClickLink() {
    return this.state.violatesSchemaPolicy ? this.openSchemaPolicyHTML : undefined
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  async loadSchemaPolicy() {
    const projectName = this.props.projectName
    const [schemaPolicy, client, latestResultData] = await Promise.all([
      api.findSchemaPolicyDfprop(projectName),
      api.findClientPropbase(projectName),
      api.findLatestTaskLog(projectName, 'doc'),
    ])
    // doc task の成否はログファイル名 (success/failure) で判定する。
    // violatesSchemaPolicy は (success/failure とは別の信号として) 違反時に
    // SchemaPolicy 結果HTMLへのリンクを出すかどうかにだけ使う。
    const violatesSchemaPolicy = !!client.violatesSchemaPolicy
    const latestResult: SchemaPolicyLatestResult | undefined = latestResultData
      ? { success: latestResultData.fileName.includes('success'), content: latestResultData.content }
      : undefined
    this.update({ schemaPolicy, latestResult, violatesSchemaPolicy })
  },
})
