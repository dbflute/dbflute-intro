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
  //                                                                       Event Handler
  //                                                                       =============
  onclickDownload: () => void
  onDownloadEngine: () => void
  onclickRemove: (version: string) => void

  // ===================================================================================
  //                                                                     Client Handling
  //                                                                     ===============
  prepareClientList: () => void
  goToDocumentsPage: (client: ClientListResult) => void
  goToClientCreate: () => void

  // ===================================================================================
  //                                                                               Modal
  //                                                                               =====
  downloadModalBase: DownloadModalBase
  downloadModal: () => DownloadModal
  onDownloadModalHide: () => void // あなた、Event Handler では？
  processModalBase: ProcessModalBase
  processModal: () => ProcessModal
  onProcessModalHide: () => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  introManifest: () => void
  engineVersions: () => Promise<void>
  latestVersion: () => Promise<void>
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
  //                                                                           Lifecycle
  //                                                                           =========
  /**
   * マウント完了時の処理。
   */
  async onMounted() {
    await this.introManifest()
    await this.engineVersions()
    await this.latestVersion()
    await this.prepareClientList()
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

  // ===================================================================================
  //                                                                               Modal
  //                                                                               =====
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
  downloadModal() {
    return this.downloadModalBase
  },
  onDownloadModalHide() {
    this.state.showDownloadModal = false
    this.update()
  },

  /**
   * DBFlute Engine のダウンロードを実施しているときに表示する Modal
   * ダウンロード中は、ユーザがその他の操作をできないように制御する
   */
  processModalBase: {
    closable: false,
  },
  processModal() {
    return this.processModalBase
  },
  onProcessModalHide() {
    this.state.showProcessModal = false
    this.update()
  },

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
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
   * DBFluteクライアントの一覧情報を準備する。
   * なければWelcome画面に遷移させる処理もここに入っている。
   */
  async prepareClientList() {
    api.clientList().then((json) => this.update({ clientList: json }))
  },

  // ===================================================================================
  //                                                                     Client Handling
  //                                                                     ===============
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
})
