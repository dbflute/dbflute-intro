<st-database-info>
  <!-- ClientのDB接続設定画面 (written at 2022/02/10)
     機能:
      o DBFluteクライアントのDB接続情報を確認できる
      o 〃を編集できる
      (databaseInfoMap.dfprop)

     作りの特徴:
      o Introで意外と少ないテキスト入力による更新、バリデーションとか普通のことが珍しい
      o Toast使って「ぴょこん」ってかわいく更新したことをお知らせする
     -->
  <div class="ui container">
    <div class="ui form">
      <div class="row">
        <div class="column">
          <div class="ui segment" title="Database info" ref="settings">
            <!-- 必須かどうかは、databaseInfoMap.dfpropの仕様に合わせている
             https://dbflute.seasar.org/ja/manual/reference/dfprop/databaseinfo/index.html

             catalogが必要になる場面はめったにないので、Introではひとまずサポートしていない。
             -->
            <!-- #hope この画面でDBMSを変えることはできないので、DBMSごとに必須を変えられたらいいかもね by jflute (2022/02/10) -->
            <div class="required field" if="{ client.mainSchemaSettings }">
              <!-- LABEL_xxxは、assetsにて定義されている -->
              <label data-is="i18n">LABEL_url</label>
              <input type="text" value="{ client.mainSchemaSettings.url }" ref="url" placeholder="jdbc:mysql://localhost:3306/maihamadb"/>
            </div>
            <div class="field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_schema</label>
              <input type="text" value="{ client.mainSchemaSettings.schema }" ref="schema" placeholder="MAIHAMADB"/>
            </div>
            <div class="required field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_user</label>
              <input type="text" value="{ client.mainSchemaSettings.user }" ref="user" placeholder="maihamadb"/>
            </div>
            <div class="field" if="{ client.mainSchemaSettings }">
              <label data-is="i18n">LABEL_password</label>
              <input type="text" value="{ client.mainSchemaSettings.password }" ref="password"/>
            </div>
            <div class="field">
              <button class="ui button primary" onclick="{ editClient }">Edit</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    const self = this
    this.client = {} // existing clients

    /**
     * マウント時の処理。
     */
    this.on('mount', () => {
      self.prepareSettings(self.opts.projectName)
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

    // #thinking editって言うと編集画面に行くイメージある？updateとかの方が誤解がないような？ by jflute (2022/02/15)
    /**
     * 入力された情報を実際のDBFluteクライアントに更新する。
     */
    this.editClient = () => {
      const url = self.refs.url.value
      const schema = self.refs.schema.value
      const user = self.refs.user.value
      const password = self.refs.password.value
      this.client.mainSchemaSettings = {url, schema, user, password}

      // #thinking updateSettings()の引数は、コールバックとかじゃないからthis.clientでも良い？ by jflute (2022/02/15)
      ApiFactory.updateSettings(self.client).then(() => {
        // 更新後は画面情報を表示し直し (やらなくてもズレはしないだろうけど念のためかな？)	
        self.prepareSettings(self.opts.projectName)
        self.showToast()
      })
    }

    /**
     * 朝起きたらトーストで焼いてカリっとしたパンの上にバターを塗ってちょい溶けた感じが良い...
     * って最近はあんまりやらないけど、ここでは更新したときにトーストを表示したい。
     * ダイアログを出してOKボタン押させるのもゴツいUIな感じだし、うんともすんとも言わないのもわかりづらいのでトーストが一番。
     */
    this.showToast = () => {
      this.successToast({
        title: 'Setting updated',
      })
    }
  </script>
</st-database-info>
