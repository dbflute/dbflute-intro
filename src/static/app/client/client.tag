<client>
  <h2>DBFlute Client { opts.projectName }</h2>
  <p>for { client.databaseCode }, { client.languageCode }, { client.containerCode }</p>

  <h3>Documents</h3>
  <div class="ui list">
    <div show="{ client.hasSchemaHtml }" class="item"><a onclick="{ openSchemaHTML }">SchemaHTML</a></div>
    <div show="{ client.hasHistoryHtml }" class="item"><a onclick="{ openHistoryHTML }">HistoryHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showDocumentSettingModal }">Edit Document Settings</button>
  <button class="ui primary button" onclick="{ generateTask }">Generate Documents (jdbc, doc)</button>

  <h3>Schema Sync Check</h3>
  <p show="{ canCheckSchemaSetting() }">for { syncSetting.url }<span show="{ syncSetting.schema != null }">, { syncSetting.schema }</span>, { syncSetting.user }</p>
  <div class="ui list">
    <div show="{ client.hasSyncCheckResultHtml }" class="item"><a onclick="{ openSyncCheckResultHTML }">SyncCheckResultHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showSyncSettingModal }">Edit Sync Check</button>
  <button show="{ canCheckSchemaSetting() }" class="ui primary button" onclick="{ schemaSyncCheckTask }">Check Schema (schema-sync-check)</button>

  <h3>Replace Schema</h3>
  <button class="ui red button" onclick="{ replaceSchemaTask }">Replace Schema (replace-schema)</button>

  <h3>Alter Check</h3>
  <div class="ui list">
    <div show="{ client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterCheckResultHTML }">AlterCheckResultHTML</a></div>
    <div show="{ client.hasAlterCheckResultHtml }" class="item"><a onclick="{ openAlterDir }">Open alter dir</a></div>
    <div show="{ client.ngMark != undefined }" class="item"><a onclick="{ showAlterFailureLog }">Check last execute log</a></div>
  </div>
  <div class="ui list">
    <div show="{ client.ngMark === 'previous-NG' }" class="ngmark">
      Found problems on <b>Previous DDL.</b><br/>
      Retry save previous.
    </div>
    <div show="{ client.ngMark === 'alter-NG' }" class="ngmark">
      Found problems on <b>Alter DDL.</b><br/>
      Complete your alter DDL, referring to AlterCheckResultHTML.<br/>
      <br/>
    </div>
    <div show="{ client.ngMark === 'next-NG' }" class="ngmark">
      Found problems on <b>Next DDL.</b><br/>
      Fix your DDL and data grammatically.
    </div>
  </div>
  <button class="ui red button" onclick="{ alterCheckTask }">Alter Check (alter-check)</button>

  <su-modal modal="{ generateModal }" class="large" ref="generateModal">
    <div class="description">
      Generating...
    </div>
  </su-modal>

  <su-modal modal="{ executeModal }" class="large" ref="executeModal">
    <div class="description">
      Executing...
    </div>
  </su-modal>

  <su-modal modal="{ documentSettingModal }" class="large" ref="documentSettingModal">
    <form class="ui form">
      <div class="field">
        <label>Alias delimiter in DB comment <span class="frm">(aliasDelimiterInDbComment)</span></label>
        <input type="text" ref="aliasDelimiterInDbComment" placeholder="e.g. :" value="{ opts.modal.documentSetting.aliasDelimiterInDbComment }">
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="upperCaseBasic" checked="{ opts.modal.documentSetting.upperCaseBasic }">
          <label>Upper case basis <span class="frm">(isTableDispNameUpperCase, isTableSqlNameUpperCase, isColumnSqlNameUpperCase)</span></label>
        </div>
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="dbCommentOnAliasBasis" checked="{ opts.modal.documentSetting.dbCommentOnAliasBasis }">
          <label>DB comment on alias basis <span class="frm">(isDbCommentOnAliasBasis)</span></label>
        </div>
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="checkColumnDefOrderDiff" checked="{ opts.modal.documentSetting.checkColumnDefOrderDiff }">
          <label>Check Column Definition Order Difference <span class="frm">(isCheckColumnDefOrderDiff)</span></label>
        </div>
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="checkDbCommentDiff" checked="{ opts.modal.documentSetting.checkDbCommentDiff }">
          <label>Check DB Comment Difference <span class="frm">(isCheckDbCommentDiff)</span></label>
        </div>
      </div>
      <div class="field">
        <div class="ui checkbox">
          <input type="checkbox" ref="checkProcedureDiff" checked="{ opts.modal.documentSetting.checkProcedureDiff }">
          <label>Check Procedure Difference <span class="frm">(isCheckProcedureDiff)</span></label>
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

  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
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
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.generateModal = {
      closable: false
    }
    this.documentSettingModal = {
      header: 'Document Settings (documentMap.dfprop, littleAdjustmentMap.dfprop)',
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
          action: 'editSyncSettings',
          closable: false
        }
      ],
      syncSetting: {}
    }
    this.resultModal = {
      closable: true,
      buttons: [
        {
          text: 'CLOSE',
          default: true
        }
      ],
      message: ''
    }

    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    this.showDocumentSettingModal = () => {
      self.refs.documentSettingModal.show()
    }

    this.showSyncSettingModal = () => {
      self.refs.syncSettingModal.show()
    }

    this.showResultModal = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
    }

    this.showAlterFailureLog = () => {
      let fileName = 'intro-last-execute-alterCheck.log'
      ApiFactory.getLog(self.opts.projectName, fileName).then((res) => {
        observable.trigger('result', { header: fileName, messages: [res.content], modalSize: 'large' })
      }).catch(() => {
        self.resultModal.message = 'log file not found'
        self.refs.resultModal.show()
      })

    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.generateTask = () => {
      this.task('doc', self.refs.generateModal)
    }

    this.schemaSyncCheckTask = () => {
      this.task('schemaSyncCheck', self.refs.checkModal)
    }

    this.replaceSchemaTask = () => {
      this.suConfirm('Are you sure to execute Replace Schema task?').then(() => {
        this.task('replaceSchema', self.refs.executeModal)
      })
}

    this.alterCheckTask = () => {
      this.suConfirm('Are you sure to execute Alter Check task?').then(() => {
        this.task('alterCheck', self.refs.executeModal)
      }).finally(() => {
        ApiFactory.clientOperation(self.opts.projectName).then((response) => {
          self.update({
            client: response
          })
        })
      })
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

    // ===================================================================================
    //                                                                       Open Document
    //                                                                       =============
    this.openSchemaHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/schemahtml/')
    }

    this.openHistoryHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/historyhtml/')
    }

    this.openSyncCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/synccheckresulthtml/')
    }

    this.openAlterCheckResultHTML = () => {
      window.open(global.ffetch.baseUrl + 'api/document/' + self.opts.projectName + '/altercheckresulthtml/')
    }

    this.canCheckSchemaSetting = () => {
      return self.syncSetting.url != null && self.syncSetting.user != null
    }

    this.openAlterDir = () => {
      ApiFactory.openAlterDir(self.opts.projectName)
    }

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject(self.opts.projectName)
      self.registerModalEvent()
    })

    this.prepareCurrentProject = () => {
      ApiFactory.clientOperation(self.opts.projectName).then((response) => {
        self.client = response
        self.update()
      })
      ApiFactory.document(self.opts.projectName).then((response) => {
        self.documentSettingModal.documentSetting = response
        self.update()
      })
      self.initSyncSchemaSetting()
    }

    this.initSyncSchemaSetting = () => {
      ApiFactory.syncSchema(self.opts.projectName).then((response) => {
        self.syncSettingModal.syncSetting = response
        self.update({
          syncSetting: response
        })
      })
    }

    this.registerModalEvent = () => {
      this.refs.documentSettingModal.on('editDocumentSettings', () => {
        const documentStringModalRefs = self.refs.documentSettingModal.refs
        const documentSetting = {
          aliasDelimiterInDbComment: documentStringModalRefs.aliasDelimiterInDbComment.value,
          upperCaseBasic:  documentStringModalRefs.upperCaseBasic.checked,
          dbCommentOnAliasBasis: documentStringModalRefs.dbCommentOnAliasBasis.checked,
          checkColumnDefOrderDiff: documentStringModalRefs.checkColumnDefOrderDiff.checked,
          checkDbCommentDiff: documentStringModalRefs.checkDbCommentDiff.checked,
          checkProcedureDiff: documentStringModalRefs.checkProcedureDiff.checked
        }
        ApiFactory.editDocument(self.opts.projectName, documentSetting)
      })

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
  </script>
</client>
