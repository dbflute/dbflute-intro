<ex-schema-policy-check>
  <!-- ClientのSchemaPolicyCheckの実行画面 (written at 2022/02/20)
   機能:
    o SchemaPolicyCheckを実行し、結果を表示する。
    o ポリシーのチェックボックスの状態を変化させることでSchemaPolicyMapを編集することができる。
    o ポリシー設定の状態を表示する。

   作りの特徴:
    o 実行中はモーダルを表示して他の操作をできなくしている。
    o チェックボックスのOn/Offでつどつどdfpropを修正しにいってる。

   tagの階層
    ex-schema-policy-check.tag
    |- schema-policy-check-statement-form-wrapper.tag
       |- schema-policy-check-statement-form.tag
          |- schema-policy-check-statement-form-expected.tag
             |- schema-policy-check-statement-form-docuement-link.tag
             |- schema-policy-check-statement-form-expected-field.tag
   -->
  <div class="ui container">
    <h2>Schema Policy Check</h2>
    <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Execute SchemaPolicyCheck ( doc task )</button>
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/genbafit/implfit/schemapolicy/index.html" target="_blank">"SchemaPolicyCheck"?</a></div>
      <p>A checking tool for DB design, embedded with <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/doc/index.html" target="_blank">Doc task</a>.</p>
    </div>

    <!-- 最新の実行結果 -->
    <div class="latest-result">
      <!-- もともとjsの方でpropsを渡していたが、(this.prepareComponents)
      projectName -> project-name というようにケバブケースを使用すれば
      タグを書くところでpropsを渡せることが判明したので修正した。 by prprmurakami (2022/03/12) -->
      <latest-result project-name ="{ projectName }" task="doc" ref="latestResult"></latest-result>
    </div>
    
    <!-- SchemaPolicy Settings for Whole, Table, Column -->
    <h3>Policy Settings</h3>
    <div class="ui segment" title="SchemaPolicy">
      <div class="ui form">
        <div class="row">
          <div class="column">
            <su-tabset
              class="three column item"
              schemapolicy="{ schemaPolicy }"
              tabtitles="{ tabTitles }"
              projectname="{ projectName }"
              onregistersuccess="{ onRegisterSuccess }"
            >
              <!-- Whole Schema Policy -->
              <su-tab label="{ opts.tabtitles['wholeMap']}" schemapolicy="{ opts.schemapolicy }" >
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.wholeMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.wholeMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'wholeMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <div class="header">{ theme.name }</div>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
              </su-tab>

              <!-- Table Schema Policy -->
              <su-tab
                label="{ opts.tabtitles['tableMap']}"
                schemapolicy="{ opts.schemapolicy }"
                projectname="{ opts.projectname }"
                onregistersuccess="{ opts.onregistersuccess }">
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.tableMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.tableMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'tableMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <div class="header">{ theme.name }</div>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <h5 class="spolicy-category">Statement</h5>

                <schema-policy-check-statement-list
                  if="{ opts.schemapolicy.tableMap }"
                  maptype="tableMap"
                  clientname="{ opts.projectname }"
                  deletestatement="{ parent.parent.deleteStatement }"
                  statements="{ opts.schemapolicy.tableMap.statementList }"
                />

                <schema-policy-check-statement-form-wrapper
                  formtype="tableMap"
                  projectname="{ opts.projectname }"
                  onregistersuccess="{ opts.onregistersuccess }"
                />
              </su-tab>

              <!-- Column Schema Policy -->
              <su-tab
                label="{ opts.tabtitles['columnMap']}"
                schemapolicy="{ opts.schemapolicy }"
                projectname="{ opts.projectname }"
                onregistersuccess="{ opts.onregistersuccess }"
              >
                <h5 class="spolicy-category">Theme</h5>
                <div class="ui divided items segment" if="{opts.schemapolicy.columnMap}">
                  <div class="item" each="{ theme in opts.schemapolicy.columnMap.themeList }">
                    <div class="ui left floated">
                      <su-checkbox class="toggle middle aligned" checked="{ theme.isActive }" onchange="{ parent.parent.parent.editSchemaPolicyMap.bind(this, 'columnMap', theme.typeCode) }"></su-checkbox>
                    </div>
                    <div class="content">
                      <a class="header">{ theme.name }</a>
                      <div class="description">
                        {theme.description}
                      </div>
                    </div>
                  </div>
                </div>
                <h5 class="spolicy-category">Statement</h5>

                <schema-policy-check-statement-list
                  if="{ opts.schemapolicy.columnMap }"
                  maptype="columnMap"
                  clientname="{ opts.projectname }"
                  deletestatement="{ parent.parent.deleteStatement }"
                  statements="{ opts.schemapolicy.columnMap.statementList }"
                />

                <schema-policy-check-statement-form-wrapper
                  formtype="columnMap"
                  projectname="{ opts.projectname }"
                  onregistersuccess="{ opts.onregistersuccess }"
                />
              </su-tab>
            </su-tabset>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Executeボタンを押したときに表示されるモーダル -->
  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="content" if="{opts.modal.status === 'Check'}">
      <div class="header" >{ opts.modal.message }</div>
    </div>
    <div class="ui positive message" if="{opts.modal.status === 'Success'}">
      <h4>{ opts.modal.message }</h4>
    </div>
    <div class="ui negative message" if="{opts.modal.status === 'Failure'}">
      <h4>{ opts.modal.message }</h4>
    </div>
  </su-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()

    let self = this
    self.client = opts.client
    this.schemaPolicy = {}
    this.projectName = ''

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント前に実行する処理
     * 詳しくはこちら
     * https://v3.riotjs.vercel.app/guide/#tag-lifecycle
     */
    this.on('before-mount', () => {
      // # thinking どうしてこの処理はbeforemountじゃないといけないんだろう？ by prprmurakami (2022/03/12)
      self.projectName = opts.projectName
    })

    /**
     * マウント処理
     */
    this.on('mount', () => {
      this.prepareSchemaPolicy(opts.projectName)
      this.prepareComponents(opts.projectName)
    })

    /**
     * schemaPolicyを初期化する。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.prepareSchemaPolicy = (projectName) => {
      self.fetchSchemaPolicy(projectName)
    }

    /**
     * 画面コンポーネントの準備をする。 (latestResultなど)
     */
    this.prepareComponents = () => {
      // もともと
      // self.latestResult = riot.mount('latest-result', { projectName: projectName, task: 'doc' })[0]
      // となっていたが、タグを書くところでprops渡せることがわかったので修正。by prprmurakami (2022/03/12)
      self.latestResult = self.refs.latestResult
      self.updateLatestResult(self.client)
    }

    this.onRegisterSuccess = () => {
      self.fetchSchemaPolicy(opts.projectName)
    }

    this.updateLatestResult = (client) => {
      if (!self.latestResult) {
        return
      }
      if (client.violatesSchemaPolicy) {
        self.latestResult.failure = {
          title: 'Result: Failure',
          link: {
            message: 'Open your SchemaPolicyCheck result (HTML)',
            clickAction: self.openSchemaHTML
          }
        }
      }
      self.latestResult.updateLatestResult()
    }

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    // -----------------------------------------------------
    //                                                 Modal
    //                                                 -----
    /**
     * モーダルに表示する情報の定義
     */
    this.checkModal = {
      header : 'SchemaPolicyCheck',
      status : 'Check',
      closable: false,
      check : function() {
        self.checkModal.status = 'Check'
        self.checkModal.message = 'Checking...'
        self.checkModal.closable = false
      },
      success : function() {
        self.checkModal.status = 'Success'
        self.checkModal.message = 'Success!!'
        self.checkModal.closable = true
      },
      fail : function () {
        self.checkModal.status = 'Failure'
        self.checkModal.message = 'Failure: You need check violation.'
        self.checkModal.closable = true
      }
    }

    // -----------------------------------------------------
    //                                       Policy Settings
    //                                       ---------------
    /**
     * ポリシー設定に並べるタブの名前の定義
     */
    this.tabTitles = {
      wholeMap : 'Whole Schema Policy',
      tableMap : 'Table Schema Policy',
      columnMap : 'Column Schema Policy'
    }

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    /**
     * SchemaPolicyMapを編集する。
     * @param {string} targetMap - 編集対象となるマップ種別 (NotNull, only 'tableMap', 'columnMap')
     * @param {string} typeCode - themeを一意に特定するコード (NotNull)
     */
    this.editSchemaPolicyMap = (targetMap, typeCode) => {
      const targetTheme = this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode)
      const toggledActiveStatus = !targetTheme.isActive

      let body = {
        wholeMap : {themeList : []},
        tableMap : {themeList : []},
        columnMap : {themeList : []}
      }
      body[targetMap].themeList = [{typeCode : typeCode, isActive : toggledActiveStatus}]

      ApiFactory.editSchemaPolicy(opts.projectName, body).then(() => {
        this.schemaPolicy[targetMap].themeList.find(theme => theme.typeCode === typeCode).isActive = toggledActiveStatus
        self.update()
      })
    }

    /**
     * statementにコメントが含まれているかどうかを返す。
     * @param {string} statement - statement文字列 (NotNull)
     * @return {boolean} statementにコメントが含まれている場合はtrue (NotNull)
     */
    this.isIncludeComment = (statement) => {
      return statement.includes('=>')
    }

    /**
     * コメントが含まれるstatementからstatementを抜粋する。
     * e.g.
     * [param] ; if columnName is suffix:_ID then alias is pattern:.+ID(\(.+\))?$ => IDカラムなら論理名は "なんとかID" にしよう
     * [return] ; if columnName is suffix:_ID then alias is pattern:.+ID(\(.+\))?$
     *
     * @param {string} statement - statement文字列 (NotNull)
     * @return {string} statement文字列（コメントが含まれていた場合はコメントを削ったもの。コメントがない場合はそのまま） (NotNull)
     */
    this.extractStatement = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return statement
      }
      const splitStatements = statement.split('=>')
      return splitStatements[0]
    }

    /**
     * コメントが含まれるstatementからコメントを抜粋する
     * e.g.
     * [param] ; if columnName is suffix:_ID then alias is pattern:.+ID(\(.+\))?$ => IDカラムなら論理名は "なんとかID" にしよう
     * [return] IDカラムなら論理名は "なんとかID" にしよう
     *
     * @param {string} statement - statement文字列 (NotNull)
     * @return {string} 抜粋したコメント (NotNull, EmptyAllowed: そもそもコメントがない場合)
     */
    this.extractComment = (statement) => {
      if (!self.isIncludeComment(statement)) {
        return ''
      }
      const splitStatements = statement.split('=>')
      return splitStatements[1]
    }

    // ===================================================================================
    //                                                                           Operation
    //                                                                           =========
    /**
     * SchemaPolicyを取得する。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.fetchSchemaPolicy = (projectName) => {
      ApiFactory.schemaPolicy(projectName).then(json => {
        self.schemaPolicy = json
        self.update()
      })
    }
    
    /**
     * statementをSchemaPolicyから削除する。
     * @param {string} mapType - 編集対象となるマップ種別 (NotNull, only 'tableMap', 'columnMap')
     * @param {string} statement - 削除対象のstatement (NotNull)
     */
    this.deleteStatement = (mapType, statement) => {
      self.suConfirm('Are you sure to delete this statement?').then(() => {
        ApiFactory.deleteSchemapolicyStatement(opts.projectName, {mapType: mapType, statement: statement}).then(() => {
          self.fetchSchemaPolicy(self.projectName)
          self.update()
        })
      })
    }

    /**
     * 登録完了時にSchemaPolicyを再取得し、更新する。
     */
    this.onRegisterSuccess = () => {
      self.fetchSchemaPolicy(opts.projectName)
    }


    /**
     * 最新の実行結果を更新する。
     * @param {string} client - 現在対象としているDBFluteクライアント. (NotNull)
     */
    this.updateLatestResult = (client) => {
      if (!self.latestResult) {
        return
      }
      if (client.violatesSchemaPolicy) {
        self.latestResult.failure = {
          title: 'Result: Failure',
          link: {
            message: 'Open your SchemaPolicyCheck result (HTML)',
            clickAction: self.openSchemaHTML
          }
        }
      }
      self.latestResult.updateLatestResult()
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    /**
     * SchemaPolicyCheckを実行する。
     * 進行状況にあわせてモーダルの文言を更新する。
     */
    this.schemaPolicyCheckTask = () => {
      self.refs.checkModal.show()
      self.checkModal.check()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        if (message === 'success') {
          self.checkModal.success()
        } else if (message === 'failure'){
          self.checkModal.fail()
        }
      }).finally(() => {
        if (self.checkModal.status === 'Check') {
          self.refs.checkModal.hide()
        }
        ApiFactory.clientPropbase(self.opts.projectName).then((response) => {
          self.client = response
          self.updateLatestResult(self.client)
          self.update()
        })
      })
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    /**
     * SchemaHTMLを開く。
     */
    this.openSchemaHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/schemahtml/')
    }
  </script>
</ex-schema-policy-check>
