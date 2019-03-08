<ex-documents>
  <h3>Documents</h3>
  <div class="ui list">
    <div show="{ opts.client.hasSchemaHtml }" class="item"><a onclick="{ openSchemaHTML }">SchemaHTML</a></div>
    <div show="{ opts.client.hasHistoryHtml }" class="item"><a onclick="{ openHistoryHTML }">HistoryHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showDocumentSettingModal }">Edit Document Settings</button>
  <button class="ui primary button" onclick="{ generateTask }">Generate Documents (jdbc, doc)</button>

  <su-modal modal="{ documentSettingModal }" class="large" ref="documentSettingModal">
    <form class="ui form">
      <div class="field">
        <label>Alias delimiter in DB comment <span class="frm">(aliasDelimiterInDbComment)</span></label>
        <input type="text" ref="aliasDelimiterInDbComment" placeholder="e.g. :"
               value="{ opts.modal.documentSetting.aliasDelimiterInDbComment }">
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

  <su-modal modal="{ generateModal }" class="large" ref="generateModal">
    <div class="description">
      Generating...
    </div>
  </su-modal>

  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    import _ApiFactory from '../../common/factory/ApiFactory.js'

    const ApiFactory = new _ApiFactory()
    let self = this

    // ===================================================================================
    //                                                                          Initialize
    //                                                                          ==========
    this.on('mount', () => {
      self.prepareCurrentProject()
      self.registerModalEvent()
    })
    this.prepareCurrentProject = () => {
      ApiFactory.document(self.opts.projectName).then((response) => {
        self.documentSettingModal.documentSetting = response
        self.update()
      })
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
    this.showResultModal = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
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

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.generateTask = () => {
      this.task('doc', self.refs.generateModal)
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
</ex-documents>
