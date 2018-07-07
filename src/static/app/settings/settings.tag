<settings>
  <h2>DBFlute Client { client.projectName }</h2>
  <div class="col-sm-6">
    <p class="text-right">
      for { client.databaseCode }, { client.languageCode }, { client.containerCode }
    </p>
  </div>
  <div class="col-sm-12" show={ !editFlg }>
    <div>
      <div heading="DB info">
        <form class="col-sm-8 form-horizontal">
          <div class="row">
            <div class="form-group">
              <label class="col-sm-2 control-label">URL</label>
              <div class="col-sm-6">
                <input type="text" class="form-control" ref="url"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">スキーマ</label>
              <div class="col-sm-6">
                <input type="text" class="form-control" ref="schema"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">ユーザー</label>
              <div class="col-sm-6">
                <input type="text" class="form-control" ref="user"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">パスワード</label>
              <div class="col-sm-6">
                <input type="text" class="form-control" ref="password"/>
              </div>
            </div>
            <div class="col-sm-3">
              <div class="row">
                <div class="col-sm-12">
                  <input type="button" class="btn btn-primary" value="Edit" onclick="{ editClient.bind(this, client) }"/>
                </div>
                <div class="col-sm-12">
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>

  <style>
  </style>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    import route from 'riot-route'

    const ApiFactory = new _ApiFactory()

    const self = this
    this.client = {} // existing clients

    // ===================================================================================
    //                                                                     Client Handling
    //                                                                     ===============
    this.prepareCurrentProject = (projectName) => {
      ApiFactory.settings(projectName).then(json => {
        self.client = json
        self.refs.url.value = json.mainSchemaSettings.url
        self.refs.schema.value = json.mainSchemaSettings.schema
        self.refs.user.value = json.mainSchemaSettings.user
        self.refs.password.value = json.mainSchemaSettings.password
        self.update()
      })
    }

    this.editClient = () => {
      this.client.mainSchemaSettings = {
        url: self.refs.url.value,
        schema: self.refs.schema.value,
        user: self.refs.user.value,
        password: self.refs.password.value
      }
      ApiFactory.updateSettings(this.client)
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      this.prepareCurrentProject(opts.projectName)
    })
  </script>
</settings>
