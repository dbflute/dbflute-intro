<client>
  <h2>DBFlute Client { opts.projectName }</h2>
  <p>for { client.databaseCode }, { client.languageCode }, { client.containerCode }</p>

  <h3>Documents</h3>
  <div class="ui list">
    <div if="{ client.hasSchemaHtml }" class="item" onclick="{ openSchemaHTML }"><a>SchemaHTML</a></div>
    <div if="{ client.hasHistoryHtml }" class="item" onclick="{ openHistoryHTML }"><a>HistoryHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showDocumentSettingModal }">Edit Document Settings</button>
  <button class="ui primary button" onclick="{ generateTask }">Generate Documents (jdbc, doc)</button>

  <h3>Schema Sync Check</h3>
  <p if="{ canCheckSchemaSetting() }">for { syncSetting.url }, { syncSetting.schema }, { syncSetting.user }</p>
  <div class="ui list">
    <div if="{ client.hasSyncCheckResultHtml }" class="item" onclick="{ openSyncCheckResultHTML }"><a>SchemaHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showSyncSettingModal }">Edit Sync Check</button>
  <button if="{ canCheckSchemaSetting() }" class="ui primary button" onclick="{ checkTask }">Check Schema (schema-sync-check)</button>

  <su-modal modal="{ generateModal }" class="large" ref="generateModal">
    <div class="description">
      Generating...
    </div>
  </su-modal>

  <su-modal modal="{ documentSettingModal }" class="large" ref="documentSettingModal">
    <form class="ui form">
      <div class="field">
        <label>Alias delimiter in DB comment</label>
        <input type="text" ref="aliasDelimiterInDbComment" placeholder="e.g. :" value="{ opts.modal.documentSetting.aliasDelimiterInDbComment }">
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="upperCaseBasic" checked="{ opts.modal.documentSetting.upperCaseBasic }">
          <label>Upper case basis</label>
        </div>
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="dbCommentOnAliasBasis" checked="{ opts.modal.documentSetting.dbCommentOnAliasBasis }">
          <label>DB comment on alias basis</label>
        </div>
      </div>
    </form>
  </su-modal>

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

  <su-modal modal="{ syncSettingModal }" class="large" ref="syncSettingModal">
    <form class="ui form">
      <div class="required field">
        <label>URL</label>
        <input type="url" ref="url" placeholder="jdbc:mysql://localhost:3306/examplesyncdb" value="{ opts.modal.syncSetting.url }">
      </div>
      <div class="field">
        <label>Schema</label>
        <input type="text" ref="schema" placeholder="EXAMPLESYNCDB" value="{ opts.modal.syncSetting.schema }">
      </div>
      <div class="required field">
        <label>User</label>
        <input type="text" ref="user" placeholder="examplesyncdb" value="{ opts.modal.syncSetting.user }">
      </div>
      <div class="field">
        <label>Password</label>
        <input type="password" ref="password" placeholder="" value="{ opts.modal.syncSetting.password }">
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="isSuppressCraftDiff" checked="{ opts.modal.syncSetting.isSuppressCraftDiff }">
          <label>Suppress Craft Diff</label>
        </div>
      </div>
    </form>
  </su-modal>

  <su-modal modal="{ successModal }" class="large" ref="successModal">
    <div class="description">
      Success!!
    </div>
  </su-modal>

  <script>
    import _ApiFactory from '../common/factory/ApiFactory.js'
    const ApiFactory = new _ApiFactory()

    let self = this
    this.client = {}
    this.syncSetting = {}

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    this.generateModal = {
      closable: false
    }
    this.documentSettingModal = {
      header: 'Document Settings',
      closable: true,
      buttons:  [
        {
          text: 'OK',
          action: 'editDocumentSettings'
        }
      ],
      documentSetting: {}
    }
    this.checkModal = {
      closable: false
    }
    this.syncSettingModal = {
      header: 'Schema Sync Check Settings',
      closable: true,
      buttons:  [
        {
          text: 'OK',
          action: 'editSyncSettings'
        }
      ],
      syncSetting: {}
    }
    this.successModal = {
      closable: true,
      buttons: [
        {
          text: 'CLOSE'
        }
      ]
    }

    this.showDocumentSettingModal = () => {
      self.refs.documentSettingModal.show()
    }

    this.showSyncSettingModal = () => {
      self.refs.syncSettingModal.show()
    }

    this.showSuccessModal = () => {
      self.refs.successModal.show()
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.prepareCurrentProject = () => {
      ApiFactory.clientOperation(self.opts.projectName).then((response) => {
        self.client = response
        self.update()
      })
      ApiFactory.document(self.opts.projectName).then((response) => {
        self.documentSettingModal.documentSetting = response
        self.update()
      })
      ApiFactory.syncSchema(self.opts.projectName).then((response) => {
        self.syncSettingModal.syncSetting = response
        self.syncSetting = response
        self.update()
      })
    }


    // ===================================================================================
    //                                                                               Task
    //                                                                              ======
    this.task = (task, modal) => {
      modal.show()
      ApiFactory.task(self.opts.projectName, task).then((response) => {
        self.showSuccessModal()
      }).finally(() => {
        modal.hide()
      })
    }

    this.generateTask = () => {
      this.task('doc', self.refs.generateModal)
    }

    this.checkTask = () => {
      this.task('schemaSyncCheck', self.refs.checkModal)
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

    this.openSyncCheckResultHTML = () => {
      window.open(ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/synccheckresulthtml/')
    }

    this.canCheckSchemaSetting = () => {
      return self.syncSetting.url != null && self.syncSetting.user != null
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject(self.opts.projectName)

      this.refs.documentSettingModal.on('editDocumentSettings', () => {
        let documentStringModalRefs = self.refs.documentSettingModal.refs
        let documentSetting = {
          aliasDelimiterInDbComment: documentStringModalRefs.aliasDelimiterInDbComment.value,
          upperCaseBasic:  documentStringModalRefs.upperCaseBasic.checked,
          dbCommentOnAliasBasis: documentStringModalRefs.dbCommentOnAliasBasis.checked
        }
        ApiFactory.editDocument(self.opts.projectName, documentSetting)
      })

      this.refs.syncSettingModal.on('editSyncSettings', () => {
        let syncSettingModalRefs = self.refs.syncSettingModal.refs
        let syncSetting = {
          url: syncSettingModalRefs.url.value,
          schema: syncSettingModalRefs.schema.value,
          user: syncSettingModalRefs.user.value,
          password: syncSettingModalRefs.password.value,
          isSuppressCraftDiff: syncSettingModalRefs.isSuppressCraftDiff.checked
        }
        ApiFactory.editSyncSchema(self.opts.projectName, syncSetting)
      })
    })
  </script>
</client>
