<alter-check-begin-form>
  <!-- 新しいAlterDDLを作成するフォーム (written at 2022/03/13)
  機能:
   o AlterDDLファイル名を入力してsqlファイルを作成できる（ファイル名には必ず "alter-schema-" というprefixがつく）
  作りの特徴:
   o 呼び出し元からファイル作成後のコールバック(opts.updateHandler)を渡してもらい、ファイル作成が完了した際に実行する
  -->
  <h4>Begin New AlterCheck!!</h4>
  <form class="ui form">
    <div class="fields">
      <div class="inline field error{!state.invalidFileName}">
        <!-- alter-schema-"チケット名".sql という感じでチケット名を入力するためのinput -->
        <label>alter-schema-</label>
        <input type="text" ref="alterNameInput" placeholder="input ticket name">
        <label>.sql</label>
      </div>
      <div class="inline field">
        <!-- このボタンを押下するとファイル作成が行われる -->
        <button class="ui primary button" onclick="{ createAlterSql }">Begin</button>
      </div>
    </div>
  </form>

  <script>
    import _ApiFactory from '../../../common/factory/ApiFactory.js'

    const apiFactory = new _ApiFactory()
    const self = this

    const projectName = self.opts.projectname
    const updateHandler = self.opts.updatehandler

    /**
     * マウント前に実行される処理
     * #thinking this.on('mount', ...) でもよさそう by hakiba (2022/03/24)
     */
    this.on('before-mount', () => {
      self.state = {
        invalidFileName: false
      }
    })

    /**
     * AlterDDLファイルを作成します
     */
    this.createAlterSql = () => {
      const ticketName = self.refs.alterNameInput.value
      // ファイル名が不正の場合、何もせずに終了
      if (self.validate(ticketName)) {
        return
      }
      apiFactory.prepareAlterSql(projectName).then(() => {
        const alterFileName = 'alter-schema-' + ticketName + '.sql'
        apiFactory.createAlterSql(projectName, alterFileName).then(() => {
          // 呼び元から渡された後続処理を実行
          updateHandler()
        })
      })
    }

    /**
     * AlterDDLのファイル名をバリデートします
     * @param {string} ticketName ファイル名。チケット名が入ることを期待 (NotEmpty)
     * @return {boolean} true:OK, false:NG
     */
    this.validate = (ticketName) => {
      // 空文字は許さない
      self.state.invalidFileName = !ticketName || ticketName === ''
      return self.state.invalidFileName
    }
  </script>
</alter-check-begin-form>
