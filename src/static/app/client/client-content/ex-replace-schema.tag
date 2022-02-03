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
    // #thinking ここの定義順って何か決まりある？関連し合ってるもの同士をくっつけたいと思っちゃったけど by jflute (2022/01/28)
    // 例えば、import _ApiFactory の直後に new _ApiFactory() を持って来たい
    let riot = require('riot')
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'

    // playsql配下のSQLのハイライトのため
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    // #thinking アンダースコアでクラス定義を連れてきて、newした変数はクラス名そのままが慣習？ by jflute (2022/01/28)
    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    
    // ドロップダウンのデフォルト選択肢
    // #thinking 後に簡単にconcat()するためにArrayになってるけど...defaultItemsって変数名じゃダメ？ by jflute (2022/01/28)
    const defaultItem = [{label: '-', value: null}]

    // #thinking そもそもthisをselfとして保持する意味ってなんだっけ？(実装場所どこでも、ここのthisが参照できるように？) by jflute (2022/02/02)
    let self = this
    this.playsqlDropDownItems = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.preparePlaysql(self.opts.projectName)
      self.prepareComponents()
    })

    /**
     * DBFluteクライアントの基本設定情報を準備する。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.prepareSettings = (projectName) => {
      // #thinking これって、ReplaceSchema画面領域じゃなくて共通領域での表示のための処理でいいのかな？ by jflute (2022/01/28)
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    /**
     * ReplaceSchemaのディレクトリであるplaysqlに関する表示領域を準備する。(SQLファイルのドロップダウンなど)
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.preparePlaysql = (projectName) => {
      // #thinking jsonって変数名でいいの？List<PlaysqlBean>がJSONになってるんだけど...業務的な名前を付けない？ by jflute
      ApiFactory.playsqlBeanList(projectName).then(json => {
        // #thinking この value 何やってんの？SQL文字列自体を埋め込んでたりする？ by jflute
        const playsqlDropDownItems = json.map(obj => ({
          label: obj.fileName,
          value: `<span style="display: none;">${obj.fileName}</span>` + Prism.highlight(obj.content, Prism.languages.sql, 'sql')
        }))
        // デフォルト選択肢(未選択)とガッチャンコしてItemsセット
        self.playsqlDropDownItems = defaultItem.concat(playsqlDropDownItems)
        self.update()
      })
    }

    /**
     * その他画面コンポーネントを準備する。(実行結果の領域など)
     */
    this.prepareComponents = () => {
      // self.latestResultがlatest-resultタグと関連付いてマウントされる
      // #thinking riot.mount()後の[0]はなんだ？mountってArrayで戻ってくるの？ by jflute
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'replaceSchema' })[0]
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    /**
     * 実行中のモーダルダイアログ用のオブジェクト。
     * #thinking 指定できるプロパティを知るのにはソースコードを参照するでOK？ by jflute
     * https://github.com/black-trooper/semantic-ui-riot/blob/master/tags/modal/su-modal.riot
     */
    this.executeModal = {
      // 実行が終わるまでは閉じれないようにする (ダイアログ外をクリックされてもダイアログが消えないように)
      closable: false
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    /**
     * dataディレクトリのオープン処理。(例えばMacならFinderが開く)
     * もうここはローカル起動してるJavaサーバーにお任せ。
     */
    this.openDataDir = () => {
      ApiFactory.openDataDir(self.opts.projectName)
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    /**
     * ReplaceSchemaタスクの実行。
     * コマンドラインのときのy/nの代わりに確認ダイアログを出すようにしている。
     */
    this.replaceSchemaTask = () => {
	  // #thinking suConfirm()ってどこにも定義されていないけど、SemanticUIってこういう感じでいきなり使えるのかな？ by jflute (2022/02/02)
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
