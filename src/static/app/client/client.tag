<client>
  <!-- DBFlute Intro のクライアント画面のメニュー以外の部分 (written at 2022/03/10)
    機能:
      o DBFluteのクライアント機能全般を提供する
      o urlだと、「#client/${clientName}/*」に当たる画面
      o この tag で実施しているのは、どの機能を表示するのか切り替えることだけ

      作りの特徴:
      o tag の切り替えは、親から渡される "clientMenuType", "clientMenuName" を元にしている
      o 豪快に mount している
  -->

  <h1>DBFlute Client { opts.projectName }</h1>
    <!-- hide for_now, not important and should be designed more by jflute (2020/09/28)
    <p>for { client.databaseCode }, { client.languageCode }, { client.containerCode }</p>
     -->

  <!-- クライアント画面がマウントされるところ -->
  <!-- script tag の中のコードを読むとわかるが、豪快に書き換えていいる -->
  <client-content></client-content>

  <script>
    // #thinking import 文に揃えてあげたい  by cabos (2022/02/26)
    let riot = require('riot')
    import _ApiFactory from '../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    let self = this
    this.client = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    /**
     * マウント時に呼び出される処理
     */
    this.on('mount', () => {
      self.prepareCurrentProject(self.opts.projectName)
    })

    /**
     * この tag の準備を行う
     * o サーバからプロジェクトに関する情報を取得して、内部に保持する
     * o 豪快に必要な画面をまるっと mount する
     */
    this.prepareCurrentProject = () => {
      ApiFactory.clientPropbase(self.opts.projectName).then((response) => {
        self.client = response
        // この中が豪快
        self.mountClientContent()
        self.update()
      })
    }

    /**
     * 親から opts 経由で渡される clientMenuType, clientMenuName を元にどの tag を mount するのかを判断し
     * <client-content></client-content> の部分に豪快に mount する
     */
    this.mountClientContent = () => {
      let tagName = null
      const menuType = this.opts.clientMenuType
      const menuName = this.opts.clientMenuName

      // #thinking 実質 routing に関する情報、探しやすいように切り出してあげたい  by cabos (2022/02/26)
      // （ただし大規模な修正になるので今やることではない）
      if (menuType === 'execute' && menuName === 'documents') {
        // tagName に入れる文字列は、client-content ディレクトリ配下のtag名から拡張子を抜いたものと一致している
        tagName = 'ex-documents'
      } else if (menuType === 'execute' && menuName === 'schema-sync-check') {
        tagName = 'ex-schema-sync-check'
      } else if (menuType === 'execute' && menuName === 'replace-schema') {
        tagName = 'ex-replace-schema'
      } else if (menuType === 'execute' && menuName === 'alter-check') {
        tagName = 'ex-alter-check'
      } else if (menuType === 'execute' && menuName === 'schema-policy-check') {
        tagName = 'ex-schema-policy-check'
      } else if (menuType === 'settings' && menuName === 'database-info') {
        tagName = 'st-database-info'
      } else if (menuType === 'files' && menuName === 'logs') {
        tagName = 'fl-logs'
      }
      if (tagName) {
        // まるっと、<client-content></client-content> の中身を差し替えている
        riot.mount('client-content', tagName, { projectName: this.opts.projectName, client: this.client })
      }
      // #thinking 存在しない tagName が入っていた場合って静かに何も表示されなくなるけど、それでいいんだっけ？ by cabos (2022/02/26)
      // https://github.com/dbflute/dbflute-intro/pull/377#discussion_r823736935
    }
  </script>
</client>
