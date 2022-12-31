import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'
import Prism from 'prismjs'
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
import { AlterDir, AlterFile, AlterZip } from './types'
import AlterCheckChecked from './alter-check-checked.riot'
import AlterCheckForm from './alter-check-form.riot'
import Raw from '../../../components/common/raw.riot'
import TaskExecuteModal from '../task-execute-modal.riot'
import LatestResult from '../latest-result.riot'
import { TaskExecuteStatus } from '../task-execute-modal'

type AlterLatestResultState = {
  title: string
  message?: string
  content?: string
}

interface Props {
  /** プロジェクト名 */
  projectName: string
}

interface State {
  /** AlterCheckの結果HTMLが存在するか */
  hasAlterCheckResultHtml: boolean
  /** Step1（AlterCheckの準備）で入力されたファイル名 */
  inputFileName?: string
  /** AlterCheck用SQLファイル */
  editingSqls: AlterFile[]
  /** チェック済みのAlterDDL zip */
  checkedZip: AlterZip
  /** 未リリースチェック済みのAlterDDL */
  unreleasedDir: AlterDir
  /** 最新のタスク実行結果 */
  latestResult?: AlterLatestResultState
  /** タスク実行ステータス */
  executeStatus: TaskExecuteStatus
  /** タスク実行結果メッセージ */
  executeResultMessage?: string
}

interface AlterCheck extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           UI Helper
  //                                                                           =========
  isEditing(): boolean
  nowPrepared(fileName: string): void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickAlterSql(alterFile: AlterFile): void
  onCompletePrepareAlterSql(inputFileName?: string): void
  onclickOpenAlterDir(): void
  onclickOpenAlterCheckResultHTML(): void
  onclickAlterCheckTask(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  updateContents(additionalState?: Partial<State>): void
  prepareUnreleased(unreleased: AlterSQLResultUnreleasedDirPart | undefined): AlterDir
  prepareChecked(checkedZip: AlterSQLResultCheckedZipPart | undefined, unreleasedDir: AlterDir): AlterZip
  prepareLatestFailureResult(ngMarkFile: AlterSQLResultNgMarkFilePart | undefined): Promise<AlterLatestResultState | undefined>
}

export default withIntroTypes<AlterCheck>({
  components: {
    AlterCheckChecked,
    AlterCheckForm,
    Raw,
    TaskExecuteModal,
    LatestResult,
  },
  state: {
    hasAlterCheckResultHtml: false,
    editingSqls: [],
    checkedZip: {
      fileName: '',
      checkedFiles: [],
    },
    unreleasedDir: {
      checkedFiles: [],
    },
    executeStatus: 'None',
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted() {
    this.updateContents()
  },

  // ===================================================================================
  //                                                                           UI Helper
  //                                                                           =========
  /**
   * 編集中のAlterDDLが存在するかチェックします
   * @return {boolean} true:存在する,false:存在しない
   */
  isEditing(): boolean {
    return this.state.editingSqls !== undefined && this.state.editingSqls.length > 0
  },
  /**
   * 画面から作成直後のファイルであるかチェックします
   * リロードするとこのチェックはfalseになります
   * @param fileName DDLファイル名
   * @return {boolean} true:作成直後である,false:作成直後でない
   */
  nowPrepared(fileName: string) {
    return this.state.inputFileName === fileName
  },
  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * AlterSqlの用意が完了した際に呼ばれます
   * 画面から作成直後のファイルであるかをチェックするためにファイル名を保存し、画面全体を更新します
   */
  onCompletePrepareAlterSql(inputFileName?: string) {
    this.updateContents({ inputFileName })
  },
  /**
   * AlterDDLの表示・非表示を切り替えます
   */
  onclickAlterSql(alterFile: AlterFile) {
    alterFile.show = !alterFile.show
    this.update()
  },
  /**
   * OSごとのファイルマネージャーでalterディレクトリを開きます
   */
  onclickOpenAlterDir() {
    api.openAlterDir(this.props.projectName)
  },
  /**
   * AlterCheckの実行結果htmlを表示します
   */
  onclickOpenAlterCheckResultHTML() {
    window.open('api/document/' + this.props.projectName + '/altercheckresulthtml/')
  },
  /**
   * AlterCheckをAPI経由で実行します
   * confirmを許可した場合のみ実行されます
   */
  onclickAlterCheckTask() {
    this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
      this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
      api
        .task(this.props.projectName, 'alterCheck')
        .then((data) => {
          const executeResultMessage = data.success ? 'Success' : 'Failure'
          this.updateContents({ executeStatus: 'Completed', executeResultMessage })
        })
        .catch(() => {
          // APIリクエストに失敗した際の情報も反映するため更新（一緒に実行モーダルは閉じる）
          this.updateContents({ executeStatus: 'None' })
        })
    })
  },
  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * 画面全体を更新します
   * @param additionalState 一緒に更新したいstate. 指定しなくてもOK
   */
  updateContents(additionalState?: Partial<State>) {
    api.alter(this.props.projectName).then((result) => {
      api.clientPropbase(this.props.projectName).then(async (client) => {
        const editingSqls = result.editingFiles.map((file) => ({
          fileName: file.fileName,
          content: Prism.highlight(file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        }))
        const unreleasedDir = this.prepareUnreleased(result.unreleasedDir)
        const checkedZip = this.prepareChecked(result.checkedZip, unreleasedDir)
        const latestResult = await this.prepareLatestFailureResult(result.ngMarkFile)
        this.update({
          hasAlterCheckResultHtml: client.hasAlterCheckResultHtml,
          editingSqls,
          unreleasedDir,
          checkedZip,
          latestResult,
          ...additionalState,
        })
      })
    })
  },
  /**
   * 未リリースチェック済みのAlterDDLを用意します（sqlファイルのみ）
   * sqlファイルはシンタックスハイライトされた状態でセットします
   * @param {AlterSQLResultUnreleasedDirPart | undefined} unreleasedDir APIで取得した未リリースディレクトリ情報 (Nullable)
   * @return {AlterDir} 未リリースAlterディレクトリのState情報
   */
  prepareUnreleased(unreleasedDir: AlterSQLResultUnreleasedDirPart | undefined): AlterDir {
    if (!unreleasedDir) {
      return { checkedFiles: [] }
    }
    return {
      checkedFiles: unreleasedDir.checkedFiles
        .filter((file) => file.fileName.includes('.sql'))
        .map((file) => ({
          fileName: file.fileName,
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })),
    }
  },
  /**
   * チェック済みのAlterDDL zipを用意します
   * sqlファイルはシンタックスハイライトされた状態でセットします
   * @param {AlterSQLResultCheckedZipPart | undefined} checkedZip APIで取得したチェック済みのAlterDDL zip情報 (Nullable)
   * @param {AlterDir} unreleasedDir 未リリースAlterディレクトリのState情報. zipから未リリースディレクトリでチェック済みのReadOnlyファイルを除外するために使用 (NotNull)
   * @return {AlterZip} 未リリースAlterディレクトリのState情報
   */
  prepareChecked(checkedZip: AlterSQLResultCheckedZipPart | undefined, unreleasedDir: AlterDir): AlterZip {
    if (!checkedZip) {
      return {
        fileName: '',
        checkedFiles: [],
      }
    }
    const excludeFileNames = unreleasedDir.checkedFiles.map((file) => file.fileName.replace('READONLY_', ''))
    return {
      fileName: checkedZip.fileName,
      checkedFiles: checkedZip.checkedFiles
        // for hybrid state 0.2.0, 0.2.1
        .filter((file) => !excludeFileNames.includes(file.fileName))
        .map((file) => ({
          fileName: file.fileName,
          // sqlファイル表示時に余白を用意するために改行(\n)を予め入れておく（多分そういう目的）
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })),
    }
  },
  /**
   * 最新の実行失敗結果を取得します
   * Step2（AlterCheck実行時）に最新の別のAlterCheckの成功結果を表示する必要がないため、現在実行中のAlterCheckの失敗結果を表示するようにしています
   * @param {AlterSQLResultNgMarkFilePart | undefined} ngMarkFile APIで取得したNgMarkFile情報 (Nullable)
   * @return {Promise<AlterLatestResultState>} 最新の実行失敗結果.最新が成功している場合はnull (Nullable)
   */
  async prepareLatestFailureResult(ngMarkFile: AlterSQLResultNgMarkFilePart | undefined): Promise<AlterLatestResultState | undefined> {
    return api.latestResult(this.props.projectName, 'alterCheck').then((res) => {
      const success = res.fileName.includes('success')
      if (success) {
        return
      }
      const content = res.content
      if (!ngMarkFile) {
        return {
          title: 'Result: Failure',
          content,
        }
      } else if (ngMarkFile.ngMark === 'previous-NG') {
        return {
          title: 'Found problems on Previous DDL.',
          message: 'Retry save previous.',
          content,
        }
      } else if (ngMarkFile.ngMark === 'alter-NG') {
        return {
          title: 'Found problems on Alter DDL.',
          message: ngMarkFile.content.split('\n')[0],
          content,
        }
      } else if (ngMarkFile.ngMark === 'next-NG') {
        return {
          title: 'Found problems on Next DDL.',
          message: 'Fix your DDL and data grammatically.',
          content,
        }
      }
    })
  },
})
