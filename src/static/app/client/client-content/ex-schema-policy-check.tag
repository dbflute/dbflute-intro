<ex-schema-policy-check>
  <div class="ui container">
    <h3>Schema Policy Check</h3>
    <button class="ui positive button" onclick="{ goToSchemaPolicySetting }">Edit Policy Check</button>
    <button class="ui primary button" onclick="{ schemaPolicyCheckTask }">Check Policy (schema-policy-check)</button>
    <div class="ui info message">
      <div class="header">What is "Check Policy"?</div>
      <p>The doc task is executed, because there is no SchemaPolicyCheck task.</p>
    </div>
  </div>

  <su-modal modal="{ checkModal }" class="large" ref="checkModal">
    <div class="description">
      Checking...
    </div>
  </su-modal>

  <su-modal modal="{ resultModal }" class="large" ref="resultModal">
    <div class="description">
      { opts.modal.message }
    </div>
  </su-modal>

  <script>
    import _DbfluteTask from '../../common/DbfluteTask'

    const DbfluteTask = new _DbfluteTask()
    let self = this
    // ===================================================================================
    //                                                                               Modal
    //                                                                               =====
    // -----------------------------------------------------
    //                                            Definition
    //                                            ----------
    this.checkModal = {
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
    this.showResultModal = (message) => {
      self.resultModal.message = message
      self.refs.resultModal.show()
    }

    // -----------------------------------------------------
    //                                                  GoTo
    //                                                  ----
    this.goToSchemaPolicySetting = () => {
      route(`operate/${self.opts.projectName}/settings/schema-policy`)
    }

    // ===================================================================================
    //                                                                        Execute Task
    //                                                                        ============
    this.schemaPolicyCheckTask = () => {
      self.refs.checkModal.show()
      DbfluteTask.task('doc', self.opts.projectName, (message) => {
        self.showResultModal(message)
      }).finally(() => {
        self.refs.checkModal.hide()
      })
    }

  </script>
</ex-schema-policy-check>
