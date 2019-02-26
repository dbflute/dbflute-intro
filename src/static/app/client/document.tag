<document>
  <h3>Documents</h3>
  <div class="ui list">
    <div show="{ client.hasSchemaHtml }" class="item"><a onclick="{ openSchemaHTML }">SchemaHTML</a></div>
    <div show="{ client.hasHistoryHtml }" class="item"><a onclick="{ openHistoryHTML }">HistoryHTML</a></div>
  </div>
  <button class="ui positive button" onclick="{ showDocumentSettingModal }">Edit Document Settings</button>
  <button class="ui primary button" onclick="{ generateTask }">Generate Documents (jdbc, doc)</button>
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

  <script>
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
    this.showDocumentSettingModal = () => {
      self.refs.documentSettingModal.show()
    }
  </script>
</document>
