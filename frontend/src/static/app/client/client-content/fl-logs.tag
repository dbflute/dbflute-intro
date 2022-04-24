<fl-logs>
  <!-- Clientのログ閲覧画面 (written at 2022/02/03)
    機能:
     o DBFluteクライアントのログファイルを確認できる (logディレクトリ配下)
     o DBFluteクライアントのログが閲覧できる e.g. dbflute.log

    作りの特徴:
     o ログファイルの内容をごっそりサーバーからもらって、dropdownのvalueにぶち込んでる
    -->
  <div class="ui container">
    <div class="ui segment" title="Log">
      <!-- dropdownのvalueにログの中身が入ってるので、refでdropdownを参照してpreに展開している -->
      <su-dropdown items="{ logDropDownItems }" ref="dropdown"></su-dropdown>
      <div class="ui message message-area">
        <pre>{ refs.dropdown.value }</pre>
      </div>
    </div>
  </div>

  <style>
    .message-area {
      /*
       * ログはドデカ(too large)だから、収まらないときはスクロール
       * overflow | MDN :: https://developer.mozilla.org/ja/docs/Web/CSS/overflow
       */
      overflow: scroll;
    }

    .message-area pre {
      font-family: Monaco, "Courier New", serif;
    }
  </style>

  <script>
    // #thinking 相対パスだとファイル構成のリファクタリングのときとか置き去りにされるからちょっといやんだよね by jflute (2022/02/04)
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    const self = this
    // #thinking concatしやすさのためのArrayだろうけど、一応ArrayだからItems(複数形)にしたい by jflute (2022/02/05)
    const defaultItem = [{label: '-', value: null}]
    this.client = {} // existing clients

    // label:fileName, value:ログの中身
    this.logDropDownItems = {}

    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
      self.prepareLogs(self.opts.projectName)
    })

    /**
     * DBFluteクライアントの基本設定情報を準備する。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.prepareSettings = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.update()
      })
    }

    /**
     * ログ情報表示部分を準備する。
     * @param {string} projectName - 現在対象としているDBFluteクライアントのプロジェクト名. (NotNull)
     */
    this.prepareLogs = (projectName) => {
      // dropdown要素の表示順序はサーバーのレスポンスデータに依存
      ApiFactory.logBeanList(projectName).then(json => {
        const logDropDownItems = json.map(obj => ({label: obj.fileName, value: obj.content}))
        self.logDropDownItems = defaultItem.concat(logDropDownItems)
        self.update()
      })
    }
  </script>
</fl-logs>
