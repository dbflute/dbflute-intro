<ex-schema-sync-check>
  <div class="ui container">
    <h3>Schema Sync Check</h3>
    <p show="{ canCheckSchemaSetting() }">for { syncSetting.url }<span
      show="{ syncSetting.schema != null }">, { syncSetting.schema }</span>, { syncSetting.user }</p>
    <div class="ui list">
      <div show="{ opts.client.hasSyncCheckResultHtml }" class="item"><a onclick="{ openSyncCheckResultHTML }">SyncCheckResultHTML</a></div>
    </div>
    <button class="ui positive button" onclick="{ showSyncSettingModal }">Edit Sync Check</button>
    <button show="{ canCheckSchemaSetting() }" class="ui primary button" onclick="{ schemaSyncCheckTask }">
      Check Schema (schema-sync-check)
    </button>
    <div class="latest-result">
      <latest-result></latest-result>
    </div>
  </div>

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

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

  <result-modal></result-modal>

  <style>
    .latest-result {
      margin-top: 1em;
    }
  </style>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'
    import _DbfluteTask from '../../common/DbfluteTask'

    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    let self = this
    this.syncSetting = {}

    this.checkModal = {
      header : 'SchemaPolicyCheck',
      closable: false
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.initSyncSchemaSetting()
      self.prepareComponents()
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

    this.prepareComponents = () => {
      self.resultModal = riot.mount('result-modal')[0]
      self.latestResult = riot.mount('latest-result', { projectName: self.opts.projectName, task: 'schemaSyncCheck' })[0]
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
      self.refs.checkModal.show()
      DbfluteTask.task('schemaSyncCheck', self.opts.projectName, (message) => {
        self.resultModal.show(message)
      }).finally(() => {
        self.refs.checkModal.hide()
        self.latestResult.updateLatestResult()
      })
    }
  </script>
</ex-schema-sync-check>
