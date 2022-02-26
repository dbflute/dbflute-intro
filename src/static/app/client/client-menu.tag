<client-menu>
  <!--
    機能:
      o DBFlute Intro 画面全体の左側のメニュー
        o のうち、クライアントが選択されているときに表示されるもの

      作りの特徴:
      o 現在選択されているメニューの情報を親から受け取り、active かどうかを判断している
      o メニューをクリックしたとき、URLを書き換えている
  -->
  <div class="ui left fixed small inverted vertical menu">

    <!-- メニューの一番上に表示されている「DBFlute Intro」の文字列 -->
    <menu-home></menu-home>
    
    <!--
      このタグの簡略図は下記

      Execute
      |- Documents
      |- Schema Policy Check
      |- Schema Sync Check
      |- Replace Schema
      |- Alter Check

      General Settings
      |- Database Info

      Files
      |- Logs
    -->
    <div class="item">
      <div class="header">Execute</div>
      <div class="ui tiny inverted vertical menu">
        <!-- 選択されていたら active というクラス名がつく、太字で目立って表示される -->
        <!-- このaタグが押されたとき、URLが変わる -->
        <a class="{ 'active': isActiveItem('execute', 'documents') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'documents') }">
          Documents
        </a>
        <a class="{ 'active': isActiveItem('execute', 'schema-policy-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'schema-policy-check') }">
          Schema Policy Check
        </a>
        <a class="{ 'active': isActiveItem('execute', 'schema-sync-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'schema-sync-check') }">
          Schema Sync Check
        </a>
        <a class="{ 'active': isActiveItem('execute', 'replace-schema') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'replace-schema') }">
          Replace Schema
        </a>
        <a class="{ 'active': isActiveItem('execute', 'alter-check') } item"
           onclick="{ goToMenuItem.bind(this, 'execute', 'alter-check') }">
          Alter Check
        </a>
      </div>
    </div>
    <div class="item">
      <div class="header">General Settings</div>
      <div class="ui tiny inverted vertical menu">
        <a class="{ 'active': isActiveItem('settings', 'database-info') } item"
           onclick="{ goToMenuItem.bind(this, 'settings', 'database-info') }">
          Database Info
        </a>
      </div>
    </div>
    <div class="item">
      <div class="header">Files</div>
      <div class="ui tiny inverted vertical menu">
        <a class="{ 'active': isActiveItem('files', 'logs') } item"
           onclick="{ goToMenuItem.bind(this, 'files', 'logs') }">
          Logs
        </a>
      </div>
    </div>
  </div>

  <style>
    .ui.tiny.inverted.vertical.menu {
      margin-left: 0px;
    }
  </style>

  <script>
    /**
     * 親から選択されている clientMenuType, clientMenuName を受け取り、メニューの行が、active かどうかを判断している
     * @param menuType {string} URLで指定されるメニュータイプ e.g. "execute", "settings", "files" (NotNull)
     * @param menuName {string} URLで指定されるメニュー名 e.g. "documents", "schema-policy-check", and so on... (NotNull)
     */
    this.isActiveItem = (menuType, menuName) => {
      return menuType === this.opts.clientMenuType && menuName === this.opts.clientMenuName
    }

    /**
     * URLを変更する
     * この変更に対応して、index.js の処理により、tagのマウントのされ方が変わる
     * @param menuType {string} URLで指定されるメニュータイプ e.g. "execute", "settings", "files" (NotNull)
     * @param menuName {string} URLで指定されるメニュー名 e.g. "documents", "schema-policy-check", and so on... (NotNull)
     * @see dbflute-intro/src/static/app/index.js@157
     */
    this.goToMenuItem = (menuType, menuName) => {
      // #thinking rooting に関する情報、探しやすいように切り出してあげたい by cabos (2022/02/26)
      // （ただし大規模な修正になるので今やることではない）
      route(`client/${this.opts.projectName}/${menuType}/${menuName}`)
    }
  </script>
</client-menu>
