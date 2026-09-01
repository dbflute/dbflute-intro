import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'

// SQLファイルの表示ハイライトのため
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

/**
 * 最新のタスク実行結果
 */
type AlterLatestResultState = {
  title: string
  message?: string
  content?: string
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Props {
  /** DBFluteクライアントプロジェクト名 e.g. maihamadb */
  projectName: string
}

interface State {
  /** AlterCheckの結果HTMLが存在するか */
  hasAlterCheckResultHtml: boolean

  /** Step1（AlterCheckの準備）で入力されたファイル名 e.g. alter-schema-SEA.sql */
  inputFileName?: string

  /** AlterCheck用SQLファイル */
  editingSqls: AlterFile[]

  /** 未リリースチェック済みのAlterDDLのディレクトリ (unreleased-checked-alter) */
  unreleasedDir: AlterDir

  /** チェック済み(未リリース)のAlterDDL zip (checked-alter-to-...zip) */
  checkedZip: AlterZip

  /** 最新のタスク実行結果 */
  latestResult?: AlterLatestResultState

  /** タスク実行ステータス */
  executeStatus: TaskExecuteStatus

  /** タスク実行結果メッセージ */
  executeResultMessage?: string
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface AlterCheck extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  onMounted(): void

  // ===================================================================================
  //                                                                           UI Helper
  //                                                                           =========
  /**
   * 編集中のAlterDDLが存在するか？
   * @return true:存在する,false:存在しない
   */
  isEditing(): boolean

  /**
   * 作成直後のファイルであるか？
   * リロードするとこのチェックはfalseになります
   * @param fileName - DDLファイル名
   * @return true:作成直後である,false:作成直後でない
   */
  nowPrepared(fileName: string): boolean

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * AlterDDLの表示・非表示を切り替えます
   * @param alterFile - クリックされたAlterDDLのファイル
   */
  onclickAlterSql(alterFile: AlterFile): void

  /**
   * AlterSqlの用意が完了した際に呼ばれます
   * 画面から作成直後のファイルであるかをチェックするためにファイル名を保存し、画面全体を更新します
   * @param inputFileName - クリックされたAlterDDLのファイル
   */
  onCompletePrepareAlterSql(inputFileName?: string): void

  /**
   * OSごとのファイルマネージャーでalterディレクトリを開きます
   */
  onclickOpenAlterDir(): void

  /**
   * AlterCheckの実行結果htmlを表示します
   */
  onclickOpenAlterCheckResultHTML(): void

  /**
   * AlterCheckをAPI経由で実行します
   * confirmを許可した場合のみ実行されます
   */
  onclickAlterCheckTask(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  // #thiking jflute Partialにしている理由を明文化したい (2026/09/01)
  /**
   * 画面全体を更新します
   * @param additionalState - 一緒に更新したいstate. 指定しなくてもOK
   */
  updateContents(additionalState?: Partial<State>): void

  /**
   * 未リリースチェック済みのAlterDDLディレクトリの情報を用意します（sqlファイルのみ）。
   * DBFlute-1.2.1 からの unreleasedDir 方式のための対応。
   * sqlファイルはシンタックスハイライトされた状態でセットします
   * @param unreleasedDir - APIで取得した未リリースディレクトリ情報
   * @return 未リリースAlterディレクトリのState情報 (EmptyAllowed: チェック済みファイルがまだない場合)
   */
  prepareUnreleasedDir(unreleased: PlaysqlMigrationAlterResult_UnreleasedDirPart | undefined): AlterDir

  /**
   * チェック済みのAlterDDL zip (未リリース) の情報を用意します。
   * DBFlute-1.2.0 までの checkedZip 方式のための対応。
   * sqlファイルはシンタックスハイライトされた状態でセットします。
   * @param checkedZip - APIで取得したチェック済みのAlterDDL zip情報
   * @param unreleasedDir - 未リリースAlterディレクトリのState情報. zipから未リリースディレクトリでチェック済みのReadOnlyファイルを除外するために使用 (NotNull)
   * @return 未リリースチェック済みのAlterDDL zipファイル情報 (EmptyAllowed: チェック済みzipがなければ)
   */
  prepareCheckedZip(checkedZip: PlaysqlMigrationAlterResult_CheckedZipPart | undefined, unreleasedDir: AlterDir): AlterZip

  /**
   * 最新の実行失敗結果を取得します
   * Step2（AlterCheck実行時）に最新の別のAlterCheckの成功結果を表示する必要がないため、現在実行中のAlterCheckの失敗結果を表示するようにしています
   * @param ngMarkFile - APIで取得したNgMarkFile情報 (Nullable)
   * @return 最新の実行失敗結果.最新が成功している場合はnull (Nullable)
   */
  prepareLatestFailureResult(
    ngMarkFile: PlaysqlMigrationAlterResult_NgMarkFilePart | undefined,
  ): Promise<AlterLatestResultState | undefined>
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

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
  isEditing(): boolean {
    return this.state.editingSqls !== undefined && this.state.editingSqls.length > 0
  },

  nowPrepared(fileName: string): boolean {
    return this.state.inputFileName === fileName
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickAlterSql(alterFile: AlterFile) {
    alterFile.show = !alterFile.show
    this.update()
  },

  onCompletePrepareAlterSql(inputFileName?: string) {
    this.updateContents({ inputFileName })
  },

  onclickOpenAlterDir() {
    api.openAlterDir(this.props.projectName)
  },

  onclickOpenAlterCheckResultHTML() {
    window.open('api/document/' + this.props.projectName + '/altercheckresulthtml/')
  },

  onclickAlterCheckTask() {
    this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
      this.update({ executeStatus: 'Executing', executeResultMessage: 'Executing...' })
      api
        .executeTask(this.props.projectName, 'alterCheck')
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
  updateContents(additionalState?: Partial<State>) {
    api.findAlterInfra(this.props.projectName).then((result) => {
      api.findClientPropbase(this.props.projectName).then(async (client) => {
        const editingSqls = result.editingFiles.map((file) => ({
          fileName: file.fileName,
          content: Prism.highlight(file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        }))
        const unreleasedDir = this.prepareUnreleasedDir(result.unreleasedDir)
        const checkedZip = this.prepareCheckedZip(result.checkedZip, unreleasedDir)
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

  prepareUnreleasedDir(unreleasedDir: PlaysqlMigrationAlterResult_UnreleasedDirPart | undefined): AlterDir {
    if (!unreleasedDir) {
      return { checkedFiles: [] }
    }
    return {
      checkedFiles: unreleasedDir.checkedFiles
        .filter((file) => file.fileName.includes('.sql'))
        .map((file) => ({
          fileName: file.fileName,
          // sqlファイル表示時に余白を用意するために改行(\n)を予め入れておく（多分そういう目的）
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })),
    }
  },

  prepareCheckedZip(checkedZip: PlaysqlMigrationAlterResult_CheckedZipPart | undefined, unreleasedDir: AlterDir): AlterZip {
    if (!checkedZip) {
      return {
        fileName: '',
        checkedFiles: [],
      }
    }

    // unreleasedDir のファイルと同名のものが checkedZip の中に入ってたら除外する。
    // 通常そういうことはありえないはず!? そもそも unreleasedDir と checkedZip は同時に発生しないはず。
    // checkedZip は DBFlute-1.2.0 までの機能で、DBFlute-1.2.1 から unreleasedDir に移行される。
    // 一瞬、マージとかで同居することはあるかもしれないが、自動で unreleasedDir に移行される。
    // そのときの何かの紛れで、同名ファイルが両方に入っちゃった時のための回避処理という感じかな!? by jflute (2026/09/01)
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

  async prepareLatestFailureResult(
    ngMarkFile: PlaysqlMigrationAlterResult_NgMarkFilePart | undefined,
  ): Promise<AlterLatestResultState | undefined> {
    return api.findLatestTaskLog(this.props.projectName, 'alterCheck').then((body) => {
      if (!body || body.fileName.includes('success')) {
        return
      }
      const content = body.content
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
