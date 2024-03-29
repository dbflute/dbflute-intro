<ex-schema-sync-check>
  <!-- DBFlute の SchemaSyncCheck 機能をGUIで操作できるようにするタグ (written at 2022/04/07)
   機能:
    o DBFlute Client が管理しているDBとは別に、スキーマの差分を確認したいデータベースへの接続情報を設定する
    o 上記の設定をもとに、SchemaSyncCheckを実行する
    o 実行結果として、「sync-check-result.html」が出力されている場合、実行結果確認用リンクが表示される

   作りの特徴:
    o DBFlute Enginge が提供するドキュメント関連の機能のうち、SchemaSyncCheckで操作可能なものは、全てこの tag の中に記述されている
    o 「window.open」によって、「sync-check-result.html」 を開くようになっている
    o Modal で documentMap.dfprop の schemaSyncCheckMap を編集させる
    o SchemaSyncCheck の実行中は、別の操作を抑制するための Modal を表示している
    o SchemaSyncCheck の実行後は、結果を Modal で表示し、ログを改めて参照できるようにしている
    o SchemaSyncCheck は管理しているデータベースに変更を加えないので、実行前の確認は行わない
   -->
  <div class="ui container">
    <h2>Schema Sync Check</h2>

    <!--  DBFlute Client が管理していない方のデータベースへの接続情報 -->
    <p show="{ canCheckSchemaSetting() }">
      for { state.syncSetting.url }<span show="{ state.syncSetting.schema != null }">, { state.syncSetting.schema }</span>, { state.syncSetting.user }
    </p>

    <!-- 各HTMLのドキュメントリンク -->
    <!-- #thinking ex-document.tag に合わせて list にしているのかな？要素は一つしか無いけど by cabos (written at 2022/04/07) -->
    <div class="ui list">
      <div show="{ state.client.hasSyncCheckResultHtml }" class="item"><a onclick="{ openSyncCheckResultHTML }">Open your SchemaSyncCheck result (HTML)</a></div>
    </div>

    <!-- DBFlute Client が管理していない方のデータベースへの接続情報を編集するための modal を表示するボタン  -->
    <button class="ui positive button" onclick="{ showSyncSettingModal }">Edit check settings</button>

    <!-- SchemaSyncCheck実行ボタン  -->
    <button show="{ canCheckSchemaSetting() }" class="ui primary button" onclick="{ schemaSyncCheckTask }">Execute SchemaSyncCheck</button>
    
    <!-- "SchemaSyncCheck" ってそもそもなんやねん？の説明 -->
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/doc/schemasynccheck.html" target="_blank">"SchemaSyncCheck"?</a></div>
      <p>A checking tool for differences between the two schemas.</p>
    </div>

    <!-- 最後に SchemaSyncCheck を実行したときのログを表示するところ -->
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
  </div>

  <!-- documentMap.dfprop の schemaSyncCheckMap を編集するための Modal -->
  <su-modal modal="{ syncSettingModal }" class="large" ref="syncSettingModal">

    <!-- documentMap.dfprop の schemaSyncCheckMap を編集するための form の実態 -->
    <form class="ui form">
      <div class="required field">
        <label>URL</label>
        <input type="url" ref="url" placeholder="jdbc:mysql://localhost:3306/examplesyncdb" value="{ opts.modal.syncSetting.url }">
      </div>
      <div class="field">
        <label>Schema</label>
        <input type="text" ref="schema" placeholder="EXAMPLESYNCDB" value="{ opts.modal.syncSetting.schema }">
      </div>
      <div class="required field">
        <label>User</label>
        <input type="text" ref="user" placeholder="examplesyncdb" value="{ opts.modal.syncSetting.user }">
      </div>
      <div class="field">
        <label>Password</label>
        <input type="text" ref="password" placeholder="" value="{ opts.modal.syncSetting.password }">
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="isSuppressCraftDiff" checked="{ opts.modal.syncSetting.isSuppressCraftDiff }">
          <label>Suppress Craft Diff</label>
        </div>
      </div>
    </form>

  </su-modal>

  <!-- SchemaSyncCheck 実行時に他の操作を抑制するための Modal -->
  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

   <!-- SchemaSyncCheck 実行結果を表示するための Modal -->
  <result-modal ref="resultModal"></result-modal>

  <!-- #thinking ex-document.tag にも同じスタイル調整が入ってる、共通化してもいいかも？ by cabos (written at 2022/04/07) -->
  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()

    const self = this

    self.props = {
      projectName: self.opts.projectName,
      client: self.opts.client
    }

    self.state = {
      client: self.props.client,
      syncSetting: {}
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント時の処理
     */
    this.on('mount', () => {
      self.initSyncSchemaSetting()
      self.prepareComponents()
      self.registerModalEvent()
    })

    /**
     * SchemaSyncCheck の設定を取得し、それを編集するための Modal 内に含まれる form を初期化する
     */
    // #thinking setting を初期化するって誰のなんのためなんだろうという違和感がある、画面としては state とか modal とかを初期化するなら意味はわかる、そういう関数名にしたい by cabos
    this.initSyncSchemaSetting = () => {
      // #thinking "response" というのはやはり、意味のある名前にしたいな by cabos
      // https://github.com/dbflute/dbflute-intro/pull/394#discussion_r850480927
      ApiFactory.syncSchema(self.props.projectName).then((response) => {
        // #thinking サーバからデータを初期化して state を初期化する処理と、modalを初期化する処理は分けたほうが見通しがいいのかな？ by cabos
        // https://github.com/dbflute/dbflute-intro/pull/394#discussion_r850495751
        self.syncSettingModal.syncSetting = response
        self.state.syncSetting = response
        self.update()
      })
    }

    /**
     * 最後の実行結果を表示するためのコンポーネントを初期化する
     */
    // #thinking prepaseLatestResult という名前の方が体を表している... by cabos
    this.prepareComponents = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.props.projectName, task: 'schemaSyncCheck' })[0]
    }

    /**
     * SchemaSyncCheck の設定を編集するための Modal の中にある、「OK」ボタンが押されたときの挙動を登録する
     * 具体的には、Modal の中にある form の値を取得し、サーバに設定更新のためのリクエストをPostする
     */
    this.registerModalEvent = () => {
      // this.syncSettingModal.action に 'editSyncSettings' とあり、そこと紐付いている
      this.refs.syncSettingModal.on('editSyncSettings', () => {
        // ref を利用して modal の中の form の値を参照している
        const syncSettingModalRefs = self.refs.syncSettingModal.refs
        const input = {
          url: syncSettingModalRefs.url.value,
          schema: syncSettingModalRefs.schema.value,
          user: syncSettingModalRefs.user.value,
          password: syncSettingModalRefs.password.value,
          isSuppressCraftDiff: syncSettingModalRefs.isSuppressCraftDiff.checked
        }
        ApiFactory.editSyncSchema(self.props.projectName, input).then(() => {
          self.refs.syncSettingModal.hide()
          self.initSyncSchemaSetting()
        })
      })
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    /**
     * SchemaSyncCheck の設定が確認可能か、つまり SchemaSyncCheckの設定が存在するかを確認する
     */
    this.canCheckSchemaSetting = () => {
      return self.state.syncSetting.url != null && self.state.syncSetting.user != null
    }

    /**
     * SchemaSyncCheck の結果を確認できる sync-check-result.html を開く
     */
    this.openSyncCheckResultHTML = () => {
      // #thinking （すごく細かいけど、微妙に気になる） "api" ではないので、URLも "doc" 始まりとかにできないかな？ by cabos
      window.open(global.ffetch.baseUrl + 'api/document/' + self.props.projectName + '/synccheckresulthtml/')
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    /**
     * SchemaSyncCheck の設定を変更するための Modal の、画面読み込み時の初期値
     */
    this.syncSettingModal = {
      header: 'Schema Sync Check Settings',
      closable: true,
      buttons: [
        {
          text: 'OK',
          action: 'editSyncSettings',
          closable: false
        }
      ],
      syncSetting: {}
    }

    /**
     * SchemaSyncCheck を実行しているあいだ、その他のユーザの操作を抑制するための Modal
     */
    // #thinking 実行中の抑制に対して "check" という名前は適切か？ by cabos
    self.checkModal = {
      header: 'SchemaPolicyCheck',
      closable: false
    }

    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    /**
     * SchemaSyncCheckを設定するための Modal を表示する
     * 「Edit check settings」というボタンを押したときに実行される
     */
    this.showSyncSettingModal = () => {
      self.refs.syncSettingModal.show()
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    /**
     * DBFlute Engine に SchemaSyncCheck を実行させるためのリクエストをバックエンドに送信する
     * SchemaSyncCheck の処理中、他の操作を抑制するための Modal を表示させている
     * SchemaSyncCheck の処理後、Modal を処理結果のもとに差し替え、最終実行ログを画面に表示する
     * 「Execute SchemaSyncCheck」というボタンをクリックすると呼び出される
     */
    this.schemaSyncCheckTask = () => {
      self.refs.checkModal.show()
      DbfluteTask.task('schemaSyncCheck', self.props.projectName, (message) => {
        // SchemaSyncCheck の結果が確認できる sync-check-result.html が存在するかどうかの情報をサーバから取得する
        // 将来的には、存在するかどうかの情報を Propbase とは別のAPIに切り出すリファクタリングを行う予定
        // https://github.com/dbflute/dbflute-intro/issues/260
        ApiFactory.clientPropbase(self.props.projectName).then((response) => {
          self.state.client = response
          self.update()
        })
        self.refs.resultModal.show(message)
      }).finally(() => {
        // 操作を抑制する Modal を隠す
        self.refs.checkModal.hide()
        self.latestResult.updateLatestResult()
      })
    }
  </script>
</ex-schema-sync-check>
