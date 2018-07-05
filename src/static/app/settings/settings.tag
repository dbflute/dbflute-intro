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
                <input type="text" class="form-control"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">スキーマ</label>
              <div class="col-sm-6">
                <input type="text" class="form-control"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">ユーザー</label>
              <div class="col-sm-6">
                <input type="text" class="form-control"/>
              </div>
            </div>
            <div class="form-group">
              <label class="col-sm-2 control-label">パスワード</label>
              <div class="col-sm-6">
                <input type="text" class="form-control"/>
              </div>
            </div>
            <div class="col-sm-3">
              <div class="row">
                <div class="col-sm-12">
                  <input type="button" class="btn btn-primary" value="Edit"/>
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
    this.prepareCurrentProject = function (projectName) {
      ApiFactory.settings(projectName).then(function (json) {
        self.client = json
        self.update()
      })
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      console.log(opts)
      this.prepareCurrentProject(opts.projectName)
    })
  </script>
</settings>
