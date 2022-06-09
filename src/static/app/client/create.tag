<create>
  <!-- Create画面 (DBFluteクライアントを新しく作るための画面) (written at 2022/06/09)
   機能:
    o DBFluteクライアントの作成に必要な最低限の情報を入力
    o DBFluteクライアント作成前にテスト接続オプション (接続失敗は業務例外で作成処理が中断される)
    o DBFluteクライアント作成

   作りの特徴:
    o ファイルアップロードもあるよ
    o DBMSの選択によって他の項目の値が書き換わるよ
  -->
  <h2 class="heading">Create Client</h2>
  <div class="ui form">
    <div class="ui stackable two column divided grid">
      <div class="row">
        <!-- DBFluteクライアントの基本情報の入力欄(1) -->
        <!-- memo : welcom 画面との差分、下記に記載されている「O/Rマッパー関連設定の入力欄」を少し下げて表示させるために「DBFluteクライアントの基本情報の入力欄」を分けている -->
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_projectName</label>
            <input type="text" ref="projectName" placeholder="e.g. maihamadb" />
          </div>
        </div>
      </div>

      <div class="row">
        <!-- DBFluteクライアントの基本情報の入力欄(2) -->
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_dbfluteVersion</label>
            <su-dropdown ref="dbfluteVersion" items="{ versions }"></su-dropdown>
          </div>
          <div class="required field">
            <label data-is="i18n">LABEL_databaseCode</label>
            <su-dropdown ref="databaseCode" items="{ targetDatabaseItems }"></su-dropdown>
          </div>
          <div class="required field">
            <label data-is="i18n">LABEL_jdbcDriverFqcn</label>
            <input type="text" ref="jdbcDriverFqcn" placeholder="com.mysql.jdbc.Driver" value="com.mysql.jdbc.Driver" />
          </div>
          <div class="required field" if="{ needsJdbcDriver }">
            <label data-is="i18n">LABEL_jdbcDriverFile</label>
            <input type="file" onchange="{ changeFile }"/>
          </div>
        </div>

        <!-- O/Rマッパー関連設定の入力欄 -->
        <!-- memo : welcom 画面との差分、この設定項目はひっそりしてない -->
        <div class="column">
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_languageCode</label>
            <su-dropdown ref="languageCode" items="{ targetLanguageItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_containerCode</label>
            <su-dropdown ref="containerCode" items="{ targetContainerItems }"></su-dropdown>
          </div>
          <div class="required field" if="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_packageBase</label>
            <input type="text" ref="packageBase" value="org.docksidestage.dbflute" />
          </div>
        </div>
      </div>
      <div class="row">
        <!-- DB接続情報の入力欄 -->
        <!-- #thinking jflute DBMSごとにplaceholder変えられたらいいかな？ドキュメントリンクとかも？ (2022/03/10) copy from welcom.tag by cabos (2022/06/09) -->
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_url</label>
            <input type="text" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb" />
          </div>
          <div class="field">
            <label data-is="i18n">LABEL_schema</label>
            <input type="text" ref="schema" placeholder="MAIHAMADB" />
          </div>
          <div class="required field">
            <label data-is="i18n">LABEL_user</label>
            <input type="text" ref="user" placeholder="maihamadb" />
          </div>
          <div class="field">
            <label data-is="i18n">LABEL_password</label>
            <input type="text" ref="password" />
          </div>
          <div class="field">
            <su-checkbox ref="testConnection">Connect as test</su-checkbox>
          </div>
          <div class="field">
            <button class="ui button primary" onclick="{ create }">Create</button>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import _UiAssist from '../common/UiAssist'
    const ApiFactory = new _ApiFactory()
    const UiAssist = new _UiAssist()

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    // 区分値の集めたMap, IntroClsAssist.javaで作られているもの
    // （本当はサーバーのクラス名をここに書きたくないけど、キー値がわからないとどうにもって感じなので）
    this.classificationMap = {} // e.g. DBMSやDIコンテナなど

    // JDBCドライバーのjarファイル情報を格納するオブジェクト
    this.jdbcDriver = null

    // JDBCドライバーのアップロードが必要なDBMSかどうか？サーバー側のDBMS定義より設定される
    // (例えば、MySQLだとDBFlute Engineに組み込まれているので false となる)
    this.needsJdbcDriver = false

    // O/Rマッパー関連設定の表示/非表示
    this.oRMapperOptionsFlg = true

    // すでにダウンロードされているDBFluteエンジンのバージョン
    this.versions = []

    // thisが何を指すのかわからなくなるので、おまじない
    const self = this

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    /**
     * Create画面で必要な区分値情報を探す。(保持する)
     */
    this.findClassifications = () => {
      ApiFactory.classifications().then((json) => {
        self.targetDatabaseItems = Object.keys(json.targetDatabaseMap).map(key => {
          return { value: key, label: json.targetDatabaseMap[key].databaseName }
        })
        self.targetLanguageItems = Object.keys(json.targetLanguageMap).map(key => {
          return { value: key, label: json.targetLanguageMap[key] }
        })
        self.targetContainerItems = Object.keys(json.targetContainerMap).map(key => {
          return { value: key, label: json.targetContainerMap[key] }
        })
        UiAssist.setBlankItem(self.targetDatabaseItems)
        UiAssist.setBlankItem(self.targetLanguageItems)
        UiAssist.setBlankItem(self.targetContainerItems)

        self.classificationMap = json
        self.update()
      })
    }

    /**
     * ダウンロード済みDBFlute Engineをサーバから取得する
     */
    this.engineVersions = () => {
      ApiFactory.engineVersions().then((json) => {
        self.versions = json.map(element => {
          return { label: element, value: element }
        })
        self.update()
      })
    }

    /**
     * ダウンロード済みDBFlute Engine一覧を取得し、保持する
     */
    this.setLatestEngineVersion = () => {
      ApiFactory.engineLatest().then((json) => {
        self.refs.dbfluteVersion.value = json.latestReleaseVersion
        self.update()
      })
    }

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    /**
     * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する
     * @param {string} databaseCode - 選択されたDBMSのコード (NotNull)
     */
    const changeDatabase = (databaseCode) => {
      let database = self.classificationMap['targetDatabaseMap'][databaseCode]
      // switch showing JDBCDriver select form
      self.needsJdbcDriver = !database.embeddedJar
      // initialize JDBC Driver
      self.jdbcDriver = null
      self.refs.jdbcDriverFqcn.value = database.driverName
      self.refs.url.value = database.urlTemplate
      self.refs.schema.value = database.defaultSchema
    }

    /**
     * DBFluteクライアントを作成する。(作成ボタンの処理)
     */
    this.create = () => {
      const client = {
        projectName: self.refs.projectName.value,
        databaseCode: self.refs.databaseCode.value,
        create: true,
        mainSchemaSettings: {
          user: self.refs.user.value,
          url: self.refs.url.value,
          schema: self.refs.schema.value,
          password: self.refs.password.value
        },
        schemaSyncCheckMap: {},
        dbfluteVersion: self.refs.dbfluteVersion.value,
        packageBase: self.refs.packageBase.value,
        containerCode: self.refs.containerCode.value,
        languageCode: self.refs.languageCode.value,
        jdbcDriverFqcn: self.refs.jdbcDriverFqcn.value,
      }
      if (self.jdbcDriver) {
        client.jdbcDriver = self.jdbcDriver
      }
      const testConnection = self.refs.testConnection.checked
      ApiFactory.createClient(client, testConnection).then(() => {
        route('')
        this.showToast(client.projectName)
      })
    }

    /**
     * JDBCファイルを読み込み、このタグのプロパティとして保持する
     * フォームでJDBCドライバーのファイルを指定したときに呼び出される
     * @param {string} event - この関数を呼び出したイベントのオブジェクト (NotNull)
     */
    this.changeFile = (event) => {
      let file = event.target.files[0]
      
      // イベントを仕込む処理を、イベントハンドラの中で定義している
      // #thinking cabos この処理は外部に切り出すことができる（ような気がする） (2022/06/16)
      let reader = new FileReader()
      reader.onload = (function () {
        return () => {
          // encode base64
          let result = window.btoa(reader.result)
          self.jdbcDriver = { fileName: null, data: null }
          self.jdbcDriver.fileName = file.name
          self.jdbcDriver.data = result
        }
      }(file))

      if (file) {
        reader.readAsBinaryString(file)
      }
    }

    /**
     * DBFluteクライアントが作成できたことを知らせるトーストを表示する
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名 (NotNull)
     */
    this.showToast = (projectName) => {
      this.successToast({
        title: 'Create task completed',
        message: 'Client for project \'' + projectName + '\', was successfully created!!',
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    // #thinking jflute initializeは上の方に持っていきたい (2022/03/13) copy from welcom.tag by cabos (2022/06/16)
    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      this.findClassifications()
      this.engineVersions()
      this.setLatestEngineVersion()

      this.refs.databaseCode.on('change', target => {
        changeDatabase(target.value)
        this.update()
      })
    })
  </script>
</create>
