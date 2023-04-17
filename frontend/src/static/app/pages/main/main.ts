import { IntroRiotComponent, withIntroTypes } from '../../app-component-types'
import { appRoutes } from '../../app-router'
import { api } from '../../api/api'

interface Props {}
interface State {
  // Implementation-Versionなど、System Infoに載せるオブジェクト
  // TODO : any を使わない、、、
  manifest: any // intro manifest
  // DBFluteエンジンのバージョン一覧 (実質的にDBFluteエンジンの一覧と考えて良い)
  versions: string[] // engine versions
  // DBFluteクライアントの一覧
  clientList: ClientListResult[] // existing clients
  // DBFluteの最新バージョンオブジェクト e.g. latestReleaseVersion
  latestVersion: EngineLatestResult // latest engin version
  // downloadModal を見せるかどうか
  showDownloadModal: boolean
  // processModal を見せるかどうか
  showProcessModal: boolean
}

interface Main extends IntroRiotComponent<Props, State> {
  // ===================================================================================
  //                                                                          Definition
  //                                                                          ==========
  downloadModalDefinition: {
    header: string
    closable: boolean
    buttons: Array<{
      text: string
      action: string
    }>
  }
  processModalDefinition: {
    closable: boolean
  }

  // ===================================================================================
  //                                                                       Event Handler
  //                                                                       =============
  onclickDownload: () => void
  onDownloadEngine: () => void
  onclickRemove: (version: string) => void

  // ===================================================================================
  //                                                                             Private
  //                                                                             =======
  introManifest: () => void
  engineVersions: () => Promise<void>
  latestVersion: () => Promise<void>

  // ===================================================================================
  //                                                                     Client Handling
  //                                                                     ===============
  prepareClientList: () => void
  goToDocumentsPage: (client: ClientListResult) => void
  goToClientCreate: () => void
}

export default withIntroTypes<Main>({
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
  processModalDefinition: {
    closable: false,
  },
  downloadModalDefinition: {
    header: 'DBFlute Engine Download',
    closable: true,
    buttons: [
      {
        text: 'Download',
        action: 'download-engine',
      },
    ],
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
  onclickRemove(version) {
    api.removeEngine({ version }).finally(() => {
      this.engineVersions()
    })
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

  // ===================================================================================
  //                                                                     Client Handling
  //                                                                     ===============
  /**
   * DBFluteクライアントの一覧情報を準備する。
   * なければWelcome画面に遷移させる処理もここに入っている。
   */
  async prepareClientList() {
    api.clientList().then((json) => this.update({ clientList: json }))
  },
  /**
   * Document画面に遷移する。
   * @param {Object} client - 遷移するDBFluteクライアントのオブジェクト (NotNull)
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
