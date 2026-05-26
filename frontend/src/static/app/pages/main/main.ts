import i18n from '../../components/common/i18n.riot'
import { api } from '../../api/api'
import { appRoutes } from '../../app-router'
import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface State {
  /** Implementation-Versionなど、System Infoに載せるオブジェクト */
  manifest: Array<Array<object>>

  /** DBFluteエンジンのバージョン一覧 (実質的にDBFluteエンジンの一覧と考えて良い) */
  versions: string[]

  /** DBFluteクライアントの一覧 */
  clientList: ClientListResult[]

  /** DBFluteの最新バージョンオブジェクト e.g. latestReleaseVersion */
  latestVersion: EngineLatestResult

  /** downloadModal を見せるかどうか */
  showDownloadModal: boolean

  /** processModal を見せるかどうか */
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

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

interface Main extends IntroRiotComponent<never, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
  /**
   * DBFlute Engine のダウンロードに必要なUIを提供する Modal
   */
  downloadModalBase: DownloadModalBase

  /**
   * DBFlute Engine のダウンロードを実施しているときに表示する Modal
   * ダウンロード中は、ユーザがその他の操作をできないように制御する
   */
  processModalBase: ProcessModalBase

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  onMounted: () => void

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  /**
   * Downloadボタン押下時の処理
   * - DBFluteエンジンをダウンロードするためのモーダルを表示する。
   */
  onclickDownload: () => void

  /**
   * DBFluteエンジンのダウンロード処理を行う。
   */
  onDownloadEngine: () => void

  /**
   * Removeボタン押下時の処理
   * - 引数で指定されたDBFluteエンジンを削除する。
   * @param version - 削除するDBFluteエンジンのバージョン
   */
  onclickRemove: (version: string) => void

  /**
   * Document画面に遷移する。
   * @param client - 遷移するDBFluteクライアントのオブジェクト
   */
  goToDocumentsPage: (client: ClientListResult) => void

  /**
   * DBFluteクライアント作成画面へ遷移する。
   */
  goToClientCreate: () => void

  /**
   * ユーザがDBFluteエンジンをダウンロードするために表示する Modal を非表示にする
   */
  onDownloadModalHide: () => void

  /**
   * DBFlute Intro がバックグランドでプロセスを実行中に
   * ユーザが他の操作を抑制するための Modal を非表示にする
   */
  onProcessModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  /**
   * DBFluteクライアントの一覧情報を準備する。
   * なければWelcome画面に遷移させる処理もここに入っている。
   */
  prepareClientList: () => void

  // #thiking jflute 関数名、動詞省略するかしないか？tagファイルの関数ではしっかり統一したい (2022/04/23)
  /**
   * DBFlute IntroのManifestファイルの情報を反映する。
   */
  introManifest: () => void

  /**
   * 既存のDBFluteエンジンのバージョン一覧を反映する。
   * 実質、これが画面上におけるDBFluteエンジンの一覧の元ネタと考えて良い。
   * (DBFluteエンジンは、バージョンごとにユニークになるので)
   * @return 業務的な戻りは特になし
   */
  engineVersions: () => Promise<void>

  /**
   * DBFluteの最新バージョン情報をダウンロードモーダルに反映する。
   * @return 業務的な戻りは特になし
   */
  latestVersion: () => Promise<void>

  /**
   * ユーザがDBFluteエンジンをダウンロードするために表示する Modal を返す。
   * @return ダウンロード時に表示する Modal
   */
  downloadModal: () => DownloadModal

  /**
   * DBFlute Intro がバックグランドでプロセスを実行中に、ユーザが他の操作を抑制するための Modal を返す。
   * @return 処理中に表示する Modal
   */
  processModal: () => ProcessModal
}

// > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > > >
// ^                                                                                     v
// ^                                                                                     v
// ^                                                                                     v
// < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < < <

export default withIntroTypes<Main>({
  components: {
    i18n,
  },
  state: {
    manifest: [],
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
  // -----------------------------------------------------
  //                                      Static Reference
  //                                      ----------------
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

  processModalBase: {
    closable: false,
  },

  // ===================================================================================
  //                                                                           Lifecycle
  //                                                                           =========
  async onMounted() {
    await Promise.all([this.introManifest(), this.engineVersions(), this.latestVersion(), this.prepareClientList()])
  },

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickDownload() {
    this.state.showDownloadModal = true
    this.update()
  },

  onDownloadEngine() {
    this.state.showProcessModal = true
    this.update()
    const version = this.inputElementBy('[ref=version]').value
    api.downloadEngine({ version }).then(() => {
      this.state.showProcessModal = false
      this.engineVersions()
    })
  },

  onclickRemove(version: string) {
    api.removeEngine({ version }).finally(() => {
      this.engineVersions()
    })
  },

  goToDocumentsPage(client: ClientListResult) {
    appRoutes.client.open(client.projectName, 'execute', 'documents')
  },

  goToClientCreate() {
    appRoutes.create.open()
  },

  onDownloadModalHide() {
    this.state.showDownloadModal = false
    this.update()
  },

  onProcessModalHide() {
    this.state.showProcessModal = false
    this.update()
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  async prepareClientList() {
    // #thinking jflute awaitしてないけど、onMounted()で本当にupdate()まで待ってるかな？ (2026/03/14)
    api.findClientList().then((json) => this.update({ clientList: json }))
  },

  async introManifest() {
    // #thinking jflute こっちも同じく、awaitしてないけど... (2026/03/14)
    api.findManifest().then((json) => {
      this.update({
        manifest: [
          ['Implementation-Version', json['Implementation-Version']],
          ['Build-Timestamp', json['Build-Timestamp']],
        ],
      })
    })
  },

  async engineVersions() {
    await api.findExistingEngineVersions().then((json) => {
      this.update({ versions: json })
    })
  },

  async latestVersion() {
    await api.findEngineLatestVersion().then((json) => {
      this.update({ latestVersion: json })
    })
  },

  downloadModal(): DownloadModal {
    return this.downloadModalBase
  },

  processModal(): ProcessModal {
    return this.processModalBase
  },
})
