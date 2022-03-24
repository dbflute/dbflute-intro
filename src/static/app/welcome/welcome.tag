<welcome>
  <!-- Welcome画面 (DBFluteクライアントが何もない状態の起動時に表示される画面) (written at 2022/03/09)
   機能:
    o DBFluteクライアントの最低限の情報を入力
    o DBFluteクライアント作成前にテスト接続
    o DBFluteクライアント作成、DBFluteエンジンのダウンロード

   作りの特徴:
    o O/Rマッパー関連設定を表示/非表示できる (最初は非表示: AltoDBFlute利用を想定して)
   -->
  <h2 class="heading">Welcome to DBFlute</h2>
  <div class="ui form">
    <div class="ui stackable two column divided grid">
      <div class="row">
        <!-- DBFluteクライアントの基本情報の入力欄 -->
        <div class="column">
          <div class="required field">
            <label data-is="i18n">LABEL_projectName</label>
            <input type="text" ref="projectName" placeholder="maihamadb" />
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
        <!-- Introとしてはオプション的存在なので、同じrowの右側にひっそり表示させる -->
        <div class="column">
          <div class="field">
            <button class="ui button mini" onclick="{ toggleOrmSetting }">O/R Mapper settings</button>
          </div>
          <div class="required field" show="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_languageCode</label>
            <su-dropdown ref="languageCode" items="{ targetLanguageItems }"></su-dropdown>
          </div>
          <div class="required field" show="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_containerCode</label>
            <su-dropdown ref="containerCode" items="{ targetContainerItems }"></su-dropdown>
          </div>
          <div class="required field" show="{ oRMapperOptionsFlg }">
            <label data-is="i18n">LABEL_packageBase</label>
            <input type="text" ref="packageBase" value="org.docksidestage.dbflute" />
          </div>
        </div>
      </div>
      <div class="row">
        <!-- DB接続情報の入力欄 -->
        <!-- #thinking jflute DBMSごとにplaceholder変えられたらいいかな？ドキュメントリンクとかも？ (2022/03/10) -->
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
        </div>
        <!-- クライアント作成決定ボタン周り -->
        <div class="column bottom aligned">
          <!-- テスト接続はデフォルトではOFF, 作成時に接続できる環境とは限らないので、とりあえずクライアントは作ってもらおう -->
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
    // #thinking jflute こういう構造 { fileName: null, data: null } って書きたいけどスクリプト言語のお作法に反する？ (2022/03/17)
    // コメントで書いても定義先で変わったときに追従できない。(ついつい型定義したくなってしまうな...)
    // というか、この宣言要るのか？なければないでundefinedとかで落ちちゃうのかな？？？
    this.jdbcDriver = null

    // JDBCドライバーのアップロードが必要なDBMSかどうか？サーバー側のDBMS定義より設定される
    // (例えば、MySQLだとDBFlute Engineに組み込まれているので false となる)
    this.needsJdbcDriver = false

    // O/Rマッパー関連設定の表示/非表示
    this.oRMapperOptionsFlg = false

    // DBFluteエンジンの最新バージョン e.g. 1.2.5
    this.latestVersion = null

    const self = this

    // ===================================================================================
    //                                                                      Initial Method
    //                                                                      ==============
    /**
     * Welcome画面で必要な区分値情報を探す。(保持する)
     */
    this.findClassifications = () => {
      ApiFactory.classifications().then((json) => {
        // [tips] Object.keys() は Javaで言うと Map@keySet() のようなもの
        // https://developer.mozilla.org/ja/docs/Web/JavaScript/Reference/Global_Objects/Object/keys
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
     * DBFlute Engineの最新バージョンをwebから取って来て設定する。
     */
    this.setLatestEngineVersion = () => {
      ApiFactory.engineLatest().then((json) => {
        self.latestVersion = json.latestReleaseVersion
        self.update()
      })
    }

    /**
     * 自動生成の言語とDIコンテナのデフォルトを設定する。
     */
    this.setDefaultLangAndDIContainer = () => {
      // #thiking jflute 本当はこういうのもサーバー側のロジックで決めたいかも？ (2022/03/17)
      self.refs.languageCode.value = 'java'
      self.refs.containerCode.value = 'lasta_di'
      self.update()
    }

    // ===================================================================================
    //                                                                        Event Method
    //                                                                        ============
    /**
     * DBMSの値が変わったときの処理、関連する項目の値を選択されたDBMSに合わせて更新する。
     * @param {string} databaseCode - 選択されたDBMSのコード (NotNull)
     */
    const changeDatabase = (databaseCode) => {
      // #thinking jflute self.targetDatabaseItems を使えばいいんじゃないか？と思ったんだけど... (2022/03/13)
      // それはあくまでリストボックス用だから、全部入りのclassificationMapの方から取ってるのかな!?
      // それはそれでいいんだけど、サーバー側のキー値に依存するコードを散らばせたくない気はする。
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
      // サーバーサイドのWelcomeCreateBody.javaのclient部分の直接的に関連
      // #thinking jflute "create: true" はサーバーサイドで定義されていないので使われてないような？ (2022/03/17)
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
        dbfluteVersion: self.latestVersion,
        packageBase: self.refs.packageBase.value,
        containerCode: self.refs.containerCode.value,
        languageCode: self.refs.languageCode.value,
        jdbcDriverFqcn: self.refs.jdbcDriverFqcn.value,
      }
      if (self.jdbcDriver) {
        client.jdbcDriver = self.jdbcDriver
      }
      const testConnection = self.refs.testConnection.checked
      observable.trigger('loading', true)
      ApiFactory.createWelcomeClient(client, testConnection).then(() => {
        route('') // メイン画面へ
        this.showToast(client.projectName)
      }).finally(() => {
        observable.trigger('loading', false)
      })
    }

    /**
     * JDBCクライアントの作成。(作成ボタンの処理)
     * @param {string} event - この関数を呼び出したイベントのオブジェクト (NotNull)
     */
    this.changeFile = (event) => {
      let file = event.target.files[0] // event.targetはイベント発生元のオブジェクト
      let reader = new FileReader() // https://developer.mozilla.org/ja/docs/Web/API/FileReader
      reader.onload = (function() { // 読み込み処理が正常に完了したとき
        return () => {
          // encode base64: https://developer.mozilla.org/ja/docs/Web/API/btoa
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

    // #thinking jflute シンプルな関数名にするか、showClientCreatedToast()とか具体的な名前にするか迷うね (2022/03/17)
    // いや、showToast()だと汎用的な処理なのかなとかふと思ってしまって。要はJavaだとhelper的なprivateメソッドみたいな。
    // もちろん、welcome.tagというクラスみたいな世界の中のshowToast()なのでクライアント作成時のものでしょうって解釈もできるけど。
    // 少なくともこういうところ、人によってバラけそうじゃない？って思った。
    /**
     * DBFluteクライアントを作成したことを知らせるパンじゃなくてトースト。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.showToast = (projectName) => {
      this.successToast({
        title: 'Create task completed',
        message: 'Client for project \'' + projectName + '\', was successfully created!!',
      })
    }

    /**
     * O/Rマッパー設定の表示/非表示をトグルする。
     */
    this.toggleOrmSetting = () => {
      this.oRMapperOptionsFlg = !this.oRMapperOptionsFlg
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    // #thinking jflute initializeは上の方に持っていきたい (2022/03/13)
    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      this.findClassifications()
      this.setLatestEngineVersion()
      this.setDefaultLangAndDIContainer()

      // DBMSの選択が変わったら関連する項目を変更させてる
      this.refs.databaseCode.on('change', target => {
        changeDatabase(target.value)
        this.update()
      })
    })
  </script>
</welcome>
