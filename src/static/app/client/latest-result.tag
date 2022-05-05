<latest-result>
  <!-- DBFluteクライアントの直近タスク実行の結果表示領域 (written at 2022/05/05)
   機能:
    o 直近タスク実行の成功/失敗がわかるる
    o 直近タスク実行のログが見られる

   作りの特徴:
    o ログ表示はクリックで表示/非表示が選べる
    o #thinking riotのrawタグを使ってログ文字列を表示している...が、rawしない方が良いのでは？ by jflute (2022/05/05)
   -->
  <div show="{ latestResult.loaded }">
    <h3 if="{ latestResult.header.show }">{ latestResult.header.text }</h3>
    <div class="ui { latestResult.success ? 'positive' : 'negative' } message">
      <!-- 成功か失敗かの表示 -->
      <h4 class="latest-result-title">{ latestResult.success ? success.title : failure.title }</h4>

      <!-- 成功や失敗のメッセージ -->
      <p if="{ latestResult.success && success.message }">{ success.message }</p>
      <p if="{ !latestResult.success && failure.message }">{ failure.message }</p>

      <!-- 失敗時の特別リンクの表示: e.g. SchemaPolicyCheck's result html -->
      <p if="{ !latestResult.success && failure.link && failure.link.message }" >
        <a onclick="{ failure.link.clickAction }">{ failure.link.message }</a>
      </p>

      <!-- ログ表示領域 -->
      <a onclick="{ toggleLatestResult }">{ latestResult.show ? 'hide latest log' : 'show latest log' }</a>
      <div show="{ latestResult.show }" class="ui message message-area">
        <pre><code><raw content="{ latestResult.content }"></raw></code></pre>
      </div>
    </div>
  </div>

  <!-- #thinking ログはwrapすると見づらいので、no-wrapにしたい気持ちがある by jflute (2022/05/05) -->
  <style>
    pre {
      overflow: auto;
      white-space: pre-wrap;
      word-break: break-word;
    }
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory'

    const ApiFactory = new _ApiFactory()
    let self = this

    // 表示領域全体の情報リソースオブジェクト
    // #thinking なんかデフォルト宣言っぽいけど、初期化時にプロパティ追加されている...これだけと思ったらいけないってのどうにかならないだろうか？ by jflute (2022/05/05)
    self.latestResult = {
      loaded: false,
      header: {
        text: 'Execution Result',
        show: true
      }
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      self.updateLatestResult()
    })

    // ===================================================================================
    //                                                              Success/Failure Object
    //                                                              ======================
    /**
     * 成功時の固定表示情報オブジェクト。
     */
    this.success = {
      title: 'Result: Success',
      message: null,
    }

    // #thinking SchemaPolicyCheckから、このfailureオブジェクトまるごと上書きされることで独自処理を追加してるけど... by jflute (2022/05/05)
    // 構造は隠蔽したいので、public的な関数呼び出しで受け取るとかの方が良いかなー。
    /**
     * 失敗時の固定表示情報オブジェクト。
     */
    this.failure = {
      title: 'Result: Failure',
      message: null,
      link: {
        message: null,
        clickAction: null
      }
    }

    // ===================================================================================
    //                                                                        UI Operation
    //                                                                        ============
    /**
     * ログ情報を取得して表示する。
     */
    this.updateLatestResult = () => {
	  // the response is LogBean.java (2022/05/04)
      ApiFactory.latestResult(self.opts.projectName, self.opts.task).then((response) => {
        if (response.fileName) { // ログファイルがなければempty responseがあり得るのでチェックしている
          // Object.assign(target, source)は、targetの内容をsourceで上書きマージしている
          // https://developer.mozilla.org/ja/docs/Web/JavaScript/Reference/Global_Objects/Object/assign
          // #thinking ここのshowはshowLogとかの方が正確なような、headerにもshowがあるので紛らわしいし by jflute (2022/05/05)
          self.latestResult = Object.assign(self.latestResult, {
            success: response.fileName.includes('success'),
            content: response.content,
            show: false,
            loaded: true,
          })
        }
        self.update()
      })
    }

    /**
     * ログ情報の表示を切り替える。
     * @return {boolean} 固定でfalseだよん #thinking 必要なのか？試しに消しても正常に動いてるけど... by jflute (2022/05/05)
     */
    this.toggleLatestResult = () => {
      self.latestResult.show = !self.latestResult.show
      return false
    }
  </script>
</latest-result>
