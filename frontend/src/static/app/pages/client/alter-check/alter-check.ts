import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api } from '../../../api/api'
import Prism from 'prismjs'
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
import { AlterFileState, AlterZipState } from './types'
import AlterCheckChecked from './alter-check-checked.riot'
import AlterCheckForm from './alter-check-form.riot'
import Raw from '../../../components/common/raw.riot'
import ResultModal from '../../../pages/client/result-modal.riot'
import LatestResult from '../latest-result.riot'

type AlterDirState = {
  checkedFiles: AlterFileState[]
}

type AlterLatestResultState = {
  title: string
  message?: string
  content?: string
}

type AlterModalState = {
  status: 'None' | 'Executing' | 'Completed'
  message?: string
}

interface State {
  hasAlterCheckResultHtml: boolean
  inputFileName?: string
  editingSqls: AlterFileState[]
  checkedZip: AlterZipState
  unreleasedDir: AlterDirState
  latestResult?: AlterLatestResultState
  modal: AlterModalState
}

interface Props {
  projectName: string
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
  onclickAlterSql(alterFile: AlterFileState): void
  onCompletePrepareAlterSql(inputFileName?: string): void
  onclickOpenAlterDir(): void
  onclickOpenAlterCheckResultHTML(): void
  onclickAlterCheckTask(): void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  updateContents(additionalState?: Partial<State>): void
  prepareUnreleased(unreleased: AlterSQLResultUnreleasedDirPart | undefined): AlterDirState
  prepareChecked(checkedZip: AlterSQLResultCheckedZipPart | undefined, unreleasedDir: AlterDirState): AlterZipState
  prepareLatestResult(ngMarkFile: AlterSQLResultNgMarkFilePart | undefined): Promise<AlterLatestResultState>
}

export default withIntroTypes<AlterCheck>({
  components: {
    AlterCheckChecked,
    AlterCheckForm,
    Raw,
    ResultModal,
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
    modal: {
      status: 'None',
    },
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
  onclickAlterSql(alterFile: AlterFileState) {
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
      this.update({ modal: { status: 'Executing' } })
      api
        .task(this.props.projectName, 'alterCheck')
        .then((data) => {
          const message = data.success ? 'success' : 'failure'
          this.update({ modal: { status: 'Completed', message } })
        })
        .finally(() => {
          // 失敗した際の情報も反映するため、APIの実行結果に問わずタグの各値を更新
          this.updateContents({ modal: { status: 'None' } })
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
        const latestResult = await this.prepareLatestResult(result.ngMarkFile)
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
   * @param unreleasedDir APIで取得した未リリースディレクトリ情報 (Nullable)
   * @return {AlterDirState} 未リリースAlterディレクトリのState情報
   */
  prepareUnreleased(unreleasedDir: AlterSQLResultUnreleasedDirPart | undefined): AlterDirState {
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
   * @param checkedZip APIで取得したチェック済みのAlterDDL zip情報 (Nullable)
   * @param unreleasedDir 未リリースAlterディレクトリのState情報. zipから未リリースディレクトリでチェック済みのReadOnlyファイルを除外するために使用 (NotNull)
   * @return {AlterDirState} 未リリースAlterディレクトリのState情報
   */
  prepareChecked(checkedZip: AlterSQLResultCheckedZipPart | undefined, unreleasedDir: AlterDirState): AlterZipState {
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
   * 最新の実行結果を取得します
   * @param ngMarkFile APIで取得したNgMarkFile情報 (Nullable)
   */
  async prepareLatestResult(ngMarkFile: AlterSQLResultNgMarkFilePart | undefined): Promise<AlterLatestResultState> {
    return api.latestResult(this.props.projectName, 'alterCheck').then((res) => {
      const success = res.fileName.includes('success')
      const content = res.content
      if (success) {
        return {
          title: 'AlterCheck Successfully finished',
          content,
        }
      } else if (!ngMarkFile) {
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
