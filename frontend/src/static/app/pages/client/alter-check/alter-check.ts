import { IntroRiotComponent, withIntroTypes } from '../../../app-component-types'
import { api, DbfluteTask } from '../../../api/api'
import Prism from 'prismjs'
import 'prismjs/components/prism-sql.min'
import 'prismjs/themes/prism.css'
import { AlterFile } from './types'
import AlterCheckChecked from './alter-check-checked.riot'
import AlterCheckForm from './alter-check-form.riot'
import Raw from '../../../components/common/raw.riot'
import ResultModal from '../../../pages/client/result-modal.riot'
import LatestResult from '../latest-result.riot'

type ExecStatus = 'Nothing' | 'Executing' | 'Completed'

interface State {
  client: any
  inputFileName: string
  editingSqls: AlterFile[]
  checkedZip: {
    fileName: string
    checkedFiles: AlterFile[]
  }
  unreleasedDir: {
    checkedFiles: AlterFile[]
  }
  execStatus: ExecStatus
  resultMessage: string
}

interface Props {
  projectName: string
}

interface AlterCheck extends IntroRiotComponent<Props, State> {
  projectName: string

  // api response
  alter: any

  // view params
  ngMarkFile: undefined
  preparedFileName: string
  validated: boolean

  isEditing(): boolean

  prepareUnreleased(): void

  prepareChecked(): void

  prepareLatestResult(): void

  updateBegin(): void

  updateContents(): void

  alterItemClick(alterItem: AlterFile): void

  nowPrepared(fileName: string): void

  openAlterDir(): void

  openAlterCheckResultHTML(): void

  alterCheckTask(): void
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
    client: {},
    inputFileName: '',
    editingSqls: [],
    checkedZip: {
      fileName: '',
      checkedFiles: [],
    },
    unreleasedDir: {
      checkedFiles: [],
    },
    execStatus: 'Nothing',
    resultMessage: '',
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  projectName: '',

  // api response
  alter: {},

  // view params
  ngMarkFile: undefined,
  preparedFileName: '',
  validated: false,

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
   * 未リリースチェック済みのAlterDDLをタグにセットします（sqlファイルのみ）
   * sqlファイルはシンタックスハイライトされた状態でセットします
   */
  prepareUnreleased() {
    if (!this.alter.unreleasedDir) {
      return
    }
    this.state.unreleasedDir = {
      checkedFiles: [],
    }
    this.state.unreleasedDir.checkedFiles = []
    this.alter.unreleasedDir.checkedFiles.forEach((file: any) => {
      if (file.fileName.indexOf('.sql') === -1) {
        return
      }
      this.state.unreleasedDir.checkedFiles.push({
        fileName: file.fileName,
        content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
        show: false,
      })
    })
  },
  /**
   * チェック済みのAlterDDL zipをタグにセットします
   * sqlファイルはシンタックスハイライトされた状態でセットします
   */
  prepareChecked() {
    if (!this.alter.checkedZip) {
      return
    }
    const unreleasedFileNames = this.state.unreleasedDir.checkedFiles.map((checkedFile) => checkedFile.fileName.replace('READONLY_', ''))
    this.state.checkedZip = {
      fileName: this.alter.checkedZip.fileName,
      checkedFiles: [],
    }
    this.alter.checkedZip.checkedFiles.forEach((file: any) => {
      // for hybrid state 0.2.0, 0.2.1
      if (unreleasedFileNames.includes(file.fileName)) {
        return
      }
      this.state.checkedZip.checkedFiles.push({
        fileName: file.fileName,
        // sqlファイル表示時に余白を用意するために改行(\n)を予め入れておく（多分そういう目的）
        content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
        show: false,
      })
    })
  },
  prepareLatestResult() {
    // this.latestResult = riot.mount('latest-result', { projectName: this.opts.projectName, task: 'alterCheck' })[0]
    // if (!this.latestResult) {
    //   return
    // }
    // this.latestResult.latestResult.header.show = false
    // // NGマークファイルに合わせて失敗時のタイトルとメッセージを用意
    // if (this.ngMarkFile.ngMark ==='previous-NG') {
    //   this.latestResult.failure = {
    //     title: 'Found problems on Previous DDL.',
    //     message: 'Retry save previous.',
    //   }
    // } else if (this.ngMarkFile.ngMark ==='alter-NG') {
    //   this.latestResult.failure = {
    //     title: 'Found problems on Alter DDL.',
    //     message: this.ngMarkFile.content.split('\n')[0],
    //   }
    // } else if (this.ngMarkFile.ngMark ==='next-NG') {
    //   this.latestResult.failure = {
    //     title: 'Found problems on Next DDL.',
    //     message: 'Fix your DDL and data grammatically.',
    //   }
    // }
    // // 成功時のタイトルを用意
    // this.latestResult.success = {
    //   title: 'AlterCheck Successfully finished',
    // }
    // // latest-resultタグの更新処理を呼び出し、結果を反映する
    // this.latestResult.updateLatestResult()
  },
  updateBegin() {
    this.state.inputFileName = this.inputElementBy('[ref="altercheckform"] [ref="beginform"] [ref="alterNameInput"]').value
    this.updateContents()
  },
  updateContents() {
    api.alter(this.props.projectName).then((data) => {
      this.alter = data
      api.clientPropbase(this.props.projectName).then((data) => {
        this.state.client = data
        this.ngMarkFile = this.alter.ngMarkFile
        this.alter.editingFiles.forEach((file: any) => {
          this.state.editingSqls.push({
            fileName: file.fileName,
            content: Prism.highlight(file.content.trim(), Prism.languages.sql, 'sql'),
            show: false,
          })
        })
        this.prepareUnreleased()
        this.prepareChecked()
        this.prepareLatestResult()
        this.update()
      })
    })
  },
  /**
   * AlterDDLの表示・非表示を切り替えます
   */
  alterItemClick(alterItem: AlterFile) {
    alterItem.show = !alterItem.show
    this.update({
      editingSqls: this.state.editingSqls,
    })
    return false
  },
  /**
   * 画面から作成直後のファイルであるかチェックします
   * リロードするとこのチェックはfalseになります
   * @param fileName DDLファイル名
   * @return {boolean} true:作成直後である,false:作成直後でない
   */
  nowPrepared(fileName: string) {
    const inputFileName = this.state.inputFileName
    const alterFileName = 'alter-schema-' + inputFileName + '.sql'
    this.update()
    return alterFileName === fileName
  },
  /**
   * OSごとのファイルマネージャーでalterディレクトリを開きます
   */
  openAlterDir() {
    api.openAlterDir(this.props.projectName)
  },
  /**
   * AlterCheckの実行結果htmlを表示します
   */
  openAlterCheckResultHTML() {
    window.open('api/document/' + this.props.projectName + '/altercheckresulthtml/')
  },
  /**
   * AlterCheckをAPI経由で実行します
   * confirmを許可した場合のみ実行されます
   */
  alterCheckTask() {
    this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
      this.update({
        execStatus: 'Executing',
      })
      DbfluteTask.task('alterCheck', this.props.projectName, (message: string) => {
        this.update({
          execStatus: 'Completed',
          resultMessage: message,
        })
      }).finally(() => {
        // 失敗した際の情報も反映するため、APIの実行結果に問わずタグの各値を更新
        // this.updateContents()
        this.update({
          execStatus: 'Nothing',
        })
      })
    })
  },
})
