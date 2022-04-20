<main>
  <!-- Introのトップページ(メイン画面) (written at 2022/03/31)
   機能:
    o DBFluteクライアントが一つ以上存在するときのトップページ
    o DBFluteクライアントが一つもないときはWelcome画面へ自動遷移
    o DBFluteクライアントの新規作成画面へ遷移
    o DBFluteクライアントの一覧表示、詳細画面へ遷移
    o DBFluteエンジンの一覧表示、個別削除、最新版ダウンロード
    o Introのシステム情報(バージョンやビルド時刻など)の表示

   作りの特徴:
    o DBFluteエンジンのダウンロードダイアログ
    o DBFluteエンジンの最新バージョンをwebから取得して設定
    o Intro自体のシステム情報をManifestファイルから取得
    o Welcome画面への強引な遷移、フハハハ
   -->
  <div class="ui text container">
    <h1>DBFlute Intro</h1>

    <!-- DBFluteクライアントの一覧 -->
    <!-- #thinking このdivはなんだ？DBFlute Engineの方は囲ってない。Createボタンと一緒に囲う必要があるのかな？ by jflute (2022/03/31) -->
    <div>
      <h2>DBFlute Client</h2>
      <input type="button" class="ui button primary" value="Create" onclick="{ goToClientCreate }" />
    </div>

    <table class="ui table">
      <!-- 表のラベル、基本情報のみ列挙 -->
      <thead>
      <tr>
        <th data-is="i18n">LABEL_projectName</th>
        <th data-is="i18n">LABEL_databaseCode</th>
        <th data-is="i18n">LABEL_languageCode</th>
        <th data-is="i18n">LABEL_containerCode</th>
      </tr>
      </thead>
      <!-- 存在するDBFluteクライアントの一覧とそれぞれの基本情報を表示 -->
      <tbody class="list-group">
      <tr each="{client in clientList}">
        <td>
          <!-- プロジェクト名がクライアント詳細画面へのリンクになる -->
          <a onclick="{ goToClient.bind(this, client) }">{ client.projectName }</a>
        </td>
        <td>{ client.databaseCode }</td>
        <td>{ client.languageCode }</td>
        <td>{ client.containerCode }</td>
      </tr>
      </tbody>
    </table>

    <!-- DBFluteエンジンの一覧 -->
    <h2>DBFlute Engine</h2>
    <!-- 最新版のDBFluteエンジンがダウンロードできる -->
    <button type="button" class="ui button primary" onclick="{ showDownloadModal }">
      <span class="glyphicon glyphicon-download-alt" aria-hidden="true"></span>Download
    </button>

    <table class="ui table">
      <!-- DBFluteエンジンは大して情報がない -->
      <thead>
      <tr>
        <th data-is="i18n">LABEL_engineVersion</th>
        <th></th>
      </tr>
      </thead>
      <!-- 存在するDBFluteエンジンの一覧 -->
      <!-- 通常は一個しかないってのが理想、クライアントごとにバージョンがバラけないように -->
      <tbody>
      <tr each="{version in versions}">
        <td>{ version }</td>
        <td class="right aligned">
          <!-- 何気に削除もできる -->
          <input type="button" class="ui negative button" value="Remove" onclick="{ removeEngine.bind(this,version) }" />
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Intro自体のシステム情報、Introのバージョンとか -->
    <h3>
      <small class="text-info">System Info</small>
    </h3>
    <div class="ui list">
      <div class="item" each="{ key, value in manifest }">
        <small>{ value } = { key }</small>
      </div>
    </div>
  </div>

  <!-- DBFluteエンジンのダウンロード処理のためのモーダル -->
  <su-modal modal="{ downloadModal }" ref="downloadModal">
    <!-- デフォルトで最新版が指定されるようになっている (webから取得) -->
    <div class="description">
      The latest version is { opts.modal.latestVersion.latestReleaseVersion } now.
      <form class="ui form">
        <div class="required field">
          <label>Version</label>
          <input type="text" ref="version" value="{ opts.modal.latestVersion.latestReleaseVersion }">
        </div>
      </form>
    </div>
  </su-modal>

  <!-- ちと待ってなさいモーダル -->
  <su-modal modal="{ processModal }" ref="processModal">
    <div class="description">
      Downloading...
    </div>
  </su-modal>

  <style>
    /* #thiking jflute おおぅ、隣接セレクタじゃん。間にタグ入れると一気に破綻する？ (2022/04/07) */ 
    table+h2,
    table+h3 {
      margin-top: 3rem;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import route from 'riot-route'

    const ApiFactory = new _ApiFactory()
    const self = this

    // Implementation-Versionなど、System Infoに載せるオブジェクト
    this.manifest = {} // intro manifest

    // DBFluteエンジンのバージョン一覧 (実質的にDBFluteエンジンの一覧と考えて良い)
    this.versions = [] // engine versions

    // #thinking jflute serverUrlやapiServerUrlなどのIntro自体の設定情報だけど...使ってないようにみえる (2022/04/20)
    this.configuration = {} // intro configuration

    // DBFluteクライアントの一覧
    this.clientList = [] // existing clients

    // DBFluteの最新バージョンオブジェクト e.g. latestReleaseVersion
    this.latestVersion = {} // latest engin verion

    // DBFluteエンジンをダウンロードするためのモーダルオブジェクト
    this.downloadModal = {
      header: 'DBFlute Engine Download',
      closable: true,
      buttons:  [
        {
          text: 'Download',
          action: 'downloadEngine'
        }
      ],
      latestVersion: {}
    }

    // ダウンロード中とかの、ちと待ってなさいモーダル
    this.processModal = {
      closable: false
    }

    // ===================================================================================
    //                                                                          Basic Data
    //                                                                          ==========
    /**
     * DBFlute IntroのManifestファイルの情報を反映する。
     */
    this.introManifest = () => {
      ApiFactory.manifest().then((json) => {
        self.manifest = {
          'Implementation-Version': json['Implementation-Version'],
          'Build-Timestamp': json['Build-Timestamp']
        }
        self.update()
      })
    }

    /**
     * 既存のDBFluteエンジンのバージョン一覧を反映する。
     * 実質、これが画面上におけるDBFluteエンジンの一覧の元ネタと考えて良い。
     * (DBFluteエンジンは、バージョンごとにユニークになるので)
     */
    this.engineVersions = () => {
      ApiFactory.engineVersions().then((json) => {
        self.versions = json
        self.update()
      })
    }

    /**
     * DBFluteの最新バージョン情報をダウンロードモーダルに反映する。
     */
    this.latestVersion = () => {
      ApiFactory.engineLatest().then((json) => {
        self.downloadModal.latestVersion = json
        self.update()
      })
    }

    /**
     * serverUrlなどのIntro自体の設定情報を反映する...だけど、使ってなくない？
     */
    this.configuration = () => {
      ApiFactory.configuration().then((json) => {
        self.configuration = json
        self.update()
      })
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    /**
     * DBFluteクライアントの一覧情報を準備する。
     * なければWelcome画面に遷移させる処理もここに入っている。
     */
    this.prepareClientList = () => {
      ApiFactory.clientList().then((json) => {
        if (json.length > 0) {
          self.clientList = json
          self.update()
        } else {
          self.goToWelcome()
        }
      })
    }

    /**
     * 引数で指定されたDBFluteクライアントの実行ページに遷移する。
     * @param {Object} client - 遷移するDBFluteクライアントのオブジェクト (NotNull)
     */
    this.goToClient = (client) => {
      route('client/' + client.projectName) // default page of client
    }

    /**
     * DBFluteクライアント作成画面へ遷移する。
     */
    this.goToClientCreate = () => {
      route('create')
    }

    /**
     * Welcome画面へ遷移する。
     */
    this.goToWelcome = () => {
      route('welcome')
    }

    /**
     * 引数で指定されたDBFluteクライアントのSettings画面へ遷移する。
     * @param {Object} client - 遷移するDBFluteクライアントのオブジェクト (NotNull)
     */
    this.goToSettings = (client) => {
      route(`settings/${client.projectName}`)
    }

    // ===================================================================================
    //                                                                              Engine
    //                                                                              ======
    /**
     * DBFluteエンジンをダウンロードするためのモーダルを表示する。
     */
    this.showDownloadModal = () => {
      self.refs.downloadModal.show()
    }

    /**
     * 引数で指定されたDBFluteエンジンを削除する。
     * @param {string} version - 削除するDBFluteエンジンのバージョン (NotNull)
     */
    this.removeEngine = (version) => {
      let params = { version: version }
      ApiFactory.removeEngine(params).finally(() => {
        this.engineVersions() // 削除したことを画面に反映させないとぅ
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    // #thinking jflute Initializeは最初に方に宣言したい (2022/04/14)
    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      this.introManifest()
      this.engineVersions()
      this.latestVersion()
      this.configuration()
      this.prepareClientList()

      this.refs.downloadModal.on('downloadEngine', () => {
        self.refs.processModal.show()
        let downloadModal = self.refs.downloadModal
        let version = downloadModal.refs.version.value
        ApiFactory.downloadEngine({version}).finally(() => {
          self.refs.processModal.hide()
          self.engineVersions()
        })
      })
    })
  </script>
</main>
