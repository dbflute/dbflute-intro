<ex-schema-sync-check>
  <h3>Schema Sync Check</h3>
  <p show="{ canCheckSchemaSetting() }">for { syncSetting.url }<span
    show="{ syncSetting.schema != null }">, { syncSetting.schema }</span>, { syncSetting.user }</p>
  <div class="ui list">
    <div show="{ opts.client.hasSyncCheckResultHtml }" class="item"><a onclick="{ openSyncCheckResultHTML }">SyncCheckResultHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showSyncSettingModal }">Edit Sync Check</button>
  <button show="{ canCheckSchemaSetting() }" class="ui primary button" onclick="{ schemaSyncCheckTask }">Check Schema
    (schema-sync-check)
  </button>

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
        <input type="text" ref="password" placeholder="" value="{ opts.modal.syncSetting.password }">
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="isSuppressCraftDiff" checked="{ opts.modal.syncSetting.isSuppressCraftDiff }">
          <label>Suppress Craft Diff</label>
        </div>
      </div>
    </form>
  </su-modal>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    let self = this
    this.syncSetting = {}

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.initSyncSchemaSetting()
      self.registerModalEvent()
    })
    this.initSyncSchemaSetting = () => {
      ApiFactory.syncSchema(self.opts.projectName).then((response) => {
        self.syncSettingModal.syncSetting = response
        self.update({
          syncSetting: response
        })
      })
    }
    this.registerModalEvent = () => {
      this.refs.syncSettingModal.on('editSyncSettings', () => {
        const syncSettingModalRefs = self.refs.syncSettingModal.refs
        const syncSetting = {
          url: syncSettingModalRefs.url.value,
          schema: syncSettingModalRefs.schema.value,
          user: syncSettingModalRefs.user.value,
          password: syncSettingModalRefs.password.value,
          isSuppressCraftDiff: syncSettingModalRefs.isSuppressCraftDiff.checked
        }
        ApiFactory.editSyncSchema(self.opts.projectName, syncSetting).then(() => {
          self.refs.syncSettingModal.hide()
          self.initSyncSchemaSetting()
        })
      })
    }

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    this.canCheckSchemaSetting = () => {
      return self.syncSetting.url != null && self.syncSetting.user != null
    }
    this.openSyncCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/synccheckresulthtml/')
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.syncSettingModal = {
      header: 'Schema Sync Check Settings',
      closable: true,
      buttons: [
        {
          text: 'OK',
          action: 'editSyncSettings',
          closable: false
        }
      ],
      syncSetting: {}
    }
    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    this.showSyncSettingModal = () => {
      self.refs.syncSettingModal.show()
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaSyncCheckTask = () => {
      this.task('schemaSyncCheck', self.refs.checkModal)
    }
    this.task = (task, modal) => {
      modal.show()
      ApiFactory.task(self.opts.projectName, task).then((response) => {
        const message = response.success ? 'success' : 'failure'
        self.showResultModal(message)
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({
            client: response
          })
        })
      }).finally(() => {
        modal.hide()
      })
    }

  </script>
</ex-schema-sync-check>
