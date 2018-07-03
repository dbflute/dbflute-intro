<client>

  <su-modal modal="{ generateModal }" class="large" ref="generateModal">
    <div class="description">
      Generating...
    </div>
  </su-modal>

  <h2>DBFlute Client { opts.projectName }</h2>
  <span>for { client.databaseCode }, { client.languageCode }, { client.containerCode }</span>

  <h3>Documents</h3>
  <div class="ui list">
    <div class="item" onclick="{ openSchemaHTML }"><a>SchemaHTML</a></div>
    <div class="item" onclick="{ openHistoryHTML }"><a>HistoryHTML</a></div>
  </div>
  <button class="ui positive button">Edit Document Settings</button>
  <button class="ui primary button" onclick="{ task.bind(this, 'doc') }">Generate Documents (jdbc, doc)</button>

  <h3>Schema Sync Check</h3>
  <button class="ui positive button">Edit Sync Check</button>
  <button class="ui primary button">Check Schema (schema-sync-check)</button>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    const ApiFactory = new _ApiFactory()

    let self = this
    this.client = {}
    this.generateModal = {
      closable: false
    }


    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.prepareCurrentProject = () => {
      ApiFactory.clientOperation(self.opts.projectName).then((response) => {
        self.client = response
        self.update()
      })
    }


    // ===================================================================================
    //                                                                               Task
    //                                                                              ======
    this.task = (task) => {
      self.refs.generateModal.show()
      ApiFactory.task(self.opts.projectName, task).then((success) => {
        self.refs.generateModal.hide()
      })
    }

    // ===================================================================================
    //                                                                           Open HTML
    //                                                                           =========
    this.openSchemaHTML = () => {
      window.open(ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/schemahtml/')
    }

    this.openHistoryHTML = () => {
      window.open(ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/historyhtml/')
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject(self.opts.projectName)
    })
  </script>
</client>
