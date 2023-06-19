import i18n from '../../components/common/i18n.riot'
import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import { appRoutes } from '../../app-router'
import { api } from '../../api/api'

interface Props {}
interface State {
  // Implementation-Versionなど、System Infoに載せるオブジェクト
  manifest: Array<Array<object>>
  // DBFluteエンジンのバージョン一覧 (実質的にDBFluteエンジンの一覧と考えて良い)
  versions: string[]
  // DBFluteクライアントの一覧
  clientList: ClientListResult[]
  // DBFluteの最新バージョンオブジェクト e.g. latestReleaseVersion
  latestVersion: EngineLatestResult
  // downloadModal を見せるかどうか
  showDownloadModal: boolean
  // processModal を見せるかどうか
  showProcessModal: boolean
}

type DownloadModalBase = {
  header: string
  closable: boolean
  buttons: Array<{
    text: string
    action: string
  }>
}
type DownloadModal = DownloadModalBase

type ProcessModalBase = {
  closable: boolean
}
type ProcessModal = ProcessModalBase

interface Main extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  downloadModalBase: DownloadModalBase
  processModalBase: ProcessModalBase

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickDownload: () => void
  onDownloadEngine: () => void
  onclickRemove: (version: string) => void
  goToDocumentsPage: (client: ClientListResult) => void
  goToClientCreate: () => void
  onDownloadModalHide: () => void // あなた、Event Handler では？
  onProcessModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  prepareClientList: () => void
  introManifest: () => void
  engineVersions: () => Promise<void>
  latestVersion: () => Promise<void>
  downloadModal: () => DownloadModal
  processModal: () => ProcessModal
}

export default withIntroTypes<Main>({
  components: {
    i18n,
  },
  state: {
    manifest: undefined,
    versions: [],
    clientList: [],
    latestVersion: {
      latestReleaseVersion: '',
      latestSnapshotVersion: '',
    },
    showDownloadModal: false,
    showProcessModal: false,
  },

  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  /**
   * DBFlute Engine のダウンロードに必要なUIを提供する Modal
   */
  downloadModalBase: {
    header: 'DBFlute Engine Download',
    closable: true,
    buttons: [
      {
        text: 'Download',
        action: 'download-engine',
      },
    ],
  },

  /**
   * DBFlute Engine のダウンロードを実施しているときに表示する Modal
   * ダウンロード中は、ユーザがその他の操作をできないように制御する
   */
  processModalBase: {
    closable: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    await Promise.all([this.introManifest(), this.engineVersions(), this.latestVersion(), this.prepareClientList()])
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * Downloadボタン押下時の処理
   * - DBFluteエンジンをダウンロードするためのモーダルを表示する。
   */
  onclickDownload() {
    this.state.showDownloadModal = true
    this.update()
  },

  /**
   * DBFluteエンジンのダウンロード処理を行う。
   */
  onDownloadEngine() {
    this.state.showProcessModal = true
    this.update()
    const version = this.state.latestVersion.latestReleaseVersion
    api.downloadEngine({ version }).then(() => {
      this.state.showProcessModal = false
      this.engineVersions()
    })
  },

  /**
   * Removeボタン押下時の処理
   * - 引数で指定されたDBFluteエンジンを削除する。
   * @param {string} version - 削除するDBFluteエンジンのバージョン (NotNull)
   */
  onclickRemove(version: string) {
    api.removeEngine({ version }).finally(() => {
      this.engineVersions()
    })
  },

  /**
   * Document画面に遷移する。
   * @param {ClientListResult} client - 遷移するDBFluteクライアントのオブジェクト (NotNull)
   */
  goToDocumentsPage(client: ClientListResult) {
    appRoutes.client.open(client.projectName, 'execute', 'documents')
  },

  /**
   * DBFluteクライアント作成画面へ遷移する。
   */
  goToClientCreate() {
    appRoutes.create.open()
  },

  /**
   * ユーザがDBFluteエンジンをダウンロードするために表示する Modal を非表示にする
   */
  onDownloadModalHide() {
    this.state.showDownloadModal = false
    this.update()
  },

  /**
   * DBFlute Intro がバックグランドでプロセスを実行中に
   * ユーザが他の操作を抑制するための Modal を非表示にする
   */
  onProcessModalHide() {
    this.state.showProcessModal = false
    this.update()
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * DBFluteクライアントの一覧情報を準備する。
   * なければWelcome画面に遷移させる処理もここに入っている。
   */
  async prepareClientList() {
    api.clientList().then((json) => this.update({ clientList: json }))
  },

  // #thiking jflute 関数名、動詞省略するかしないか？tagファイルの関数ではしっかり統一したい (2022/04/23)
  /**
   * DBFlute IntroのManifestファイルの情報を反映する。
   */
  async introManifest() {
    api.manifest().then((json) => {
      this.update({
        manifest: [
          ['Implementation-Version', json['Implementation-Version']],
          ['Build-Timestamp', json['Build-Timestamp']],
        ],
      })
    })
  },

  /**
   * 既存のDBFluteエンジンのバージョン一覧を反映する。
   * 実質、これが画面上におけるDBFluteエンジンの一覧の元ネタと考えて良い。
   * (DBFluteエンジンは、バージョンごとにユニークになるので)
   */
  async engineVersions() {
    await api.engineVersions().then((json) => {
      this.update({ versions: json })
    })
  },

  /**
   * DBFluteの最新バージョン情報をダウンロードモーダルに反映する。
   */
  async latestVersion() {
    await api.findEngineLatest().then((json) => {
      this.update({ latestVersion: json })
    })
  },

  /**
   * ユーザがDBFluteエンジンをダウンロードするために表示する Modal を返す
   * @returns {DownloadModal} ダウンロード時に表示する Modal
   */
  downloadModal(): DownloadModal {
    return this.downloadModalBase
  },

  /**
   * DBFlute Intro がバックグランドでプロセスを実行中に
   * ユーザが他の操作を抑制するための Modal を返す
   * @returns {ProcessModal} 処理中に表示する Modal
   */
  processModal(): ProcessModalBase {
    return this.processModalBase
  },
})
