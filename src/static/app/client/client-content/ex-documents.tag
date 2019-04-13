<ex-documents>
  <div class="ui container">
    <h3>Documents</h3>
    <div class="ui list">
      <div show="{ opts.client.hasSchemaHtml }" class="item"><a onclick="{ openSchemaHTML }">SchemaHTML</a></div>
      <div show="{ opts.client.hasHistoryHtml }" class="item"><a onclick="{ openHistoryHTML }">HistoryHTML</a></div>
    </div>
    <button class="ui positive button" onclick="{ showDocumentSettingModal }">Edit Document Settings</button>
    <button class="ui primary button" onclick="{ generateTask }">Generate Documents (jdbc, doc)</button>
    <div show="{ latestLog }" class="ui list">
      <h4 class="ui header">Latest Log</h4>
      <div class="ui { latestLog.success ? 'positive' : 'negative' } message">
        <span>Result: { latestLog.success ? 'Success' : 'Failure' }&nbsp;&nbsp;&nbsp;&nbsp;</span><a onclick="{ toggleLatestLog }">show more detail...</a>
        <div show="{ latestLog.show }" class="ui message message-area">
        <pre>
          <code>
            <raw content="{ latestLog.content }"></raw>
          </code>
        </pre>
        </div>
      </div>
    </div>
  </div>

  <su-modal modal="{ documentSettingModal }" class="large" ref="documentSettingModal">
    <form class="ui form">
      <div class="field">
        <label>Alias delimiter in DB comment <span class="frm">(aliasDelimiterInDbComment)</span></label>
        <input type="text" ref="aliasDelimiterInDbComment" placeholder="e.g. :"
               value="{ opts.modal.documentSetting.aliasDelimiterInDbComment }">
      </div>
      <div class="field">
        <di class="ui checkbox">
          <input type="checkbox" ref="upperCaseBasic" checked="{ opts.modal.documentSetting.upperCaseBasic }">
          <label>Upper case basis <span class="frm">(isTableDispNameUpperCase, isTableSqlNameUpperCase, isColumnSqlNameUpperCase)</span></label>
        </di>
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

  <su-modal modal="{ generateModal }" class="large" ref="generateModal">
    <div class="description">
      Generating...
    </div>
  </su-modal>

  <result-modal></result-modal>

  <style>
    pre {
      overflow: auto;
      white-space: pre-wrap;
      word-break: break-word;
    }
  </style>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory'
    import _DbfluteTask from '../../common/DbfluteTask'


    const ApiFactory = new _ApiFactory()
    const DbfluteTask = new _DbfluteTask()
    let self = this

    self.latestLog = null

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject()
      self.prepareModal()
      self.registerModalEvent()
    })

    this.prepareCurrentProject = () => {
      ApiFactory.document(self.opts.projectName).then((response) => {
        self.documentSettingModal.documentSetting = response
        self.update()
      })
      ApiFactory.latestLog(self.opts.projectName, 'doc').then((response) => {
        if (response.fileName) {
          self.latestLog = {
            success: response.fileName.includes('success'),
            content: response.content,
            show: false,
          }
        }
        self.update()
      })
    }

    this.prepareModal = () => {
      self.resultModal = riot.mount('result-modal')[0]
    }

    this.registerModalEvent = () => {
      this.refs.documentSettingModal.on('editDocumentSettings', () => {
        const documentStringModalRefs = self.refs.documentSettingModal.refs
        const documentSetting = {
          aliasDelimiterInDbComment: documentStringModalRefs.aliasDelimiterInDbComment.value,
          upperCaseBasic: documentStringModalRefs.upperCaseBasic.checked,
          dbCommentOnAliasBasis: documentStringModalRefs.dbCommentOnAliasBasis.checked,
          checkColumnDefOrderDiff: documentStringModalRefs.checkColumnDefOrderDiff.checked,
          checkDbCommentDiff: documentStringModalRefs.checkDbCommentDiff.checked,
          checkProcedureDiff: documentStringModalRefs.checkProcedureDiff.checked
        }
        ApiFactory.editDocument(self.opts.projectName, documentSetting)
      })
    }

    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.documentSettingModal = {
      header: 'Document Settings (documentMap.dfprop, littleAdjustmentMap.dfprop)',
      closable: true,
      buttons: [
        {
          text: 'OK',
          action: 'editDocumentSettings'
        }
      ],
      documentSetting: {}
    }

    this.generateModal = {
      closable: false
    }

    // -----------------------------------------------------
    //                                                  Show
    //                                                  ----
    this.showDocumentSettingModal = () => {
      self.refs.documentSettingModal.show()
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
    this.toggleLatestLog = () => {
      self.latestLog.show = !self.latestLog.show
      return false
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.generateTask = () => {
      self.refs.generateModal.show()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        self.resultModal.show(message)
      }).finally(() => {
        self.refs.generateModal.hide()
      })
    }
  </script>
</ex-documents>
