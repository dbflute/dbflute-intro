<ex-replace-schema>
  <!-- ClientのReplaceSchema画面 (written at 2022/01/28)
   機能:
    o ReplaceSchemaが実行できる
    o テストデータのディレクトリをFinderなどで開ける
    o playsql配下のSQLファイルの中身を表示できる (ハイライト付き)

   作りの特徴:
    o ReplaceSchemaのy/nの代わりにダイアログで「いいのやっちゃって？」の確認をさせている
    o SQLファイルの一覧のためにドロップダウンを利用している
    o prism.jsを使ってSQLのハイライトを実現している
   -->
  <div class="ui container">
    <h2>Replace Schema</h2>

    <!-- 実行ボタンの領域 -->
    <button class="ui red button" onclick="{ replaceSchemaTask }">Execute ReplaceSchema</button>
    <button class="ui button" onclick="{ openDataDir }">
      <!-- #thinking この folder open icon も semantic-ui なのかな？ by jflute (2022/01/28) -->
      <i class="folder open icon"></i>TestData Directory
    </button>

    <!-- 固定メッセージの領域: 関連ページへのリンクなど -->
    <div class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/task/doc/schemasynccheck.html" target="_blank">"Replace Schema"?</a></div>
      <p>A mechanism to automates (re) building your DB schema.</p>
    </div>

    <!-- 実行結果の領域: Introのログとかが表示される -->
    <div class="latest-result">
      <latest-result></latest-result>
    </div>

    <!-- playsqlの操作: DDLファイルを表示するなど -->
    <h3>Play SQL</h3>
    <div class="ui segment" title="PlaySQL">
      <su-dropdown items="{ playsqlDropDownItems }" ref="dropdown"></su-dropdown>
      <div class="ui message message-area">
        <pre>
          <!-- codeタグのclass定義は、prism.jsで用意されているSQLのハイライト -->
          <code class="language-sql">
            <raw content="{ refs.dropdown.value }"></raw>
          </code>
        </pre>
      </div>
    </div>
  </div>

  <!-- タスク実行中の示すダイアログ -->
  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <!-- タスク実行結果を表示するダイアログ -->
  <result-modal ref="resultModal"></result-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    const defaultItem = [{label: '-', value: null}]
    let self = this
    this.playsqlDropDownItems = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.preparePlaysql(self.opts.projectName)
      self.prepareComponents()
    })

    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    this.preparePlaysql = (projectName) => {
      ApiFactory.playsqlBeanList(projectName).then(json => {
        const playsqlDropDownItems = json.map(obj => ({
          label: obj.fileName,
          value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content, Prism.languages.sql, 'sql')
        }))
        self.playsqlDropDownItems = defaultItem.concat(playsqlDropDownItems)
        self.update()
      })
    }

    this.prepareComponents = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'replaceSchema' })[0]
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.executeModal = {
      closable: false
    }
    
    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    this.openDataDir = () => {
      ApiFactory.openDataDir(self.opts.projectName)
    }
    
    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.replaceSchemaTask = () => {
      this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
        self.refs.executeModal.show()
        DbfluteTask.task('replaceSchema', self.opts.projectName, (message) => {
          self.refs.resultModal.show(message)
        }).finally(() => {
          self.refs.executeModal.hide()
          self.latestResult.updateLatestResult()
        })
      })
    }
  </script>
</ex-replace-schema>
