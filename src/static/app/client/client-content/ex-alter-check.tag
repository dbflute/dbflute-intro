<ex-alter-check>
  <!-- ClientのAlterCheck画面 (written at 2022/03/13)
  機能:
   o AlterCheckが実行できる
   o 2Stepに分けて処理を行える
   o Step1ではAlterDDLの用意を行う。未リリースのAlterDDLがある場合はそれも表示される
   o Step2ではAlterDDLの実行を行う。編集中のDDLが他にもあればそれも表示される
  作りの特徴:
   o Stepの切り替えは編集中のAlterDDLファイルが存在するかしないかによって切り替わる
   o SQLファイルの一覧のためにドロップダウンを利用している
   o prism.jsを使ってSQLのハイライトを実現している
   #thinking STEP1とSTEP2で利用しているtagをそれぞれ別ディレクトリに分けるなどした方が迷子になりづらそう by hakiba (2022/03/24)
  -->
  <div class="ui container">
    <h2>AlterCheck</h2>

    <section class="ui info message">
      <div class="header">What is <a href="http://dbflute.seasar.org/ja/manual/function/generator/intro/alterbyintro.html" target="_blank">"AlterCheck"?</a></div>
      <p>A mechanism to validate differential DDL with ReplaceSchema.</p>
    </section>

    <div class="ui divider"></div>

    <!-- Step 1: AlterDDLの用意 -->
    <section if="{ !isEditing() }">
      <h3>Step1. Prepare alter sql</h3>
      <!-- 未リリースの確認済みDDLの一覧 -->
      <alter-check-checked checkedzip="{ checkedZip }" unreleaseddir="{ unreleasedDir }" />
      <!-- 新しいDDLファイルを作成するためのフォーム -->
      <alter-check-form ref="altercheckform" projectname="{ opts.projectName }" updatehandler="{ updateBegin }" />
    </section>

    <!-- Step 2: AlterCheckの実行 -->
    <section show="{ isEditing() }">
      <h3>Step2. Execute AlterCheck</h3>

      <div class="ui list">
        <div class="item" each="{ alterItem in editingSqls }">
          <!-- 押下された alter sql ファイルの表示・非表示を切り替える。また、STEP1で作成されたファイルには印をつけて表示する -->
          <a onclick="{ alterItemClick.bind(this, alterItem) }">{ alterItem.fileName } <span show="{ nowPrepared(alterItem.fileName) }">(now prepared)</span></a>
          <div show="{ alterItem.show }" class="ui message message-area">
            <!-- SQLの改行やインデントが反映されるようにpreタグを利用
              <pre><code>...</code></pre> は一行で記述しないと余分な改行やインデントが入ってしまうので注意
              cssのwhite-spaceを利用するとSQLの先頭に記述したインデントが効かなくなるのでこうするしかなさそう...
              #thinking: <pre><code>...</code></pre>を含めて共通のコンポーネントにした方が良い by hakiba
            -->
            <pre><code><raw content="{ alterItem.content }"></raw></code></pre>
          </div>
        </div>
      </div>

      <div class="ui list">
        <div class="item">
          <!-- AlterDDLディレクトリを開くボタン -->
          <button class="ui button" onclick="{ openAlterDir }"><i class="folder open icon"></i>SQL Files Directory</button>
        </div>

        <div class="item altercheck-execution">
          <!-- AlterCheckを実行するボタン -->
          <button class="ui red button" onclick="{ alterCheckTask }"><i class="play icon"></i>Execute AlterCheck</button>
          <!-- AlterCheckの結果HTMLを表示するボタン -->
          <button class="ui button blue" show="{ client.hasAlterCheckResultHtml }" onclick="{ openAlterCheckResultHTML }"><i class="linkify icon"></i>Open Check Result HTML</button>
        </div>
      </div>
      <!-- 最新のAlterCheckの結果ログを出力する。実行ログがなければ何も表示しない -->
      <div class="latest-result">
        <latest-result></latest-result>
      </div>
    </section>

  </div>

  <!-- 実行時に表示されるモーダル -->
  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <!-- 実行結果を表示するモーダル -->
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
    // sqlファイルをシンタックスハイライトするために利用するライブラリ 参照: https://prismjs.com/
    import Prism from 'prismjs'
    import 'prismjs/components/prism-sql.min'
    import 'prismjs/themes/prism.css'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    let self = this

    self.client = opts.client
    self.projectName = opts.projectName

    // api response
    self.alter = {}

    // view params
    self.ngMarkFile = undefined
    self.editingSqls = []
    self.checkedZip = {
      fileName : '',
      checkedFiles : []
    }
    self.unreleasedDir = {
      checkedFiles : []
    }
    self.preparedFileName = ''
    self.validated = false

    self.state = {
      inputFileName : ''
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント時の処理
     */
    this.on('mount', () => {
      self.updateContents()
    })

    /**
     * クライアントの基本情報を取得します
     */
    // done cabos remove this because this method replace parent object
    // fix this issue https://github.com/dbflute/dbflute-intro/issues/260
    this.prepareClient = () => {
      return ApiFactory.clientPropbase(self.projectName).then(resp => {
        self.client = resp
      })
    }

    /**
     * AlterCheck関連の情報をAPIで取得します
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     * @return {Promise} AlterCheck関連の情報（存在しているファイルなど）
     */
    this.prepareAlterInfo = (projectName) => {
      return ApiFactory.alter(projectName).then(resp => {
        self.alter = resp
      })
    }

    /**
     * ng-markファイルをタグにセットします
     */
    this.prepareNgMark = () => {
      self.ngMarkFile = self.alter.ngMarkFile
    }

    /**
     * 編集中のAlterDDLをタグにセットします
     */
    this.prepareEditing = () => {
      self.editingSqls = []
      self.alter.editingFiles.forEach(file => {
        self.editingSqls.push({
          fileName: file.fileName,
          content: Prism.highlight(file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    /**
     * チェック済みのAlterDDL zipをタグにセットします
     * sqlファイルはシンタックスハイライトされた状態でセットします
     */
    this.prepareChecked = () => {
      if (!self.alter.checkedZip) {
        return
      }
      const unreleasedFileNames = self.unreleasedDir.checkedFiles.map(checkedFile => checkedFile.fileName.replace('READONLY_', ''))
      self.checkedZip = {}
      self.checkedZip.fileName = self.alter.checkedZip.fileName
      self.checkedZip.checkedFiles = []
      self.alter.checkedZip.checkedFiles.forEach(file => {
        // for hybrid state 0.2.0, 0.2.1
        if (unreleasedFileNames.includes(file.fileName)) {
          return
        }
        self.checkedZip.checkedFiles.push({
          fileName: file.fileName,
          // sqlファイル表示時に余白を用意するために改行(\n)を予め入れておく（多分そういう目的）
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    /**
     * 未リリースチェック済みのAlterDDLをタグにセットします（sqlファイルのみ）
     * sqlファイルはシンタックスハイライトされた状態でセットします
     */
    this.prepareUnreleased = () => {
      if (!self.alter.unreleasedDir) {
        return
      }
      self.unreleasedDir = {}
      self.unreleasedDir.checkedFiles = []
      self.alter.unreleasedDir.checkedFiles.forEach(file => {
        if (file.fileName.indexOf('.sql') === -1) {
          return
        }
        self.unreleasedDir.checkedFiles.push({
          fileName: file.fileName,
          content: Prism.highlight('\n' + file.content.trim(), Prism.languages.sql, 'sql'),
          show: false,
        })
      })
    }

    /**
     * 最新の実行結果ログをタグにセットします
     */
    this.prepareLatestResult = () => {
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'alterCheck' })[0]
      self.updateLatestResult()
    }

    /**
     * 最新の実行結果ログ表示をステータスに合わせて切り替えます
     */
    this.updateLatestResult = () => {
      if (!self.latestResult) {
        return
      }
      self.latestResult.latestResult.header.show = false
      // NGマークファイルに合わせて失敗時のタイトルとメッセージを用意
      if (self.ngMarkFile.ngMark ==='previous-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Previous DDL.',
          message: 'Retry save previous.',
        }
      } else if (self.ngMarkFile.ngMark ==='alter-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Alter DDL.',
          message: self.ngMarkFile.content.split('\n')[0],
        }
      } else if (self.ngMarkFile.ngMark ==='next-NG') {
        self.latestResult.failure = {
          title: 'Found problems on Next DDL.',
          message: 'Fix your DDL and data grammatically.',
        }
      }
      // 成功時のタイトルを用意
      self.latestResult.success = {
        title: 'AlterCheck Successfully finished',
      }
      // latest-resultタグの更新処理を呼び出し、結果を反映する
      self.latestResult.updateLatestResult()
    }

    /**
     * 編集中のAlterDDLが存在するかチェックします
     * @return {boolean} true:存在する,false:存在しない
     */
    this.isEditing = () => {
      return self.editingSqls !== undefined && self.editingSqls.length > 0
    }

    /**
     * チェック済みの AlterDDL zip が存在するかチェックします
     * @return {boolean} true:存在する,false:存在しない
     */
    this.existsCheckedFiles = () => {
      return self.checkedZip !== undefined && self.checkedZip.checkedFiles !== undefined && self.checkedZip.checkedFiles.length > 0
    }

    /**
     * 未リリースチェック済みの AlterDDL が存在するかチェックします
     * @return {boolean} true:存在する,false:存在しない
     */
    this.existsUnreleasedFiles = () => {
      return self.unreleasedDir !== undefined && self.unreleasedDir.checkedFiles !== undefined && self.unreleasedDir.checkedFiles.length > 0
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    /**
     * AlterCheck実行時のモーダルの設定値
     */
    this.executeModal = {
      closable: false
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    /**
     * AlterCheckの実行結果htmlを表示します
     */
    this.openAlterCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/altercheckresulthtml/')
    }

    /**
     * OSごとのファイルマネージャーでalterディレクトリを開きます
     */
    this.openAlterDir = () => {
      ApiFactory.openAlterDir(self.opts.projectName)
    }

    /**
     * AlterDDLの表示・非表示を切り替えます
     */
    this.alterItemClick = (alterItem) => {
      alterItem.show = !(alterItem.show)
      return false
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    /**
     * AlterCheckをAPI経由で実行します
     * confirmを許可した場合のみ実行されます
     */
    this.alterCheckTask = () => {
      this.suConfirm('Are you sure to execute AlterCheck task?').then(() => {
        self.refs.executeModal.show()
        DbfluteTask.task('alterCheck', self.opts.projectName, (message) => {
          self.refs.resultModal.show(message)
        }).finally(() => {
          // 失敗した際の情報も反映するため、APIの実行結果に問わずタグの各値を更新
          self.updateContents()
          self.refs.executeModal.hide()
        })
      })
    }

    /**
     * 新しいAlterDDLをAPI経由で作成します
     * "alter-schema-" というprefixが必ずファイル名に付加されます
     */
    this.createAlterSql = () => {
      const ticketName = self.refs.alterNameInput.value
      if (!ticketName || ticketName === '') {
        self.validated = true
        return
      }
      const alterFileName = 'alter-schema-' + ticketName + '.sql'
      ApiFactory.createAlterSql(self.opts.projectName, alterFileName)
        .then(() => {
          self.preparedFileName = alterFileName
          // #thinking: この関数呼び出し多分デッドコードになっている。本当はalter-check-formのprepareAlterCheckを呼び出したい？
          self.prepareAlterCheck()
        })
    }

    /**
     * 画面から作成直後のファイルであるかチェックします
     * リロードするとこのチェックはfalseになります
     * @param fileName DDLファイル名
     * @return {boolean} true:作成直後である,false:作成直後でない
     */
    this.nowPrepared = (fileName) => {
      const inputFileName = self.state.inputFileName
      const alterFileName = 'alter-schema-' + inputFileName + '.sql'
      return alterFileName === fileName
    }

    /**
     * AlterCheckファイルの作成を開始するために各種情報をタグに反映します
     */
    this.updateBegin = () => {
      self.state = {
        inputFileName : self.refs.altercheckform.refs.beginform.refs.alterNameInput.value
      }
      self.updateContents()
    }

    /**
     * AlterCheckの各種情報をタグに反映します
     */
    this.updateContents = () => {
      self.prepareAlterInfo(self.projectName).then(() => {
        self.prepareClient().then(() => {
          self.prepareNgMark()
          self.prepareEditing()
          self.prepareUnreleased()
          self.prepareChecked()
          self.prepareLatestResult()
        }).finally(() => {
          self.update()
        })
      })
    }
  </script>
</ex-alter-check>
